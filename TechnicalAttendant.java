
public class TechnicalAttendant implements Runnable {
	private String identifier;
	private ArrivalBus currentBus;
	private long sleepTime;
	private Queue<ArrivalBus> cleanerQueue;
	private BoundedQueue<ArrivalBus> fuelAttendantQueue;
	private Queue<ArrivalBus> technicalAttendantQueue;
	private Queue<BusDetails> managerQueue;

	// Constructor
	public TechnicalAttendant(String identifier, Queue<ArrivalBus> cleanerQueue,
			BoundedQueue<ArrivalBus> fuelAttendantQueue, Queue<ArrivalBus> technicalAttendantQueue,
			Queue<BusDetails> managerQueue) {
		this.identifier = identifier;
		this.cleanerQueue = cleanerQueue;
		this.fuelAttendantQueue = fuelAttendantQueue;
		this.technicalAttendantQueue = technicalAttendantQueue;
		this.managerQueue = managerQueue;
	}

	public void run() {
		while (!Manager.dayOver) {// While the day is not over
			currentBus = technicalAttendantQueue.extract();
			if (currentBus == null) {// Null is a sign for end day and wake all remaining threads
				this.technicalAttendantQueue.insert(currentBus);
				break;
			}
			double repairCost = (Math.random() * (1000 - 500 + 1) + 500);
			currentBus.setRepairCost(repairCost);// Add cost of repair
			try {
				sleepTime = (long) ((Math.random() * (5 - 3 + 1) + 3) * 1000);
				Thread.sleep(this.sleepTime);
				currentBus.setTreatmentTime((Math.random() * (5 - 3 + 1) + 3));
			} catch (InterruptedException e) {
			}
			if (currentBus.getTechnicalProblemNum() == 3) {// Sleep 1 sec if this was a fuel technical problem
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
			returnToQueue(currentBus.getTechnicalProblemNum());// Return current bus to it's next stop
		}
	}

	public String getIdentifier() {
		return this.identifier;
	}

	private void returnToQueue(int techProbNum) {//// Return current bus to it's next stop
		if (techProbNum == 1)// After a problem in logistics send to cleaner
			cleanerQueue.insert(currentBus);
		else if (techProbNum == 2)// After a problem in cleaner send to fuel attendant
			fuelAttendantQueue.insert(currentBus);
		else if (techProbNum == 3)// After a problem in fuel create a Bus details doc
			createBusDetailsDoc();
	}

	private void createBusDetailsDoc() {// Create Bus details doc for entering bus
		String busCode = currentBus.getBusCode();
		int passengersNumber = currentBus.getPassengersNumber();
		int cargo = ((ArrivalBus) currentBus).getCargo();
		double repairCost = currentBus.getRepairCost();
		double treatmentTime = currentBus.getTreatmentTime();
		boolean suspiciousObject = currentBus.getSuspiciousObject();
		BusDetails bD = new BusDetails(busCode, passengersNumber, cargo, repairCost, treatmentTime, suspiciousObject);
		managerQueue.insert(bD);
	}
}
