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
import java.util.Arrays;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

public class Problem {

	/**
	 * Number of products for the customer
	 */
	protected int np ;

	/**
	 * Due dates of the parts
	 */
	protected double[] dueDates ;

	/**
	 * customer holding cost per unit of time and per part
	 */
	protected double customerHoldingCost;

	/**
	 * Production and Holding costs for the supplier
	 */
	protected double[] batchProductionHoldingCosts;

	/**
	 * List of the transporters for the supplylink
	 */
	protected Transporter transporter ;

	/** 
	 * Production time (including setup time for a given batch size)
	 * for a given class of product
	 */
	protected double productionTime ;
	protected double setupTime ;

	/**
	 * supplier holding cost per unit of time and per part
	 */
	protected double supplierUnitHoldingCost ;
	protected double setupCost ;

	/**
	 * 
	 */
	public Problem () {
	}


	public Problem(String string) {
		open(string) ;
	}


	// Getters
	public int getNp() {
		return np;
	}
	
	public double getDueDate(int i) { 
		return dueDates[i] ; 
	}
	
	public Transporter getTransporter() {
		return transporter;
	}
	
	public double getSetupTime() {
		return setupTime;
	}
	
	public double getProductionTime() {
		return productionTime;
	}

	public double getSupplierUnitHoldingCost() {
		return supplierUnitHoldingCost ;
	}


	/**
	 * Get the customer Holding cost for job i with completion time=t
	 * @param i : job indice 
	 * @param t : completionTime
	 * @return
	 */
	public double getCustomerHoldingCost(int i, double t) {
		return 	(dueDates[i]-t)*customerHoldingCost ;
	}
	
	/**
	 * Get the production and holding total cost for the batch of size s
	 * @param s : size of the batch
	 * @return
	 */
	public double getBatchProductionHoldingCost(int s) {
		return batchProductionHoldingCosts[s] ;
	}




	/**
	 * Ouverture d'un fichier de type supplychain problem
	 * 
	 * @see GProblem#open(java.lang.String)
	 */
	public int open(String name) {
		System.out.println (">>> GSupplyChainProblem::open("+name+")\n") ;
		if (name==null) return 1 ;

		int encore = 1 ;

		FichierLecture fe = new FichierLecture (name) ;

		transporter =  new Transporter() ;

		while (encore == 1) {
			String s = fe.lireLigne() ;

			StringTokenizer st = new StringTokenizer(s, new String(":")) ;
			String sg = st.nextToken().trim() ;

			if (sg.compareToIgnoreCase("NBR_PRODUCT")==0) np = Integer.parseInt(st.nextToken().trim()) ;

			if ((sg.compareToIgnoreCase("CUSTOMER_HOLDING_COST")==0) ||
					(sg.compareToIgnoreCase("CUSTOMER_HOLDING_COSTS")==0) ) {
				StringTokenizer st2 = new StringTokenizer(st.nextToken().trim(), new String(";")) ;
				customerHoldingCost = (new Double(st2.nextToken())).doubleValue() ;

			}

			if ((sg.compareToIgnoreCase("TRANSPORTER_CAPACITY")==0) ) { 
				transporter.setCapacity(Integer.parseInt(st.nextToken().trim()) )  ;
			}

			if (((sg.compareToIgnoreCase("TRANSPORTER_DELIVERY_TIMES")==0) || 
					(sg.compareToIgnoreCase("TRANSPORTER_DELIVERY_TIME")==0)) ) {
				transporter.setBatchDeliveryTimes( (new Double(st.nextToken())).doubleValue() ) ;
			}


			if (((sg.compareToIgnoreCase("TRANSPORTER_DELIVERY_COSTS")==0) ||
					(sg.compareToIgnoreCase("TRANSPORTER_DELIVERY_COST")==0)) ) {
				transporter.setBatchDeliveryCosts( (new Double(st.nextToken())).doubleValue() ) ;
			}

			if (((sg.compareToIgnoreCase("SUPPLIER_PRODUCTION_TIMES")==0) ||
					(sg.compareToIgnoreCase("SUPPLIER_PRODUCTION_TIME")==0)) ) {
				StringTokenizer st2 = new StringTokenizer(st.nextToken().trim(), new String(";")) ;
				productionTime = (new Double(st2.nextToken())).doubleValue() ;
			}

			if (((sg.compareToIgnoreCase("SUPPLIER_SETUP_TIMES")==0) ||
					(sg.compareToIgnoreCase("SUPPLIER_SETUP_TIME")==0)) ) {
				StringTokenizer st2 = new StringTokenizer(st.nextToken().trim(), new String(";")) ;
				setupTime = (new Double(st2.nextToken())).doubleValue() ;
			}

			if (((sg.compareToIgnoreCase("SUPPLIER_SETUP_COSTS")==0) ||
					(sg.compareToIgnoreCase("SUPPLIER_SETUP_COST")==0)) ) {
				StringTokenizer st2 = new StringTokenizer(st.nextToken().trim(), new String(";")) ;
				setupCost = (new Double(st2.nextToken())).doubleValue() ;
			}

			if (((sg.compareToIgnoreCase("SUPPLIER_HOLDING_COSTS")==0) ||
					(sg.compareToIgnoreCase("SUPPLIER_HOLDING_COST")==0)) ) {
				StringTokenizer st2 = new StringTokenizer(st.nextToken().trim(), new String(";")) ;
				supplierUnitHoldingCost =  (new Double(st2.nextToken())).doubleValue()  ;
			}

			if (( (sg.compareToIgnoreCase("SUPPLIER_PRODUCTION_HOLDING_COSTS")==0) ||
					(sg.compareToIgnoreCase("SUPPLIER_PRODUCTION_HOLDING_COST")==0)) ) {
				StringTokenizer st2 = new StringTokenizer(st.nextToken().trim(), new String(";")) ;
				batchProductionHoldingCosts = new double[np+1] ;
				for (int i=0;i<=np;i++) {
					Double doub = new Double(st2.nextToken()) ;
					batchProductionHoldingCosts[i] = doub.doubleValue() ;
				}
			}

			if ((sg.compareToIgnoreCase("DUE_DATES")==0) ) {
				StringTokenizer st2 = new StringTokenizer(st.nextToken().trim(), new String(";")) ;
				dueDates = new double[np] ;
				for (int i=0;i<np;i++)
					dueDates[i] = (new Double(st2.nextToken())).doubleValue() ;
				encore = 0 ;
			}	
		}

		
		fe.fermer() ;

		return 0 ;

	}


	@Override
	public String toString() {
		return "Problem [np=" + np + ", dueDates=" + Arrays.toString(dueDates)
				+ ", customerHoldingCost=" + customerHoldingCost
				+ ", batchProductionHoldingCosts="
				+ Arrays.toString(batchProductionHoldingCosts)
				+ ", transporter=" + transporter + ", productionTime="
				+ productionTime + ", setupTime=" + setupTime
				+ ", supplierUnitHoldingCost=" + supplierUnitHoldingCost
				+ ", setupCost=" + setupCost + "]";
	}
	
	


}
