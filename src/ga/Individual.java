package ga;
import problems.Problem;
import java.util.ArrayList;

public class Individual implements Comparable<Individual> {

	protected int defaultGeneLength = 2;
	private ArrayList<Double> genes = new ArrayList<Double>();
	private double fitness = Double.MIN_VALUE;
	private Problem problem;

	public Individual(Problem problem) {
		this.problem = problem;
		defaultGeneLength = problem.getDimensions();
		genes = getRandomPoint();
	}

	private ArrayList<Double> getRandomPoint() {
		ArrayList<Double> initPoint = new ArrayList<>();
		for (int dim = 0; dim < problem.getDimensions(); dim++) {
			initPoint.add(problem.getMinValues().get(dim) + Math.random() * (problem.getMaxValues().get(dim) - problem.getMinValues().get(dim)));
		}
		return initPoint;
	}
	protected ArrayList<Double> getGenes() {
		return genes;
	}

	protected double getSingleGene(int index) {
		return genes.get(index);
	}

	protected void setSingleGene(int index, double value) {
		genes.set(index,value);
		fitness = Double.MIN_VALUE;
	}

	public double getFitness() {
		if (fitness == Double.MIN_VALUE) {
			fitness = this.problem.Eval(getGenes());
		}
		return fitness;
	}

	@Override
	public String toString() {
		String geneString = "(";
		for (int i = 0; i < genes.size(); i++) {
			geneString += getSingleGene(i);
			if (i < genes.size()-1){
				geneString += ", ";
			}
		}
		geneString += ")";
		return geneString;
	}

	public int getDefaultGeneLength() {
		return defaultGeneLength;
	}

	@Override
	public int compareTo(Individual otherIndividual) {
		if (this.getFitness() < otherIndividual.getFitness())
			return -1;
		if (this.getFitness() > otherIndividual.getFitness())
			return 1;
		return 0;
	}
}