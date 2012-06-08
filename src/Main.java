import java.util.Random;

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
public class Main {
	
	// ----------------------------------------
	public static void main(String[] args) {
		Problem pb = new Problem("data/problem-001-200.txt") ;
		//Solution meilleure : 113429
		System.out.println("problem="+pb.toString()+"\n") ;
		
		Solution sol = new Solution(pb) ;
		Solution sol2 = new Solution(pb);
		sol2.randomize();
		
		Random r = new Random();
		
		int rand = r.nextInt(1000);
		/*sol.setFromString("1 3 6 8 2/3 7 3 3 4");
		sol.swapRandomBatches(sol.productionSequenceMT);
		sol.swapRandomBatches(sol.deliverySequenceMT);
		
		System.out.println(sol.productionSequenceMT + "|" + sol.deliverySequenceMT);
		
		Population pop = new Population(600, pb);
		sol.reverseRandomBatchSequence(sol.productionSequenceMT);*/
		
		//Population pop = new Population(10000, pb);
		
		Algorithme algo = new Algorithme(10000, 200, (float)0.5, (float)0.5, pb);
		
		System.out.println(sol.toString());
		
		
		/*pop.insertSolution(sol2, rand);
		pop.findPosition(sol2, rand);*/
   }
}
