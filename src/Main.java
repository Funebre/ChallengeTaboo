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
public class Main
{
	public static void main(String[] args)
	{
		String problem_file = "data/problem-001-100.txt";
		
		//Stop conditions.
		//Set exectime to 0.0 to use the number of loop with unchanged best solution as stop condition.
		double exectime = 15.0; //seconds
		int unchanged_max = 25;
		
		//Evolutionary algorithm.
		int generations_nbr = 1000;
		int population_size = 20;
		double mutation_rate = 0.3;
		double crossbreed_rate = 0.8;
		//End of user configurable settings.
		
		
		if (args.length == 5)
		{
			problem_file = args[0];
			exectime = Double.parseDouble(args[1]);
			unchanged_max = Integer.parseInt(args[2]);
			generations_nbr = Integer.parseInt(args[3]);
			population_size = Integer.parseInt(args[4]);
		}
		
		
		long beginTime = System.currentTimeMillis(); 
		int unchanged = 0;
		
		Problem pb = new Problem(problem_file);
		
		Solution sol = new Solution(pb);

		AlgorithmeEvolutionnaire algo = new AlgorithmeEvolutionnaire(1, pb);
		Solution best = algo.run();
		
		best.evaluate();
		System.out.println("Start solution : " + best.getEvaluation() + " " + best.productionSequenceMT + "|" + best.deliverySequenceMT);
		
		//Time is teh stop condition
		if (exectime > 0) {
			double starttime = System.currentTimeMillis();
			while ((System.currentTimeMillis() - starttime) < exectime * 1000) {
				algo = new AlgorithmeEvolutionnaire(generations_nbr, population_size, crossbreed_rate, mutation_rate, pb);
				sol = algo.run();
				
				if (sol.evaluation < best.evaluation)
					best = sol;
			}
		}
		else {

			while (unchanged < unchanged_max) {
				algo = new AlgorithmeEvolutionnaire(generations_nbr, population_size, crossbreed_rate, mutation_rate, pb);
				sol = algo.run();
				
				if (sol.evaluation < best.evaluation)
					best = sol;
				else
					unchanged++;
			}
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
		
		if (exectime > 0)
			System.out.println((endTime - beginTime)/1000 + " seconds.");
		else
			System.out.println((endTime - beginTime)/1000 + " seconds, stopped after " + unchanged + " stale iterations.");
   }
}
