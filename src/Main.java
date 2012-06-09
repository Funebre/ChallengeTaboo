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
		
		Problem pb = new Problem("data/problem-001-200.txt");
		//Solution acceptable < 100 000
		//System.out.println("problem="+pb.toString()+"\n");
		
		Solution sol = new Solution(pb);
		//Solution sol2 = new Solution(pb);
		
		Solution best = sol;
		best.randomize();
		best.evaluate();
		
		Algorithme algo = new Algorithme(1, pb);
		
		int unchanged = 0;
		while(unchanged < 100) {
			algo = new Algorithme(2000, 1000, (float)0.5, (float)0.3, pb);
			sol = algo.run();
			
			if(sol.evaluation < best.evaluation) {
				best = sol;
				unchanged = 0;
			}
			else 
				unchanged++;
		}
	
		System.out.println("Meilleure solution : " + best.toString());
		
		long endTime = System.currentTimeMillis();
		System.out.println((endTime - beginTime) + " millisecondes");
   }
}
