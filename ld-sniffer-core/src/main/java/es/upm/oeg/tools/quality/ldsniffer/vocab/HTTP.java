package es.upm.oeg.tools.quality.ldsniffer.vocab;

import com.hp.hpl.jena.rdf.model.Property;
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
public class HTTP extends VocabBase {

    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://www.w3.org/2011/http#";

    public static final String PREFIX = "http";

    /** <p>The namespace of the vocabulary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}

    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );

    public static final Resource Response = m_model.createResource( NS + "Response" );
    public static final Resource Request = m_model.createResource( NS + "Request" );

    public static final Property statusCodeValue = m_model.createProperty( NS + "statusCodeValue" );
    public static final Property methodName = m_model.createProperty( NS + "methodName" );
    public static final Property reasonPhrase = m_model.createProperty( NS + "reasonPhrase" );
    public static final Property resp = m_model.createProperty( NS + "resp" );
    public static final Property httpVersion = m_model.createProperty( NS + "httpVersion" );


}
