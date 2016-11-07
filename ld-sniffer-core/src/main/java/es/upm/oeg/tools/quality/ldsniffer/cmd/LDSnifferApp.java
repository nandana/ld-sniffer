package es.upm.oeg.tools.quality.ldsniffer.cmd;

import es.upm.oeg.tools.quality.ldsniffer.eval.Executor;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
public class LDSnifferApp {

    private static final Options OPTIONS = new Options();

    private static int evaluationTimeout;

    private static boolean includeMetricDefinitions = false;

    private static boolean rdfOutput = false;

    private static final Logger logger = LoggerFactory.getLogger(LDSnifferApp.class);

    static {

        OPTIONS.addOption(Option.builder("ul")
                .longOpt("uriList")
                .desc("The path of the file containing the urls of resources to  be assessed")
                .hasArg()
                .argName("URI-FILE-PATH")
                .build());

        OPTIONS.addOption(Option.builder("url")
                .longOpt("url")
                .desc("URL of the resource to  be assessed")
                .hasArg()
                .argName("URL")
                .build());

        OPTIONS.addOption(Option.builder("ml")
                .longOpt("metricsList")
                .desc("The path of the file containing the list of metrics to be calculated")
                .hasArg()
                .argName("METRICS-FILE-PATH")
                .build());

        OPTIONS.addOption(Option.builder("tdb")
                .longOpt("tdb")
                .desc("The path of directory for Jena TDB files ")
                .hasArg()
                .argName("TDB-DIR-PATH")
                .build());

        OPTIONS.addOption(Option.builder("t")
                .longOpt("timeout")
                .desc("Timeout (in minutes) for a single evaluation")
                .hasArg()
                .argName("T-MINS")
                .type(Integer.class)
                .build());

        OPTIONS.addOption(Option.builder("md")
                .longOpt("metrics-definition")
                .desc("Include the metric definitions in the results")
                .build());

        OPTIONS.addOption(Option.builder("rdf")
                .longOpt("rdf-output")
                .desc("Output the RDF serialization of the results")
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

            if (line.hasOption("md")) {
                includeMetricDefinitions = true;
            }
            if (line.hasOption("rdf")) {
                rdfOutput = true;
            }

            logger.info("URL List: " + line.getOptionValue("ul"));
            logger.info("TDB Path: " + line.getOptionValue("tdb"));
            logger.info("Metrics Path: " + line.getOptionValue("ml"));
            logger.info("Include Metric definitions: " + line.getOptionValue("ml"));
            logger.info("RDF output: " + line.getOptionValue("rdf"));
            logger.info("Timeout (mins): " + evaluationTimeout);

            if (line.hasOption("ml")) {
                Path path = Paths.get(line.getOptionValue("ml"));
                if (!Files.exists(path)) {
                    throw new IOException( path.toAbsolutePath().toString() + " : File doesn't exit.");
                }
            }

            //Set the TDB path
            String tdbDirectory;
            if (line.hasOption("tdb")) {
                tdbDirectory = line.getOptionValue("tdb");
            } else {
                Path tempPath = Files.createTempDirectory("tdb_");
                tdbDirectory = tempPath.toAbsolutePath().toString();
            }

            // Create the URL list for the evaluation
            if (!line.hasOption("ul") && !line.hasOption("url")) {
                System.out.println("One of the following parameters are required: url or urlList ");
                help.printHelp("LDSnifferApp", header, OPTIONS, footer, true);
                System.exit(0);
            } else if (line.hasOption("ul") && line.hasOption("url")) {
                System.out.println("You have to specify either url or urlList, not both.");
                help.printHelp("LDSnifferApp", header, OPTIONS, footer, true);
                System.exit(0);
            }

            List<String> urlList = null;
            if (line.hasOption("ul")) {
                Path path = Paths.get(line.getOptionValue("ul"));
                logger.info("Path : " + path.toAbsolutePath().toString());
                logger.info("Path exits : " + Files.exists(path));
                urlList = Files.readAllLines(path, Charset.defaultCharset());
            } else if (line.hasOption("url")) {
                urlList = new ArrayList<>();
                urlList.add(line.getOptionValue("url"));
            }

            Executor executor = new Executor(tdbDirectory, urlList);
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

    public static String eval(List<String> urlList, boolean includeMetrics, int timeout) throws IOException {

        Path tempPath = Files.createTempDirectory("tdb_");
        String tdbDirectory = tempPath.toAbsolutePath().toString();

        evaluationTimeout = timeout;
        includeMetricDefinitions = includeMetrics;
        rdfOutput = true;

        Executor executor = new Executor(tdbDirectory, urlList);
        return executor.execute();

    }



    public static int getEvaluationTimeout() {
        return evaluationTimeout;
    }

    protected static CommandLine parseArguments(String[] args) throws ParseException {

        CommandLineParser parser = new DefaultParser();
        return parser.parse(OPTIONS, args);
    }

    public static boolean isIncludeMetricDefinitions() {
        return includeMetricDefinitions;
    }

    public static boolean isRdfOutput() {
        return rdfOutput;
    }
}
