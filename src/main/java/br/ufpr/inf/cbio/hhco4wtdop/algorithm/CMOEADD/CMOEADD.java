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
package br.ufpr.inf.cbio.hhco4wtdop.algorithm.CMOEADD;

import br.ufpr.inf.cbio.hhco.algorithm.MOEADD.MOEADD;
import br.ufpr.inf.cbio.hhco.algorithm.MOEADD.MOEADDBuilder;
import br.ufpr.inf.cbio.hhco.algorithm.MOEADD.Utils;
import java.util.ArrayList;
import java.util.List;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.solutionattribute.Ranking;
import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;

/**
 *
 * @author Gian Fritsche <gmfritsche at inf.ufpr.br>
 * @param <S>
 */
public class CMOEADD<S extends Solution<?>> extends MOEADD<S> {

    private final DominanceComparator<DoubleSolution> comparator = new DominanceComparator<>();

    public CMOEADD(MOEADDBuilder<S> builder) {
        super(builder);
    }

    @Override
    public int checkDominance(Solution a, Solution b) {
        return comparator.compare((DoubleSolution) a, (DoubleSolution) b) * -1;
    }

    @Override
    public void run() {

        evaluations_ = 0;

        T_ = 20;
        delta_ = 0.9;

        population_ = new ArrayList<>(populationSize_);

        neighborhood_ = new int[populationSize_][T_];
        lambda_ = new double[populationSize_][problem_.getNumberOfObjectives()];

        zp_ = new double[problem_.getNumberOfObjectives()]; // ideal point for Pareto-based population
        nzp_ = new double[problem_.getNumberOfObjectives()]; // nadir point for Pareto-based population

        rankIdx_ = new int[populationSize_ + 1][populationSize_ + 1];
        subregionIdx_ = new int[populationSize_][populationSize_];
        subregionDist_ = new double[populationSize_][populationSize_];

        // STEP 1. Initialization
        initUniformWeight();
        initNeighborhood();
        initPopulation();
        initIdealPoint();
        initNadirPoint();

        // initialize the distance
        for (int i = 0; i < populationSize_; i++) {
            double distance = calculateDistance2(population_.get(i), lambda_[i], zp_, nzp_);
            subregionDist_[i][i] = distance;
        }

        Ranking ranking = (new DominanceRanking()).computeRanking(population_);
        int curRank;
        dominanceRankingAttributeIdentifier = (new DominanceRanking()).getAttributeIdentifier();
        for (int i = 0; i < populationSize_; i++) {
            curRank = (int) population_.get(i).getAttribute(dominanceRankingAttributeIdentifier);
            rankIdx_[curRank][i] = 1;
        }

        // main procedure
        do {
            int[] permutation = new int[populationSize_];
            Utils.randomPermutation(permutation, populationSize_);

            for (int i = 0; i < populationSize_; i++) {
                int cid = permutation[i];

                int type;
                double rnd = randomGenerator.nextDouble();

                // mating selection style
                if (rnd < delta_) {
                    type = 1; // neighborhood
                } else {
                    type = 2; // whole population
                }
                List<S> parents;
                List<S> offSpring;
                parents = matingSelection(cid, type);

                // SBX crossover
                offSpring = crossover_.execute(parents);

                mutation_.execute(offSpring.get(0));
                mutation_.execute(offSpring.get(1));

                problem_.evaluate(offSpring.get(0));
                problem_.evaluate(offSpring.get(1));

                evaluations_ += 2;

                // update ideal points
                updateReference(offSpring.get(0), zp_);
                updateReference(offSpring.get(1), zp_);

                // update nadir points
                updateNadirPoint(offSpring.get(0), nzp_);
                updateNadirPoint(offSpring.get(1), nzp_);

                updateArchive(offSpring.get(0));
                updateArchive(offSpring.get(1));
            } // for			
        } while (evaluations_ < maxEvaluations);

    }

    @Override
    public void updateArchive(S indiv) {

        // find the location of 'indiv'
        setLocation(indiv, zp_, nzp_);
        int location = (int) indiv.getAttribute(MOEADD_ATTRIBUTES.Region);

        numRanks = nondominated_sorting_add(indiv);

        System.out.println(numRanks);

        if (numRanks == 1) {
            deleteRankOne(indiv, location);
        } else {
            List<S> lastFront = new ArrayList<>(populationSize_);
            int frontSize = countRankOnes(numRanks - 1);
            if (frontSize == 0) {	// the last non-domination level only contains 'indiv'
                frontSize++;
                lastFront.add(indiv);
            } else {
                for (int i = 0; i < populationSize_; i++) {
                    if (rankIdx_[numRanks - 1][i] == 1) {
                        lastFront.add(population_.get(i));
                    }
                }
                if ((int) indiv.getAttribute(dominanceRankingAttributeIdentifier) == (numRanks - 1)) {
                    frontSize++;
                    lastFront.add(indiv);
                }
            }

            if (frontSize == 1 && lastFront.get(0).equals(indiv)) {	// the last non-domination level only has 'indiv'
                int curNC = countOnes(location);
                if (curNC > 0) {	// if the subregion of 'indiv' has other solution, drop 'indiv'
                    nondominated_sorting_delete(indiv);
                } else {	// if the subregion of 'indiv' has no solution, keep 'indiv'
                    deleteCrowdRegion1(indiv, location);
                }
            } else if (frontSize == 1 && !lastFront.get(0).equals(indiv)) { // the last non-domination level only has one solution, but not 'indiv'
                int targetIdx = findPosition(lastFront.get(0));
                int parentLocation = findRegion(targetIdx);
                int curNC = countOnes(parentLocation);
                if (parentLocation == location) {
                    curNC++;
                }

                if (curNC == 1) {	// the subregion only has the solution 'targetIdx', keep solution 'targetIdx'
                    deleteCrowdRegion2(indiv, location);
                } else {	// the subregion contains some other solutions, drop solution 'targetIdx'
                    int indivRank = (int) indiv.getAttribute(dominanceRankingAttributeIdentifier);
                    int targetRank = (int) population_.get(targetIdx).getAttribute(dominanceRankingAttributeIdentifier);
                    rankIdx_[targetRank][targetIdx] = 0;
                    rankIdx_[indivRank][targetIdx] = 1;

                    Solution targetSol = population_.get(targetIdx).copy();

                    population_.set(targetIdx, indiv);
                    subregionIdx_[parentLocation][targetIdx] = 0;
                    subregionIdx_[location][targetIdx] = 1;

                    // update the non-domination level structure
                    nondominated_sorting_delete(targetSol);
                }
            } else {
                double indivFitness = fitnessFunction(indiv, lambda_[location]);

                // find the index of the solution in the last non-domination level, and its corresponding subregion
                int[] idxArray = new int[frontSize];
                int[] regionArray = new int[frontSize];

                for (int i = 0; i < frontSize; i++) {
                    idxArray[i] = findPosition(lastFront.get(i));
                    if (idxArray[i] == -1) {
                        regionArray[i] = location;
                    } else {
                        regionArray[i] = findRegion(idxArray[i]);
                    }
                }

                // find the most crowded subregion, if more than one exist, keep them in 'crowdList'
                List<Integer> crowdList = new ArrayList<>();
                int crowdIdx;
                int nicheCount = countOnes(regionArray[0]);
                if (regionArray[0] == location) {
                    nicheCount++;
                }
                crowdList.add(regionArray[0]);
                for (int i = 1; i < frontSize; i++) {
                    int curSize = countOnes(regionArray[i]);
                    if (regionArray[i] == location) {
                        curSize++;
                    }
                    if (curSize > nicheCount) {
                        crowdList.clear();
                        nicheCount = curSize;
                        crowdList.add(regionArray[i]);
                    } else if (curSize == nicheCount) {
                        crowdList.add(regionArray[i]);
                    }
                }
                // find the index of the most crowded subregion
                if (crowdList.size() == 1) {
                    crowdIdx = crowdList.get(0);
                } else {
                    int listLength = crowdList.size();
                    crowdIdx = crowdList.get(0);
                    double sumFitness = sumFitness(crowdIdx);
                    if (crowdIdx == location) {
                        sumFitness = sumFitness + indivFitness;
                    }
                    for (int i = 1; i < listLength; i++) {
                        int curIdx = crowdList.get(i);
                        double curFitness = sumFitness(curIdx);
                        if (curIdx == location) {
                            curFitness = curFitness + indivFitness;
                        }
                        if (curFitness > sumFitness) {
                            crowdIdx = curIdx;
                            sumFitness = curFitness;
                        }
                    }
                }

                switch (nicheCount) {
                    case 0:
                        System.out.println("Impossible empty subregion!!!");
                        break;
                    case 1:
                        // if the subregion of each solution in the last non-domination level only has one solution, keep them all
                        deleteCrowdRegion2(indiv, location);
                        break;
                    default:
                        // delete the worst solution from the most crowded subregion in the last non-domination level
                        List<Integer> list = new ArrayList<>();
                        for (int i = 0; i < frontSize; i++) {
                            if (regionArray[i] == crowdIdx) {
                                list.add(i);
                            }
                        }
                        if (list.isEmpty()) {
                            System.out.println("Cannot happen!!!");
                        } else {
                            double maxFitness, curFitness;
                            int targetIdx = list.get(0);
                            if (idxArray[targetIdx] == -1) {
                                maxFitness = indivFitness;
                            } else {
                                maxFitness = fitnessFunction(population_.get(idxArray[targetIdx]), lambda_[crowdIdx]);
                            }
                            for (int i = 1; i < list.size(); i++) {
                                int curIdx = list.get(i);
                                if (idxArray[curIdx] == -1) {
                                    curFitness = indivFitness;
                                } else {
                                    curFitness = fitnessFunction(population_.get(idxArray[curIdx]), lambda_[crowdIdx]);
                                }
                                if (curFitness > maxFitness) {
                                    targetIdx = curIdx;
                                    maxFitness = curFitness;
                                }
                            }
                            if (idxArray[targetIdx] == -1) {
                                nondominated_sorting_delete(indiv);
                            } else {
                                int indivRank = (int) indiv.getAttribute(dominanceRankingAttributeIdentifier);
                                int targetRank = (int) population_.get(idxArray[targetIdx]).getAttribute(dominanceRankingAttributeIdentifier);
                                rankIdx_[targetRank][idxArray[targetIdx]] = 0;
                                rankIdx_[indivRank][idxArray[targetIdx]] = 1;

                                Solution targetSol = population_.get(idxArray[targetIdx]).copy();

                                population_.set(idxArray[targetIdx], indiv);
                                subregionIdx_[crowdIdx][idxArray[targetIdx]] = 0;
                                subregionIdx_[location][idxArray[targetIdx]] = 1;

                                // update the non-domination level structure
                                nondominated_sorting_delete(targetSol);
                            }
                        }
                        break;
                }
            }
        }

    }

    void initIdealPoint() {
        for (int i = 0; i < problem_.getNumberOfObjectives(); i++) {
            zp_[i] = 1.0e+30;
        }

        for (int i = 0; i < populationSize_; i++) {
            updateReference(population_.get(i), zp_);
        }
    } // initIdealPoint

    void updateReference(Solution indiv, double[] z_) {
        for (int i = 0; i < problem_.getNumberOfObjectives(); i++) {
            if (indiv.getObjective(i) < z_[i]) {
                z_[i] = indiv.getObjective(i);
            }
        }
    } // updateReference

    /**
     * Initialize the nadir point
     *
     * @throws JMException
     * @throws ClassNotFoundException
     */
    void initNadirPoint() {
        for (int i = 0; i < problem_.getNumberOfObjectives(); i++) {
            nzp_[i] = -1.0e+30;
        }

        for (int i = 0; i < populationSize_; i++) {
            updateNadirPoint(population_.get(i), nzp_);
        }
    } // initNadirPoint

    void updateNadirPoint(Solution indiv, double[] nz_) {
        for (int i = 0; i < problem_.getNumberOfObjectives(); i++) {
            if (indiv.getObjective(i) > nz_[i]) {
                nz_[i] = indiv.getObjective(i);
            }
        }
    } // updateNadirPoint

    void RefreshNadirPoint() {
        for (int i = 0; i < problem_.getNumberOfObjectives(); i++) {
            nzp_[i] = -1.0e+30;
        }

        for (int i = 0; i < populationSize_; i++) {
            updateNadirPoint(population_.get(i), nzp_);
        }
    } // initNadi

    double fitnessFunction(Solution indiv, double[] lambda) {
        double fitness = 0;

        switch (functionType_) {
            case "_TCHE1": {
                double maxFun = -1.0e+30;
                for (int n = 0; n < problem_.getNumberOfObjectives(); n++) {
                    double diff = Math.abs(indiv.getObjective(n) - zp_[n]);

                    double feval;
                    if (lambda[n] == 0) {
                        feval = 0.0001 * diff;
                    } else {
                        feval = diff * lambda[n];
                    }
                    if (feval > maxFun) {
                        maxFun = feval;
                    }
                } // for
                fitness = maxFun;
                break;
            }
            case "_TCHE2": {
                double maxFun = -1.0e+30;
                for (int i = 0; i < problem_.getNumberOfObjectives(); i++) {
                    double diff = Math.abs(indiv.getObjective(i) - zp_[i]);

                    double feval;
                    if (lambda[i] == 0) {
                        feval = diff / 0.000001;
                    } else {
                        feval = diff / lambda[i];
                    }
                    if (feval > maxFun) {
                        maxFun = feval;
                    }
                } // for
                fitness = maxFun;
                break;
            }
            case "_PBI":
                double theta; // penalty parameter
                theta = 5.0;
                // normalize the weight vector (line segment)
                double nd = norm_vector(lambda);
                for (int i = 0; i < problem_.getNumberOfObjectives(); i++) {
                    lambda[i] = lambda[i] / nd;
                }
                double[] realA = new double[problem_.getNumberOfObjectives()];
                double[] realB = new double[problem_.getNumberOfObjectives()];
                // difference between current point and reference point
                for (int n = 0; n < problem_.getNumberOfObjectives(); n++) {
                    realA[n] = (indiv.getObjective(n) - zp_[n]);
                }   // distance along the line segment
                double d1 = Math.abs(innerproduct(realA, lambda));
                // distance to the line segment
                for (int n = 0; n < problem_.getNumberOfObjectives(); n++) {
                    realB[n] = (indiv.getObjective(n) - (zp_[n] + d1 * lambda[n]));
                }
                double d2 = norm_vector(realB);
                fitness = d1 + theta * d2;
                break;
            default:
                System.out.println("MOEAD.fitnessFunction: unknown type "
                        + functionType_);
                System.exit(-1);
        }
        return fitness;
    } // fitnessEvaluation
}
