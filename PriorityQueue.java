public class PriorityQueue <T extends Prioritable > extends Queue<T>{//A queue for objects with Prioritize

	public PriorityQueue (){ //Constructor
		super ();//Inherit queue constructor
	}

	public synchronized T extract() {//Remove from priority queue
		while (buffer.isEmpty()) {
			try {
				this.wait();
			} catch (InterruptedException e) {}
		}
		T item;
		for (int i = 0; i<this.buffer.size(); i++) {//Remove the object with priority first 
			if(buffer.get(i)!=null && buffer.get(i).getPriority()) {
				item = buffer.remove(i);
				return item;
			}
		}
		item = buffer.remove(0);
		return item;
	}
}

