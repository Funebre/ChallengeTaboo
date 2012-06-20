import java.util.Random;
import java.util.Vector;


public class AlgorithmeEvolutionnaire {
	
	private Population pop;
	private int nbGenerations;
	private int popSize;
	private double crossbreedLevel;
	private double mutationLevel;
	private Problem pb;
	
	public AlgorithmeEvolutionnaire(int nbG, int popul, double cbLevel, double mLevel, Problem prob) {
		nbGenerations = nbG;
		popSize = popul;
		crossbreedLevel = cbLevel;
		mutationLevel = mLevel;
		pb = prob;
		pop = new Population(popSize, pb);
	}
	
	//Constructeur pour un full random
	public AlgorithmeEvolutionnaire(int popul, Problem prob) {
		popSize = popul;
		pb = prob;
		pop = new Population(popSize, pb);
	}
	
	public Solution run() {
		Solution sol = new Solution(pb);
		int i = 0;
		while (i < nbGenerations) {
			Random r = new Random();
			
			if (r.nextInt(100) < crossbreedLevel*100) {
				
				//Utilisation de la selection au hasard
				Solution father = pop.getIndividuals().get(r.nextInt(popSize));
				Solution mother = pop.getIndividuals().get(r.nextInt(popSize));
				
				//Utilisation de la selection par roulette
				/*Solution father = pop.rouletteSelection();
				Solution mother = pop.rouletteSelection();*/
							
				crossbreed(father, mother);
			}
			
			if (r.nextInt(100) < mutationLevel*100) {
				Solution father = pop.getIndividuals().get(r.nextInt(popSize));
				
				father.reverseRandomBatchSequence(father.productionSequenceMT);
				father.reverseRandomBatchSequence(father.deliverySequenceMT);
				
				if (father.evaluate() < pop.getBest().getEvaluation()) 
					pop.setBest(father);
			}
				
			i++;
		}
		
		sol = pop.getBest();
		
		return sol;
	}

	//cross breed two solutions
	public void crossbreed(Solution father, Solution mother) {
		Solution newChild1 = new Solution(mother);
		Solution newChild2 = new Solution(father);
		
		//production of mother in newChild1
		newChild1.setProductionSequenceMT((Vector) father.getProductionSequenceMT().clone());
		
		//production of father in newChild2
		newChild2.setProductionSequenceMT((Vector) mother.getProductionSequenceMT().clone());
		
		//if the're better solutions than their parents, replace
		if (newChild1.evaluate() < pop.getBest().evaluation) {
			pop.setBest(newChild1);
			father = newChild1;
		}
		
		if (newChild2.evaluate() < pop.getBest().evaluation) {
			pop.setBest(newChild2);
			mother = newChild2;
		}
	}

	public Population getPop() {
		return pop;
	}
}
