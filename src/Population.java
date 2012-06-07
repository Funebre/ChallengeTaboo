import java.util.LinkedList;
import java.util.ListIterator;


public class Population {

	private LinkedList<Solution> individuals;
	private int pop_size;
	private Problem pb;
	
	public Population(int size, Problem pb) {
		this.pop_size = size;
		this.individuals = new LinkedList<Solution>();
		int i = 0;
		while(i < size) {
			Solution sol = new Solution(pb);
			sol.randomize();
			sol.evaluate();
			if(!alreadyPresent(sol)) {
				individuals.add(sol);
				i++;
			}
		}
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
	
	public void organize() {
		
		//System.out.print(pop_size);
		Solution inter;

		for(int i=0; i<pop_size-1; i++)
			for(int j=i+1; j<pop_size; j++)
				if(individuals.get(i).evaluation > individuals.get(j).evaluation)
				{
					inter    = individuals.get(i);
					individuals.set(i, individuals.get(j));
					individuals.set(j, inter);
				}
	}
	
	public Solution get_best() {
		System.out.println("Organizing...");
		System.out.println(individuals.getFirst().productionSequenceMT + "|" + individuals.getFirst().deliverySequenceMT);
		return individuals.getFirst();
	}
	
}
