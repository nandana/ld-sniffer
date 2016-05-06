package es.upm.oeg.tools.quality.ldsniffer.eval;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.RDF;
import es.upm.oeg.tools.quality.ldsniffer.BadRequestException;
import es.upm.oeg.tools.quality.ldsniffer.ServerError;
import es.upm.oeg.tools.quality.ldsniffer.cmd.LDSnifferApp;
import es.upm.oeg.tools.quality.ldsniffer.model.HttpResponse;
import es.upm.oeg.tools.quality.ldsniffer.query.QueryUtils;
import es.upm.oeg.tools.quality.ldsniffer.vocab.*;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RiotException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static es.upm.oeg.tools.quality.ldsniffer.query.QueryCollection.*;

/**
 * Copyright 2014-2016 Ontology Engineering Group, Universidad Politécnica de Madrid, Spain
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Nandana Mihindukulasooriya
 * @since 1.0.0
 */
public class Evaluation {

    private static final Logger logger = LoggerFactory.getLogger(Evaluation.class);

    private String url;

    private Dataset dataset;

    private Resource subject;

    private int totalTriples;

    final Set<String> iriSet = new HashSet<>();

    private List<String> iriSubjects;

    private List<String> iriPredicates;

    private List<String> iriObjects;

    final Map<String, HttpResponse> responseMap = new HashMap<>();

    final AtomicInteger derefIriSubjectCount =  new AtomicInteger(0);

    final AtomicInteger derefIriPredicateCount =  new AtomicInteger(0);

    final AtomicInteger derefIriObjectCount =  new AtomicInteger(0);

    final AtomicInteger derefIriCount = new AtomicInteger(0);

    Resource evaluation;

    final static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"){
        public Date parse(String source, ParsePosition pos) {
            return super.parse(source.replaceFirst(":(?=[0-9]{2}$)",""),pos);
        }
    };

    final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.0000");

    public Evaluation(String url, Dataset dataset){
        this.url = url;
        this.dataset = dataset;
    }

    public void process() {

        logger.info("Start evaluating the URI {}", url);

        Date startTime = new Date();

        //calculate the metrics
        calculateMetrics();

        Date endTime = new Date();

        dataset.begin(ReadWrite.WRITE);
        try {
            Model model = dataset.getDefaultModel();

            subject = model.createResource(url);
            evaluation = model.createResource("http://linkeddata.es/resource/ldqm/evaluation/" + UUID.randomUUID().toString());

            //Add the evaluation activity
            addEvaluationActivity(model, startTime, endTime);

            //Add base measures
            addMeasure(model, LDQM.Numberoftriples, totalTriples);

            addMeasure(model, LDQM.Numberofdistinctiris, iriSet.size());
            if (iriSet.size() > 0) {

                addMeasure(model, LDQM.Numberofdereferenceableiris, derefIriCount.intValue());
                addMeasure(model, LDQM.Averageiridereferenceability, getPercentage(derefIriCount.intValue(), iriSet.size()));

                if (iriSubjects.size() > 0) {
                    addMeasure(model, LDQM.Numberofdistinctsubjects, iriSubjects.size());
                    addMeasure(model, LDQM.Numberofdereferenceablesubjects, derefIriSubjectCount.intValue());
                    addMeasure(model, LDQM.Averagesubjectdereferenceability, getPercentage(derefIriSubjectCount.intValue(), iriSubjects.size()));

                }

                if (iriPredicates.size() > 0) {
                    addMeasure(model, LDQM.Numberofdistinctpredicates, iriPredicates.size());
                    addMeasure(model, LDQM.Numberofdereferenceablepredicates, derefIriPredicateCount.intValue());
                    addMeasure(model, LDQM.Averagepredicatedereferenceability, getPercentage(derefIriPredicateCount.intValue(), iriPredicates.size()));
                }

                if (iriObjects.size() > 0) {
                    addMeasure(model, LDQM.Numberofdistinctobjects, iriObjects.size());
                    addMeasure(model, LDQM.Numberofdereferenceableobjects, derefIriObjectCount.intValue());
                    addMeasure(model, LDQM.Averageobjectdereferenceability, getPercentage(derefIriObjectCount.intValue(), iriObjects.size()));
                }

                addQualityReport(model);

                //We create a copy of the map here to avoid ConcurrentModificationException when the executor is timed out
                //before finishing the evaluation of all URIs and keep adding new ones to the responseMap
                final Map<String, HttpResponse> responseMapCopy = new HashMap<>(responseMap);

                for (HttpResponse response: responseMapCopy.values()) {
                    if(!response.isCached()){
                        addDereferenceabilityMeasure(model, response);
                    }
                }

            }

            dataset.commit();

        } finally {
            dataset.end();
        }

    }

    public void addEvaluationActivity(Model model, Date startTime, Date endTime) {

        evaluation.addProperty(RDF.type, EVAL.Evaluation);
        evaluation.addProperty(RDF.type, PROV.Activity);
        evaluation.addLiteral(EVAL.performedOn, ResourceFactory.createTypedLiteral( DATE_FORMAT.format(startTime), XSDDatatype.XSDdateTime));
        evaluation.addLiteral(PROV.startedAtTime, ResourceFactory.createTypedLiteral( DATE_FORMAT.format(startTime), XSDDatatype.XSDdateTime));
        evaluation.addLiteral(PROV.endedAtTime, ResourceFactory.createTypedLiteral( DATE_FORMAT.format(endTime), XSDDatatype.XSDdateTime));
        evaluation.addProperty(EVAL.evaluatedSubject, subject);
        evaluation.addProperty(LDQ.utilizes, LDQM.AT_SPARQL_Query);
        evaluation.addProperty(LDQ.utilizes, LDQM.AT_Url_Dereferencing);
    }

    public void addMeasure(Model model, Resource metric, int value) {
        Resource measure = addMeasure(model, metric, subject);
        measure.addLiteral(DQV.value, value);
        measure.addLiteral(EVAL.hasLiteralValue, value);
    }

    public void addMeasure(Model model, Resource metric, double value) {
        Resource measure = addMeasure(model, metric, subject);
        measure.addLiteral(DQV.value, value);
        measure.addLiteral(EVAL.hasLiteralValue, value);
    }

    public void addDereferenceabilityMeasure(Model model, HttpResponse response) {
        Resource subject = model.createResource(response.getUri());
        try {
            subject.addProperty(URI4URI.host, new URI(response.getUri()).getHost());
        } catch (URISyntaxException e) {
            //We quietly ignore if we can't parse the URI
        }
        Resource measure = addMeasure(model, LDQM.IRIdereferenceability, subject);
        if (response.getStatusCode() >= 200 && response.getStatusCode() < 300 ) {
            measure.addLiteral(DQV.value, model.createTypedLiteral(true, XSDDatatype.XSDboolean));
            measure.addLiteral(EVAL.hasLiteralValue, model.createTypedLiteral(true, XSDDatatype.XSDboolean));
        } else {
            measure.addLiteral(DQV.value, model.createTypedLiteral(false, XSDDatatype.XSDboolean));
            measure.addLiteral(EVAL.hasLiteralValue, model.createTypedLiteral(false, XSDDatatype.XSDboolean));
        }
        Resource request = model.createResource(HTTP.Request);
        measure.addProperty(DCTerms.references, request);
        request.addLiteral(HTTP.methodName, response.getMethod());
        request.addLiteral(HTTP.httpVersion, "1.1");
        request.addLiteral(DCTerms.date, model.createTypedLiteral(DATE_FORMAT.format(response.getDate()),XSDDatatype.XSDdateTime));

        Resource res = model.createResource(HTTP.Response);
        request.addProperty(HTTP.resp, res);
        if (response.getStatusCode() > 0) {
            res.addLiteral(HTTP.statusCodeValue, model.createTypedLiteral(response.getStatusCode(),XSDDatatype.XSDint));
        }
        if (response.getReason() != null){
            res.addLiteral(HTTP.reasonPhrase, response.getReason());
        }
    }

    public Resource addMeasure(Model model, Resource metric, Resource subject) {
        Resource measure = model.createResource(LDQM.MEASURE_PREFIX + UUID.randomUUID().toString());
        measure.addProperty(RDF.type, DQV.QualityMeasurement);
        measure.addProperty(RDF.type, DAQ.Observation);
        measure.addProperty(RDF.type, EVAL.QualityValue);
        measure.addProperty(DQV.computedOn, subject);
        measure.addProperty(EVAL.obtainedFrom, evaluation);
        measure.addProperty(PROV.wasGeneratedBy, evaluation);
        measure.addProperty(DQV.isMeasurementOf, metric);
        measure.addProperty(EVAL.forMeasure,  metric);
        measure.addLiteral(DAQ.isEstimate, false);
        return measure;
    }

    public Resource addQualityReport(Model model) {
        Resource qualityReport = model.createResource(LDQM.QUALITY_REPORT_PREFIX + UUID.randomUUID().toString(),QPRO.QualityReport);
        qualityReport.addProperty(QPRO.computedOn, ResourceFactory.createTypedLiteral(DATE_FORMAT.format(new Date()), XSDDatatype.XSDdateTime));
        Resource noDerefSubjectsProblem = model.createResource(QPRO.QualityProblem);
        noDerefSubjectsProblem.addProperty(QPRO.isDescribedBy, LDQM.IRIdereferenceability);
        for (HttpResponse response : responseMap.values()) {
            if (response.getStatusCode() >= 300 || response.getStatusCode() < 200) {
                Resource errSubject = model.createResource(LDQM.Defect_UndereferenceableURI);
                errSubject.addProperty(DCTerms.subject,response.getUri());
                if (response.getStatusCode() > 0) {
                    errSubject.addLiteral(HTTP.statusCodeValue, model.createTypedLiteral(response.getStatusCode(), XSDDatatype.XSDint));
                }
                errSubject.addLiteral(HTTP.methodName, response.getMethod());
                if (response.getReason() != null) {
                    errSubject.addLiteral(HTTP.reasonPhrase, response.getReason());
                }
                noDerefSubjectsProblem.addProperty(QPRO.problematicThing, errSubject);
            }
        }
        qualityReport.addProperty(QPRO.hasProblem, noDerefSubjectsProblem);
        qualityReport.addProperty(PROV.wasGeneratedBy, evaluation);
        return qualityReport;
    }

    public void calculateMetrics() {

        Model model = ModelFactory.createDefaultModel();
        //TODO try to limit the size of the file being loaded
        try {
            model.read(url);
        } catch (RiotException e) {
            model = RDFDataMgr.loadModel(url+".rdf");
        } catch (Exception e) {
            throw new BadRequestException(String.format("Unable to read the content from the uri - %s \n(%s)", url, e.getMessage()), e);
        }

        totalTriples = QueryUtils.getCountAsC(model, TOTAL_TRIPLES);
        iriSubjects = QueryUtils.getIriList(model, DISTINCT_IRI_SUBJECTS);
        iriPredicates = QueryUtils.getIriList(model, DISTINCT_IRI_PREDICATES);
        iriObjects = QueryUtils.getIriList(model, DISTINCT_IRI_OBJECTS);

        iriSet.addAll(iriSubjects);
        iriSet.addAll(iriPredicates);
        iriSet.addAll(iriObjects);

        final ExecutorService executor = Executors.newFixedThreadPool(5);

        iriSet.forEach(iri -> {
            executor.submit( () -> {

                HttpResponse cachedResponse = Executor.getCachedResponse(iri);

                if (cachedResponse == null) {

                    Date date = new Date();

                    CloseableHttpClient httpclient = HttpClients.createDefault();
                    HttpHead head = new HttpHead(iri);
                    String method = "HEAD";

                    try (CloseableHttpResponse response = httpclient.execute(head);) {
                        StatusLine statusLine = response.getStatusLine();
                        int statusCode = statusLine.getStatusCode();
                        if (statusCode == HttpStatus.SC_METHOD_NOT_ALLOWED ||
                                statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                            HttpGet get = new HttpGet(iri);
                            method = "GET";
                            try (CloseableHttpResponse getResponse = httpclient.execute(get);) {
                                statusLine = getResponse.getStatusLine();
                                statusCode = statusLine.getStatusCode();
                            }
                        }
                        responseMap.put(iri, new HttpResponse(iri, method, statusCode, statusLine.getReasonPhrase(), date, false));
                        if (statusCode >= 200 && statusCode < 300) {
                            derefIriCount.getAndIncrement();
                        }

                        Executor.putCachedResponse(iri, new HttpResponse(iri, method, statusCode, statusLine.getReasonPhrase(), date, true));

                    } catch (ConnectException e) {
                        logger.error("Connection timed out ...", e);
                        responseMap.put(iri, new HttpResponse(iri, method, -1, e.getMessage(), date, false));
                    } catch (IOException e) {
                        logger.error("IO error occurred ..", e);
                        responseMap.put(iri, new HttpResponse(iri, method, -1, e.getMessage(), date, false));
                    }
                } else {
                    responseMap.put(iri, cachedResponse);
                }
            });
        });

        executor.shutdown();
        try {
            executor.awaitTermination(LDSnifferApp.getEvaluationTimeout(), TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new ServerError("Interrupted ...");
        }

        iriSubjects.forEach(subject -> incrementDerefCount(subject, derefIriSubjectCount));

        iriPredicates.forEach(predicate -> incrementDerefCount(predicate, derefIriPredicateCount));

        iriObjects.forEach(object-> incrementDerefCount(object, derefIriObjectCount));

    }


    private void incrementDerefCount(String uri, AtomicInteger derefCount) {
        HttpResponse response = responseMap.get(uri);
        if (response == null) {
            logger.error("Response is null - '{}'", uri);
        } else if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
            derefCount.getAndIncrement();
        }

    }

    private double getPercentage(int count, int total) {

        //This shouldn't be necessary
        if (total == 0) {
            return 0;
        }
        return new BigDecimal(((double) count / total) * 100).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();

    }


}
