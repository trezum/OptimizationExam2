package problems;

import java.util.ArrayList;

public abstract class Problem {

	private ArrayList<Double> maxValues;
	private ArrayList<Double> minValues;

	public Problem() {
		maxValues = new ArrayList<>();
		minValues = new ArrayList<>();
	}

	public abstract double Eval(ArrayList<Double> paramVals);

	public abstract int getDimensions();

	public void setMaxValues(ArrayList<Double> maxVals) {
		maxValues = maxVals;
	}

	public void setMinValues(ArrayList<Double> minVals) {
		minValues = minVals;
	}

	public ArrayList<Double> getMaxValues() {
		return maxValues;
	}

	public ArrayList<Double> getMinValues() {
		return minValues;
	}

}
