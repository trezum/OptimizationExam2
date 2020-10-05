package ga;
import problems.*;

public class MainApp {

	public static void main(String[] args) {
		Problem p = new RevSphere();
		SimpleGeneticAlgorithm ga = new SimpleGeneticAlgorithm(p,1000,0.025,0.5,5);
		ga.runAlgorithm(50);

		System.out.println("Eval calls:" + p.EvalCallCount);
	}
}
