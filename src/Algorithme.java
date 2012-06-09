import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;


public class Algorithme {
	
	private Population pop;
	private int nbGenerations;
	private int popSize;
	private double crossbreedLevel;
	private double mutationLevel;
	private Problem pb;
	
	
	public Algorithme(int nbG, int popul, float cbLevel, float mLevel, Problem prob) {
		nbGenerations = nbG;
		popSize = popul;
		crossbreedLevel = cbLevel;
		mutationLevel = mLevel;
		pb = prob;
		pop = new Population(popSize, pb);
	}
	
	//Constructeur pour un full random
	public Algorithme(int popul, Problem prob) {
		popSize = popul;
		pb = prob;
		pop = new Population(popSize, pb);
	}
	
	public Solution run() {
		Solution sol = new Solution(pb);
		int i = 0;
		while (i < nbGenerations) {
			Random r = new Random();
			
			if(r.nextInt(100) < crossbreedLevel*100) {
				int r1 = r.nextInt(popSize);
				int r2 = r.nextInt(popSize);
				
				//Ces affectations sont très longues à cause des get
				Solution father = pop.getIndividuals().get(r1);
				Solution mother = pop.getIndividuals().get(r2);
							
				crossbreedProduction(father, mother);
			}
			
			if(r.nextInt(100) < mutationLevel*100) {			
				//long (environ 6 secondes)
				Solution father = pop.getIndividuals().get(r.nextInt(popSize));
				father.swapRandomBatches(father.productionSequenceMT);
				pop.getIndividuals().add(r.nextInt(popSize), father);
				
				//long (environ 5 secondes)
				/*while(pop.isPresent(father))
					father.swapRandomBatches(father.productionSequenceMT);*/
				
				
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
		Solution newChild2 = new Solution(pb);
		newChild1.setProductionSequenceMT(father.productionSequenceMT);
		newChild1.setDeliverySequenceMT(mother.deliverySequenceMT);
		
		newChild2.setProductionSequenceMT(mother.productionSequenceMT);
		newChild2.setDeliverySequenceMT(father.deliverySequenceMT);

		//check if newChild1 and newChild2 are not in population, if the're better solutions than their parents, replace
		/*if ( ! pop.isPresent(newChild1)) {*/
			if (newChild1.evaluate() < father.evaluation) {
				pop.setBest(newChild1);
				father = newChild1;
			}
		/*}*/
		
		/*if ( ! pop.isPresent(newChild2)) {*/
			if (newChild2.evaluate() < mother.evaluation) {
				pop.setBest(newChild2);
				mother = newChild2;
			}
		/*}*/
	}
	
	public void crossbreedDelivery(Solution father, Solution mother)
	{
		Solution newChild1 = new Solution(pb);
		Solution newChild2 = new Solution(pb);
		newChild2.setProductionSequenceMT(father.productionSequenceMT);
		newChild2.setDeliverySequenceMT(mother.deliverySequenceMT);
		
		newChild1.setProductionSequenceMT(mother.productionSequenceMT);
		newChild1.setDeliverySequenceMT(father.deliverySequenceMT);
		
		//check if newChild1 and newChild2 are not in population, if the're better solutions than their parents, replace
		if (newChild1.evaluate() < father.evaluation) {
			pop.setBest(newChild1);
			father = newChild1;
		}
		
		if (newChild2.evaluate() < mother.evaluation) {
			pop.setBest(newChild2);
			mother = newChild2;
		}		
	}
	
	

	public Population getPop() {
		return pop;
	}
}
