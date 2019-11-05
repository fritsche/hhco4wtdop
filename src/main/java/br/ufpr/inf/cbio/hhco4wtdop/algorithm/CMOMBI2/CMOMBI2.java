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
package br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOMBI2;

import br.ufpr.inf.cbio.hhco.algorithm.MOMBI2.MOMBI2;
import br.ufpr.inf.cbio.hhco4wtdop.util.R2RankingWithConstraintsNormalized;
import java.util.List;
import org.uma.jmetal.algorithm.multiobjective.mombi.util.R2Ranking;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.evaluator.SolutionListEvaluator;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 * @param <S>
 */
public class CMOMBI2<S extends Solution<?>> extends MOMBI2<S> {

    public CMOMBI2(Problem<S> problem, int maxIterations, CrossoverOperator<S> crossover, MutationOperator<S> mutation, SelectionOperator<List<S>, S> selection, SolutionListEvaluator<S> evaluator, String pathWeights) {
        super(problem, maxIterations, crossover, mutation, selection, evaluator, pathWeights);
    }

    @Override
    protected R2Ranking<S> computeRanking(List<S> solutionList) {
        R2Ranking<S> ranking = new R2RankingWithConstraintsNormalized<>(this.getUtilityFunctions(), this.normalizer);
        ranking.computeRanking(solutionList);
        return ranking;
    }

}
