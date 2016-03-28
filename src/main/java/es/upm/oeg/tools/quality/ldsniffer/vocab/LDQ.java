package es.upm.oeg.tools.quality.ldsniffer.vocab;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class LDQ {

    /** <p>The RDF model that holds the vocabulary terms</p> */
    private static Model m_model = ModelFactory.createDefaultModel();

    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://www.linkeddata.es/ontology/ldq#";

    public static final String PREFIX = "ldq";

    /** <p>The namespace of the vocabulary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}

    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );


    //Classes

    public static final Resource AssessmentFrequency = m_model.createResource( NS + "AssessmentFrequency" );

    public static final Resource AssessmentTechnique = m_model.createResource( NS + "AssessmentTechnique" );

    public static final Resource AutomationLevel = m_model.createResource( NS + "AutomationLevel" );


    //Properties

    public static final Property about = m_model.createProperty( NS + "about" );
    public static final Property calculatedWith = m_model.createProperty( NS + "calculatedWith" );
    public static final Property computedOn = m_model.createProperty( NS + "computedOn" );
    public static final Property duration = m_model.createProperty( NS + "duration" );
    public static final Property frequency = m_model.createProperty( NS + "frequency" );
    public static final Property from = m_model.createProperty( NS + "from" );
    public static final Property hasAspect = m_model.createProperty( NS + "hasAspect" );
    public static final Property hasAssessmentFrequency = m_model.createProperty( NS + "hasAssessmentFrequency" );
    public static final Property hasAutomationLevel = m_model.createProperty( NS + "hasAutomationLevel" );
    public static final Property hasDuration = m_model.createProperty( NS + "hasDuration" );
    public static final Property hasGranularity = m_model.createProperty( NS + "hasGranularity" );
    public static final Property requires = m_model.createProperty( NS + "requires" );
    public static final Property toAssessOn = m_model.createProperty( NS + "toAssessOn" );
    public static final Property until = m_model.createProperty( NS + "until" );
    public static final Property utilizes = m_model.createProperty( NS + "utilizes" );
    public static final Property validDuring = m_model.createProperty( NS + "validDuring" );


    //Named individuals

    public static final Resource Automatic = m_model.createResource( NS + "Automatic" );
    public static final Resource Dataset = m_model.createResource( NS + "Dataset" );
    public static final Resource DomainData = m_model.createResource( NS + "DomainData" );
    public static final Resource FileServer = m_model.createResource( NS + "FileServer" );
    public static final Resource Graph = m_model.createResource( NS + "Graph" );

}
