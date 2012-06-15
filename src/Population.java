import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;


public class Population {

	private LinkedList<Solution> individuals;
	private int popSize;
	private Solution best;
	private Problem pb;
	
	public Population(int size, Problem prob) {
		popSize = size;
		pb = prob;
		best = new Solution(pb);
		best.randomize();
		best.evaluate();
		//System.out.println(best.toString());
		this.individuals = new LinkedList<Solution>();
		
		int i = 0;
		while (i < size) {
			Solution sol = new Solution(pb);
			sol.randomize2();
			Solution sol2 = new Solution(pb);
			sol2.randomize();
			
			if ( ! isPresent(sol)) {
				individuals.add(sol);
				if (sol.evaluate() < best.evaluation)
					best = sol;
				++i;
			}
			
			if ( ! isPresent(sol2)) {
				individuals.add(sol2);
				if (sol2.evaluate() < best.evaluation)
					best = sol2;
				++i;
			}
		}
	}
	
	public boolean isPresent(Solution sol) {
		boolean res = false;
		ListIterator<Solution> it = individuals.listIterator();
		
		while (it.hasNext() && !res) {
			if (it == sol) {
				res = true;
			}	

			it.next();
		}
		
		return res;
	}
	
	public Solution getBest() {
		return best;
	}
	
	//Mettre un ÈlÈment ‡ sa place
	public void findPosition(Solution sol,  int rank) {
		//int rank = individuals.indexOf(sol);
		if(rank < popSize-2 && rank > 0) {
			Solution prev = individuals.get(rank - 1);
			Solution next = individuals.get(rank + 1);
			//System.out.println(prev.toString());
			
			boolean found = false;
			
			if(rank != -1 && !found) {
				if (prev.evaluation > sol.evaluation) {
					Solution temp = sol;
					sol = prev;
					prev = temp;
					System.out.println(rank);
					findPosition(sol, rank - 1);
					//System.out.println("Need to go left");
				} else if (next.evaluation < sol.evaluation) {
					Solution temp = sol;
					sol = next;
					next = temp;
					System.out.println(rank);
					findPosition(sol, rank + 1);
					//System.out.println("Need to go right");
				}
				else
					found = true;
			}
		}
	}
	
	//InsÈrer un ÈlÈment ‡ un rang r
	public void insertSolution(Solution sol, int r) {
		individuals.add(r, sol);
	}
	
	public Solution rouletteSelection() {
		Solution sol = new Solution(pb);
		Random r = new Random();
		double totalFitness = individuals.getFirst().evaluation;
		double minFitness = totalFitness;
		double portionSum;
		
		for(int i = 0; i<popSize; i++) {
			totalFitness += individuals.get(i).evaluation;
			if (minFitness < individuals.get(i).evaluation)
				minFitness = individuals.get(i).evaluation;
		}
		
		portionSum = minFitness*popSize - totalFitness;
		
		double rand = r.nextDouble();

		int ind = 0;
		double portion = (minFitness - individuals.getFirst().evaluation)*1./portionSum;
		while ((ind<popSize-1) && (rand>=portion))
		{
			ind++;
			portion += (minFitness - individuals.get(ind).evaluation)*1./portionSum;
		}
		return individuals.get(ind);
	}
		
	public Solution getFirst() {
		System.out.println(individuals.getFirst().productionSequenceMT + "|" + individuals.getFirst().deliverySequenceMT);
		return individuals.getFirst();
	}
	
	public LinkedList<Solution> getIndividuals() {
		return individuals;
	}

	public void setBest(Solution newbest) {
		best = newbest;		
	}
}
