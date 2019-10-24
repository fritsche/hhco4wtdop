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

import br.ufpr.inf.cbio.hhco.util.output.Utils;
import br.ufpr.inf.cbio.hhco.config.AlgorithmConfigurationFactory;
import br.ufpr.inf.cbio.hhco4wtdop.problem.WindTurbineDesign;
import br.ufpr.inf.cbio.hhco4wtdop.runner.methodology.ECSymposium2019CompetitionMethodology;
import br.ufpr.inf.cbio.hhcoanalysis.util.SolutionListUtils;
import java.util.List;
import java.util.logging.Level;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class NSGAIIRunner {

    private Algorithm<List<DoubleSolution>> configure(String algorithmName, int id) {
        WindTurbineDesign problem = new WindTurbineDesign(algorithmName, id);
        ECSymposium2019CompetitionMethodology methodology = new ECSymposium2019CompetitionMethodology();
        int populationSize = methodology.getPopulationSize();
        int maxEvaluations = methodology.getMaxFitnessEvaluations();
        int generations = maxEvaluations / populationSize;
        AlgorithmConfigurationFactory configurationFactory = new AlgorithmConfigurationFactory();
        return configurationFactory.getAlgorithmConfiguration(algorithmName).configure(populationSize, maxEvaluations, problem);
    }

    private void run(Algorithm<List<DoubleSolution>> algorithm) {
        long initTime = System.currentTimeMillis();
        algorithm.run();
        long computingTime = System.currentTimeMillis() - initTime;
        JMetalLogger.logger.log(Level.INFO, "Total execution time: {0}ms", computingTime);
    }

    private void printResult(Algorithm<List<DoubleSolution>> algorithm, int id, String experimentBaseDirectory) {
        String methodologyName = "ECSymposium2019CompetitionMethodology";
        String problemName = "WindTurbineDesign";
        int m = 5;
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
    }

    public static void main(String[] args) {
        // set log level
        JMetalLogger.logger.setLevel(Level.INFO);
        // read arguments
        int i = 0;
        long seed = Long.parseLong(args[i++]);
        int id = Integer.parseInt(args[i++]);
        String experimentBaseDirectory = args[i++];
        // set constants
        String algorithmName = "NSGAII";
        // set seed
        JMetalRandom.getInstance().setSeed(seed);
        // run algorithm
        NSGAIIRunner runner = new NSGAIIRunner();
        Algorithm<List<DoubleSolution>> algorithm = runner.configure(algorithmName, id);
        runner.run(algorithm);
        runner.printResult(algorithm, id, experimentBaseDirectory);
    }
}
