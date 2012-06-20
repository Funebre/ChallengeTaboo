import java.util.Random;
import java.util.Vector;


public class AlgorithmeTatonnement {
	
	private Problem pb;
	private Solution bestNeighbour;
	
	public AlgorithmeTatonnement(Problem prob, Solution init) {
		pb = prob;
		bestNeighbour = new Solution(init);
	}
	
	public Solution getBestNeighbour(Solution sol) {
		Solution temp = new Solution(pb);
		Random r = new Random();
		bestNeighbour.evaluate();
		
		//Verifier tous les voisins :
		
		//Cote production
		getBestProductionNeighbour(sol, temp, r);
		//Cote delivery
		getBestDeliveryNeighbour(sol, temp, r);
		
		return bestNeighbour;
	}
	
	public void getBestProductionNeighbour(Solution sol, Solution temp, Random r) {
		int size = sol.getNumberOfProducedBatches();
		
		temp.deliverySequenceMT = (Vector<Batch>)sol.deliverySequenceMT.clone();
		
		for (int i = 0; i < size; ++i) {
			temp.productionSequenceMT = (Vector<Batch>)sol.productionSequenceMT.clone();

			//Subtract 1 from the i-th batch
			int k = sol.getProductionBatchSize(i);
			if (size == 1) {
				temp.setProductionBatchSize(i, k - 1);
				temp.addProductionLast(0);
				k = 1;
			}
			else {
				if (k == 1) {
					temp.delProduction(i);
					k = r.nextInt(size - 1);
				}
				else {
					temp.setProductionBatchSize(i, k - 1);
					while ((k = r.nextInt(size)) == i);
				}
			}
			
			//Add it to another batch
			int qte = temp.getProductionBatchSize(k);
			temp.setProductionBatchSize(k, qte + 1);
			
			//Test the resulting solution
			if (temp.evaluate() < bestNeighbour.evaluation) {
				bestNeighbour.productionSequenceMT = (Vector<Batch>)temp.productionSequenceMT.clone();
				bestNeighbour.deliverySequenceMT = (Vector<Batch>)temp.deliverySequenceMT.clone();
				bestNeighbour.evaluate();
			}
		}
	}
	
	public void getBestDeliveryNeighbour(Solution sol, Solution temp, Random r) {
		int size = sol.getNumberOfDeliveredBatches();
		
		temp.productionSequenceMT = (Vector<Batch>)sol.productionSequenceMT.clone();
		
		for (int i = 0; i < size; ++i) {
			temp.deliverySequenceMT = (Vector<Batch>)sol.deliverySequenceMT.clone();
			
			//Subtract 1 from the i-th batch
			int k = sol.getDeliveryBatchSize(i);
			if (size == 1) {
				temp.setDeliveryBatchSize(i, k - 1);
				temp.addDeliveryLast(0);
				k = 1;
			}
			else {
				if (k == 1) {
					temp.delDelivery(i);
					k = r.nextInt(size - 1);
				}
				else {
					temp.setDeliveryBatchSize(i, k - 1);
					while ((k = r.nextInt(size)) == i);
				}
			}
			
			//Add it to another batch
			while (temp.getDeliveryBatchSize(k) == temp.slpb.getTransporter().getCapacity()) {
				k = r.nextInt(size);
			}
			int qte = temp.getDeliveryBatchSize(k);
			temp.setDeliveryBatchSize(k, qte + 1);
			
			//Test the resulting solution
			if (temp.evaluate() < bestNeighbour.getEvaluation()) {
				//If better, copy temp to bestNeighbour
				bestNeighbour.productionSequenceMT = (Vector<Batch>)temp.productionSequenceMT.clone();
				bestNeighbour.deliverySequenceMT = (Vector<Batch>)temp.deliverySequenceMT.clone();
				bestNeighbour.evaluate();
			}
		}
	}
}
