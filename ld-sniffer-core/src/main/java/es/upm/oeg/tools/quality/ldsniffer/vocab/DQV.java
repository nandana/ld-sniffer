package es.upm.oeg.tools.quality.ldsniffer.vocab;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class DQV extends VocabBase {

    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://www.w3.org/ns/dqv#";

    public static final String PREFIX = "dqv";

    /** <p>The namespace of the vocabulary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}

    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );

    //Classes

    public static final Resource QualityMeasurement = m_model.createResource( NS + "QualityMeasurement" );

    public static final Resource Metric = m_model.createResource( NS + "Metric" );

    public static final Resource Dimension = m_model.createResource( NS + "Dimension" );

    public static final Resource Category = m_model.createResource( NS + "Category" );

    public static final Resource QualityPolicy = m_model.createResource( NS + "QualityPolicy" );

    public static final Resource QualityMetadata = m_model.createResource( NS + "QualityMetadata" );

    public static final Resource QualityAnnotation = m_model.createResource( NS + "QualityAnnotation" );

    public static final Resource QualityMeasurementDataset = m_model.createResource( NS + "QualityMeasurementDataset" );

    public static final Resource UserQualityFeedback = m_model.createResource( NS + "UserQualityFeedback" );

    public static final Resource QualityCertificate = m_model.createResource( NS + "QualityCertificate" );


    public static final Property computedOn = m_model.createProperty( NS + "computedOn" );
    public static final Property value = m_model.createProperty( NS + "value" );
    public static final Property isMeasurementOf = m_model.createProperty( NS + "isMeasurementOf" );
    public static final Property inCategory = m_model.createProperty( NS + "inCategory" );
    public static final Property inDimension = m_model.createProperty( NS + "inDimension" );
    public static final Property expectedDataType = m_model.createProperty( NS + "expectedDataType" );



}
