package es.upm.oeg.tools.quality.ldsniffer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import es.upm.oeg.tools.quality.ldsniffer.model.AssessmentResult;
import es.upm.oeg.tools.quality.ldsniffer.model.HttpResponse;
import es.upm.oeg.tools.quality.ldsniffer.query.QueryUtils;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.net.ConnectException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static es.upm.oeg.tools.quality.ldsniffer.query.QueryCollection.*;

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
//@Controller
//@RequestMapping("/assess")
public class LdSnifferController {

//    private static final Logger logger = LoggerFactory.getLogger(LdSnifferController.class);
//
//    @RequestMapping(method= RequestMethod.GET)
//    public @ResponseBody AssessmentResult assess(@RequestParam(value="uri", required=true) String uri) {
//
//
//        final AssessmentResult assessmentResult = new AssessmentResult(uri);
//
//
//        Model model = ModelFactory.createDefaultModel();
//        //TODO try to limit the size of the file being loaded
//        try {
//            model.read(uri);
//        } catch (Exception e) {
//            throw new BadRequestException(String.format("Unable to read the content from the uri - %s \n(%s)", uri, e.getMessage()), e);
//        }
//
//        final int totalTriples = QueryUtils.getCountAsC(model, TOTAL_TRIPLES);
//        System.out.println("URI:" + uri);
//        System.out.println("Total triples: " + totalTriples);
//
//        final List<String> iriSubjects = QueryUtils.getIriList(model, DISTINCT_IRI_SUBJECTS);
//        final List<String> iriPredicates = QueryUtils.getIriList(model, DISTINCT_IRI_PREDICATES);
//        final List<String> iriObjects = QueryUtils.getIriList(model, DISTINCT_IRI_OBJECTS);
//
//        final Set<String> iriSet = new HashSet<>();
//        iriSet.addAll(iriSubjects);
//        iriSet.addAll(iriPredicates);
//        iriSet.addAll(iriObjects);
//
//        System.out.println("Distinct IRIs: " + iriSet.size());
//        System.out.println("Distinct Subject IRIs: " + iriSubjects.size());
//        System.out.println("Distinct Predicate IRIs:" + iriPredicates.size());
//        System.out.println("Distinct Object IRIs:" + iriObjects.size());
//
//        final Map<String, HttpResponse> responseMap = new HashMap<>();
//
//        final ExecutorService executor = Executors.newFixedThreadPool(5);
//
//        AtomicInteger derefIriCount = new AtomicInteger(0);
//        iriSet.forEach(iri -> {
//                executor.submit( () -> {
//                    CloseableHttpClient httpclient = HttpClients.createDefault();
//                    HttpHead head = new HttpHead(iri);
//                    String method = "HEAD";
//
//                    try (CloseableHttpResponse response = httpclient.execute(head);) {
//                        StatusLine statusLine = response.getStatusLine();
//                        int statusCode = statusLine.getStatusCode();
//                        if (statusCode == HttpStatus.SC_METHOD_NOT_ALLOWED ||
//                                statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
//                            HttpGet get = new HttpGet(iri);
//                            method = "GET";
//                            try (CloseableHttpResponse getResponse = httpclient.execute(get);) {
//                                statusLine = getResponse.getStatusLine();
//                                statusCode = statusLine.getStatusCode();
//                            }
//                        }
//                        responseMap.put(iri, new HttpResponse(iri, method, statusCode,statusLine.getReasonPhrase(), false));
//                        if(statusCode >= 200 && statusCode < 300){
//                            derefIriCount.getAndIncrement();
//                        }
//
//                    } catch (ConnectException e) {
//                        logger.error("Connection timed out ...", e);
//                        responseMap.put(iri, new HttpResponse(iri, method, -1, e.getMessage(), false));
//                    } catch (IOException e) {
//                        logger.error("IO error occurred ..", e);
//                        responseMap.put(iri, new HttpResponse(iri, method, -1, e.getMessage(), false));
//                    }
//                });
//        });
//
//        executor.shutdown();
//        try {
//            executor.awaitTermination(5, TimeUnit.MINUTES);
//        } catch (InterruptedException e) {
//            throw new ServerError("Interrupted ...");
//        }
//
//        final AtomicInteger derefIriSubjectCount =  new AtomicInteger(0);
//        iriSubjects.forEach(subject -> {
//            HttpResponse response = responseMap.get(subject);
//            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
//                derefIriSubjectCount.getAndIncrement();
//            }
//        });
//
//        final AtomicInteger derefIriPredicateCount =  new AtomicInteger(0);
//        iriPredicates.forEach(predicate -> {
//            HttpResponse response = responseMap.get(predicate);
//            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
//                derefIriPredicateCount.getAndIncrement();
//            }
//        });
//
//        final AtomicInteger derefIriObjectCount =  new AtomicInteger(0);
//        iriPredicates.forEach(object-> {
//            HttpResponse response = responseMap.get(object);
//            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
//                derefIriObjectCount.getAndIncrement();
//            }
//        });
//
//        System.out.println("Deref. IRIs: " + derefIriCount.get());
//        System.out.println("Deref. Subject IRIs: " + derefIriSubjectCount.get());
//        System.out.println("Deref. Predicate IRIs:" + derefIriPredicateCount.get());
//        System.out.println("Deref. Object IRIs:" + derefIriObjectCount.get());
//
//        return assessmentResult;
//    }

}