/*
 * Copyright (C) 2020 Gian Fritsche <gmfritsche at inf.ufpr.br>
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
package br.ufpr.inf.cbio.hhco4wtdop.hyperheuristics;

import br.ufpr.inf.cbio.hhco.algorithm.HypE.COHypEConfiguration;
import br.ufpr.inf.cbio.hhco.algorithm.NSGAII.CONSGAIIConfiguration;
import br.ufpr.inf.cbio.hhco.algorithm.NSGAIII.CONSGAIIIConfiguration;
import br.ufpr.inf.cbio.hhco.algorithm.SPEA2.COSPEA2Configuration;
import br.ufpr.inf.cbio.hhco.algorithm.SPEA2SDE.COSPEA2SDEConfiguration;
import br.ufpr.inf.cbio.hhco.algorithm.ThetaDEA.COThetaDEAConfiguration;
import br.ufpr.inf.cbio.hhco.config.AlgorithmConfiguration;
import br.ufpr.inf.cbio.hhco.hyperheuristic.CooperativeAlgorithm;
import br.ufpr.inf.cbio.hhco.hyperheuristic.HHLA.HHLA;
import br.ufpr.inf.cbio.hhco.hyperheuristic.HHLA.HHLABuilder;
import br.ufpr.inf.cbio.hhco.hyperheuristic.HHLA.observer.SelectedMOEALogger;
import br.ufpr.inf.cbio.hhco.hyperheuristic.selection.LearningAutomaton;
import br.ufpr.inf.cbio.hhco.hyperheuristic.selection.SelectionFunction;
import br.ufpr.inf.cbio.hhco.metrics.fir.FitnessImprovementRateCalculator;
import br.ufpr.inf.cbio.hhco4wtdop.utils.R2WithConstraintsFIR;
import br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOEAD.COCMOEADConfiguration;
import br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOEADD.COCMOEADDConfiguration;
import br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOMBI2.COCMOMBI2Configuration;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 * @param <S>
 */
public class CHHLAConfiguration<S extends Solution> implements AlgorithmConfiguration<HHLA> {

    protected final String name;
    protected SelectionFunction<CooperativeAlgorithm> selection;
    protected FitnessImprovementRateCalculator fir;
    protected Problem problem;
    protected int popSize;
    private int maxFitnessEvaluations;
    private int k;
    private double deltaV;
    private final String experimentBaseDirectory;
    private final int id;

    public CHHLAConfiguration(String experimentBaseDirectory, int id) {
        this.experimentBaseDirectory = experimentBaseDirectory;
        this.id = id;
        this.name = "CHHLA";
    }

    @Override
    public void setup() {
        double m = 2.5;
        double tau = 0.5;
        int n = maxFitnessEvaluations / popSize;
        this.selection = new LearningAutomaton<>(m, n, tau);
        this.fir = new R2WithConstraintsFIR(problem, popSize);
        // The remaining two parameters are not significantly influential on the performance
        this.k = 3;
        this.deltaV = 0.0075;
    }

    @Override
    public HHLA configure(int popSize, int maxFitnessEvaluations, Problem problem) {
        this.problem = problem;
        this.popSize = popSize;
        this.maxFitnessEvaluations = maxFitnessEvaluations;

        setup();

        HHLABuilder builder = new HHLABuilder(problem);
        builder
                .addAlgorithm(new COSPEA2Configuration().configure(popSize, maxFitnessEvaluations, problem))
                .addAlgorithm(new COCMOEADConfiguration().configure(popSize, maxFitnessEvaluations, problem))
                .addAlgorithm(new CONSGAIIConfiguration().configure(popSize, maxFitnessEvaluations, problem))
                .addAlgorithm(new COCMOEADDConfiguration().configure(popSize, maxFitnessEvaluations, problem))
                .addAlgorithm(new COCMOMBI2Configuration().configure(popSize, maxFitnessEvaluations, problem))
                .addAlgorithm(new CONSGAIIIConfiguration().configure(popSize, maxFitnessEvaluations, problem))
                .addAlgorithm(new COThetaDEAConfiguration().configure(popSize, maxFitnessEvaluations, problem))
                .addAlgorithm(new COSPEA2SDEConfiguration().configure(popSize, maxFitnessEvaluations, problem))
                .addAlgorithm(new COHypEConfiguration().configure(popSize, maxFitnessEvaluations, problem));

        HHLA hhla = builder.setName(name).setSelection(selection).setFir(fir)
                .setMaxEvaluations(maxFitnessEvaluations).setPopulationSize(popSize).setK(k).setDeltaV(deltaV).build();

        String outputfolder = experimentBaseDirectory + "/output/";
        hhla.addLogger(new SelectedMOEALogger(outputfolder, "selected." + id + ".txt"));

        return hhla;
    }

}
