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
import br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOEADSBX.CMOEADSBX;
import br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOEADSBX.CMOEADSBXConfiguration;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 */
public class AlgorithmConfigurationFactory extends br.ufpr.inf.cbio.hhco.config.AlgorithmConfigurationFactory {

    @Override
    public AlgorithmConfiguration getAlgorithmConfiguration(String algorithm) {
        if (algorithm.equals(CMOEAD.class.getSimpleName())) {
            return new CMOEADConfiguration();
        } else if (algorithm.equals(CMOEADSBX.class.getSimpleName())) {
            return new CMOEADSBXConfiguration();
        } else {
            return super.getAlgorithmConfiguration(algorithm);
        }
    }

}
