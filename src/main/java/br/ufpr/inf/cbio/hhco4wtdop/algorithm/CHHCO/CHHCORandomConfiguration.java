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
package br.ufpr.inf.cbio.hhco4wtdop.algorithm.CHHCO;

import br.ufpr.inf.cbio.hhco.algorithm.HypE.COHypEConfiguration;
import br.ufpr.inf.cbio.hhco.algorithm.NSGAII.CONSGAIIConfiguration;
import br.ufpr.inf.cbio.hhco.algorithm.NSGAIII.CONSGAIIIConfiguration;
import br.ufpr.inf.cbio.hhco.algorithm.SPEA2.COSPEA2Configuration;
import br.ufpr.inf.cbio.hhco.algorithm.SPEA2SDE.COSPEA2SDEConfiguration;
import br.ufpr.inf.cbio.hhco.algorithm.ThetaDEA.COThetaDEAConfiguration;
import br.ufpr.inf.cbio.hhco.config.AlgorithmConfiguration;
import br.ufpr.inf.cbio.hhco.hyperheuristic.HHCO.HHCO;
import br.ufpr.inf.cbio.hhco.hyperheuristic.HHCO.observer.SelectedMOEALogger;
import br.ufpr.inf.cbio.hhco.hyperheuristic.HHCORandom.HHCORandomBuilder;
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
public class CHHCORandomConfiguration<S extends Solution> implements AlgorithmConfiguration<HHCO> {

    protected Problem problem;
    protected int popSize;

    private final String experimentBaseDirectory;
    private final int id;

    public CHHCORandomConfiguration(String experimentBaseDirectory, int id) {
        this.experimentBaseDirectory = experimentBaseDirectory;
        this.id = id;
    }

    @Override
    public HHCO configure(int popSize, int maxFitnessEvaluations, Problem problem) {

        this.problem = problem;
        this.popSize = popSize;

        setup();

        HHCORandomBuilder builder = new HHCORandomBuilder(problem);

        builder
                .addAlgorithm(new COSPEA2Configuration().configure(popSize, maxFitnessEvaluations, problem))
                .addAlgorithm(new COCMOEADConfiguration().configure(popSize, maxFitnessEvaluations, problem))
                .addAlgorithm(new CONSGAIIConfiguration().configure(popSize, maxFitnessEvaluations, problem))
//                .addAlgorithm(new COCMOEADDConfiguration().configure(popSize, maxFitnessEvaluations, problem))
                .addAlgorithm(new COCMOMBI2Configuration().configure(popSize, maxFitnessEvaluations, problem))
                .addAlgorithm(new CONSGAIIIConfiguration().configure(popSize, maxFitnessEvaluations, problem))
                .addAlgorithm(new COThetaDEAConfiguration().configure(popSize, maxFitnessEvaluations, problem))
                .addAlgorithm(new COSPEA2SDEConfiguration().configure(popSize, maxFitnessEvaluations, problem))
                .addAlgorithm(new COHypEConfiguration().configure(popSize, maxFitnessEvaluations, problem));

        HHCO hhco = builder.setMaxEvaluations(maxFitnessEvaluations).setPopulationSize(popSize).build();
        String outputfolder = experimentBaseDirectory + "/output/";
        hhco.addObserver(new SelectedMOEALogger(outputfolder, "selected." + id + ".txt"));
        return hhco;
    }

    @Override
    public void setup() {

    }
}
