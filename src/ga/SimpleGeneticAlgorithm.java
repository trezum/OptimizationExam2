package ga;
import problems.*;
import java.util.Collections;

public class SimpleGeneticAlgorithm {

	private double crossoverRate = 0.5;
	private double mutationRate = 0.025;
	private int tournamentSize = 5;
	private Problem problem;
	private int generations = 0;
	private int elite = 0;
	private boolean verbose = false;

	public SimpleGeneticAlgorithm(Problem problem, int generations, double mutationRate, double crossOverRate, int tournamentSize){
		this.problem = problem;
		this.generations = generations;
		this.mutationRate = mutationRate;
		this.crossoverRate = crossOverRate;
		this.tournamentSize = tournamentSize;
	}

	public boolean runAlgorithm(int populationSize) {
		Population myPop = new Population(populationSize, true, this.problem);

		int generationCount = 1;
		while (generationCount < generations) {
			System.out.println("Generation: " + generationCount + " Eval calls: "+ problem.EvalCallCount+" Eval: " + myPop.getFittest().getFitness());
			myPop = evolvePopulation(myPop);
			generationCount++;
		}
		System.out.println("Generation: " + generationCount);
		System.out.println("Genes: ");
		System.out.println(myPop.getFittest());
		return true;
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
			Individual indiv1 = tournamentSelection(pop);
			Individual indiv2 = tournamentSelection(pop);
			Individual newIndiv = crossover(indiv1, indiv2);
			newPopulation.getIndividuals().add(i, newIndiv);
		}

		for (int i = elitismOffset; i < newPopulation.getIndividuals().size(); i++) {
			mutate(newPopulation.getIndividual(i));
		}

		return newPopulation;
	}

	private Individual crossover(Individual indiv1, Individual indiv2) {
		Individual newChild = new Individual(this.problem);
		for (int i = 0; i < newChild.getDefaultGeneLength(); i++) {
			if (Math.random() <= crossoverRate) {
				newChild.setSingleGene(i, indiv1.getSingleGene(i));
			} else {
				newChild.setSingleGene(i, indiv2.getSingleGene(i));
			}
		}
		if(verbose){
			System.out.println("newChild: "+ newChild);
		}
		return newChild;
	}

	private void mutate(Individual indiv) {
		for (int i = 0; i < indiv.getDefaultGeneLength(); i++) {
			if (Math.random() <= mutationRate) {
				byte gene = (byte) Math.round(Math.random());
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

	protected static double getFitness(Individual individual, Problem problem) {
		return problem.Eval(individual.getGenes());
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}
}