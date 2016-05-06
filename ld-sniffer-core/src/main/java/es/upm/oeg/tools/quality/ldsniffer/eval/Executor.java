package es.upm.oeg.tools.quality.ldsniffer.eval;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.tdb.TDBFactory;
import com.hp.hpl.jena.vocabulary.*;
import es.upm.oeg.tools.quality.ldsniffer.model.HttpResponse;
import es.upm.oeg.tools.quality.ldsniffer.vocab.*;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static es.upm.oeg.tools.quality.ldsniffer.query.QueryCollection.TOTAL_TRIPLES;

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
public class Executor {

    private static Cache<String, HttpResponse> cache;

    private static CacheManager cacheManager;

    private static final Logger logger = LoggerFactory.getLogger(Executor.class);

    private String tdbDirectory;

    private String urlListPath;

    public Executor(String tdbDirectory, String urlListPath) throws IOException {

        this.tdbDirectory = tdbDirectory;
        this.urlListPath = urlListPath;


//        new Evaluation("http://dbpedia.org/resource/Galle", dataset).process();

//        FileWriter writer = new FileWriter(resultsPath);
//        dataset.begin(ReadWrite.READ) ;
//            Model model = dataset.getDefaultModel() ;
//            final int totalTriples = QueryUtils.getCountAsC(model, TOTAL_TRIPLES);
//            System.out.println("Totoal triples :" + totalTriples);
//
//            model.write(writer, "TURTLE");
//            writer.flush();
//            writer.close();
//        dataset.end();

    }


    public void execute() throws IOException {

        Path path = Paths.get(urlListPath);

        logger.info("Path : " + path.toAbsolutePath().toString());
        logger.info("Path exits : " + Files.exists(path));

        List<String> urlList = Files.
                readAllLines(path, Charset.defaultCharset());

        cacheManager
                = CacheManagerBuilder.newCacheManagerBuilder()
                .withCache("uriCache",
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, HttpResponse.class))
                .build();
        cacheManager.init();

        cache = cacheManager.getCache("uriCache", String.class, HttpResponse.class);

        Dataset dataset = TDBFactory.createDataset(tdbDirectory) ;
        dataset.begin(ReadWrite.WRITE) ;
        try {
            Model model = dataset.getDefaultModel();
            setPrefixes(model);
            //addDimensions(model);
            //addScales(model);
            //addTechniques(model);
            //addBaseMeasures(model);
            //addDerivedMeasures(model);
            //addIndicators(model);
            dataset.commit();
        } finally {
            dataset.end();
        }

        urlList.forEach(url -> {
            try {
                new Evaluation(url, dataset).process();
            } catch (Exception e) {
                logger.error("Evaluation of '{}' failed with the exception {}" + e.getMessage());
                e.printStackTrace();
            }
        });

        cacheManager.removeCache("uriCache");
        cacheManager.close();;

    }

    private static void setPrefixes(Model model) {

        model.setNsPrefix(DAQ.PREFIX, DAQ.NS);
        model.setNsPrefix(DQV.PREFIX, DQV.NS);
        model.setNsPrefix(QMO.PREFIX, QMO.NS);
        model.setNsPrefix(LDQ.PREFIX, LDQ.NS);
        model.setNsPrefix(EVAL.PREFIX, EVAL.NS);
        model.setNsPrefix(SKOS.PREFIX, SKOS.NS);
        model.setNsPrefix(PROV.PREFIX, PROV.NS);
        model.setNsPrefix(QPRO.PREFIX, QPRO.NS);
        model.setNsPrefix(HTTP.PREFIX, HTTP.NS);
        model.setNsPrefix(OM.PREFIX, OM.NS);
        model.setNsPrefix("rdf", RDF.getURI());
        model.setNsPrefix("dc", DC_11.getURI());
        model.setNsPrefix("dcterms", DCTerms.getURI());
        model.setNsPrefix("xsd", XSD.getURI());
        model.setNsPrefix("owl", OWL.getURI());
        model.setNsPrefix("dqm", "http://www.diachron-fp7.eu/dqm#");
        model.setNsPrefix("dbr", "http://dbpedia.org/resource/");
        model.setNsPrefix("uri4uri", "http://uri4uri.net/vocab#");
        model.setNsPrefix("derivedmeasure", "http://linkeddata.es/resource/ldqm/DerivedMeasure/");
        model.setNsPrefix("basemeasure", "http://linkeddata.es/resource/ldqm/BaseMeasure/");
        model.setNsPrefix("indicator", "http://linkeddata.es/resource/ldqm/QualityIndicator/");
        model.setNsPrefix("category", "http://linkeddata.es/resource/ldqm/SubjectCategory/");
        model.setNsPrefix("dimension", "http://linkeddata.es/resource/ldqm/Dimension/");
        model.setNsPrefix("defect", "http://linkeddata.es/resource/ldqm/Defect/");
        model.setNsPrefix("scale", "http://linkeddata.es/resource/ldqm/Scale/");
        model.setNsPrefix("technique", "http://linkeddata.es/resource/ldqm/AssessmentTechnique/");
        model.setNsPrefix("measure", "http://linkeddata.es/resource/ldqm/measure/");
        model.setNsPrefix("evaluation", "http://linkeddata.es/resource/ldqm/evaluation/");
        model.setNsPrefix("report", "http://linkeddata.es/resource/ldqm/report/");

    }

    private static void addDimensions(Model model) {

        Resource accessibilityDimension = model.createResource(LDQM.Dimension_Accessibility.getURI());
        accessibilityDimension.addProperty(RDF.type, QMO.QualityCharacteristic);
        accessibilityDimension.addProperty(RDF.type, DQV.Dimension);
        accessibilityDimension.addProperty(RDF.type, DAQ.Dimension);
        accessibilityDimension.addLiteral(DC.title,  model.createLiteral("Accessibility quality characteristic (dimension)", "en"));
        accessibilityDimension.addProperty(SKOS.prefLabel, model.createLiteral("Accessibility quality characteristic (dimension)", "en"));
        accessibilityDimension.addProperty(SKOS.definition, model.createLiteral("The degree to which data can be accessed in a specific context of use."));
        accessibilityDimension.addProperty(OWL.sameAs, model.createResource("http://www.diachron-fp7.eu/dqm#Accessibility"));

        accessibilityDimension.addProperty(QMO.isMeasuredWith, LDQM.Averageiridereferenceability);
        accessibilityDimension.addProperty(QMO.isMeasuredWith, LDQM.Averagesubjectdereferenceability);
        accessibilityDimension.addProperty(QMO.isMeasuredWith, LDQM.Averagepredicatedereferenceability);
        accessibilityDimension.addProperty(QMO.isMeasuredWith, LDQM.Averageobjectdereferenceability);

    }


    private static void addBaseMeasures(Model model) {

        Resource tripleCountMetric = model.createResource(LDQM.Numberoftriples.getURI());
        tripleCountMetric.addProperty(RDF.type, QMO.BaseMeasure);
        tripleCountMetric.addProperty(RDF.type, QMO.QualityMeasure);
        tripleCountMetric.addProperty(RDF.type, DQV.Metric);
        tripleCountMetric.addProperty(QMO.hasScale, LDQM.Scale_Interval);
        tripleCountMetric.addProperty(LDQ.hasGranularity, LDQ.Graph);
        tripleCountMetric.addProperty(LDQ.hasGranularity, LDQ.Dataset);
        tripleCountMetric.addProperty(DC.title, model.createLiteral("Number of triples", "en"));
        tripleCountMetric.addLiteral(SKOS.prefLabel, model.createLiteral("Number of triples", "en"));
        tripleCountMetric.addProperty(DC.description, model.createLiteral("Number of triples in a graph, or dataset.", "en"));
        tripleCountMetric.addProperty(SKOS.definition, "SPARQL query: 'select (count(*) as ?c) where {?s ?p ?o}'");
        tripleCountMetric.addProperty(DQV.expectedDataType, XSD.positiveInteger);
        tripleCountMetric.addProperty(LDQ.calculatedWith, LDQM.AT_SPARQL_Query);
        tripleCountMetric.addProperty(DQV.inDimension, LDQM.Dimension_Accessibility);

        Resource iriCountMetric = model.createResource(LDQM.Numberofdistinctiris.getURI());
        iriCountMetric.addProperty(RDF.type, QMO.BaseMeasure);
        iriCountMetric.addProperty(RDF.type, QMO.QualityMeasure);
        iriCountMetric.addProperty(RDF.type, DQV.Metric);
        iriCountMetric.addProperty(QMO.hasScale, LDQM.Scale_Count_HigherBest);
        iriCountMetric.addProperty(LDQ.hasGranularity, LDQ.Triple);
        iriCountMetric.addProperty(LDQ.hasGranularity, LDQ.Graph);
        iriCountMetric.addProperty(LDQ.hasGranularity, LDQ.Dataset);
        iriCountMetric.addProperty(DC.title, model.createLiteral("Number of distinct IRIs", "en"));
        iriCountMetric.addLiteral(SKOS.prefLabel, model.createLiteral("Number of distinct IRIs", "en"));
        iriCountMetric.addProperty(DC.description, model.createLiteral("Number of distinct IRIs in a triple, graph, or dataset.", "en"));
        iriCountMetric.addProperty(SKOS.definition, "SPARQL query: '' ");
        iriCountMetric.addProperty(DQV.expectedDataType, XSD.positiveInteger);
        iriCountMetric.addProperty(LDQ.calculatedWith, LDQM.AT_SPARQL_Query);
        iriCountMetric.addProperty(DQV.inDimension, LDQM.Dimension_Accessibility);


        Resource subjectsCountMetric = model.createResource(LDQM.Numberofdistinctsubjects.getURI());
        subjectsCountMetric.addProperty(RDF.type, QMO.BaseMeasure);
        subjectsCountMetric.addProperty(RDF.type, QMO.QualityMeasure);
        subjectsCountMetric.addProperty(RDF.type, DQV.Metric);
        subjectsCountMetric.addProperty(QMO.hasScale, LDQM.Scale_Count_HigherBest);
        subjectsCountMetric.addProperty(LDQ.hasGranularity, LDQ.Graph);
        subjectsCountMetric.addProperty(LDQ.hasGranularity, LDQ.Dataset);
        subjectsCountMetric.addProperty(DC.title, model.createLiteral("Number of distinct IRI subjects", "en"));
        subjectsCountMetric.addLiteral(SKOS.prefLabel, model.createLiteral("Number of distinct IRI subjects", "en"));
        subjectsCountMetric.addProperty(DC.description, model.createLiteral("Number of distinct IRI subjects in a graph or dataset.", "en"));
        subjectsCountMetric.addProperty(SKOS.definition, "SPARQL query: 'select (count(distinct ?s) as ?c) where {?s ?p ?o FILTER (isIRI(?s))}'");
        subjectsCountMetric.addProperty(DQV.expectedDataType, XSD.positiveInteger);
        subjectsCountMetric.addProperty(LDQ.calculatedWith, LDQM.AT_SPARQL_Query);
        subjectsCountMetric.addProperty(DQV.inDimension, LDQM.Dimension_Accessibility);

        Resource predicateCountMetric = model.createResource(LDQM.Numberofdistinctpredicates.getURI());
        predicateCountMetric.addProperty(RDF.type, QMO.BaseMeasure);
        predicateCountMetric.addProperty(RDF.type, QMO.QualityMeasure);
        predicateCountMetric.addProperty(RDF.type, DQV.Metric);
        predicateCountMetric.addProperty(QMO.hasScale, LDQM.Scale_Count_HigherBest);
        predicateCountMetric.addProperty(LDQ.hasGranularity, LDQ.Graph);
        predicateCountMetric.addProperty(LDQ.hasGranularity, LDQ.Dataset);
        predicateCountMetric.addProperty(DC.title, model.createLiteral("Number of distinct predicates", "en"));
        predicateCountMetric.addLiteral(SKOS.prefLabel, model.createLiteral("Number of distinct predicates", "en"));
        predicateCountMetric.addProperty(DC.description, model.createLiteral("Number of distinct predicates in a graph or dataset.", "en"));
        predicateCountMetric.addProperty(SKOS.definition, "SPARQL query: 'select (count(distinct ?p) as ?c) where {?s ?p ?o FILTER (isIRI(?p))}'");
        predicateCountMetric.addProperty(DQV.expectedDataType, XSD.positiveInteger);
        predicateCountMetric.addProperty(LDQ.calculatedWith, LDQM.AT_SPARQL_Query);
        predicateCountMetric.addProperty(DQV.inDimension, LDQM.Dimension_Accessibility);

        Resource objectsCountMetric = model.createResource(LDQM.Numberofdistinctobjects.getURI());
        objectsCountMetric.addProperty(RDF.type, QMO.BaseMeasure);
        objectsCountMetric.addProperty(RDF.type, QMO.QualityMeasure);
        objectsCountMetric.addProperty(RDF.type, DQV.Metric);
        objectsCountMetric.addProperty(QMO.hasScale, LDQM.Scale_Count_HigherBest);
        objectsCountMetric.addProperty(LDQ.hasGranularity, LDQ.Graph);
        objectsCountMetric.addProperty(LDQ.hasGranularity, LDQ.Dataset);
        objectsCountMetric.addProperty(DC.title, model.createLiteral("Number of distinct IRI objects", "en"));
        objectsCountMetric.addLiteral(SKOS.prefLabel, model.createLiteral("Number of distinct IRI objects", "en"));
        objectsCountMetric.addProperty(DC.description, model.createLiteral("Number of distinct IRI objects in a graph or dataset.", "en"));
        objectsCountMetric.addProperty(SKOS.definition, "SPARQL query: 'select (count(distinct ?o) as ?c) where {?s ?p ?o FILTER (isIRI(?o))}'");
        objectsCountMetric.addProperty(DQV.expectedDataType, XSD.positiveInteger);
        objectsCountMetric.addProperty(LDQ.calculatedWith, LDQM.AT_SPARQL_Query);
        objectsCountMetric.addProperty(DQV.inDimension, LDQM.Dimension_Accessibility);

        Resource iriDereferenceabilityMetric = model.createResource(LDQM.IRIdereferenceability.getURI());
        iriDereferenceabilityMetric.addProperty(RDF.type, QMO.BaseMeasure);
        iriDereferenceabilityMetric.addProperty(RDF.type, QMO.QualityMeasure);
        iriDereferenceabilityMetric.addProperty(RDF.type, DQV.Metric);
        iriDereferenceabilityMetric.addProperty(QMO.hasScale, LDQM.Scale_Ordinal);
        iriDereferenceabilityMetric.addProperty(LDQ.hasGranularity, LDQ.Iri);
        iriDereferenceabilityMetric.addProperty(DC.title, model.createLiteral("IRI Dereferenceability", "en"));
        iriDereferenceabilityMetric.addLiteral(SKOS.prefLabel, model.createLiteral("IRI Dereferenceability", "en"));
        iriDereferenceabilityMetric.addProperty(DC.description, model.createLiteral("If an IRI can be dereferenced using HTTP protocol.", "en"));
        iriDereferenceabilityMetric.addProperty(SKOS.definition, "True if the IRI returns a successful (2xx) HTTP response (after redirection), false otherwise.");
        iriDereferenceabilityMetric.addProperty(DQV.expectedDataType, XSD.xboolean);
        iriDereferenceabilityMetric.addProperty(LDQ.calculatedWith, LDQM.AT_Url_Dereferencing);
        iriDereferenceabilityMetric.addProperty(DQV.inDimension, LDQM.Dimension_Accessibility);


    }

    private static void addDerivedMeasures(Model model) {

        Resource derefIriMetric = model.createResource(LDQM.Numberofdereferenceableiris.getURI());
        derefIriMetric.addProperty(RDF.type, QMO.DerivedMeasure);
        derefIriMetric.addProperty(RDF.type, QMO.QualityMeasure);
        derefIriMetric.addProperty(RDF.type, DQV.Metric);
        derefIriMetric.addProperty(QMO.hasScale, LDQM.Scale_Count_HigherBest);
        derefIriMetric.addProperty(LDQ.hasGranularity, LDQ.Triple);
        derefIriMetric.addProperty(LDQ.hasGranularity, LDQ.Graph);
        derefIriMetric.addProperty(LDQ.hasGranularity, LDQ.Dataset);
        derefIriMetric.addProperty(DC.title, model.createLiteral("Number of dereferenceable IRIs", "en"));
        derefIriMetric.addLiteral(SKOS.prefLabel, model.createLiteral("Number of dereferenceable IRIs", "en"));
        derefIriMetric.addProperty(DC.description, model.createLiteral("Number of dereferenceable IRIs in a triple, dataset, or a graph.", "en"));
        derefIriMetric.addProperty(SKOS.definition, model.createLiteral("Count the number of IRIs that return a successful (2xx) HTTP response (after redirection).", "en"));
        derefIriMetric.addProperty(DQV.expectedDataType, XSD.positiveInteger);
        derefIriMetric.addProperty(LDQ.calculatedWith, LDQM.AT_Url_Dereferencing);
        derefIriMetric.addProperty(DQV.inDimension, LDQM.Dimension_Accessibility);

        Resource derefSubjectsMetric = model.createResource(LDQM.Numberofdereferenceablesubjects.getURI());
        derefSubjectsMetric.addProperty(RDF.type, QMO.DerivedMeasure);
        derefSubjectsMetric.addProperty(RDF.type, QMO.QualityMeasure);
        derefSubjectsMetric.addProperty(RDF.type, DQV.Metric);
        derefSubjectsMetric.addProperty(QMO.hasScale, LDQM.Scale_Count_HigherBest);
        derefSubjectsMetric.addProperty(LDQ.hasGranularity, LDQ.Triple);
        derefSubjectsMetric.addProperty(LDQ.hasGranularity, LDQ.Graph);
        derefSubjectsMetric.addProperty(LDQ.hasGranularity, LDQ.Dataset);
        derefSubjectsMetric.addProperty(DC.title, model.createLiteral("Number of dereferenceable IRI subjects", "en"));
        derefSubjectsMetric.addLiteral(SKOS.prefLabel, model.createLiteral("Number of dereferenceable IRI subjects", "en"));
        derefSubjectsMetric.addProperty(DC.description, model.createLiteral("Number of dereferenceable IRI subjects in a triple, dataset, or a graph.", "en"));
        derefSubjectsMetric.addProperty(SKOS.definition, model.createLiteral("Count the number of subject IRIs that return a successful(2xx) HTTP response (after redirection).", "en"));
        derefSubjectsMetric.addProperty(DQV.expectedDataType, XSD.positiveInteger);
        derefSubjectsMetric.addProperty(LDQ.calculatedWith, LDQM.AT_Url_Dereferencing);
        derefSubjectsMetric.addProperty(DQV.inDimension, LDQM.Dimension_Accessibility);

        Resource derefPredicatesMetric = model.createResource(LDQM.Numberofdereferenceablepredicates.getURI());
        derefPredicatesMetric.addProperty(RDF.type, QMO.DerivedMeasure);
        derefPredicatesMetric.addProperty(RDF.type, QMO.QualityMeasure);
        derefPredicatesMetric.addProperty(RDF.type, DQV.Metric);
        derefPredicatesMetric.addProperty(QMO.hasScale, LDQM.Scale_Count_HigherBest);
        derefPredicatesMetric.addProperty(LDQ.hasGranularity, LDQ.Triple);
        derefPredicatesMetric.addProperty(LDQ.hasGranularity, LDQ.Graph);
        derefPredicatesMetric.addProperty(LDQ.hasGranularity, LDQ.Dataset);
        derefPredicatesMetric.addProperty(DC.title, model.createLiteral("Number of dereferenceable IRI predicates", "en"));
        derefPredicatesMetric.addLiteral(SKOS.prefLabel, model.createLiteral("Number of dereferenceable IRI predicates", "en"));
        derefPredicatesMetric.addProperty(DC.description, model.createLiteral("Number of dereferenceable IRI predicates in a triple, dataset, or a graph.", "en"));
        derefPredicatesMetric.addProperty(SKOS.definition, model.createLiteral("Count the number of predicates IRIs that return a successful(2xx) HTTP response (after redirection).", "en"));
        derefPredicatesMetric.addProperty(DQV.expectedDataType, XSD.positiveInteger);
        derefPredicatesMetric.addProperty(LDQ.calculatedWith, LDQM.AT_Url_Dereferencing);
        derefPredicatesMetric.addProperty(DQV.inDimension, LDQM.Dimension_Accessibility);

        Resource derefObjectsMetric = model.createResource(LDQM.Numberofdereferenceableobjects.getURI());
        derefObjectsMetric.addProperty(RDF.type, QMO.DerivedMeasure);
        derefObjectsMetric.addProperty(RDF.type, QMO.QualityMeasure);
        derefObjectsMetric.addProperty(RDF.type, DQV.Metric);
        derefObjectsMetric.addProperty(QMO.hasScale, LDQM.Scale_Count_HigherBest);
        derefObjectsMetric.addProperty(LDQ.hasGranularity, LDQ.Triple);
        derefObjectsMetric.addProperty(LDQ.hasGranularity, LDQ.Graph);
        derefObjectsMetric.addProperty(LDQ.hasGranularity, LDQ.Dataset);
        derefObjectsMetric.addProperty(DC.title, model.createLiteral("Number of dereferenceable IRI predicates", "en"));
        derefObjectsMetric.addLiteral(SKOS.prefLabel, model.createLiteral("Number of dereferenceable IRI predicates", "en"));
        derefObjectsMetric.addProperty(DC.description, model.createLiteral("Number of dereferenceable IRI predicates in a triple, dataset, or a graph.", "en"));
        derefObjectsMetric.addProperty(SKOS.definition, model.createLiteral("Count the number of predicates IRIs that return a successful(2xx) HTTP response (after redirection).", "en"));
        derefObjectsMetric.addProperty(DQV.expectedDataType, XSD.positiveInteger);
        derefObjectsMetric.addProperty(LDQ.calculatedWith, LDQM.AT_Url_Dereferencing);
        derefObjectsMetric.addProperty(DQV.inDimension, LDQM.Dimension_Accessibility);

    }

    private static void addIndicators(Model model) {

        Resource avgDerefIriMetric = model.createResource(LDQM.Averagesubjectdereferenceability.getURI());
        avgDerefIriMetric.addProperty(RDF.type, QMO.QualityIndicator);
        avgDerefIriMetric.addProperty(RDF.type, QMO.QualityMeasure);
        avgDerefIriMetric.addProperty(RDF.type, DQV.Metric);
        avgDerefIriMetric.addProperty(QMO.hasScale, LDQM.Scale_Percentage_HigherBest);
        avgDerefIriMetric.addProperty(LDQ.hasGranularity, LDQ.Graph);
        avgDerefIriMetric.addProperty(LDQ.hasGranularity, LDQ.Dataset);
        avgDerefIriMetric.addProperty(LDQ.hasAspect, LDQ.LinkedDataServer);
        avgDerefIriMetric.addProperty(PROV.wasDerivedFrom, LDQM.Numberofdereferenceablesubjects);
        avgDerefIriMetric.addProperty(PROV.wasDerivedFrom, LDQM.Numberofdistinctsubjects);
        avgDerefIriMetric.addProperty(DQV.inDimension, LDQM.Dimension_Availability);
        avgDerefIriMetric.addProperty(QMO.measuresCharacteristic, LDQM.Dimension_Availability);
        avgDerefIriMetric.addProperty(DC.title, model.createLiteral("Average IRI dereferenceability", "en"));
        avgDerefIriMetric.addLiteral(SKOS.prefLabel, model.createLiteral("Average IRI dereferenceability", "en"));
        avgDerefIriMetric.addProperty(DC.description, model.createLiteral("Average of dereferenceable IRIs in a triple, dataset, or a graph.", "en"));
        avgDerefIriMetric.addProperty(SKOS.definition, "(Number of dereferenceable IRIs/ Number of distinct IRIs) * 100 %");
        avgDerefIriMetric.addProperty(DQV.expectedDataType, XSD.xdouble);
        avgDerefIriMetric.addProperty(DQV.inDimension, LDQM.Dimension_Accessibility);

        Resource avgDerefSubjectsMetric = model.createResource(LDQM.Averagesubjectdereferenceability.getURI());
        avgDerefSubjectsMetric.addProperty(RDF.type, QMO.QualityIndicator);
        avgDerefSubjectsMetric.addProperty(RDF.type, QMO.QualityMeasure);
        avgDerefSubjectsMetric.addProperty(RDF.type, DQV.Metric);
        avgDerefSubjectsMetric.addProperty(QMO.hasScale, LDQM.Scale_Percentage_HigherBest);
        avgDerefSubjectsMetric.addProperty(LDQ.hasGranularity, LDQ.Graph);
        avgDerefSubjectsMetric.addProperty(LDQ.hasGranularity, LDQ.Dataset);
        avgDerefSubjectsMetric.addProperty(LDQ.hasAspect, LDQ.LinkedDataServer);
        avgDerefSubjectsMetric.addProperty(PROV.wasDerivedFrom, LDQM.Numberofdereferenceablesubjects);
        avgDerefSubjectsMetric.addProperty(PROV.wasDerivedFrom, LDQM.Numberofdistinctsubjects);
        avgDerefSubjectsMetric.addProperty(DQV.inDimension, LDQM.Dimension_Availability);
        avgDerefSubjectsMetric.addProperty(QMO.measuresCharacteristic, LDQM.Dimension_Availability);
        avgDerefSubjectsMetric.addProperty(DC.title, model.createLiteral("Average IRI subject dereferenceability", "en"));
        avgDerefSubjectsMetric.addLiteral(SKOS.prefLabel, model.createLiteral("Average IRI subject dereferenceability", "en"));
        avgDerefSubjectsMetric.addProperty(DC.description, model.createLiteral("Average of dereferenceable IRI subjects in a triple, dataset, or a graph.", "en"));
        avgDerefSubjectsMetric.addProperty(SKOS.definition, "(Number of dereferenceable IRI subjects / Number of distinct IRI subjects) * 100 %");
        avgDerefSubjectsMetric.addProperty(DQV.expectedDataType, XSD.xdouble);
        avgDerefSubjectsMetric.addProperty(DQV.inDimension, LDQM.Dimension_Accessibility);

        Resource avgDerefPredicateMetric = model.createResource(LDQM.Averagepredicatedereferenceability.getURI());
        avgDerefPredicateMetric.addProperty(RDF.type, QMO.QualityIndicator);
        avgDerefPredicateMetric.addProperty(RDF.type, QMO.QualityMeasure);
        avgDerefPredicateMetric.addProperty(RDF.type, DQV.Metric);
        avgDerefPredicateMetric.addProperty(QMO.hasScale, LDQM.Scale_Percentage_HigherBest);
        avgDerefPredicateMetric.addProperty(LDQ.hasGranularity, LDQ.Graph);
        avgDerefPredicateMetric.addProperty(LDQ.hasGranularity, LDQ.Dataset);
        avgDerefPredicateMetric.addProperty(LDQ.hasAspect, LDQ.LinkedDataServer);
        avgDerefPredicateMetric.addProperty(PROV.wasDerivedFrom, LDQM.Numberofdereferenceablesubjects);
        avgDerefPredicateMetric.addProperty(PROV.wasDerivedFrom, LDQM.Numberofdistinctsubjects);
        avgDerefPredicateMetric.addProperty(DQV.inDimension, LDQM.Dimension_Availability);
        avgDerefPredicateMetric.addProperty(QMO.measuresCharacteristic, LDQM.Dimension_Availability);
        avgDerefPredicateMetric.addProperty(DC.title, model.createLiteral("Average IRI predicate dereferenceability", "en"));
        avgDerefPredicateMetric.addLiteral(SKOS.prefLabel, model.createLiteral("Average IRI predicate dereferenceability", "en"));
        avgDerefPredicateMetric.addProperty(DC.description, model.createLiteral("Average of dereferenceable IRI predicates in a triple, dataset, or a graph.", "en"));
        avgDerefPredicateMetric.addProperty(SKOS.definition, "(Number of dereferenceable IRI predicates / Number of distinct IRI predicates) * 100 %");
        avgDerefPredicateMetric.addProperty(DQV.expectedDataType, XSD.xdouble);
        avgDerefPredicateMetric.addProperty(DQV.inDimension, LDQM.Dimension_Accessibility);

        Resource avgDerefObjectMetric = model.createResource(LDQM.Averageobjectdereferenceability.getURI());
        avgDerefObjectMetric.addProperty(RDF.type, QMO.QualityIndicator);
        avgDerefObjectMetric.addProperty(RDF.type, QMO.QualityMeasure);
        avgDerefObjectMetric.addProperty(RDF.type, DQV.Metric);
        avgDerefObjectMetric.addProperty(QMO.hasScale, LDQM.Scale_Percentage_HigherBest);
        avgDerefObjectMetric.addProperty(LDQ.hasGranularity, LDQ.Graph);
        avgDerefObjectMetric.addProperty(LDQ.hasGranularity, LDQ.Dataset);
        avgDerefObjectMetric.addProperty(LDQ.hasAspect, LDQ.LinkedDataServer);
        avgDerefObjectMetric.addProperty(PROV.wasDerivedFrom, LDQM.Numberofdereferenceablesubjects);
        avgDerefObjectMetric.addProperty(PROV.wasDerivedFrom, LDQM.Numberofdistinctsubjects);
        avgDerefObjectMetric.addProperty(DQV.inDimension, LDQM.Dimension_Availability);
        avgDerefObjectMetric.addProperty(QMO.measuresCharacteristic, LDQM.Dimension_Availability);
        avgDerefObjectMetric.addProperty(DC.title, model.createLiteral("Average IRI predicate dereferenceability", "en"));
        avgDerefObjectMetric.addLiteral(SKOS.prefLabel, model.createLiteral("Average IRI predicate dereferenceability", "en"));
        avgDerefObjectMetric.addProperty(DC.description, model.createLiteral("Average of dereferenceable IRI predicates in a triple, dataset, or a graph.", "en"));
        avgDerefObjectMetric.addProperty(SKOS.definition, "(Number of dereferenceable IRI predicates / Number of distinct IRI predicates) * 100 %");
        avgDerefObjectMetric.addProperty(DQV.expectedDataType, XSD.xdouble);
        avgDerefObjectMetric.addProperty(DQV.inDimension, LDQM.Dimension_Accessibility);

    }



    private static void addTechniques(Model model) {

        Resource uriCountAssessmentTechnique = model.createResource(LDQM.AT_SPARQL_Query.getURI());
        uriCountAssessmentTechnique.addProperty(RDF.type, LDQ.AssessmentTechnique);
        uriCountAssessmentTechnique.addLiteral(LDQ.isSubjective, false);
        uriCountAssessmentTechnique.addProperty(LDQ.hasAutomationLevel, LDQ.Automatic);
        uriCountAssessmentTechnique.addLiteral(DC.description, model.createLiteral("Distinct IRI counts for a given RDF graph are calculated " +
                "with a SPARQL query. This technique is used to count distinct iris, subjects, predicates, objects, etc.", "en"));

        Resource assessmentTechnique = model.createResource(LDQM.AT_Url_Dereferencing.getURI());
        assessmentTechnique.addProperty(RDF.type, LDQ.AssessmentTechnique);
        assessmentTechnique.addLiteral(LDQ.isSubjective, false);
        assessmentTechnique.addProperty(LDQ.hasAutomationLevel, LDQ.Automatic);
        assessmentTechnique.addLiteral(DC.description, model.createLiteral("Each URI is dereferenced using the HTTP HEAD method, and if fails " +
                "using the HTTP GET method. Resources with 2XX responses (after redirection) are considered dereferenceable.", "en"));

    }

    private static void addScales(Model model) {

        Resource ratioScale_0_100 = model.createResource("http://linkeddata.es/resource/ldqm/Scale/percentageHigherBest");
        ratioScale_0_100.addProperty(RDF.type, OM.Ratio_scale);
        ratioScale_0_100.addLiteral(QMO.hasLowerBoundary, 0);
        ratioScale_0_100.addLiteral(QMO.hasUpperBoundary, 100);
        ratioScale_0_100.addProperty(QMO.hasRankingFunction, QMO.Ranking_HigherBest);

        Resource ratioScale_0 = model.createResource("http://linkeddata.es/resource/ldqm/Scale/countHigherBest");
        ratioScale_0_100.addProperty(RDF.type, OM.Ratio_scale);
        ratioScale_0_100.addLiteral(QMO.hasLowerBoundary, 0);
        ratioScale_0_100.addProperty(QMO.hasRankingFunction, QMO.Ranking_HigherBest);

    }

    public static void putCachedResponse(String uri, HttpResponse response) {
        if (cache != null) {
            cache.put(uri, response);
        } else {
            throw new IllegalStateException("Cache is null ...");
        }
    }

    public static HttpResponse getCachedResponse(String uri) {
        if (cache != null) {
            if (!cache.containsKey(uri)) {
                return null;
            } else {
                return cache.get(uri);
            }
        } else {
            throw new IllegalStateException("Cache is null ...");
        }
    }

    public static void main(String[] args) throws Exception {

        String dir = "src/main/resources/database/test";
        String urlFile = "src/main/resources/data/class/Person.txt";
        String resultsFile = "src/main/resources/results/Galle.ttl";

        Executor executor = new Executor(dir, urlFile);

    }

}
