package es.upm.oeg.tools.quality.ldsniffer.query;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.Model;

import java.util.ArrayList;
import java.util.List;

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
public class QueryUtils {

    public static int getCountAsC(Model model, String queryString) {

        int count = -1;
        Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();

        for (; results.hasNext(); ) {
            QuerySolution soln = results.nextSolution();
            if (soln.contains("c")) {
                count = soln.get("c").asLiteral().getInt();
            }
        }
        // Important - free up resources used running the query
        qe.close();

        return count;
    }

    public static List<String> getIriList(Model model, String queryString){

        List<String> iriList = new ArrayList<>();

        Query query = QueryFactory.create(queryString);

        // Execute the query and obtain results
        QueryExecution qe = QueryExecutionFactory.create(query, model);
        ResultSet results = qe.execSelect();

        for (; results.hasNext(); ) {
            QuerySolution soln = results.nextSolution();
            if (soln.contains("iri") && soln.get("iri").isURIResource()) {
                iriList.add(soln.get("iri").asResource().getURI());
            }
        }
        // Important - free up resources used running the query
        qe.close();

        return iriList;

    }



}
