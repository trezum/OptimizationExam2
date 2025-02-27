package ga;
import problems.*;
import java.util.Collections;
import java.util.Random;

public class SimpleGeneticAlgorithm {

	private double crossoverRate;
	private double mutationRate;
	private int tournamentSize;
	private Problem problem;
	private int generations;
	private int elite;
	private int populationSize;
	private boolean verbose = false;

	public SimpleGeneticAlgorithm(Problem problem, int generations, double mutationRate, double crossOverRate, int tournamentSize, int elite, int populationSize){
		this.problem = problem;
		this.generations = generations;
		this.mutationRate = mutationRate;
		this.crossoverRate = crossOverRate;
		this.tournamentSize = tournamentSize;
		this.elite = elite;
		this.populationSize = populationSize;
	}

	public Individual runAlgorithm() {
		Population myPop = new Population(populationSize, true, this.problem);

		int generationCount = 1;
		while (generationCount < generations) {
			if (verbose){
				System.out.println("Generation: " + generationCount + " Eval calls: "+ problem.getEvalCallCount()+" Eval: " + myPop.getFittest().getFitness());
			}
			myPop = evolvePopulation(myPop);
			generationCount++;
		}
		if (verbose){
			System.out.println("Generation: " + generationCount);
			System.out.println("Genes: ");
			System.out.println(myPop.getFittest());
		}
		return myPop.getFittest();
	}

	public Individual runAlgorithmWithStopAtOptimum() {
		problem.resetEvalCallCount();
		Population myPop = new Population(populationSize, true, this.problem);

		int generationCount = 1;
		while (generationCount < generations) {
			if (this.problem.getOptimumEvalIfKnown() != null) {
				if (myPop.getFittest().getFitness() == this.problem.getOptimumEvalIfKnown()){
					//Using break here just to handle the null in java, could be added to the while loop with inline nullcheck.
					break;
				}
			}
			myPop = evolvePopulation(myPop);
			generationCount++;
		}
		return myPop.getFittest();
	}

	public int[] runAlgorithmReturnStatistics() {
		Population myPop = new Population(populationSize, true, this.problem);
		int generationCount = 0;
		int[] improvementStatistics = new int[generations];
		double bestFitness = -10000000;
		while (generationCount < generations) {
			var fitness = myPop.getFittest().getFitness();
			if (fitness > bestFitness)
			{
				bestFitness = fitness;
				improvementStatistics[generationCount]++;
			}
			myPop = evolvePopulation(myPop);
			generationCount++;
		}
		return improvementStatistics;
	}

	public Population evolvePopulation(Population pop) {
		int elitismOffset = elite;
		Population newPopulation = new Population(pop.getIndividuals().size(), false, this.problem);
		pop.getIndividuals().sort(Individual::compareTo);

		// Reverse because we are maximizing
		Collections.reverse(pop.getIndividuals());

		// Adding the elite individuals
		for (int i = 0; i < elitismOffset; i++) {
			newPopulation.getIndividuals().add(i, pop.getIndividuals().get(i));
		}

		for (int i = elitismOffset; i < pop.getIndividuals().size(); i++) {
			Individual parent1 = tournamentSelection(pop);
			Individual parent2 = tournamentSelection(pop);
			Individual child = crossover(parent1, parent2);
			newPopulation.getIndividuals().add(i, child);
		}

		for (int i = elitismOffset; i < newPopulation.getIndividuals().size(); i++) {
			mutate(newPopulation.getIndividual(i));
		}

		return newPopulation;
	}

	private Individual crossover(Individual parent1, Individual parent2) {
		Individual child = new Individual(this.problem);
		for (int i = 0; i < child.getDefaultGeneLength(); i++) {

			// Averaging the parents values is mutating them only the else is doing crossover in my opinion.
			if (Math.random() <= crossoverRate) {
				child.setSingleGene(i, (parent1.getSingleGene(i) + parent2.getSingleGene(i))/2);
			} else {
				if (Math.random() <= 0.5) {
					child.setSingleGene(i, parent1.getSingleGene(i));
				}
				else{
					child.setSingleGene(i, parent2.getSingleGene(i));
				}
			}
		}
		return child;
	}

	private void mutate(Individual indiv) {
		Random r = new Random();
		for (int i = 0; i < indiv.getDefaultGeneLength(); i++) {
			if (Math.random() <= mutationRate)
			{
				double gene = 0.1*r.nextGaussian() + indiv.getSingleGene(i);
				if (gene > this.problem.getMaxValues().get(i)){
					gene = this.problem.getMaxValues().get(i);
				}
				else if (gene < this.problem.getMinValues().get(i)){
					gene = this.problem.getMinValues().get(i);
				}
				indiv.setSingleGene(i, gene);
			}
		}
	}

	private Individual tournamentSelection(Population pop) {
		Population tournament = new Population(tournamentSize, false, this.problem);
		for (int i = 0; i < tournamentSize; i++) {
			int randomId = (int) (Math.random() * pop.getIndividuals().size());
			tournament.getIndividuals().add(i, pop.getIndividual(randomId));
		}
		Individual fittest = tournament.getFittest();
		return fittest;
	}
}