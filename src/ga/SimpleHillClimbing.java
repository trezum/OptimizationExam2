package ga;

import problems.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class SimpleHillClimbing {


	private final Problem curProblem;

	public SimpleHillClimbing(final Problem problem) {
		this.curProblem = problem;
	}

	public ArrayList<Double> findOptima(int iterations, double stepSize, int neighbours) {


		ArrayList<Double> bestGlobalPoint = this.getRandomPoint();
		double bestGlobal = this.curProblem.Eval(bestGlobalPoint);

		for (int i = 0; i < iterations; i++) {
			boolean shouldContinue;

			// Hack for increasing exploitation temporarily, for a P2 Experiment.
			//var bestPoint = new ArrayList<Double>();
			//bestPoint.add(1.6973296405794807);
			//bestPoint.add(1.6973296405794807);

			// Select a random value as a starting point aka 'best solution'
			ArrayList<Double> bestPoint = this.getRandomPoint();
			double bestSolution = this.curProblem.Eval(bestPoint);

			do {
				// Select a random neighbour
				ArrayList<Double> newPoint = this.getBestNeighbourPoint(bestPoint, stepSize, neighbours);
				double newSolution = this.curProblem.Eval(newPoint);
				// If a new solution's value is greater than current, best solution
				if (bestSolution < newSolution) {
					// Change the best solution
					bestSolution = newSolution;
					bestPoint = newPoint;
					// And continue searching
					shouldContinue = true;
				} else {
					// Otherwise stop
					shouldContinue = false;
				}

			} while (shouldContinue);

			if (bestGlobal < bestSolution )
			{
				bestGlobal = bestSolution;
				bestGlobalPoint = bestPoint;
				//System.out.println(bestGlobal);
			}
		}
		return bestGlobalPoint;
	}

	private ArrayList<Double> getBestNeighbourPoint(ArrayList<Double> point, double stepSize, int neighbours){
		ArrayList<Double> bestNeighbourPoint = getNeighbourPoint(point, stepSize);
		double bestNeighbour = curProblem.Eval(bestNeighbourPoint);
		for (int i = 0; i < neighbours-1; i++) {
			ArrayList<Double> newNeighbour = getNeighbourPoint(point, stepSize);
			double newNeighborEvaluation = curProblem.Eval(newNeighbour);
			if (newNeighborEvaluation > bestNeighbour){
				bestNeighbourPoint = newNeighbour;
				bestNeighbour = newNeighborEvaluation;
			}
		}
		//System.out.println("Better neighbors found:" + betterNeighbourCounter);
		return bestNeighbourPoint;
	}

	private ArrayList<Double> getNeighbourPoint(ArrayList<Double> point, double stepSize){
		ArrayList<Double> neighbourPoint = new ArrayList<Double>();

		for (int i = 0; i < curProblem.getDimensions(); i++) {
			double newParam;
			if (Math.random() > 0.5){
				newParam = point.get(i) - Math.random() * stepSize;
				if (newParam < curProblem.getMinValues().get(i)) {
					newParam = curProblem.getMinValues().get(i);
				}
			}
			else
			{
				newParam = point.get(i) + Math.random() * stepSize;
				if (newParam > curProblem.getMaxValues().get(i)) {
					newParam = curProblem.getMaxValues().get(i);
				}
			}
			neighbourPoint.add(newParam);
		}
		return neighbourPoint;
	}

	private ArrayList<Double> getRandomPoint() {
		ArrayList<Double> initPoint = new ArrayList<>();
		for (int dim = 0; dim < curProblem.getDimensions(); dim++) {
			initPoint.add(curProblem.getMinValues().get(dim) + Math.random() * (curProblem.getMaxValues().get(dim) - curProblem.getMinValues().get(dim)));
		}
		return initPoint;
	}

	private static void iterateAllParams(){
		ArrayList<Problem> problems = new ArrayList<Problem>();
		problems.add(new P1());
		problems.add(new P2());
		problems.add(new RevAckley());

		int[] iterations = new int[3];
		iterations[0] = 10;
		iterations[1] = 100;
		iterations[2] = 200;

		int[] neighbours = new int[3];
		neighbours[0] = 10;
		neighbours[1] = 100;
		neighbours[2] = 200;

		double[] stepSizes = new double[3];
		stepSizes[0] = 0.1;
		stepSizes[1] = 0.01;
		stepSizes[2] = 0.001;

		Locale currentLocale = Locale.getDefault();
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(currentLocale);
		otherSymbols.setDecimalSeparator(',');
		DecimalFormat df = new DecimalFormat("#.####################", otherSymbols);

		for (Problem p : problems)
		{
			SimpleHillClimbing test = new SimpleHillClimbing(p);
			System.out.println("---------------------Next Problem---------------------");
			for (int i = 0; i < iterations.length; i++) {
				for (int n = 0; n < neighbours.length; n++) {
					for (int s = 0; s < stepSizes.length; s++)
					{
						var point = test.findOptima(iterations[i],stepSizes[s],neighbours[n]);
						//For console
						System.out.println("Iterations:" + iterations[i] + "\tStepsize:" + stepSizes[s]+ "\tNeighbours:" + neighbours[n]+"\t"+ p.EvalCallCount+ "\t" + String.format("%.14f", p.Eval(point))+ "\t" + point);

						//For excel
						//System.out.println(iterations[i] + "\t" + stepSizes[s]+ "\t" + neighbours[n]+"\t"+ p.EvalCallCount+ "\t" + df.format(p.Eval(point))+ "\t" + point);

						p.ResetEvalCallCount();
					}
				}
			}
		}
	}
	public static void runOne(){
		Problem p = new P2();
		SimpleHillClimbing test = new SimpleHillClimbing(p);
		var point = test.findOptima(1000000,0.00001,100);
		System.out.println(point);
		System.out.println(p.Eval(point));
		System.out.println(p.EvalCallCount);
}
	public static void evalBest(){
		var p1Top = new ArrayList<Double>();
		p1Top.add(0.0);
		p1Top.add(0.0);
		System.out.println("P1 top eval: " + new P1().Eval(p1Top));

		var p2Top = new ArrayList<Double>();
		p2Top.add(1.6973307014531214);
		p2Top.add(1.6973307025972542);
		System.out.println("P2 top eval: " + new P2().Eval(p2Top));

		var revAckleyTop = new ArrayList<Double>();
		revAckleyTop.add(0.0);
		revAckleyTop.add(0.0);
		System.out.println("RevAckley top eval: " + new RevAckley().Eval(revAckleyTop));
	}
	private static void evalP2Points() {
		//Point from experimentation
		//[1.6973296405794807, 1.697333243601135]

		var p2Found = new ArrayList<Double>();
		p2Found.add(1.6973296405794807);
		p2Found.add(1.697333243601135);
		System.out.println("P2 found point: " + new P2().Eval(p2Found));

		var p2Left = new ArrayList<Double>();
		p2Left.add(1.6973296405794807);
		p2Left.add(1.6973296405794807);
		System.out.println("P2 repeated left: " + new P2().Eval(p2Left));

		var p2Right = new ArrayList<Double>();
		p2Right.add(1.697333243601135);
		p2Right.add(1.697333243601135);
		System.out.println("P2 repeated right: " + new P2().Eval(p2Right));

	}
	public static void main(String[] args) {
		//runOne();
		//evalP2Points();
		iterateAllParams();
		evalBest();
	}
}