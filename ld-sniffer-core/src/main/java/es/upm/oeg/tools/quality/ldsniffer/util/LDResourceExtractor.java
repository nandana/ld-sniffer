package es.upm.oeg.tools.quality.ldsniffer.util;

import com.google.common.collect.ImmutableMap;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.RDFNode;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
public class LDResourceExtractor {

    public static final String LITERAL_BINDINGS  = "literal";
    public static final String IRI_BINDINGS  = "iri";

    public static final String OFFSET_PARAM = "offset";

    public static final String LIMIT_PARAM = "limit";

    private static String isntancesQueryPath = "src/main/resources/query/individualsOfClass.sparql";

    private static String isntancesQuery;

    static {

        try {
            isntancesQuery = readFile(isntancesQueryPath, Charset.defaultCharset());
        } catch (IOException ex) {
            throw new IllegalStateException("Error loading the instance query");
        }
    }


    public static List<String> extractInstances(String sparqlEndpoint, String clazz) throws IOException {

        ArrayList<String> list = new ArrayList<>();


        Map<String, String> litMap = new HashMap<>();
        Map<String, String> iriMap = ImmutableMap.of("class", clazz);

        String queryString = bindQueryString(isntancesQuery, ImmutableMap.of(IRI_BINDINGS, iriMap, LITERAL_BINDINGS, litMap));


        List<RDFNode> nodeList = executeQueryForList(queryString, sparqlEndpoint, "s");
        for (RDFNode clazzNode : nodeList) {
            if (clazzNode.isURIResource()) {
                list.add(clazzNode.asResource().getURI());
            }
        }

        return list;
    }

    public static void writeToFile(List<String> list, Path path) throws IOException {

        BufferedWriter writer =
                Files.newBufferedWriter( path, Charset.defaultCharset(),
                        StandardOpenOption.CREATE);
        for (String listItem : list) {
            writer.write(listItem);
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }

    public static List<RDFNode> executeQueryForList(String queryString, String serviceEndpoint, String var) {


        List<RDFNode> resultsList = new ArrayList<>();

        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.sparqlService(serviceEndpoint, query)) {
            {
                ResultSet results = qexec.execSelect();

                // collect all the values
                for (; results.hasNext(); ) {
                    QuerySolution soln = results.nextSolution();
                    if (soln.contains(var)) {
                        resultsList.add(soln.get(var));
                    }
                }

                return resultsList;
            }
        }
    }

    public static String bindQueryString(String queryString, Map<String, Map<String, String>> bindings){

        ParameterizedSparqlString pss = new ParameterizedSparqlString();
        pss.setCommandText(queryString);

        if (bindings.containsKey(LITERAL_BINDINGS)) {
            Map<String, String> litBindings = bindings.get(LITERAL_BINDINGS);
            for(Map.Entry<String, String> binding : litBindings.entrySet()) {
                String key = binding.getKey();
                if(OFFSET_PARAM.equals(key) || LIMIT_PARAM.equals(key) ){
                    pss.setLiteral(key, Integer.parseInt(binding.getValue()));
                } else {
                    pss.setLiteral(key, binding.getValue());
                }
            }
        }

        if (bindings.containsKey(IRI_BINDINGS)) {
            Map<String, String> iriBindings = bindings.get(IRI_BINDINGS);
            for(Map.Entry<String, String> binding : iriBindings.entrySet()) {
                pss.setIri(binding.getKey(), binding.getValue());
            }
        }

        return pss.toString();

    }

    public static String readFile(String path, Charset encoding)
            throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static void main(String[] args) throws Exception {

        String endpoint = "http://infra2.dia.fi.upm.es:8899/sparql";
        String clazz = "http://dbpedia.org/ontology/Work";
        String clazzLocal = "Work";
        Path path = FileSystems.getDefault().getPath("src/main/resources/data/class/" + clazzLocal + ".txt");

        List<String> individualList = extractInstances(endpoint, clazz);
        writeToFile(individualList, path);

    }



}
