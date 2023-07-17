public class Cleaner implements Runnable {
	private double seniority;
	private double workRate;
	private ArrivalBus currentBus;
	private long sleepTime;
	private Queue<ArrivalBus> cleanerQueue;
	private BoundedQueue<ArrivalBus> fuelAttendantQueue;
	private Queue<ArrivalBus> technicalAttendantQueue;

	// Constructor
	public Cleaner(double seniority, double workRate, Queue<ArrivalBus> cleanerQueue,
			BoundedQueue<ArrivalBus> fuelAttendantQueue, Queue<ArrivalBus> technicalAttendantQueue) {
		this.seniority = seniority;
		this.workRate = workRate;
		this.cleanerQueue = cleanerQueue;
		this.fuelAttendantQueue = fuelAttendantQueue;
		this.technicalAttendantQueue = technicalAttendantQueue;
	}

	public void run() {
		while (!Manager.dayOver) {// While the day is not over
			currentBus = cleanerQueue.extract();
			if (currentBus == null) {// Null is a sign for end day and wake all remaining threads
				this.cleanerQueue.insert(currentBus);
				break;
			}
			try {
				this.sleepTime = (long) (workRate * 1000);
				Thread.sleep(sleepTime);
				currentBus.setTreatmentTime(workRate);// Add to treatment time
			} catch (InterruptedException e) {
			}

			double chancesOfSuspiciousObject = Math.random();
			if (chancesOfSuspiciousObject <= 0.05) { // A suspicious object was found
				try {
					currentBus.setSuspiciousObject();
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
			}
			double chancesOfMalfunction = Math.random();
			if (chancesOfMalfunction <= 0.25) { // A malfunction has occurred
				currentBus.setTechnicalProblemNum(2);// Classification of technical problem for the technical attendant
				technicalAttendantQueue.insert(currentBus);
			} else
				fuelAttendantQueue.insert(currentBus);
		}
	}

	public double getSeniority() {
		return this.seniority;
	}

	public double getWorkRate() {
		return this.workRate;
	}
}
