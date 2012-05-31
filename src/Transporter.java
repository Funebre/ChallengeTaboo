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
 * 
 *
 */

import java.util.Arrays;
import java.util.Random;


public class Transporter {

	/**
	 * Chargement max du transporteur
	 */
	protected int capacity ;
	
	/**
	 * Time for delivering one batch : 
	 * it is the time to deliver a batch from the producer to the customer, or 
	 * the time to go back from the customer to the producer. Thus, the time to go from producer
	 * to customer and go back to producer = 2*batchDeliveryTimes
	 */
	protected double batchDeliveryTimes;
	
	/**
	 * Cost for delivering one batch 
	 */
	private double batchDeliveryCosts;
	
	/**
	 * 
	 */
	public Transporter() {
		capacity = 0 ;
		batchDeliveryTimes = 0 ;
		batchDeliveryCosts = 0 ;
	}

	public Transporter(Transporter t) {
		setCapacity(t.capacity) ;
		batchDeliveryCosts = t.batchDeliveryCosts ;
		batchDeliveryTimes = t.batchDeliveryTimes ;
	}
	
	/**
	 * @param i
	 */
	public int getCapacity() {return capacity ;}
	
	/**
	 * Assign capacity of the transporter to "i"
	 * and create in memory the two arrays : deliveryTimes and deliveryCosts
	 * 
	 * @param i = new capacity of the transporter
	 */
	public void setCapacity(int i) { 
		capacity = i ; 
	}
	
	public double getBatchDeliveryTimes() { return batchDeliveryTimes ;	}
	public double getBatchDeliveryCost() {  return batchDeliveryCosts ; }
	
	public void setBatchDeliveryCosts(double transportationcost) { batchDeliveryCosts = transportationcost ;	}
	public void setBatchDeliveryTimes(double d) { batchDeliveryTimes = d ; }

	@Override
	public String toString() {
		return "Transporter [capacity=" + capacity + ", batchDeliveryTimes="
				+ batchDeliveryTimes + ", batchDeliveryCosts="
				+ batchDeliveryCosts + "]";
	}

}
