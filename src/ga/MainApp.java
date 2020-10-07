package ga;
import problems.*;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class MainApp {
	private static final int numberOfAveragingLoops = 1000;

	private static final int defaultElite = 5;
	private static final int defaultPopulationSize = 100;
	private static final int defaultGenerations = 100;
	private static final int defaultTournamentSize = 5;
	private static final double defaultCrossoverRate = 0.5;
	private static final double defaultMutationRate = 0.025;

	public static void main(String[] args) {

		//iterateParams()
		//iterateParamsIndividually();
		//paramsOptimizedByExperiments();
		paramsBestOnlyTwentyRuns();

//		Problem p = new RevRosenbrock();
//		SimpleGeneticAlgorithm sga = new SimpleGeneticAlgorithm(p,100000,0.025,0.50,5,1);
//		printAverages(new ArrayList(Arrays.asList(sga.runAlgorithm(5000))),p,"");
	}

	private static void paramsBestOnlyTwentyRuns() {
		int loops = 20;
		simpleAveraging(loops, new P1(),defaultGenerations,200,defaultTournamentSize,defaultElite,defaultMutationRate,defaultCrossoverRate);
		simpleAveraging(loops, new P2(),500,200,5,4,0.45,0.3);
		simpleAveraging(loops, new RevAckley(),500,200,2,2,0.475,0.9);
		simpleAveraging(loops, new RevSphere(),500,200,15,3,0.075,0.2);
		simpleAveraging(loops, new RevRosenbrock(), 480,200,6,6,0.6,0.6);
	}
	
	private static void paramsOptimizedByExperiments() {
		int loops = 1000;
		simpleAveraging(loops, new P1(),500,200,2,0,0.475,0.9);
		simpleAveraging(loops, new P2(),500,200,5,4,0.45,0.3);
		simpleAveraging(loops, new RevAckley(),500,200,2,2,0.475,0.9);
		simpleAveraging(loops, new RevSphere(),500,200,15,3,0.075,0.2);
		simpleAveraging(loops, new RevRosenbrock(), 480,200,6,6,0.6,0.6);
	}

	private static void simpleAveraging(int loops,Problem p,int generations, int populationSize,int tournamentSize, int elite, double mutationRate, double crossoverRate) {
		System.out.println("--------------------- " + p.getName() + " ---------------------");
		SimpleGeneticAlgorithm sga;
		ArrayList<Individual> individuals = new ArrayList<>();
		for (int j = 0; j < loops; j++) {
			sga = new SimpleGeneticAlgorithm(p, generations, mutationRate, crossoverRate, tournamentSize, elite);
			individuals.add(sga.runAlgorithm(populationSize));
		}
		printAverages(individuals,p,"");
	}

	private static void iterateParamsIndividually(){
		long start = System.currentTimeMillis();

		ArrayList<Problem> problems = new ArrayList<Problem>();
		problems.add(new P1());
		problems.add(new P2());
		problems.add(new RevAckley());
		problems.add(new RevSphere());
		problems.add(new RevRosenbrock());

		for (Problem p : problems)
		{
			System.out.println("---------------------" + p.getName() + "---------------------");
			System.out.println("Default parameters");
			SimpleGeneticAlgorithm sga = new SimpleGeneticAlgorithm(p, defaultGenerations, defaultMutationRate, defaultCrossoverRate, defaultTournamentSize, defaultElite);
			printAverages(new ArrayList(Arrays.asList(sga.runAlgorithm(defaultPopulationSize))),p,"");

			// The vary methods could be merged to one, but it seems like more work because of the many parameters.
			// Also a way of handling the swap of iterated parameter would be needed.
			varyGenerations(p);
			varyPopulationSize(p);
			varyTournamentSize(p);
			varyElite(p);
			varyCrossoverRate(p);
			varyMutationRate(p);
		}
		long finish = System.currentTimeMillis();
		long timeElapsedSeconds = (finish - start)/1000;
		System.out.println("Total run time:" + timeElapsedSeconds + " seconds");
	}

	private static void varyMutationRate(Problem p) {
		System.out.println("--------------------- " + p.getName() + " vary MutationRate ---------------------");
		SimpleGeneticAlgorithm sga;
		for (double i = 0.0; i <= 0.51 ; i+=0.025) {
			ArrayList<Individual> individuals = new ArrayList<>();
			for (int j = 0; j < numberOfAveragingLoops; j++) {
				sga = new SimpleGeneticAlgorithm(p, defaultGenerations, i, defaultCrossoverRate, defaultTournamentSize, defaultElite);
				individuals.add(sga.runAlgorithm(defaultPopulationSize));
			}
			printAverages(individuals,p,"MutationRate:\t" + i +"\t");
		}
	}

	private static void varyCrossoverRate(Problem p) {
		System.out.println("--------------------- " + p.getName() + " vary CrossoverRate ---------------------");
		SimpleGeneticAlgorithm sga;
		for (double i = 0.1; i <= 1 ; i+=0.1) {
			ArrayList<Individual> individuals = new ArrayList<>();
			for (int j = 0; j < numberOfAveragingLoops; j++) {
				sga = new SimpleGeneticAlgorithm(p, defaultGenerations, defaultMutationRate, i, defaultTournamentSize, defaultElite);
				individuals.add(sga.runAlgorithm(defaultPopulationSize));
			}
			printAverages(individuals,p,"CrossoverRate:\t" + i +"\t");
		}
	}

	private static void varyElite(Problem p) {
		System.out.println("--------------------- " + p.getName() + " vary Elite ---------------------");
		SimpleGeneticAlgorithm sga;
		for (int i = 0; i <= 20 ; i+=1) {
			ArrayList<Individual> individuals = new ArrayList<>();
			for (int j = 0; j < numberOfAveragingLoops; j++) {
				sga = new SimpleGeneticAlgorithm(p, defaultGenerations, defaultMutationRate, defaultCrossoverRate, defaultTournamentSize, i);
				individuals.add(sga.runAlgorithm(defaultPopulationSize));
			}
			printAverages(individuals,p,"Elite:\t" + i +"\t");
		}
	}

	private static void varyTournamentSize(Problem p){
		System.out.println("--------------------- " + p.getName() + " vary TournamentSize ---------------------");
		SimpleGeneticAlgorithm sga;
		for (int i = 1; i <= 15 ; i+=1) {
			ArrayList<Individual> individuals = new ArrayList<>();
			for (int j = 0; j < numberOfAveragingLoops; j++) {
				sga = new SimpleGeneticAlgorithm(p, defaultGenerations, defaultMutationRate, defaultCrossoverRate, i, defaultElite);
				individuals.add(sga.runAlgorithm(defaultPopulationSize));
			}
			printAverages(individuals,p,"TournamentSize:\t" + i +"\t");
		}
	}

	private static void varyPopulationSize(Problem p){
		System.out.println("--------------------- " + p.getName() + " vary PopulationSize ---------------------");
		SimpleGeneticAlgorithm sga;
		//Can not be lower than 5 becaus of the default elite size.
		for (int i = 20; i <= 200 ; i+=20) {
			ArrayList<Individual> individuals = new ArrayList<>();
			for (int j = 0; j < numberOfAveragingLoops; j++) {
				sga = new SimpleGeneticAlgorithm(p, defaultGenerations, defaultMutationRate, defaultCrossoverRate, defaultTournamentSize, defaultElite);
				individuals.add(sga.runAlgorithm(i));
			}
			printAverages(individuals,p,"PopulationSize:\t" + i +"\t");
		}
	}

	private static void varyGenerations(Problem p){
		System.out.println("--------------------- " + p.getName() + " vary Generations ---------------------");
		SimpleGeneticAlgorithm sga;
		for (int i = 0; i <= 500 ; i+=20) {
			ArrayList<Individual> individuals = new ArrayList<>();
			for (int j = 0; j < numberOfAveragingLoops; j++) {
				sga = new SimpleGeneticAlgorithm(p, i, defaultMutationRate, defaultCrossoverRate, defaultTournamentSize, defaultElite);
				individuals.add(sga.runAlgorithm(defaultPopulationSize));
			}
			printAverages(individuals,p,"Generations:\t" + i +"\t");
		}
	}

	//Averaging eval does not make sense for this evolutionary algorithm because it is always the same with the same parameters.
	//If we add a goal for the algorithm and end the run after it has been reached it would make sense, maybe i will have time to do that.
	private static void printAverages(ArrayList<Individual> individuals, Problem p, String vary){
		//Console
		//System.out.println(vary + "Fitness:\t" + calculateAverageFitness(individuals) + "\t\t" + "Evaluations:\t" + p.getEvalCallCount()/individuals.size() + "\t" + calculateAverageIndividual(individuals,p));

		//Excel
		Locale currentLocale = Locale.getDefault();
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
		otherSymbols.setDecimalSeparator(',');
		DecimalFormat df = new DecimalFormat("#.####################", otherSymbols);
		System.out.println(vary+df.format(+calculateAverageFitness(individuals)) + "\t" + p.getEvalCallCount()/individuals.size() + "\t" + calculateAverageIndividual(individuals,p));

		//Reset eval count
		p.resetEvalCallCount();
	}

	//The problem p argument is not really used here, this is kind of a hacked usage of individuals in their current form.
	//I added the problem here for good measure so the individual still belongs to the current problem.
	private static Individual calculateAverageIndividual(ArrayList<Individual> individuals, Problem p){
		double[] averageGenes = new double[individuals.get(0).defaultGeneLength];
		for (Individual i : individuals) {
			for (int j = 0; j < i.defaultGeneLength; j++) {
				averageGenes[j] += i.getSingleGene(j);
			}
		}
		Individual averageIndividual = new Individual(p);
		for (int i = 0; i < individuals.get(0).defaultGeneLength; i++) {
			averageIndividual.setSingleGene(i,averageGenes[i]/individuals.size());
		}
		return averageIndividual;
	}

	private static double calculateAverageFitness(ArrayList<Individual> individuals){
		double fitness = 0;

		for (Individual i : individuals) {
			fitness+= i.getFitness();
		}
		return fitness/individuals.size();
	}

	private static void iterateParams(){
		//The data output of this method is too big to continue working with manually, iterateParamsIndividually() instead.
		ArrayList<Problem> problems = new ArrayList<Problem>();
		problems.add(new P1());
//		problems.add(new P2());
//		problems.add(new RevAckley());
//		problems.add(new RevSphere());
//		problems.add(new RevRosenbrock());

		Locale currentLocale = Locale.getDefault();
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
		otherSymbols.setDecimalSeparator(',');
		DecimalFormat df = new DecimalFormat("#.####################", otherSymbols);

		for (Problem p : problems)
		{
			System.out.println("---------------------" + p.getName() + "---------------------");

			for (int population = 25; population <= 200; population += 25) {
				for (int generations = 50; generations <= 200; generations += 50) {
					for (double mutationRate = 0.025; mutationRate <= 0.25; mutationRate += 0.1){
						for (double crossoverRate = 0.25; crossoverRate <= 1.0; crossoverRate += 0.25) {
							for (int tournamentSize = 2; tournamentSize <= 10; tournamentSize += 2) {
								for (int elite = 0; elite <= 5; elite += 1) {
									SimpleGeneticAlgorithm test = new SimpleGeneticAlgorithm(p, generations, mutationRate, crossoverRate, tournamentSize,elite);
									var bestIndividual = test.runAlgorithm(population);

									//For console
									System.out.println(
											"Population:" + population +
											"\tGenerations:" + generations +
											"\tMutationRate:" + mutationRate +
											"\tCrossoverRate:" + crossoverRate +
											"\tTournamentSize:" + tournamentSize +
											"\tElite:" + elite +
											"\tEval:" + p.getEvalCallCount() +
											"\tFittest:" + String.format("%.14f", bestIndividual.getFitness()) +
											"\tGenes:" + bestIndividual.getGenes()
									);

									//For excel
									//System.out.println(iterations[i] + "\t" + stepSizes[s]+ "\t" + neighbours[n]+"\t"+ p.EvalCallCount+ "\t" + df.format(p.Eval(point))+ "\t" + point);

									p.resetEvalCallCount();
								}
							}
						}
					}
				}
			}
		}
	}
}
