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
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class ECSymposium2019CompetitionMethodology implements Methodology {

    private final int populationSize;
    private final int maxFitnessEvaluations;

    public ECSymposium2019CompetitionMethodology() {
        /**
         * Using same population size than NSGAIIIMethodology since
         * ECSymposium2019CompetitionMethodology does not define a population
         * size
         */
        this.populationSize = 210;
        /**
         * WARNING: changed for 15,000 to compare HHCO and HHLA!
         * 
         * @TODO: fix back to 10,000
         * 
         * The maximum number of evaluations is 15,000, so as to finish each
         * optimization within a night.Since it takes about 3 seconds to
         * evaluate one design candidate, it takes about 8 hours to complete one
         * optimization case. However, some algorithms use the number of
         * generations as stop criteria.
         */
        this.maxFitnessEvaluations = 15000;
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
