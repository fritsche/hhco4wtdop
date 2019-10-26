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
import org.uma.jmetal.util.solutionattribute.impl.NumberOfViolatedConstraints;
import org.uma.jmetal.util.solutionattribute.impl.OverallConstraintViolation;

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

    @Test
    public void testEvaluateConstraints() {
        System.out.println("evaluateConstraints");
        System.out.println("evaluate");
        WindTurbineDesign instance = new WindTurbineDesign("test", 5);
        DoubleSolution solution = instance.createSolution();

        double[] variables = new double[]{
            0.8318217986942065, 0.7788561133519986, 0.9800265921054313, 0.6253613081466335,
            0.17779247345501978, 0.6816849736617654, 0.8566342274586246, 0.43730647858803495,
            0.3562234738084365, 0.9033181945157065, 0.5358241741377521, 0.3758945537738312,
            0.3195686690802791, 0.6383990657626567, 0.6530003783638528, 0.2738298919179506,
            0.14417349891848888, 0.3303075624499322, 0.4408850061616495, 0.06714566080800066,
            0.3434863318249317, 0.3595609659454928, 0.46464514555974895, 0.5573205574174515,
            0.09003705401539938, 0.9832690511473879, 0.9194966036490683, 0.3681654648453154,
            0.31958144111699605, 0.49804962053229007, 0.08176304228141162, 0.29313477428757395};

        int index = 0;
        for (Double value : variables) {
            solution.setVariableValue(index++, value);
        }

        instance.evaluate(solution);
        double[] objectives = solution.getObjectives();
        double[] result = new double[]{-8922874.2488451, 1344486.0471836622, 74673179.58106622, 77.74947732733733, -0.9311889254995676};
        assertArrayEquals(objectives, result, 1e-6);
        
        OverallConstraintViolation<DoubleSolution> overallConstraintViolation = new OverallConstraintViolation();
        double violationDegree = overallConstraintViolation.getAttribute(solution);
        double expectedViolationDegree = -0.06923908757;
        assertEquals(violationDegree, expectedViolationDegree, 1e-6);
        
        NumberOfViolatedConstraints<DoubleSolution> numberOfViolatedConstraints = new NumberOfViolatedConstraints();
        int violatedConstraints = numberOfViolatedConstraints.getAttribute(solution);
        int expectedViolatedConstraints = 2;
        assertEquals(violatedConstraints, expectedViolatedConstraints);
    }

}
