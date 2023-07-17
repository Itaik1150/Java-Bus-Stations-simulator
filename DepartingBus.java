
public class DepartingBus extends Bus{
	private String destination;
	
	//Constructor
	public DepartingBus(String tripNumber, int passengersNumber ,int arrivalTime, Queue <Bus> busQueue, String destination){ 
		super(tripNumber, passengersNumber, arrivalTime, busQueue);//Inherit Bus constructor
		this.destination = destination;
	}
	
	public boolean getPriority() {//Exiting bus get priority over entering
		return true;
	}
	
	public String getDestination() {//Return bus's destination
		return this.destination;
	}
}
