package es.upm.oeg.tools.quality.ldsniffer;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import es.upm.oeg.tools.quality.ldsniffer.model.AssessmentResult;
import es.upm.oeg.tools.quality.ldsniffer.model.HttpResponse;
import es.upm.oeg.tools.quality.ldsniffer.query.QueryUtils;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
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
@Controller
@RequestMapping("/assess")
public class LdSnifferController {

    @RequestMapping(method= RequestMethod.GET)
    public @ResponseBody AssessmentResult assess(@RequestParam(value="uri", required=true) String uri) {


        final AssessmentResult assessmentResult = new AssessmentResult(uri);


        Model model = ModelFactory.createDefaultModel();
        //TODO try to limit the size of the file being loaded
        try {
            model.read(uri);
        } catch (Exception e) {
            throw new BadRequestException(String.format("Unable to read the content from the uri - %s \n(%s)", uri, e.getMessage()), e);
        }

        final int totalTriples = QueryUtils.getCountAsC(model, TOTAL_TRIPLES);
        final int distinctIriSubjects = QueryUtils.getCountAsC(model, DISTINCT_IRI_SUBJECT_COUNT);
        final int distinctIriPredicates = QueryUtils.getCountAsC(model, DISTINCT_IRI_PREDICATES_COUNT);
        final int distinctIriObjects = QueryUtils.getCountAsC(model, DISTINCT_IRI_OBJECT_COUNT);

        assessmentResult.setNumberOfSubjects(distinctIriSubjects);

        final List<String> iriSubjects = QueryUtils.getIriList(model, DISTINCT_IRI_SUBJECTS);
        final List<String> iriPredicates = QueryUtils.getIriList(model, DISTINCT_IRI_PREDICATES);
        final List<String> iriObjects = QueryUtils.getIriList(model, DISTINCT_IRI_OBJECTS);

        final ExecutorService executor = Executors.newFixedThreadPool(5);

        final AtomicInteger derefSubjectCount = new AtomicInteger(0);
        iriSubjects.forEach(subject -> {
                executor.submit( () -> {
                    CloseableHttpClient httpclient = HttpClients.createDefault();
                    HttpHead head = new HttpHead(subject);
                    try (CloseableHttpResponse response = httpclient.execute(head);) {
                        StatusLine statusLine = response.getStatusLine();
                        final int statusCode = statusLine.getStatusCode();
                        if (statusCode >= 200 && statusCode < 300) {
                            derefSubjectCount.incrementAndGet();
                        } else {
                            assessmentResult.addErrSubject(new HttpResponse(subject, "HEAD", statusLine.getStatusCode(), statusLine.getReasonPhrase()));
                        }
                    } catch (IOException e) {
                        //TODO handle exception properly
                        throw new RuntimeException(e);
                    }
                });
        });

        iriPredicates.forEach(predicate -> {

            executor.submit( () -> {
                CloseableHttpClient httpclient = HttpClients.createDefault();
                HttpHead head = new HttpHead(predicate);
                try (CloseableHttpResponse response = httpclient.execute(head);) {
                    final int statusCode = response.getStatusLine().getStatusCode();

                } catch (IOException e) {
                    //TODO handle exception properly
                    throw new RuntimeException(e);
                }

            });
        });


        iriObjects.forEach(object -> {

            executor.submit( () -> {
                CloseableHttpClient httpclient = HttpClients.createDefault();
                HttpHead head = new HttpHead(object);
                try (CloseableHttpResponse response = httpclient.execute(head);) {
                    final int statusCode = response.getStatusLine().getStatusCode();

                } catch (IOException e) {
                    //TODO handle exception properly
                    throw new RuntimeException(e);
                }

            });
        });

        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new ServerError("Interrupted ...");
        }

        assessmentResult.setNumberOfDereferenceableSubjects(derefSubjectCount.get());

        String rdf = assessmentResult.toRDF();
        System.out.println(rdf);

        return assessmentResult;
    }

}