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
		
		Problem pb = new Problem("data/problem-011-100.txt");
		
		Solution sol = new Solution(pb);

		AlgorithmeEvolutionnaire algo = new AlgorithmeEvolutionnaire(1, pb);
		
		Solution best = algo.run();
		
		best.evaluate();
		
		int unchanged = 0;
		while (unchanged < 10) {
			algo = new AlgorithmeEvolutionnaire(15000, 300, (float)0.5, (float)0.5, pb);
			sol = algo.run();
			
			if (sol.evaluation < best.evaluation)
				best = sol;
			else
				unchanged++;
		}
		
		System.out.println("Best solution after evolutionary algorithm : " + best.evaluation);
		System.out.println(best.productionSequenceMT + "|" + best.deliverySequenceMT);
		
		//Utilize a hill-climbing algorithm on the best solution to refine it (execution time between AE and AT is insignificant)
		AlgorithmeTatonnement hc = new AlgorithmeTatonnement(pb, best);
		Solution optimum = new Solution(pb);
		optimum.copy(best);
		
		System.out.println("Starting hillclimb with : " + optimum.evaluation);
		System.out.println(optimum.productionSequenceMT + "|" + optimum.deliverySequenceMT);
		
		boolean foundbetter = true;
		int i = 0;
		int limit = 50;
		
		while (foundbetter && i < limit) {
			best.copy(optimum);
			System.out.println("Optimizing");
			optimum.copy(hc.getBestNeighbour(best));
			optimum.evaluate();
			
			if (optimum.evaluation < best.evaluation) {
				System.out.println("Found better ! " + optimum.evaluation);
			}
			else {
				foundbetter = false;
			}
			
			i++;
		}
		
		System.out.println("Meilleure solution : " + optimum.evaluation);
		System.out.println(optimum.productionSequenceMT + "|" + optimum.deliverySequenceMT);
		
		long endTime = System.currentTimeMillis();
		System.out.println((endTime - beginTime)/1000 + " seconds, stopped after " + unchanged + " stale iterations.");
   }
}
