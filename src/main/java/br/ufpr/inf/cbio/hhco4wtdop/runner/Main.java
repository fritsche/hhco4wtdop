/*
 * Copyright (C) 2019 Gian Fritsche <gmfritsche at inf.ufpr.br>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.ufpr.inf.cbio.hhco4wtdop.runner;

import br.ufpr.inf.cbio.hhco.runner.methodology.Methodology;
import br.ufpr.inf.cbio.hhco.util.output.Utils;
import br.ufpr.inf.cbio.hhco4wtdop.algorithm.AlgorithmConfigurationFactory;
import br.ufpr.inf.cbio.hhco4wtdop.problem.TestProblem;
import br.ufpr.inf.cbio.hhco4wtdop.problem.WindTurbineDesign;
import br.ufpr.inf.cbio.hhco4wtdop.runner.methodology.ECSymposium2019CompetitionMethodology;
import br.ufpr.inf.cbio.hhco4wtdop.runner.methodology.WaterMethodology;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.problem.multiobjective.Water;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class Main {

    private long seed;
    private int id;
    private String experimentBaseDirectory;
    private String algorithmName;
    private String problemName;
    private Problem problem;
    private Methodology methodology;

    public Main(CommandLine cmd) {
        String aux;

        this.seed = System.currentTimeMillis();
        if ((aux = cmd.getOptionValue("s")) != null) {
            this.seed = Long.parseLong(aux);
        }

        this.id = 0;
        if ((aux = cmd.getOptionValue("id")) != null) {
            this.id = Integer.parseInt(aux);
        }

        this.experimentBaseDirectory = "root/experiment/";
        if ((aux = cmd.getOptionValue("P")) != null) {
            this.experimentBaseDirectory = aux;
        }

        this.algorithmName = "NSGAII";
        if ((aux = cmd.getOptionValue("a")) != null) {
            this.algorithmName = aux;
        }

        this.problemName = "WindTurbineDesign";
        if ((aux = cmd.getOptionValue("p")) != null) {
            problemName = aux;
        }
    }

    public static CommandLine parse(String[] args) {

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        Options options = new Options();

        try {
            options.addOption(Option.builder("h").longOpt("help").desc("print this message and exits.").build());
            options.addOption(Option.builder("P").longOpt("output-path").hasArg().argName("path")
                    .desc("directory path for output (if no path is given experiment/ will be used.)").build());
            options.addOption(Option.builder("id").hasArg().argName("id")
                    .desc("set the independent run id, default 0.").build());
            options.addOption(Option.builder("s").longOpt("seed").hasArg().argName("seed")
                    .desc("set the seed for JMetalRandom, default System.currentTimeMillis()").build());
            options.addOption(Option.builder("a").longOpt("algorithm").hasArg().argName("algorithm")
                    .desc("set the algorithm to be executed: [NSGAII|HypE]").build());
            options.addOption(Option.builder("p").longOpt("problem").hasArg().argName("problem")
                    .desc("set the problem instance: [WindTurbineDesign|Water|Test].").build());

            // parse command line
            cmd = parser.parse(options, args);
            // print help and exit
            if (cmd.hasOption("h") || args.length == 0) {
                help(options);
                System.exit(0);
            }
            return cmd;
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE,
                    "Failed to parse command line arguments. Execute with -h for usage help.", ex);
        }
        return null;
    }

    public static void help(Options options) {
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
                "java -cp <jar> br.ufpr.inf.cbio.hhco4wtdop.runner.Main",
                "\nExecute a single independent run of the <algorithm> on a given <problem>.\n",
                options,
                "\nPlease report issues at https://github.com/fritsche/hhco4wtdop/issues", true);
    }

    private Algorithm<List<DoubleSolution>> configure() {
        JMetalRandom.getInstance().setSeed(seed);
        switch (problemName) {
            case "WindTurbineDesign":
                problem = new WindTurbineDesign(algorithmName, id);
                methodology = new ECSymposium2019CompetitionMethodology();
                break;
            case "Water":
                problem = new Water();
                methodology = new WaterMethodology();
                break;
            case "Test":
                problem = new TestProblem();
                methodology = new ECSymposium2019CompetitionMethodology();
                break;
            default:
                throw new JMetalException("No configuration found for problem [" + problemName + "]");
        }
        int populationSize = methodology.getPopulationSize();
        int maxEvaluations = methodology.getMaxFitnessEvaluations();
        AlgorithmConfigurationFactory configurationFactory = new AlgorithmConfigurationFactory();
        return configurationFactory.getAlgorithmConfiguration(algorithmName).configure(populationSize, maxEvaluations, problem);
    }

    private long run(Algorithm<List<DoubleSolution>> algorithm) {
        long initTime = System.currentTimeMillis();
        algorithm.run();
        long computingTime = System.currentTimeMillis() - initTime;
        JMetalLogger.logger.log(Level.INFO, "Total execution time: {0}ms", computingTime);
        return computingTime;
    }

    private void printResult(Algorithm<List<DoubleSolution>> algorithm, long computingTime) throws IOException {
        String methodologyName = methodology.getClass().getSimpleName();
        int m = problem.getNumberOfObjectives();

        List population = SolutionListUtils.getNondominatedSolutions(algorithm.getResult());
        String folder = experimentBaseDirectory + "/"
                + methodologyName + "/"
                + m
                + "/data/"
                + algorithm.getName() + "/"
                + problemName + "/";

        Utils outputUtils = new Utils(folder);
        outputUtils.prepareOutputDirectory();

        new SolutionListOutput(population).setSeparator("\t")
                .setVarFileOutputContext(new DefaultFileOutputContext(folder + "VAR" + id + ".tsv"))
                .setFunFileOutputContext(new DefaultFileOutputContext(folder + "FUN" + id + ".tsv"))
                .print();

        // log elapsed time
        DefaultFileOutputContext context = new DefaultFileOutputContext(folder + "TIME" + id + ".tsv");
        try (BufferedWriter writer = context.getFileWriter()) {
            writer.write(Long.toString(computingTime) + context.getSeparator());
            writer.newLine();
            writer.close();
        }
    }

    public static void main(String[] args) throws IOException {
        JMetalLogger.logger.setLevel(Level.INFO);
        Main runner = new Main(parse(args));
        Algorithm<List<DoubleSolution>> algorithm = runner.configure();
        long computingTime = runner.run(algorithm);
        runner.printResult(algorithm, computingTime);
    }

}
