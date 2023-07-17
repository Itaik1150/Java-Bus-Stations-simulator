
public class FuelAttendant implements Runnable {
	private String identifier;
	private int MaxFuelCapacity;
	private int currentFuelCapacity;
	private ArrivalBus currentBus;
	private BoundedQueue<ArrivalBus> fuelAttendantQueue;
	private Queue<ArrivalBus> technicalAttendantQueue;
	private Queue<BusDetails> managerQueue;

	// Constructor
	public FuelAttendant(String identifier, int MaxFuelCapacity, BoundedQueue<ArrivalBus> fuelAttendantQueue,
			Queue<ArrivalBus> technicalAttendantQueue, Queue<BusDetails> managerQueue) {
		this.identifier = identifier;
		this.MaxFuelCapacity = MaxFuelCapacity;
		currentFuelCapacity = MaxFuelCapacity;
		this.fuelAttendantQueue = fuelAttendantQueue;
		this.technicalAttendantQueue = technicalAttendantQueue;
		this.managerQueue = managerQueue;
	}

	public void run() {
		while (!Manager.dayOver) {// While the day is not over
			if (currentFuelCapacity < 200) { // There is not enough fuel to refuel the bus
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
				currentFuelCapacity = MaxFuelCapacity; // Refuel the Fuel Attendant tank - takes 5 seconds
			} else {// There is enough fuel
				currentBus = fuelAttendantQueue.extract();
				if (currentBus == null) {// Null is a sign for end day and wake all remaining threads
					this.fuelAttendantQueue.insert(currentBus);
					break;
				}
				try {
					double refuelingTime = Math.random();
					long sleepTime = (long) ((refuelingTime + 3) * 1000);
					Thread.sleep(sleepTime);
					currentBus.setTreatmentTime((refuelingTime + 3));
				} catch (InterruptedException e) {
				}
				currentFuelCapacity -= 200;
				double chancesOfMalfunction = Math.random();
				if (chancesOfMalfunction <= 0.3) {// A malfunction has occurred - send to technical attendant
					((ArrivalBus) currentBus).setTechnicalProblemNum(3);// Classification of technical problem for the
																		// technical attendant
					technicalAttendantQueue.insert(currentBus);
				} else {// Finished all treatments
					createBusDetailsDoc();// Create Bus details doc
				}
			}
		}
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public int getMaxFuelCapacity() {
		return this.MaxFuelCapacity;
	}

	private void createBusDetailsDoc() {// Create Bus details doc for entering bus
		String busCode = currentBus.getBusCode();
		int passengersNumber = currentBus.getPassengersNumber();
		int cargo = ((ArrivalBus) currentBus).getCargo();
		double repairCost = ((ArrivalBus) currentBus).getRepairCost();
		double treatmentTime = ((ArrivalBus) currentBus).getTreatmentTime();
		boolean suspiciousObject = currentBus.getSuspiciousObject();
		BusDetails bD = new BusDetails(busCode, passengersNumber, cargo, repairCost, treatmentTime, suspiciousObject);
		managerQueue.insert(bD);
	}

}
