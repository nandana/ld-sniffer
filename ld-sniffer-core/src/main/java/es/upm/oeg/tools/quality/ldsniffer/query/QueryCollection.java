package es.upm.oeg.tools.quality.ldsniffer.query;

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
public class QueryCollection {

    public static final String TOTAL_TRIPLES = "select (count(*) as ?c) where {?s ?p ?o}";

    public static final String DISTINCT_IRI_SUBJECT_COUNT = "select (count(distinct ?s) as ?c) where {?s ?p ?o FILTER (isIRI(?s)) }";

    public static final String DISTINCT_IRI_PREDICATES_COUNT = "select (count(distinct ?p) as ?c) where {?s ?p ?o FILTER (isIRI(?p)) }";

    public static final String DISTINCT_IRI_OBJECT_COUNT = "select (count(distinct ?o) as ?c) where {?s ?p ?o FILTER (isIRI(?o)) }";

    public static final String DISTINCT_IRI_SUBJECTS = "select distinct ?iri where {?iri ?p ?o FILTER (isIRI(?iri))}";

    public static final String DISTINCT_IRI_PREDICATES = "select distinct ?iri where {?s ?iri ?o FILTER (isIRI(?iri))}";

    public static final String DISTINCT_IRI_OBJECTS = "select distinct ?iri where {?s ?p ?iri FILTER (isIRI(?iri))}";


}
