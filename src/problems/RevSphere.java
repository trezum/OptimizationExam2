package problems;

import java.util.ArrayList;

public class RevSphere extends Problem {

	public RevSphere() {
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
		double sum = 0.0;
		for (int i = 0; i < paramVals.size(); i++) {
			sum = sum - Math.pow(paramVals.get(i), 2.0);
		}
		return sum;
	}

	@Override
	public int getDimensions() {
		return 20;
	}

}
