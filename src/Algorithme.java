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
		
		Population pop = new Population(popSize, pb);
	}
	
	public Solution run() {
		Solution sol = new Solution(pb);
		
		for(int i = 0; i<nbGenerations; i++) {
			Random r = new Random();
			Solution father = pop.getIndividuals().get(r.nextInt(popSize));
			Solution mother = pop.getIndividuals().get(r.nextInt(popSize));
			
			Solution son1 = new Solution(pb);
			Solution son2 = new Solution(pb);
			
			if(r.nextInt(100)/100 < crossbreedLevel*100) {
				crossbreed(father, mother, son1, son2);
			}
			
			father = pop.getIndividuals().get(r.nextInt(popSize));
			
			if(r.nextInt(100)/100 < mutationLevel*100) {
				father.swapRandomBatches(father.productionSequenceMT);
				father.evaluate();
				if (father.evaluation < pop.getBest().evaluate()) 
					pop.setBest(father);
					
			}
				
			
		}
		
		sol = pop.getBest();
		
		return sol;
	}

	//cross breed two solutions
	public void crossbreed(Solution father, Solution mother, Solution child1, Solution child2)
	{
		Solution newChild1 = new Solution(father.slpb);
		Solution newChild2 = new Solution(mother.slpb);
		int i;
		
		//production of father in newChild1
		for (i = 0; i < father.getNumberOfProducedBatches(); ++i) {
			newChild1.addProductionLast(father.getProductionBatch(i));
		}
		
		//delivery of father in newChild2
		for (i = 0; i < mother.getNumberOfDeliveredBatches(); ++i) {
			newChild2.addDeliveryLast(mother.getDeliveryBatch(i));
		}
		
		//production of mother in newChild2
		for (i = 0; i < mother.getNumberOfProducedBatches(); ++i) {
			newChild2.addProductionLast(mother.getProductionBatch(i));
		}
		
		//delivery of mother in newChild1
		for (i = 0; i < mother.getNumberOfDeliveredBatches(); ++i) {
			newChild1.addDeliveryLast(mother.getDeliveryBatch(i));
		}
		
		
		//check if newChild1 and newChild2 are not in population
		if ( ! pop.isPresent(newChild1)) {
			if (newChild1.evaluate() < pop.getBest().evaluation) {
				child1 = newChild1;
				pop.setBest(child1);
			}
		}
		
		if ( ! pop.isPresent(newChild2)) {
			if (newChild2.evaluate() < pop.getBest().evaluation) {
				child2 = newChild2;
				pop.setBest(child2);
			}
		}
	}
}
