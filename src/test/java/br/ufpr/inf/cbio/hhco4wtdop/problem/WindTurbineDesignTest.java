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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.uma.jmetal.solution.DoubleSolution;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class WindTurbineDesignTest {

    public WindTurbineDesignTest() {
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
     * Test of evaluate method, of class WindTurbineDesign.
     */
    @Test
    public void testEvaluate() {
        System.out.println("evaluate");
         WindTurbineDesign instance = new WindTurbineDesign("test", 5);
        DoubleSolution solution = instance.createSolution();

        double[] variables = new double[]{
            0.6680430003119201, 0.7637015086839573, 0.9836514891754233, 0.001456602553327478,
            0.46416491846404573, 0.8554272375294075, 0.8449932478312188, 0.43346710610354955,
            0.3561024740843566, 0.9303696580717351, 0.35500796404487656, 0.7630374696603606,
            0.3117287573143056, 0.5454305621211675, 0.24340692209573328, 0.37312239149180165,
            0.06965134416274331, 0.23618749049544513, 0.12639351946526173, 0.7724665733223737,
            0.8201219770341109, 0.3885332435799661, 0.6536367309291194, 0.12129799175353642,
            0.2824220040966746, 0.9117087026101117, 0.9914283433438114, 0.9478169855764108,
            0.1128738972094555, 0.498033274507695, 0.08081075917816673, 0.09420785177666803};

        int index = 0;
        for (Double value : variables) {
            solution.setVariableValue(index++, value);
        }
       
        instance.evaluate(solution);
        double[] objectives = solution.getObjectives();
        double[] result = new double[]{-13944107.195469113, 1457986.3360570574, 69748932.46052217, 48.134773019488854, -2.177180806632029};
        assertArrayEquals(objectives, result, 1e-6);
    }

}
