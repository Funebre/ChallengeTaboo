import java.util.LinkedList;
import java.util.ListIterator;


public class Population {

	private LinkedList<Solution> individuals;
	private int pop_size;
	private Solution best;
	
	public Population(int size, Problem pb) {
		this.pop_size = size;
		best = new Solution(pb);
		best.randomize();
		best.evaluate();
		//System.out.println(best.toString());
		this.individuals = new LinkedList<Solution>();
		int i = 0;
		while (i < size) {
			Solution sol = new Solution(pb);
			sol.randomize();
			
			if (!isPresent(sol)) {
				individuals.add(sol);
				i++;
			}
			
			sol.evaluate();
			if(sol.evaluation < best.evaluation) {
				best = sol;
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
	
	//Mettre un élément à sa place
	public void findPosition(Solution sol,  int rank) {
		//int rank = individuals.indexOf(sol);
		if(rank < pop_size-2 && rank > 0) {
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
	
	//Insérer un élément à un rang r
	public void insertSolution(Solution sol, int r) {
		individuals.add(r, sol);
	}
	
	//Trier la population par ordre croissant de fitness
	public void organize() {
		_organize_quicksort(0, pop_size - 1);
	}
	
	public void _organize_quicksort(int debut, int fin) {
		if (debut < fin) {
			int indicePivot = _organize_partition(debut, fin);
			_organize_quicksort(debut, indicePivot-1);
			_organize_quicksort(indicePivot+1, fin);
		}
	}
	
	public int _organize_partition(int debut, int fin) {
		Solution valeurPivot = individuals.get(debut);
		int d = debut+1;
		int f = fin;
		Solution inter;
		
		while (d < f) {
			while (d < f && individuals.get(f).evaluation >= valeurPivot.evaluation) f--;
			while (d < f && individuals.get(d).evaluation <= valeurPivot.evaluation) d++;
			
			inter    = individuals.get(d);
			individuals.set(d, individuals.get(f));
			individuals.set(f, inter);
			
		}
		
		if (individuals.get(d).evaluation > valeurPivot.evaluation)
			d--;
		
		individuals.set(debut, individuals.get(d));
		individuals.set(d, valeurPivot);
		
		return d;
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
