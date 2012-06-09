import java.util.Random;
import java.util.Vector;


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
		//System.out.println("Running evolutionnary algorithm, vive Darwin...");
		Solution sol = new Solution(pb);
		int i = 0;
		while (i < nbGenerations) {
			Random r = new Random();
			
			if(r.nextInt(100) < crossbreedLevel*100) {
				
				//Ces affectations sont très longues à cause des get
				Solution father = pop.getIndividuals().get(r.nextInt(popSize));
				Solution mother = pop.getIndividuals().get(r.nextInt(popSize));
							
				crossbreedProduction(father, mother);
			}
			
			if(r.nextInt(100) < mutationLevel*100) {			
				//long (environ 6 secondes)
				Solution father = pop.getIndividuals().get(r.nextInt(popSize));
				father.swapRandomBatches(father.productionSequenceMT);
				
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
		newChild1.setDeliverySequenceMT(mother.deliverySequenceMT);
		Solution newChild2 = new Solution(pb);
		newChild2.setDeliverySequenceMT(father.deliverySequenceMT); 
		int i;
		
		//production of father in newChild1
		for (i = 0; i < father.getNumberOfProducedBatches(); ++i) {
			newChild1.addProductionLast(father.getProductionBatch(i).getQuantity());
		}
		
		/*//delivery of father in newChild2
		for (i = 0; i < mother.getNumberOfDeliveredBatches(); ++i) {
			newChild2.addDeliveryLast(mother.getDeliveryBatch(i).getQuantity());
		}*/
		
		//production of mother in newChild2
		for (i = 0; i < mother.getNumberOfProducedBatches(); ++i) {
			newChild2.addProductionLast(mother.getProductionBatch(i).getQuantity());
		}
		
		/*//delivery of mother in newChild1
		for (i = 0; i < mother.getNumberOfDeliveredBatches(); ++i) {
			newChild1.addDeliveryLast(mother.getDeliveryBatch(i).getQuantity());
		}*/
		
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
		Solution newChild2 = new Solution(pb);
		int i;
		
		/*//production of father in newChild1
		for (i = 0; i < father.getNumberOfProducedBatches(); ++i) {
			newChild1.addProductionLast(father.getProductionBatch(i).getQuantity());
		}*/
		
		//delivery of father in newChild2
		for (i = 0; i < mother.getNumberOfDeliveredBatches(); ++i) {
			newChild2.addDeliveryLast(mother.getDeliveryBatch(i).getQuantity());
		}
		
		/*//production of mother in newChild2
		for (i = 0; i < mother.getNumberOfProducedBatches(); ++i) {
			newChild2.addProductionLast(mother.getProductionBatch(i).getQuantity());
		}*/
		
		//delivery of mother in newChild1
		for (i = 0; i < mother.getNumberOfDeliveredBatches(); ++i) {
			newChild1.addDeliveryLast(mother.getDeliveryBatch(i).getQuantity());
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
	
	

	public Population getPop() {
		return pop;
	}
}
