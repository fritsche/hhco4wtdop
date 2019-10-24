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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class ECSymposium2019CompetitionMethodologyTest {
    
    public ECSymposium2019CompetitionMethodologyTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getPopulationSize method, of class ECSymposium2019CompetitionMethodology.
     */
    @Test
    public void testGetPopulationSize() {
        System.out.println("getPopulationSize");
        ECSymposium2019CompetitionMethodology instance = new ECSymposium2019CompetitionMethodology();
        /**
         * Using same population size than NSGAIIIMethodology since
         * ECSymposium2019CompetitionMethodology does not define a population
         * size
         */
        int expResult = 210;
        int result = instance.getPopulationSize();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMaxFitnessEvaluations method, of class ECSymposium2019CompetitionMethodology.
     */
    @Test
    public void testGetMaxFitnessEvaluations() {
        System.out.println("getMaxFitnessEvaluations");
        ECSymposium2019CompetitionMethodology instance = new ECSymposium2019CompetitionMethodology();
        /**
         * The maximum number of evaluations is 10,000, so as to finish each
         * optimization within a night.Since it takes about 3 seconds to
         * evaluate one design candidate, it takes about 8 hours to complete one
         * optimization case. However, some algorithms use the number of
         * generations as stop criteria. For this reason, we have bounded all
         * algorithms to 9,870, which is 47 generations for 210 solutions.
         */
        int expResult = 9870;
        int result = instance.getMaxFitnessEvaluations();
        assertEquals(expResult, result);
    }
    
}
