package es.upm.oeg.tools.quality.ldsniffer.vocab;

import com.hp.hpl.jena.rdf.model.Resource;

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
public class LDQM extends VocabBase {

    public static final Resource Numberoftriples = m_model.createResource( "http://linkeddata.es/resource/ldqm/BaseMeasure/Numberoftriples" );

    public static final Resource IRIdereferenceability = m_model.createResource( "http://linkeddata.es/resource/ldqm/BaseMeasure/IRIdereferenceability" );

    public static final Resource Numberofdistinctiris = m_model.createResource( "http://linkeddata.es/resource/ldqm/BaseMeasure/Numberofdistinctiris" );

    public static final Resource Numberofdistinctsubjects = m_model.createResource( "http://linkeddata.es/resource/ldqm/BaseMeasure/Numberofdistinctsubjects" );

    public static final Resource Numberofdistinctpredicates = m_model.createResource( "http://linkeddata.es/resource/ldqm/BaseMeasure/Numberofdistinctpredicates" );

    public static final Resource Numberofdistinctobjects = m_model.createResource( "http://linkeddata.es/resource/ldqm/BaseMeasure/Numberofdistinctobjects" );

    public static final Resource Numberofdereferenceableiris = m_model.createResource("http://linkeddata.es/resource/ldqm/DerivedMeasure/Numberofdereferenceableiris");

    public static final Resource Numberofdereferenceablesubjects = m_model.createResource("http://linkeddata.es/resource/ldqm/DerivedMeasure/Numberofdereferenceablesubjects");

    public static final Resource Numberofdereferenceablepredicates = m_model.createResource("http://linkeddata.es/resource/ldqm/DerivedMeasure/Numberofdereferenceablepredicates");

    public static final Resource Numberofdereferenceableobjects = m_model.createResource("http://linkeddata.es/resource/ldqm/DerivedMeasure/Numberofdereferenceableobjects");

    public static final Resource Averageiridereferenceability = m_model.createResource("http://linkeddata.es/resource/ldqm/QualityIndicator/Averageiridereferenceability");

    public static final Resource Averagesubjectdereferenceability = m_model.createResource("http://linkeddata.es/resource/ldqm/QualityIndicator/Averagesubjectdereferenceability");

    public static final Resource Averagepredicatedereferenceability = m_model.createResource("http://linkeddata.es/resource/ldqm/QualityIndicator/Averagepredicatedereferenceability");

    public static final Resource Averageobjectdereferenceability = m_model.createResource("http://linkeddata.es/resource/ldqm/QualityIndicator/Averageobjectdereferenceability");

    public static final Resource Scale_Interval = m_model.createResource( "http://linkeddata.es/resource/ldqm/Scale/Interval" );

    public static final Resource Scale_Ratio = m_model.createResource( "http://linkeddata.es/resource/ldqm/Scale/Ratio" );

    public static final Resource Scale_Ordinal = m_model.createResource( "http://linkeddata.es/resource/ldqm/Scale/Ordinal" );

    public static final Resource Scale_Percentage_HigherBest = m_model.createResource( "http://linkeddata.es/resource/ldqm/Scale/percentageHigherBest" );

    public static final Resource Scale_Count_HigherBest = m_model.createResource( "http://linkeddata.es/resource/ldqm/Scale/countHigherBest" );

    public static final Resource Dimension_Accessibility = m_model.createResource( "http://linkeddata.es/resource/ldqm/Dimension/Accessibility" );

    public static final Resource Dimension_Availability = m_model.createResource( "http://linkeddata.es/resource/ldqm/Dimension/Availability" );

    public static final Resource Defect_UndereferenceableURI = m_model.createResource("http://linkeddata.es/resource/ldqm/Defect/UndereferenceableURI");

    public static final Resource AT_SPARQL_Query = m_model.createResource("http://linkeddata.es/resource/ldqm/AssessmentTechnique/SPARQLQuery");

    public static final Resource AT_Url_Dereferencing = m_model.createResource("http://linkeddata.es/resource/ldqm/AssessmentTechnique/UrlDereferencing");



    public static final String MEASURE_PREFIX = "http://linkeddata.es/resource/ldqm/measure/";

    public static final String QUALITY_REPORT_PREFIX = "http://linkeddata.es/resource/ldqm/report/";






}
