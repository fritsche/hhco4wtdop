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
package br.ufpr.inf.cbio.hhco4wtdop.algorithm;

import br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOEAD.CMOEADConfiguration;
import br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOEAD.CMOEAD;
import br.ufpr.inf.cbio.hhco.config.AlgorithmConfiguration;
import br.ufpr.inf.cbio.hhco4wtdop.hyperheuristics.CHHCOConfiguration;
import br.ufpr.inf.cbio.hhco4wtdop.hyperheuristics.CHHCORandomConfiguration;
import br.ufpr.inf.cbio.hhco4wtdop.hyperheuristics.CHHLAConfiguration;
import br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOEADD.CMOEADD;
import br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOEADD.CMOEADDConfiguration;
import br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOEADSBX.CMOEADSBX;
import br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOEADSBX.CMOEADSBXConfiguration;
import br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOMBI2.CMOMBI2;
import br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOMBI2.CMOMBI2Configuration;
import br.ufpr.inf.cbio.hhco4wtdop.algorithm.ConstraintMOEAD.ConstraintMOEADConfiguration;
import org.uma.jmetal.algorithm.multiobjective.moead.MOEADBuilder;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class AlgorithmConfigurationFactory extends br.ufpr.inf.cbio.hhco.config.AlgorithmConfigurationFactory {

    private final String experimentBaseDirectory;
    private final int id;

    public AlgorithmConfigurationFactory(String experimentBaseDirectory, int id) {
        this.experimentBaseDirectory = experimentBaseDirectory;
        this.id = id;
    }

    @Override
    public AlgorithmConfiguration getAlgorithmConfiguration(String algorithm) {
        if (algorithm.equals(CMOEAD.class.getSimpleName())) {
            return new CMOEADConfiguration();
        } else if (algorithm.equals(CMOEADSBX.class.getSimpleName())) {
            return new CMOEADSBXConfiguration();
        } else if (algorithm.equals(CMOEADD.class.getSimpleName())) {
            return new CMOEADDConfiguration();
        } else if (algorithm.equals(CMOMBI2.class.getSimpleName())) {
            return new CMOMBI2Configuration();
        } else if (algorithm.equals(MOEADBuilder.Variant.ConstraintMOEAD.name())) {
            return new ConstraintMOEADConfiguration();
        } else if (algorithm.equals("CHHLA")) {
            return new CHHLAConfiguration(experimentBaseDirectory, id);
        } else if (algorithm.equals("CHHCO")) {
            return new CHHCOConfiguration(experimentBaseDirectory, id);
        } else if (algorithm.equals("CHHCORandom")) {
            return new CHHCORandomConfiguration(experimentBaseDirectory, id);
        } else {
            return super.getAlgorithmConfiguration(algorithm);
        }
    }

}
