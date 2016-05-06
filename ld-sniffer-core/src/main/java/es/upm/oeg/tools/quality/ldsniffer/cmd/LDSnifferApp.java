package es.upm.oeg.tools.quality.ldsniffer.cmd;

import es.upm.oeg.tools.quality.ldsniffer.eval.Executor;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
public class LDSnifferApp {

    private static final Options OPTIONS = new Options();

    private static int evaluationTimeout;

    private static final Logger logger = LoggerFactory.getLogger(LDSnifferApp.class);

    static {

        OPTIONS.addOption(Option.builder("ul")
                .longOpt("uriList")
                .desc("The path of the file containing the urls of resources to  be assessed")
                .hasArg()
                .argName("URI-FILE-PATH")
                .required()
                .build());

        OPTIONS.addOption(Option.builder("tdb")
                .longOpt("tdb")
                .desc("The path of directory for Jena TDB files ")
                .hasArg()
                .argName("TDB-DIR-PATH")
                .required()
                .build());

        OPTIONS.addOption(Option.builder("t")
                .longOpt("timeout")
                .desc("Timeout in minutesfor a single evaluation")
                .hasArg()
                .argName("T-MINS")
                .type(Integer.class)
                .build());


        OPTIONS.addOption(Option.builder("h")
                .longOpt("help")
                .desc("Print this help message")
                .build());

    }

    public static void main(String[] args) {

        HelpFormatter help = new HelpFormatter();
        String header = "Assess a list of Linked Data resources using Linked Data Quality Model.";
        String footer = "Please report issues at https://github.com/nandana/ld-sniffer";

        try {
            CommandLine line = parseArguments(args);
            if (line.hasOption("help")) {
                help.printHelp("LDSnifferApp", header, OPTIONS, footer, true);
                System.exit(0);
            }

            evaluationTimeout = Integer.parseInt(line.getOptionValue("t", "10"));

            logger.info("URL List: " + line.getOptionValue("ul"));
            logger.info("TDB Path: " + line.getOptionValue("tdb"));
            logger.info("Timeout (mins): " + evaluationTimeout);

            Executor executor = new Executor(line.getOptionValue("tdb"), line.getOptionValue("ul"));
            executor.execute();

        } catch (MissingOptionException e) {
            help.printHelp("LDSnifferApp", header, OPTIONS, footer, true);
            logger.error("Missing arguments.  Reason: "  + e.getMessage(), e);
            System.exit(1);
        } catch (ParseException e ) {
            logger.error("Parsing failed.  Reason: "  + e.getMessage(), e);
            System.exit(1);
        } catch (IOException e) {
            logger.error("Execution failed.  Reason: "  + e.getMessage(), e);
            System.exit(1);
        }

    }

    public static int getEvaluationTimeout() {
        return evaluationTimeout;
    }

    protected static CommandLine parseArguments(String[] args) throws ParseException {

        CommandLineParser parser = new DefaultParser();
        return parser.parse(OPTIONS, args);
    }



}
