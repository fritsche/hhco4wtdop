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
package br.ufpr.inf.cbio.hhco4wtdop.util;

import java.io.Serializable;
import java.util.Comparator;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.comparator.ConstraintViolationComparator;
import org.uma.jmetal.util.comparator.FitnessComparator;
import org.uma.jmetal.util.comparator.impl.OverallConstraintViolationComparator;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 * @param <S>
 */
public class ConstraintAndFitnessComparator<S extends Solution<?>> implements Comparator<S>, Serializable {

    private final ConstraintViolationComparator<S> constraintViolationComparator;
    private final FitnessComparator<S> fitnessComparator;

    public ConstraintAndFitnessComparator() {
        constraintViolationComparator = new OverallConstraintViolationComparator<>();
        fitnessComparator = new FitnessComparator<>();
    }

    @Override
    public int compare(S solution1, S solution2) {
        if (solution1 == null) {
            throw new JMetalException("Solution1 is null");
        } else if (solution2 == null) {
            throw new JMetalException("Solution2 is null");
        } else if (solution1.getNumberOfObjectives() != solution2.getNumberOfObjectives()) {
            throw new JMetalException("Cannot compare because solution1 has "
                    + solution1.getNumberOfObjectives() + " objectives and solution2 has "
                    + solution2.getNumberOfObjectives());
        }
        int result;
        result = constraintViolationComparator.compare(solution1, solution2);
        if (result == 0) {
            result = fitnessComparator.compare(solution1, solution2);
        }
        return result;
    }

}
