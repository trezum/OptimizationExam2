package problems;

import ga.Individual;
import ga.SimpleGeneticAlgorithm;

import java.util.ArrayList;

import static java.lang.Math.abs;

public class MetaEvolution extends Problem  {

    private static final int populationSize = 50;
    private static final int generations = 100;
    private static final int averagingCount = 20;
    private Problem problem;

    public MetaEvolution(Problem p) {
        super();
        problem = p;

        ArrayList<Double> maxValues = new ArrayList<>();
        maxValues.add((double)populationSize); // Elite
        maxValues.add((double)populationSize); // TournamentSize
        maxValues.add(1.0); // CrossoverRate
        maxValues.add(1.0); // MutationRate
        this.setMaxValues(maxValues);

        ArrayList<Double> minValues = new ArrayList<>();
        minValues.add(0.0); // Elite
        minValues.add(1.0); // TournamentSize
        minValues.add(0.0); // CrossoverRate
        minValues.add(0.0); // MutationRate
        this.setMinValues(minValues);
    }
    @Override
    public double Eval(ArrayList<Double> paramVals) {
        this.EvalCallCount++;
        ArrayList<Individual> bestIndividuals = new ArrayList<>();

        int evalCount = 0;

        for (int i = 0; i < averagingCount; i++) {
            // We are using doubles to work with integer values here, the evolution would be more efficient if it supported working with both int and doubles.
            SimpleGeneticAlgorithm sga = new SimpleGeneticAlgorithm(problem, generations, paramVals.get(3), paramVals.get(2), paramVals.get(1).intValue(), paramVals.get(0).intValue(), populationSize);
            bestIndividuals.add(sga.runAlgorithmWithStopAtOptimum());
            evalCount += problem.getEvalCallCount();
        }
        double eval = abs(calculateAverageFitness(bestIndividuals)) * (-1*evalCount);
        //System.out.println(eval + "\t" + paramVals);
        return eval;
    }

    @Override
    public Double getOptimumEvalIfKnown() {
        return null;
    }

    @Override
    public int getDimensions() {
        return 4;
    }

    private static double calculateAverageFitness(ArrayList<Individual> individuals){
        double fitness = 0;

        for (Individual i : individuals) {
            fitness+= i.getFitness();
        }
        return fitness/individuals.size();
    }
}
