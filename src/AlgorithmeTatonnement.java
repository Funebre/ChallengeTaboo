import java.util.Random;
import java.util.Vector;


public class AlgorithmeTatonnement {
	
	private Problem pb;
	private Solution startPoint;
	private Solution bestNeighbour;
	
	public AlgorithmeTatonnement(Problem prob, Solution init) {
		pb = prob;
		startPoint =  init;
		bestNeighbour = init;
	}
	
	public void getBestNeighbour(Solution sol) {
		Solution temp = new Solution(pb);
		Random r = new Random();
		bestNeighbour.evaluate();
		
		//Verifier tous les voisins :
		
		//Cote production
		getBestProductionNeighbour(sol, temp, r);
		
		
		//for(int i = 0; i < sol.deliverySequenceMT.size(); i++) {
			//Refaire pareil pour delivery
		//}
	}
	
	public void getBestProductionNeighbour(Solution sol, Solution temp, Random r) {
		int size = sol.productionSequenceMT.size();
		System.out.println("Initial solution : " + sol.productionSequenceMT + " | " + sol.deliverySequenceMT + " => " + sol.evaluate());		
		
		for(int i = 0; i < size; ++i) {
			//Copy solution into temp (ultra-sauvage)
			temp.productionSequenceMT = (Vector<Batch>)sol.productionSequenceMT.clone();
			temp.deliverySequenceMT = (Vector<Batch>)sol.deliverySequenceMT.clone();
			
			//Subtract 1 from the i-th batch
			int k = sol.getProductionBatchSize(i);
			System.out.println("Quantity in batch to decrement : " + k);
			temp.setProductionBatchSize(i, k-1);
			
			//Add it to another batch
			k = r.nextInt(size);
			while(k == i)
				k = r.nextInt(size);
			
			System.out.println("Rank to increment a batch to compensate : " + k);
			int qte = sol.getProductionBatchSize(k);
			temp.setProductionBatchSize(k, qte + 1);			
			
			//Afficher
			System.out.println("Resulting solution : " + temp.productionSequenceMT + " | " + temp.deliverySequenceMT + " => " + temp.evaluate());
			System.out.println();
			//Test the resulting solution
			if (temp.evaluate() < bestNeighbour.evaluate()) {
				bestNeighbour.productionSequenceMT = (Vector<Batch>)temp.productionSequenceMT.clone();
				bestNeighbour.deliverySequenceMT = (Vector<Batch>)temp.deliverySequenceMT.clone();
			
			}
		}
	}
}
