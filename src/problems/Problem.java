package problems;

import java.util.ArrayList;

public abstract class Problem {

	private ArrayList<Double> maxValues;
	private ArrayList<Double> minValues;
	protected int EvalCallCount = 0;

	public Problem() {
		maxValues = new ArrayList<>();
		minValues = new ArrayList<>();
	}

	public abstract double Eval(ArrayList<Double> paramVals);
	public abstract Double getOptimumEvalIfKnown();
	public abstract int getDimensions();
	public String getName(){
		String[] stringArray = getClass().getName().split("\\.");
		return stringArray[stringArray.length-1];
	}

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

	public void resetEvalCallCount() {
		EvalCallCount = 0;
	}
	public int getEvalCallCount() {
		return EvalCallCount;
	}
}
