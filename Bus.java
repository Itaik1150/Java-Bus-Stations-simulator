
abstract public class Bus implements Prioritable,Runnable{
	private String busCode;
	private int passengersNumber;	
	private int arrivalTime;
	private Queue <Bus> busQueue;
	
	public Bus (String busCode, int passengersNumber, int arrivalTime, Queue <Bus> busQueue) {//Constructor
		this.busCode = busCode;
		this.passengersNumber = passengersNumber;
		this.arrivalTime = arrivalTime;
		this.busQueue = busQueue;
	}
	
	public void run() {
		try {
			Thread.sleep(arrivalTime * 1000 );//arrival time to gate
		} catch (InterruptedException e) {}
		busQueue.insert(this);
	}

	abstract public boolean getPriority();//Prioritization method 
	
	public String getBusCode() {//Return the bus's code
		return this.busCode;
	}
	
	public int getPassengersNumber() {//Return passengers number in bus
		return this.passengersNumber;
	}
	
	public int getArrivalTime() {//Return arrival time to gate of bus
		return this.arrivalTime;
	}
}


