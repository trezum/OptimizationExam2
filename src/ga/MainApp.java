package ga;
import problems.*;

public class MainApp {

	public static void main(String[] args) {
		SimpleGeneticAlgorithm ga = new SimpleGeneticAlgorithm(new P1(),10,0.025,0.5,5);
		ga.runAlgorithm(50);
	}
}
