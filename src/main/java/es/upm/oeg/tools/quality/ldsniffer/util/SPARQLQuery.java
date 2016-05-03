package es.upm.oeg.tools.quality.ldsniffer.util;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.RDFNode;
import es.upm.oeg.tools.quality.ldsniffer.query.QueryCollection;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
public class SPARQLQuery {

    public static void main(String[] args) {

        String  query = QueryCollection.TOTAL_TRIPLES;

        try (QueryExecution qexec = QueryExecutionFactory.sparqlService("http://localhost:3030/ldqm/query", query)) {
            {
                ResultSet results = qexec.execSelect();

                // We only return the first solutions, may be a map of map
                for (; results.hasNext(); ) {
                    QuerySolution soln = results.nextSolution();
                    Map<String, RDFNode> resultsMap = new HashMap<String, RDFNode>();

                        if (soln.contains("c")) {
                            System.out.println(soln.get("c").asLiteral().getInt());
                        }
                    }
                }
            }
        }
    }
