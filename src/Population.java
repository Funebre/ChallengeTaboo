import java.util.LinkedList;
import java.util.ListIterator;


public class Population {

	private LinkedList<Solution> individuals;
	private int pop_size;
	
	public Population(int size, Problem pb) {
		this.pop_size = size;
		this.individuals = new LinkedList<Solution>();
		int i = 0;
		while(i < size) {
			Solution sol = new Solution(pb);
			sol.randomize();
			
			if(!alreadyPresent(sol)) {
				individuals.add(sol);
				i++;
			}
			
			sol.evaluate();
		}
		
		System.out.println("Organizing...");
		organize();
	}
	
	public boolean alreadyPresent(Solution sol) {
		
		boolean res = false;
		ListIterator<Solution> it = individuals.listIterator();
		
		while(it.hasNext() && !res) {
			if(it == sol) {
				res = true;
			}	

			it.next();
		}
		
		return res;
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
	
	public int _organize_partition (int debut, int fin) {
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
	
	
	public Solution get_best() {
		System.out.println(individuals.getFirst().productionSequenceMT + "|" + individuals.getFirst().deliverySequenceMT);
		return individuals.getFirst();
	}
	
}
