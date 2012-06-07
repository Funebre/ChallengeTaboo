import java.util.LinkedList;
import java.util.ListIterator;


public class Population {

	private LinkedList<Solution> individuals;
	private int pop_size;
	
	public Population(int size) {
		this.pop_size = size;
		this.individuals = new LinkedList();
	}
	
	public boolean alreadyPresent(Solution sol) {
		
		boolean res = false;
		ListIterator<Solution> it = individuals.listIterator();
		
		while(it.hasNext() && !res) {
			if(it == sol) {
				res = true;
			}	
		}
		
		return res;
	}
	
}
