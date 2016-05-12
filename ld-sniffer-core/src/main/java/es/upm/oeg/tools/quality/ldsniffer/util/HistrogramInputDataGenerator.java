package es.upm.oeg.tools.quality.ldsniffer.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static es.upm.oeg.tools.quality.ldsniffer.util.SparqlUtils.*;

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
public class HistrogramInputDataGenerator {

    private static final String HIST_QUERY_PATH = "src/main/resources/query/iriDereferenciabilityHistrogram.spaql";
    private static final String HIST_QUERY_STRING;

    static {
        try {
            HIST_QUERY_STRING = SparqlUtils.readFile(HIST_QUERY_PATH, Charset.defaultCharset());
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid query string path : " + HIST_QUERY_PATH, e);
        }

    }


    public static void histrogram(String SPARQLEndpoint, List<String> metrics) {


        int min = 100;
        int max = 0;

        Map<String, Map<Integer, Long>> histrograms = new HashMap<>();
        for (String metric : metrics) {
            Map<String, String> litMap = new HashMap<>();
            Map<String, String> iriMap = ImmutableMap.of("metric", metric);
            String queryString = bindQueryString(HIST_QUERY_STRING, ImmutableMap.of(IRI_BINDINGS, iriMap, LITERAL_BINDINGS, litMap));

            List<Map<String, RDFNode>> results = executeQueryForList(queryString, SPARQLEndpoint, ImmutableSet.of("valueInt", "count"));

            Map<Integer, Long> resultMap = new HashMap<>();

            long sum = 0;
            long countTotal = 0;


            for (Map<String, RDFNode> result : results) {
                int value = result.get("valueInt").asLiteral().getInt();
                long count = result.get("count").asLiteral().getLong();
                resultMap.put(value, count);

                long subTotal = value * count;
                sum += subTotal;
                countTotal += count;

                //set min and max
                if (value < min) {
                    min = value;
                } else if (value > max) {
                    max = value;
                }
            }

            double average = ((double)(sum)) / countTotal;

            System.out.println(metric + " : total : " + sum);
            System.out.println(metric + " : count : " + countTotal);
            System.out.println(metric + " : abg : " + average);

            histrograms.put(metric, resultMap);
        }

        StringBuffer buffer = new StringBuffer();
        buffer.append("data.addRows([");
        for (int avgValue = min; avgValue < max ; avgValue++) {
            long[] value = new long[metrics.size()];

            for (int metricID = 0;  metricID < metrics.size();  metricID++) {
                Map<Integer, Long> metricResultMap = histrograms.get(metrics.get(metricID));
                value[metricID] = metricResultMap.containsKey(avgValue) ? metricResultMap.get(avgValue) : 0;
            }

            buffer.append("[");
            buffer.append(avgValue + ",");
            for (int metricID = 0;  metricID < (metrics.size() - 1);  metricID++) {
                buffer.append(value[metricID] + ",");
            }
            buffer.append(value[(metrics.size() - 1)] + "],");
            buffer.append("\n");
        }

        System.out.println(buffer.toString());

    }


    public static void main(String[] args) {


        String endpoint = "http://infra2.dia.fi.upm.es:8899/sparql";
        String iri = "http://linkeddata.es/resource/ldqm/QualityIndicator/Averageiridereferenceability";
        String subject = "http://linkeddata.es/resource/ldqm/QualityIndicator/Averagesubjectdereferenceability";
        String predicate = "http://linkeddata.es/resource/ldqm/QualityIndicator/Averagepredicatedereferenceability";
        String object = "http://linkeddata.es/resource/ldqm/QualityIndicator/Averageobjectdereferenceability";

        histrogram(endpoint, ImmutableList.of(iri, subject, predicate, object));

    }


}
