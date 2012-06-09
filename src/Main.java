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
		
		long beginTime = System.currentTimeMillis(); 
		
		Problem pb = new Problem("data/problem-003-200.txt");
		
		Solution sol = new Solution(pb);
		
		Solution best = sol;
		best.randomize();
		best.evaluate();
		
		Algorithme algo = new Algorithme(1, pb);
		
		int unchanged = 0;
		/*int i = 0;
		for (i=0; i<10; i++) {*/
		while(unchanged < 50) {
			algo = new Algorithme(10000, 150, (float)0.7, (float)0.4, pb);
			sol = algo.run();
			
			if (sol.evaluation < best.evaluation)
				best = sol;
			else
				unchanged++;
		}
		
		System.out.println("Meilleure solution : " + best.evaluation);
		System.out.println(best.productionSequenceMT + "|" + best.deliverySequenceMT);
		
		long endTime = System.currentTimeMillis();
		System.out.println((endTime - beginTime)/1000 + " seconds, stopped after " + unchanged + " stale iterations.");
   }
}
