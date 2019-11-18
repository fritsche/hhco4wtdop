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
package br.ufpr.inf.cbio.hhco4wtdop.problem;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.uma.jmetal.solution.DoubleSolution;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class Water extends org.uma.jmetal.problem.multiobjective.Water {

    private double feasibleCount, evaluationsCount;

    public Water() {
        feasibleCount = evaluationsCount = 0;
    }

    @Override
    public void evaluate(DoubleSolution solution) {
        super.evaluate(solution);
        double overallConstraintViolation = overallConstraintViolationDegree.getAttribute(solution);
        int violatedConstraints = numberOfViolatedConstraints.getAttribute(solution);
        if (overallConstraintViolation == 0.0) {
            feasibleCount++;
        }
        evaluationsCount++;

        Logger.getLogger(Water.class.getName()).log(Level.INFO, "{0}: {1}, {2}, {3}%",
                new Object[]{evaluationsCount, violatedConstraints, feasibleCount, feasibleCount / (double) evaluationsCount * 100});

    }

}
