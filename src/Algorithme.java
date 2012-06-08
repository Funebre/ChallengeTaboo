
public class Algorithme {
	
	private int nbGenerations;
	private int popSize;
	private double crossbreedLevel;
	private double mutationLevel;
	private Problem pb;
	
	public Algorithme(int nbG, int popul, int cbLevel, int mLevel, Problem pb) {
		nbGenerations = nbG;
		popSize = popul;
		crossbreedLevel = cbLevel;
		mutationLevel = mLevel;
		
		Population pop = new Population(10000, pb);
	}
}
