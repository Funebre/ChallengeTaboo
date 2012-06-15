import java.util.Random;
import java.util.Vector;


public class AlgorithmeEvolutionnaire {
	
	private Population pop;
	private int nbGenerations;
	private int popSize;
	private double crossbreedLevel;
	private double mutationLevel;
	private Problem pb;
	
	public AlgorithmeEvolutionnaire(int nbG, int popul, float cbLevel, float mLevel, Problem prob) {
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
							
				crossbreedProduction(father, mother);
				crossbreedDelivery(father, mother);
			}
			
			if (r.nextInt(100) < mutationLevel*100) {			
				//long (environ 6 secondes)
				Solution father = pop.getIndividuals().get(r.nextInt(popSize));
				
				if(r.nextInt(100) < 80) {
					father.reverseRandomBatchSequence(father.productionSequenceMT);
					father.reverseRandomBatchSequence(father.deliverySequenceMT);
				} 
				else { //Ne marche pas encore
					father.mutation(father.productionSequenceMT);
					father.mutation(father.deliverySequenceMT);
				}
				
				if (father.evaluate() < pop.getBest().evaluate()) 
					pop.setBest(father);
					
			}
				
			i++;
		}
		
		sol = pop.getBest();
		
		return sol;
	}

	//cross breed two solutions
	public void crossbreedProduction(Solution father, Solution mother)
	{
		Solution newChild1 = new Solution(pb);
		newChild1.setDeliverySequenceMT(mother.deliverySequenceMT);
		Solution newChild2 = new Solution(pb);
		newChild2.setDeliverySequenceMT(father.deliverySequenceMT); 
		int i;
		
		//production of father in newChild1
		for (i = 0; i < father.getNumberOfProducedBatches(); ++i) {
			newChild1.addProductionLast(father.getProductionBatch(i).getQuantity());
		}
		
		//production of mother in newChild2
		for (i = 0; i < mother.getNumberOfProducedBatches(); ++i) {
			newChild2.addProductionLast(mother.getProductionBatch(i).getQuantity());
		}
		
		//check if newChild1 and newChild2 are not in population, if the're better solutions than their parents, replace
		/*if ( ! pop.isPresent(newChild1)) {*/
			if (newChild1.evaluate() < pop.getBest().evaluation) {
				pop.setBest(newChild1);
				father = newChild1;
			}
		/*}*/
		
		/*if ( ! pop.isPresent(newChild2)) {*/
			if (newChild2.evaluate() < pop.getBest().evaluation) {
				pop.setBest(newChild2);
				mother = newChild2;
			}
		/*}*/		
	}
	
	public void crossbreedDelivery(Solution father, Solution mother)
	{
		Solution newChild1 = new Solution(pb);
		newChild1.setProductionSequenceMT(mother.productionSequenceMT);
		Solution newChild2 = new Solution(pb);
		newChild2.setProductionSequenceMT(father.productionSequenceMT); 

		int i;
		//delivery of mother in newChild2
		for (i = 0; i < mother.getNumberOfDeliveredBatches(); ++i) {
			newChild2.addDeliveryLast(mother.getDeliveryBatch(i).getQuantity());
		}
		//delivery of father in newChild1
		for (i = 0; i < father.getNumberOfDeliveredBatches(); ++i) {
			newChild1.addDeliveryLast(father.getDeliveryBatch(i).getQuantity());
		}
		
		//check if newChild1 and newChild2 are better solutions than their parents, if so, replace
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
