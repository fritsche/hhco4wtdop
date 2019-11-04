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
package br.ufpr.inf.cbio.hhco4wtdop.runner.methodology;

import br.ufpr.inf.cbio.hhco.runner.methodology.Methodology;

/**
 * Values based on (Jain, 2014). H. Jain and K. Deb, "An Evolutionary
 * Many-Objective Optimization Algorithm Using Reference-Point Based
 * Nondominated Sorting Approach, Part II: Handling Constraints and Extending to
 * an Adaptive Approach," in IEEE Transactions on Evolutionary Computation, vol.
 * 18, no. 4, pp. 602-622, Aug. 2014. doi: 10.1109/TEVC.2013.2281534
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class WaterMethodology implements Methodology {

    private final int populationSize;
    private final int maxFitnessEvaluations;

    public WaterMethodology() {
        this.populationSize = 210;
        // 1000 generations
        // this.maxFitnessEvaluations = 1000 * populationSize;
        // 10000 FE
        this.maxFitnessEvaluations = 10000;
    }

    @Override
    public int getPopulationSize() {
        return populationSize;
    }

    @Override
    public int getMaxFitnessEvaluations() {
        return maxFitnessEvaluations;
    }
}

