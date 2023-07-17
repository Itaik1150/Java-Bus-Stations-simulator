import java.util.Vector;
public class BoundedQueue<T> {
	private Vector<T> buffer;
	private int maxSize;


	public BoundedQueue(int maxsize) {
		this.buffer = new Vector<T>();
		this.maxSize = maxsize;
	}

	public synchronized void insert(T item) {//Add to bounded queue
		while (buffer.size() == maxSize) {
			try {
				this.wait();
			} catch (InterruptedException e) {}
		}
			this.buffer.add(item);
			this.notifyAll();
	}
	
	public synchronized T extract() {//Remove from bounded queue
		while (buffer.isEmpty()) {
			try {
				this.wait();
			} catch (InterruptedException e) {}
		}
		T item = buffer.remove(0);
		this.notifyAll();
		return item;
	}
}