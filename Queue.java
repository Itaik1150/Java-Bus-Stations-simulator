import java.util.Vector;

public class Queue <T> {
	protected Vector <T> buffer;

	public Queue() {
		buffer = new Vector <T>();
	}

	public synchronized void insert ( T item) {//Add to unbounded queue
		buffer.add(item);
		notifyAll();
	}

	public synchronized T extract() {//Remove from unbounded queue
		while (buffer.isEmpty()) {
			try {
				this.wait();
			} catch (InterruptedException e) {}
		}
		T item = buffer.remove(0);
		return item;
	}
	
	public boolean isEmpty() {//Check if the queue is empty
		return this.buffer.isEmpty();
	}
	
	public int size() {//Return the size of the queue
		return this.buffer.size();
	}
	
	public T get(int index) {//Get an object in wanted index
		return this.buffer.get(index);
	}
}




