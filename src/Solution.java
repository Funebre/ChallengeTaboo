/**
 * 
 *     This file is part of ag41-print12-challenge.
 *     
 *     ag41-print12-challenge is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *     
 *     ag41-print12-challenge is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *     
 *     You should have received a copy of the GNU General Public License
 *     along with ag41-print12-challenge.  If not, see <http://www.gnu.org/licenses/>.
 *     
 */

/**
 * 
 * @author Olivier Grunder
 * @version 0.02
 * @date 23 mai 2012
 *
 */

import java.util.* ;

public class Solution {

	protected Problem slpb ;
	protected double evaluation ;

	/*
	 * Array of the production dates, indice is job
	 */
	protected double productionStartingDates[] ;
	public double productionCompletionTimes[] ;

	public double transportationCompletionTimes[] ;

	protected double evaluationManufacturer;
	protected double evaluationTransporter;
	protected double evaluationCustomer;

	/*
	 * Array of the first transporter departure, indice is a transporter
	 */
	protected double dateTransporterDeparture ;
	protected double dateProductionDeparture ;

	public static final double MOINS_INFINI = -1000000.0;

	/**
	 * multi transporter loading sequence :
	 * a solution is composed of different batches, each batch is defined
	 * by the identifier of the transporter and the number of transported parts.
	 */
	protected Vector deliverySequenceMT ;

	/**
	 * production sequence :
	 */
	protected Vector productionSequenceMT ;


	//----------------------------------------
	public Solution(Problem pb_) {
		slpb = pb_ ;
		productionCompletionTimes = new double[slpb.getNp()] ;
		transportationCompletionTimes = new double[slpb.getNp()] ;
		productionStartingDates = new double[slpb.getNp()] ;

		deliverySequenceMT = new Vector() ;
		productionSequenceMT = new Vector() ;
	}


	public void reset() {
		deliverySequenceMT = new Vector() ;
		productionSequenceMT = new Vector() ;
		productionCompletionTimes = new double[slpb.getNp()] ;
		productionStartingDates = new double[slpb.getNp()] ;
		System.gc();
	}
	
	//Construction of a random solution
	public void randomize() {
		int i = slpb.getNp();
		int j;
		
		while (i != 0) {
			Random r = new Random();
			j = r.nextInt(i) + 1;
			i -= j;
			
			addProductionLast(j);
		}
		
		i = slpb.getNp();
		while (i != 0) {
			Random r = new Random();
			j = r.nextInt(i) + 1;
			i -= j;
			
			addDeliveryLast(j);
		}
		
		System.out.println(productionSequenceMT + "|" + deliverySequenceMT + " => " + evaluate());		
	}

	//----------------------------------------
	public double getEvaluation() {
		return evaluation;
	}
	//----------------------------------------
	public void setDeliverySequenceMT(Vector newListChargt1) { deliverySequenceMT = newListChargt1 ; }

	//----------------------------------------
	public int getNumberOfDeliveredBatches(){ return deliverySequenceMT.size(); }
	public int getNumberOfProducedBatches(){ return productionSequenceMT.size(); }

	//----------------------------------------
	public int getNumberOfDeliveredParts() {
		int nc = getNumberOfDeliveredBatches() ;
		int nbrParts = 0 ;
		for (int i=0;i<nc;i++) {
			nbrParts += getDeliveryBatchSize(i) ;
		}
		return nbrParts ;
	}

	public int getNumberOfProducedParts() {
		int nc = getNumberOfProducedBatches() ;
		int nbrParts = 0 ;
		for (int i=0;i<nc;i++) {
			nbrParts += getProductionBatch(i).getQuantity() ;
		}
		return nbrParts ;
	}

	//----------------------------------------
	public Batch getDeliveryBatch(int i){
		return (Batch)deliverySequenceMT.get(i) ;
	}

	//----------------------------------------
	public Batch getProductionBatch(int i){
		return (Batch)productionSequenceMT.get(i) ;
	}

	//----------------------------------------
	private void setDeliveryBatch(int i, Batch b) {
		deliverySequenceMT.set(i,b) ;
	}

	private void insertDeliveryBatch(int i5, Batch batch) {
		deliverySequenceMT.insertElementAt(batch, i5) ;
	}

	//----------------------------------------
	public int getDeliveryBatchSize(int i){
		Batch chargt = getDeliveryBatch(i) ;
		if (chargt!= null) return chargt.getQuantity() ;
		return -1 ;
	}

	//----------------------------------------
	public int getProductionBatchSize(int i){
		Batch chargt = getProductionBatch(i) ;
		if (chargt!= null) return chargt.getQuantity() ;
		return -1 ;
	}

	public void setProductionBatchSize(int indice, int batchsize) {
		productionSequenceMT.set(0, new Batch(batchsize)) ;		
	}	

	/**
	 * @param lot1
	 * @param Delivery
	 */
	public void setDelivery(int lot1, Batch Delivery) {
		deliverySequenceMT.set (lot1, Delivery) ;		
	}

	/**
	 * @param i
	 * @param qteAEclater
	 * @param newtr
	 */
	public void addDelivery(int i, int qte) {
		deliverySequenceMT.insertElementAt(new Batch(qte), i) ;

	}


	//----------------------------------------
	/**
	 * @param j = quantity of delivered parts
	 * @param tr = transporteur
	 */
	public void addDeliveryFirst(int j) { deliverySequenceMT.add(0, new Batch(j)) ; }
	public void addProductionFirst(int j) { productionSequenceMT.add(0, new Batch(j)) ; }
	public void addProductionLast(int j) { productionSequenceMT.add(new Batch(j));}
	public void addDeliveryLast(int i) { deliverySequenceMT.add(new Batch(i)) ; }

	public void setDelivery(int i, int j) { deliverySequenceMT.set(i, new Batch(j)) ; }

	public void delDelivery(int lot1) { deliverySequenceMT.remove(lot1) ;	}

	//----------------------------------------
	public void delFirstDelivery() { deliverySequenceMT.remove(0) ;}
	public void delFirstProduction() { productionSequenceMT.remove(0) ;}
	public void delAllProduction() { productionSequenceMT.removeAllElements() ;}

	
	//----------------------------------------
	/**
	 * Evolutionist algorithm : mutations
	 */
	//swap two batches
	public void swapTwoBatches(int i, int j, Vector batches) {
		Object inter = batches.elementAt(i);
		batches.set(i, batches.elementAt(j));
		batches.set(j, inter);
	}
	
	//swapping two random batches	
	public void swapRandomBatches(Vector batches) {
		Random r = new Random();
		int i = r.nextInt(batches.size());
		int j = r.nextInt(batches.size());

		swapTwoBatches(i, j, batches);
		
		//System.out.println(productionSequenceMT + "|" + deliverySequenceMT);
	}
	
	//invert a sequence of batches
	public void reverseRandomBatchSequence(Vector batches) {
		Random r = new Random();
		int i = r.nextInt(batches.size());
		int j = r.nextInt(batches.size());
		
		while (j<i)
			j = r.nextInt(batches.size());
		
		int k = j-i;
		int iter = 0;

		while (k > 0) {
			swapTwoBatches(i+iter, j-iter, batches);
			iter++;
			k -= 2;
		}
	}
	
	//cross breed two solutions
	
	/**
	 * check the sequence
	 */
	protected boolean check() {
		int i=0 ;
		while (i<getNumberOfDeliveredBatches()) {
			if (getDeliveryBatchSize(i)<=0) {
				System.out.println ("WARNING GSupplyLinkSolution.check : getDelivery("+i+")="+getDeliveryBatch(i)+" <= 0 => removing it !" );
				deliverySequenceMT.remove(i) ;
			}
			else
				i++ ;
		}

		if (getNumberOfDeliveredParts()>slpb.getNp()) {
			System.out.println ("ERROR GSupplyLinkSolution.check : getNbrProduit()="+getNumberOfDeliveredParts()+" > slpb.getNp()="+slpb.getNp()+" => don't know what to do !" );
			return false ;
		}
		
		if (getNumberOfDeliveredParts() < slpb.getNp()) {
			System.out.println ("ERROR : On n'a pas livré assez de produits ! Le client sera mécontent");
			return false;
		}
		
		return true;
	}

	private class Evaluator {
		// Supplier/Transporter/Customer cost
		int job ;
		double completiontime ;
		double[] transportationStartingTimes ;

		public Evaluator() {
			transportationStartingTimes = new double[slpb.getNp()] ;
		}

		public double evaluate() {
			evaluation = 0 ;
			evaluationManufacturer = 0 ;
			evaluationTransporter = 0 ;
			evaluationCustomer = 0 ;

			job=slpb.getNp()-1 ;
			completiontime = slpb.getDueDate(job) ;

			evaluateTransporterCustomerCosts() ;
			evaluateSupplierCosts() ;

			return evaluation ;
		}

		/**
		 *  ================================
		 *  Customer & Transporter cost
		 *  ================================ 
		 */
		private void evaluateTransporterCustomerCosts() {
			int indiceDelivBatch = getNumberOfDeliveredBatches()-1 ;
			while (job>=0 && indiceDelivBatch>=0) {
				int delivbatch = getDeliveryBatchSize(indiceDelivBatch) ;
				Transporter transp = slpb.getTransporter() ;

				// Transporter cost
				evaluationTransporter += transp.getBatchDeliveryCost() ;

				// Computation of the arrival date of the transporter
				for (int i=job;i>=job-delivbatch+1;i--) {
					try  {
						if (slpb.getDueDate(i)<completiontime)
							completiontime = slpb.getDueDate(i) ;
					}
					catch (Exception e) {
						System.out.println ("Exception GSupplyLinkSolution.evaluate:"+e.getMessage()) ;
						System.out.println ("i="+i) ;
						System.out.println ("slpb.getNp()="+slpb.getNp()) ;
						System.out.println ("job="+job) ;
						System.out.println ("np_chargt="+(delivbatch)) ;
						System.out.println ("job-np_chargt+1="+(job-delivbatch+1)) ;
						System.out.println ("solution="+toString());
						System.out.println ("numberOfDeliveredParts="+getNumberOfDeliveredParts()) ;
						check() ;

						System.exit(0) ;
					}
				}

				for (int i=job;i>=job-delivbatch+1;i--) {
					// Customer cost
					transportationCompletionTimes[i] = completiontime ;
					transportationStartingTimes[i] = transportationCompletionTimes[i] - transp.getBatchDeliveryTimes() ;
					evaluationCustomer += slpb.getCustomerHoldingCost(i, completiontime) ;
				}
				job -= delivbatch ;

				// Transportation of the parts
				completiontime -= transp.getBatchDeliveryTimes() ;

				//		    	dateTransportationStart[indiceDelivBatch] = completiontime ;
				dateTransporterDeparture = completiontime ;

				// Empty transporter which come back from the customer to the supplier
				completiontime -= transp.getBatchDeliveryTimes() ;

				indiceDelivBatch-- ;
			}

			evaluation += evaluationTransporter + evaluationCustomer ;
		}

		/**
		 *  ================================
		 *  Supplier Cost
		 *  ================================ 
		 */
		private void evaluateSupplierCosts() {
			int indiceProdBatch = getNumberOfProducedBatches()-1 ;
			int sumProd = 0 ; 

			job = slpb.getNp()-1 ;
			double completionTimeBatch = transportationStartingTimes[job] ;
			dateProductionDeparture = completionTimeBatch ;

			while (job>=0 && indiceProdBatch>=0) {
				int prodbatch = getProductionBatchSize(indiceProdBatch) ;

				// Production cost and WIP holding cost (1st holding cost) for supplier	    	
				evaluationManufacturer += slpb.getBatchProductionHoldingCost(prodbatch);

				double prodCompletionTime = dateProductionDeparture ;
				int jobstart = job ;
				for (int job2=0;job2<prodbatch;job2++) {
					if (transportationStartingTimes[job]<prodCompletionTime)
						prodCompletionTime = transportationStartingTimes[job] ;
					job -- ;
				}

				job = jobstart ;
				for (int job2=0;job2<prodbatch;job2++) {
					productionCompletionTimes[job] = prodCompletionTime ;
					dateProductionDeparture = productionCompletionTimes[job]- slpb.getSetupTime() - slpb.getProductionTime()*prodbatch ;

					// Second holding cost for supplier
					evaluationManufacturer += (transportationStartingTimes[job]-productionCompletionTimes[job])*slpb.getSupplierUnitHoldingCost()  ;

					job -- ;
				} // end for
				indiceProdBatch-- ;
			} // end while


			evaluation += evaluationManufacturer ;

		}

	}

	/**
	 * Evaluation of the diff batch solution
	 */

	public double evaluate()  {
		if (slpb==null) return -1 ;

		// The number of produced parts has to be greater or equal to the 
		// number of delivered parts
		if (getNumberOfDeliveredBatches()<=0) {
			return -1 ;
		}
		
		// Verify that we are answering to the client's demand
		if (check() == false) {
			return 0;
		}

		Evaluator ev = new Evaluator() ;
		return ev.evaluate() ;
	}


	/**
	 * 
	 * @param solstring : "production sequence / delivery sequence", e.g. "24 26/5 3 6 5 5 5 4 4 6 7"
	 */
	public void setFromString(String solstring) {
		StringTokenizer st = new StringTokenizer(solstring, new String("/")) ;
		String prodst = st.nextToken().trim() ;
		String delivst = st.nextToken().trim() ;

		st = new StringTokenizer(prodst, new String(" ")) ;
		while (st.hasMoreTokens()) {
			String nxtst = st.nextToken().trim();
			addProductionLast(Integer.parseInt(nxtst)) ;
		}

		st = new StringTokenizer(delivst, new String(" ")) ;
		while (st.hasMoreTokens()) {
			String nxtst = st.nextToken().trim();
			addDeliveryLast(Integer.parseInt(nxtst)) ;
		}
	}



	@Override
	public String toString() {
		return "Solution [evaluation=" + evaluation
				+ ", productionStartingDates="
				+ Arrays.toString(productionStartingDates)
				+ ", productionCompletionTimes="
				+ Arrays.toString(productionCompletionTimes)
				+ ", transportationCompletionTimes="
				+ Arrays.toString(transportationCompletionTimes)
				+ ", evaluationManufacturer=" + evaluationManufacturer
				+ ", evaluationTransporter=" + evaluationTransporter
				+ ", evaluationCustomer=" + evaluationCustomer
				+ ", dateTransporterDeparture=" + dateTransporterDeparture
				+ ", dateProductionDeparture=" + dateProductionDeparture
				+ ", deliverySequenceMT=" + deliverySequenceMT
				+ ", productionSequenceMT=" + productionSequenceMT + "]";
	}
} 
