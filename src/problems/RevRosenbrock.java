package problems;

import java.util.ArrayList;

public class RevRosenbrock extends Problem {

	public RevRosenbrock() {
		super();

		ArrayList<Double> maxValues = new ArrayList<>();
		for (int i = 0; i < getDimensions(); i++) {
			maxValues.add(2.0);
		}
		this.setMaxValues(maxValues);

		ArrayList<Double> minValues = new ArrayList<>();
		for (int i = 0; i < getDimensions(); i++) {
			minValues.add(-2.0);
		}
		this.setMinValues(minValues);
	}

	@Override
	public double Eval(ArrayList<Double> paramVals) {
		int numberOfVariables = paramVals.size();

		double[] x = new double[numberOfVariables];

		for (int i = 0; i < numberOfVariables; i++) {
			x[i] = paramVals.get(i);
		}

		double sum = 0.0;

		for (int i = 0; i < numberOfVariables - 1; i++) {
			double temp1 = (x[i] * x[i]) - x[i + 1];
			double temp2 = x[i] - 1.0;
			sum += (100.0 * temp1 * temp1) + (temp2 * temp2);
		}
		sum = -1 * sum;
		return sum;
	}

	@Override
	public int getDimensions() {
		return 3;
	}
}
