package es.upm.oeg.tools.quality.ldsniffer.model;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.*;
import es.upm.oeg.tools.quality.ldsniffer.vocab.*;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
public class AssessmentResult {

    private final String subject;

    private long numberOfDistinctURIs;

    private long numberOfDereferenceableURIs;

    private long numberOfDereferenceableSubjects = -1;

    private long numberOfSubjects = -1;

    private double avgSubjectDereferenceablility = -1;

    private long numberOfDereferenceablePredicts;

    private long getNumberOfDereferenceableObjects;

    private HttpResponse[] errURIs;

    private Set<HttpResponse> errSubjects = new HashSet<>();

    private HttpResponse[] errPredicats;

    private HttpResponse[] errObjects;

    public AssessmentResult(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public long getNumberOfDistinctURIs() {
        return numberOfDistinctURIs;
    }

    public void setNumberOfDistinctURIs(long numberOfDistinctURIs) {
        this.numberOfDistinctURIs = numberOfDistinctURIs;
    }

    public long getNumberOfDereferenceableURIs() {
        return numberOfDereferenceableURIs;
    }

    public void setNumberOfDereferenceableURIs(long numberOfDereferenceableURIs) {
        this.numberOfDereferenceableURIs = numberOfDereferenceableURIs;
    }

    public long getNumberOfDereferenceableSubjects() {
        return numberOfDereferenceableSubjects;
    }

    public void setNumberOfDereferenceableSubjects(long numberOfDereferenceableSubjects) {
        this.numberOfDereferenceableSubjects = numberOfDereferenceableSubjects;
    }

    public long getNumberOfDereferenceablePredicts() {
        return numberOfDereferenceablePredicts;
    }

    public void setNumberOfDereferenceablePredicts(long numberOfDereferenceablePredicts) {
        this.numberOfDereferenceablePredicts = numberOfDereferenceablePredicts;
    }

    public long getGetNumberOfDereferenceableObjects() {
        return getNumberOfDereferenceableObjects;
    }

    public void setGetNumberOfDereferenceableObjects(long getNumberOfDereferenceableObjects) {
        this.getNumberOfDereferenceableObjects = getNumberOfDereferenceableObjects;
    }

    public long getNumberOfSubjects() {
        return numberOfSubjects;
    }

    public void setNumberOfSubjects(long numberOfSubjects) {
        this.numberOfSubjects = numberOfSubjects;
    }

    public HttpResponse[] getErrURIs() {
        return errURIs;
    }

    public void setErrURIs(HttpResponse[] errURIs) {
        this.errURIs = errURIs;
    }

    public Set<HttpResponse> getErrSubjects() {
        return errSubjects;
    }

    public void setErrSubjects(Set<HttpResponse> errSubjects) {
        this.errSubjects = errSubjects;
    }

    public synchronized void addErrSubject(HttpResponse errSubject) {
        errSubjects.add(errSubject);
    }

    public double getAvgSubjectDereferenceablility() {
        return avgSubjectDereferenceablility;
    }

    public void setAvgSubjectDereferenceablility(double avgSubjectDereferenceablility) {
        this.avgSubjectDereferenceablility = avgSubjectDereferenceablility;
    }

    public HttpResponse[] getErrPredicats() {
        return errPredicats;
    }

    public void setErrPredicats(HttpResponse[] errPredicats) {
        this.errPredicats = errPredicats;
    }

    public HttpResponse[] getErrObjects() {
        return errObjects;
    }

    public void setErrObjects(HttpResponse[] errObjects) {
        this.errObjects = errObjects;
    }

    public String toRDF() {

        Model model = ModelFactory.createDefaultModel();
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
        model.setNsPrefix("xsd", XSD.getURI());
        model.setNsPrefix("owl", OWL.getURI());
        model.setNsPrefix("dqm", "http://www.diachron-fp7.eu/dqm#");
        model.setNsPrefix("derivedmeasure", "http://linkeddata.es/resource/ldqm/DerivedMeasure/");
        model.setNsPrefix("basemeasure", "http://linkeddata.es/resource/ldqm/BaseMeasure/");
        model.setNsPrefix("indicator", "http://linkeddata.es/resource/ldqm/QualityIndicator/");
        model.setNsPrefix("category", "http://linkeddata.es/resource/ldqm/SubjectCategory/");
        model.setNsPrefix("defect", "http://linkeddata.es/resource/ldqm/Defect/");
        model.setNsPrefix("scale", "http://linkeddata.es/resource/ldqm/Scale/");


        Resource evaluationSubject = model.createResource(subject);
        evaluationSubject.addProperty(DC.title, "Evaluation subject, the Linked Data resource '" + subject + "'.");

        Resource assessmentTechnique = model.createResource();
        assessmentTechnique.addProperty(RDF.type, LDQ.AssessmentTechnique);
        assessmentTechnique.addLiteral(LDQ.isSubjective, false);
        assessmentTechnique.addProperty(LDQ.hasAutomationLevel, LDQ.Automatic);
        assessmentTechnique.addLiteral(DC.description, "Each URI is dereferenced using the HTTP HEAD method, and if fails " +
                "using the HTTP GET method. Resources with 2XX responses (after redirection) are considered dereferenceable.");

        Resource uriCountAssessmentTechnique = model.createResource();
        uriCountAssessmentTechnique.addProperty(RDF.type, LDQ.AssessmentTechnique);
        uriCountAssessmentTechnique.addLiteral(LDQ.isSubjective, false);
        uriCountAssessmentTechnique.addProperty(LDQ.hasAutomationLevel, LDQ.Automatic);
        uriCountAssessmentTechnique.addLiteral(DC.description, "Distinct URI counts for a given RDF graph are calculated " +
                "with a SPARQL query ");

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"){
            public Date parse(String source, ParsePosition pos) {
                return super.parse(source.replaceFirst(":(?=[0-9]{2}$)",""),pos);
            }
        };

        Resource evaluation = model.createResource();
        evaluation.addProperty(RDF.type, EVAL.Evaluation);
        evaluation.addProperty(RDF.type, PROV.Activity);
        evaluation.addLiteral(EVAL.performedOn, ResourceFactory.createTypedLiteral( df.format(new Date()), XSDDatatype.XSDdateTime));
        evaluation.addLiteral(EVAL.evaluatedSubject, evaluationSubject);
        evaluation.addProperty(LDQ.utilizes, uriCountAssessmentTechnique);
        evaluation.addProperty(LDQ.utilizes, assessmentTechnique);

        Resource ratioScale_0_100 = model.createResource();
        ratioScale_0_100.addProperty(RDF.type, OM.Ratio_scale);
        ratioScale_0_100.addLiteral(QMO.hasLowerBoundary, 0);
        ratioScale_0_100.addLiteral(QMO.hasUpperBoundary, 100);
        ratioScale_0_100.addLiteral(QMO.hasRankingFunction, QMO.Ranking_HigherBest);

        Resource ratioScale_0 = model.createResource();
        ratioScale_0_100.addProperty(RDF.type, OM.Ratio_scale);
        ratioScale_0_100.addLiteral(QMO.hasLowerBoundary, 0);
        ratioScale_0_100.addLiteral(QMO.hasRankingFunction, QMO.Ranking_HigherBest);

        Resource availabilityDimension = model.createResource(LDQM.Dimension_Availability.getURI());
        availabilityDimension.addProperty(RDF.type, QMO.QualityCharacteristic);
        availabilityDimension.addProperty(RDF.type, DQV.Dimension);
        availabilityDimension.addProperty(RDF.type, DAQ.Dimension);
        availabilityDimension.addProperty(DC.title, "Availability dimension / characteristic.");
        availabilityDimension.addProperty(SKOS.prefLabel, "Availability dimension / characteristic.");
        availabilityDimension.addProperty(SKOS.definition, "Availability of a dataset is the extent to which data (or some portion of it) is present, obtainable and ready for use.");
        availabilityDimension.addProperty(OWL.sameAs, model.createResource("http://www.diachron-fp7.eu/dqm#Availability"));


        Resource subjectCategory = model.createResource("http://linkeddata.es/resource/ldqm/SubjectCategory/LinkedDataResource");
        subjectCategory.addProperty(RDF.type, EVAL.SubjectCategory);
        availabilityDimension.addProperty(QMO.isCharacteristicOf, subjectCategory);
        evaluationSubject.addProperty(EVAL.belongsToCategory, subjectCategory);

        Resource subjectsMetric = model.createResource(LDQM.Numberofdistinctsubjects.getURI());
        subjectsMetric.addProperty(RDF.type, QMO.BaseMeasure);
        subjectsMetric.addProperty(RDF.type, QMO.QualityMeasure);
        subjectsMetric.addProperty(RDF.type, DQV.Metric);
        subjectsMetric.addProperty(QMO.hasScale, ratioScale_0);
        subjectsMetric.addProperty(LDQ.hasGranularity, LDQ.Graph);
        subjectsMetric.addProperty(LDQ.hasGranularity, LDQ.Dataset);
        subjectsMetric.addProperty(DC.title, "Number of distinct subjects");
        subjectsMetric.addLiteral(SKOS.prefLabel, model.createLiteral("Number of distinct subjects", "en"));
        subjectsMetric.addProperty(DC.description, "Number of distinct subjects in a graph or dataset.");
        subjectsMetric.addProperty(SKOS.definition, "select (count(distinct ?s) as ?c) where {?s ?p ?o FILTER (isIRI(?s))}");
        subjectsMetric.addProperty(DQV.expectedDataType, XSD.positiveInteger);
        subjectsMetric.addProperty(LDQ.calculatedWith, uriCountAssessmentTechnique);

        Resource numberOfSubjectsMeasure = model.createResource();
        numberOfSubjectsMeasure.addProperty(RDF.type, DQV.QualityMeasurement);
        numberOfSubjectsMeasure.addProperty(RDF.type, DAQ.Observation);
        numberOfSubjectsMeasure.addProperty(RDF.type, EVAL.QualityValue);
        numberOfSubjectsMeasure.addProperty(DQV.computedOn, model.createResource(subject));
        numberOfSubjectsMeasure.addProperty(EVAL.obtainedFrom, evaluation);
        numberOfSubjectsMeasure.addProperty(PROV.wasGeneratedBy, evaluation);
        numberOfSubjectsMeasure.addProperty(DQV.isMeasurementOf, subjectsMetric);
        numberOfSubjectsMeasure.addLiteral(DAQ.isEstimate, false);
        numberOfSubjectsMeasure.addLiteral(DQV.value, numberOfSubjects);
        numberOfSubjectsMeasure.addLiteral(EVAL.hasLiteralValue, numberOfSubjects);
        numberOfSubjectsMeasure.addProperty(EVAL.forMeasure, subjectsMetric);


        Resource derefSubjectsMetric = model.createResource(LDQM.Numberofdereferenceablesubjects.getURI());
        derefSubjectsMetric.addProperty(RDF.type, QMO.DerivedMeasure);
        derefSubjectsMetric.addProperty(RDF.type, QMO.QualityMeasure);
        derefSubjectsMetric.addProperty(RDF.type, DQV.Metric);
        derefSubjectsMetric.addProperty(QMO.hasScale, ratioScale_0);
        derefSubjectsMetric.addProperty(LDQ.hasGranularity, LDQ.Graph);
        derefSubjectsMetric.addProperty(LDQ.hasGranularity, LDQ.Dataset);
        derefSubjectsMetric.addProperty(DC.title, "Number of dereferenceable subjects");
        derefSubjectsMetric.addLiteral(SKOS.prefLabel, model.createLiteral("Number of dereferenceable subjects", "en"));
        derefSubjectsMetric.addProperty(DC.description, "Number of dereferenceable subjects in a triple, dataset, or a graph.");
        derefSubjectsMetric.addProperty(SKOS.definition, "Count the number of subject URIs that return a successful HTTP response.");
        derefSubjectsMetric.addProperty(DQV.expectedDataType, XSD.positiveInteger);
        derefSubjectsMetric.addProperty(LDQ.calculatedWith, assessmentTechnique);

        Resource numberOfDerefSubjects = model.createResource();
        numberOfDerefSubjects.addProperty(RDF.type, DQV.QualityMeasurement);
        numberOfDerefSubjects.addProperty(RDF.type, DAQ.Observation);
        numberOfDerefSubjects.addProperty(RDF.type, EVAL.QualityValue);
        numberOfDerefSubjects.addProperty(DQV.computedOn, model.createResource(subject));
        numberOfDerefSubjects.addProperty(EVAL.obtainedFrom, evaluation);
        numberOfDerefSubjects.addProperty(PROV.wasGeneratedBy, evaluation);
        numberOfDerefSubjects.addProperty(DQV.isMeasurementOf, derefSubjectsMetric);
        numberOfDerefSubjects.addLiteral(DAQ.isEstimate, false);
        numberOfDerefSubjects.addLiteral(DQV.value, numberOfDereferenceableSubjects);
        numberOfDerefSubjects.addLiteral(EVAL.hasLiteralValue, numberOfDereferenceableSubjects);
        numberOfDerefSubjects.addProperty(EVAL.forMeasure, derefSubjectsMetric);


        Resource avgDerefSubjectsMetric = model.createResource(LDQM.AverageSubjectdereferenceability.getURI());
        avgDerefSubjectsMetric.addProperty(RDF.type, QMO.QualityIndicator);
        avgDerefSubjectsMetric.addProperty(RDF.type, QMO.QualityMeasure);
        avgDerefSubjectsMetric.addProperty(RDF.type, DQV.Metric);
        avgDerefSubjectsMetric.addProperty(QMO.hasScale, ratioScale_0_100);
        avgDerefSubjectsMetric.addProperty(LDQ.hasGranularity, LDQ.Graph);
        avgDerefSubjectsMetric.addProperty(LDQ.hasGranularity, LDQ.Dataset);
        avgDerefSubjectsMetric.addProperty(LDQ.hasAspect, LDQ.LinkedDataServer);
        avgDerefSubjectsMetric.addProperty(PROV.wasDerivedFrom, LDQM.Numberofdereferenceablesubjects);
        avgDerefSubjectsMetric.addProperty(PROV.wasDerivedFrom, LDQM.Numberofdistinctsubjects);
        avgDerefSubjectsMetric.addProperty(DQV.inDimension, LDQM.Dimension_Availability);
        avgDerefSubjectsMetric.addProperty(QMO.measuresCharacteristic, LDQM.Dimension_Availability);
        //avgDerefSubjectsMetric.addProperty(QMO.hasUnitOfMeasurement, model.createResource("http://this.we.have.to.decided.com/test2"));
        avgDerefSubjectsMetric.addProperty(DC.title, "Average subject dereferenceability");
        avgDerefSubjectsMetric.addLiteral(SKOS.prefLabel, model.createLiteral("Average subject dereferenceability", "en"));
        avgDerefSubjectsMetric.addProperty(DC.description, "Average number of dereferenceable subjects in a triple, dataset, or a graph.");
        avgDerefSubjectsMetric.addProperty(SKOS.definition, "(Number of dereferenceable subjects / Number of distinct subjects) * 100 %");
        avgDerefSubjectsMetric.addProperty(DQV.expectedDataType, XSD.xdouble);


        availabilityDimension.addProperty(QMO.isMeasuredWith, avgDerefSubjectsMetric);


        Resource avgDerefSubjects = model.createResource();
        avgDerefSubjects.addProperty(RDF.type, DQV.QualityMeasurement);
        avgDerefSubjects.addProperty(RDF.type, DAQ.Observation);
        avgDerefSubjects.addProperty(RDF.type, EVAL.QualityValue);
        avgDerefSubjects.addProperty(DQV.computedOn, model.createResource(subject));
        avgDerefSubjects.addProperty(EVAL.obtainedFrom, evaluation);
        avgDerefSubjects.addProperty(PROV.wasGeneratedBy, evaluation);
        avgDerefSubjects.addProperty(DQV.isMeasurementOf, avgDerefSubjectsMetric);
        avgDerefSubjects.addLiteral(DAQ.isEstimate, false);

        avgSubjectDereferenceablility = (((double) numberOfDereferenceableSubjects / numberOfSubjects) * 100);

        avgDerefSubjects.addLiteral(DQV.value, avgSubjectDereferenceablility);
        avgDerefSubjects.addLiteral(EVAL.hasLiteralValue, avgSubjectDereferenceablility);
        avgDerefSubjects.addProperty(EVAL.forMeasure, avgDerefSubjectsMetric);

        Resource qualityReport = model.createResource(QPRO.QualityReport);
        qualityReport.addProperty(QPRO.computedOn, ResourceFactory.createTypedLiteral( df.format(new Date()), XSDDatatype.XSDdateTime));

        Resource noDerefSubjectsProblem = model.createResource(QPRO.QualityProblem);
        noDerefSubjectsProblem.addProperty(QPRO.isDescribedBy, avgDerefSubjectsMetric);
        for (HttpResponse response : errSubjects) {
            Resource errSubject = model.createResource(LDQM.Defect_UndereferenceableURI);
            errSubject.addProperty(RDF.subject,response.getUri());
            errSubject.addLiteral(HTTP.statusCodeValue, response.getStatusCode());
            errSubject.addLiteral(HTTP.methodName, response.getMethod());
        }
        qualityReport.addProperty(QPRO.hasProblem, noDerefSubjectsProblem);

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        model.write(os, "TURTLE");
        return os.toString();

    }



}
