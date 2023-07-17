public class LogisticsAttendant implements Runnable {
	private String identifier;
	private double workRate;
	private ArrivalBus currentBus;
	private long sleepTime;
	private Queue <ArrivalBus> logisticsAttendantQueue;
	private Queue <ArrivalBus> cleanerQueue;
	private Queue <ArrivalBus> technicalAttendantQueue;

	//Constructor
	public LogisticsAttendant( String identifier , double workRate, Queue <ArrivalBus> logisticsAttendantQueue, Queue <ArrivalBus> cleanerQueue, Queue <ArrivalBus> technicalAttendantQueue) {
		this.identifier = identifier;
		this.workRate = workRate;
		this.logisticsAttendantQueue = logisticsAttendantQueue;
		this.cleanerQueue = cleanerQueue;
		this.technicalAttendantQueue = technicalAttendantQueue;
		Manager.dayOver = false; //Every run starts when the day isn't over
	}

	public void run() {
		while(!Manager.dayOver) {//While the day is not over
			currentBus = (ArrivalBus)logisticsAttendantQueue.extract();
			if(currentBus == null) {//Null is a sign for end day and wake all remaining threads
				this.logisticsAttendantQueue.insert(currentBus);
				break;
			}
			try {
				this.sleepTime = (long)(workRate * currentBus.getCargo()*1000);
				Thread.sleep(sleepTime);
				currentBus.setTreatmentTime(workRate * currentBus.getCargo());
			} catch (InterruptedException e) {}
			double chancesOfMalfunction = Math.random();
			if (chancesOfMalfunction <= 0.1 ) {//A malfunction has occurred
				currentBus.setTechnicalProblemNum(1);//Classification of technical problem for the technical attendant
				technicalAttendantQueue.insert(currentBus);
			}
			else
				cleanerQueue.insert(currentBus);
		}
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public double getWorkRate() {
		return this.workRate;
	}

	public void setWorkRate(double newWorkRate) {
		this.workRate = newWorkRate;
	}

}


