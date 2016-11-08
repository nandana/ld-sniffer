package es.upm.oeg.tools.quality.ldsniffer.webapp;

import com.google.common.base.Preconditions;
import es.upm.oeg.tools.quality.ldsniffer.cmd.LDSnifferApp;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.apache.jena.iri.impl.Specification.schemes;

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

@RestController
@EnableAutoConfiguration
public class EvalController {

    private static final Logger logger = LoggerFactory.getLogger(EvalController.class);

    final static UrlValidator urlValidator = new UrlValidator(new String[]{"http","https"});

    @RequestMapping("/eval")
    public ResponseEntity<String> eval(@RequestParam("uriList") String uriList,
                                       @RequestParam("metricList") String metricList,
                                       @RequestParam("metricDef") String metricDef,
                                       @RequestParam("timeout") String timeout) throws IOException {

        Preconditions.checkNotNull(uriList, "uriList parameter is required and can not be null");

        logger.info("uriList value : \n'{}'", uriList);

        // Evaluating the URI list
        String result = LDSnifferApp.eval(parseUriList(uriList), true, 10);

        // Creating the HTTP response
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(new MediaType("text", "turtle"));
        return new ResponseEntity<String>(result, responseHeaders, HttpStatus.OK);

    }


    /***
     * Parse the input of uriList, validate them and create a list of URIs
     * @param uriListParam
     * @return
     */
    public static List<String> parseUriList(String uriListParam) {

        List<String> uriList = new ArrayList<String>();

        String[] uriArray = uriListParam.split("[\\r\\n]+");

        //validate URIs
        for (String uri : uriArray) {

            //check for empty string and spaces
            if (uri.trim().length() == 0) {
                continue;
            }
            if(urlValidator.isValid(uri)) {
                uriList.add(uri);
            }else {
                throw new IllegalArgumentException("Invalid URL : " + uri);
            }
        }

        return uriList;

    }

}
