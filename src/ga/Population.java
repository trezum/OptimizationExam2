package ga;

import problems.Problem;

import java.util.ArrayList;
import java.util.List;

public class Population {

	private List<Individual> individuals;
	private Problem problem;

	public Population(int size, boolean createNew, Problem problem) {
		this.problem = problem;
		individuals = new ArrayList<>();
		if (createNew) {
			createNewPopulation(size);
		}
	}

	protected Individual getIndividual(int index) {
		return individuals.get(index);
	}

	protected Individual getFittest() {
		Individual fittest = individuals.get(0);
		for (int i = 0; i < individuals.size(); i++) {
			if (fittest.getFitness() <= getIndividual(i).getFitness()) {
				fittest = getIndividual(i);
			}
		}
		return fittest;
	}

	private void createNewPopulation(int size) {
		for (int i = 0; i < size; i++) {
			Individual newIndividual = new Individual(this.problem);
			individuals.add(i, newIndividual);
		}
	}

	public List<Individual> getIndividuals() {
		return individuals;
	}
}
