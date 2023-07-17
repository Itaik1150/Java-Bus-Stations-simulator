public class GateKeeper implements Runnable{
	private PriorityQueue<Bus> gateKeeperQueue;
	private Queue<ArrivalBus> logisticsAttendantQueue;
	private Queue<BusDetails> managerQueue;
	private long sleepTime;
	private Bus currentBus;
	private static int dailyNumOfBuses;//Only gate keepers can access

	//Constructor
	public GateKeeper (PriorityQueue <Bus> busQueue, Queue <ArrivalBus> logisticsAttendantQueue, Queue <BusDetails> managerQueue) {
		this.gateKeeperQueue = busQueue;
		this.logisticsAttendantQueue = logisticsAttendantQueue;
		this.managerQueue = managerQueue;
		GateKeeper.dailyNumOfBuses=CentralBusStation.dailyNumOfBuses;//Holds the total number of buses (entering & exiting)
	}

	public void run() {
		while (!(dailyNumOfBuses == 0)) {//While there are buses that need to pass throw the gate
			currentBus = gateKeeperQueue.extract();
			if(currentBus == null) {//Null is a sign for end day and wake all remaining threads
				this.gateKeeperQueue.insert(currentBus);
				break;
			}
			try {
				sleepTime = (long)(1000*(Math.random()*(10-5+1)+5));
				Thread.sleep(this.sleepTime);
			}
			catch (InterruptedException e) {}
			if (currentBus instanceof ArrivalBus) {//If the bus is entering bus - insert arrival queue
				logisticsAttendantQueue.insert((ArrivalBus)currentBus);
				dailyNumOfBuses--;
			}
			else {//If the bus is exiting bus - create doc
				createBusDetailsDoc();
				dailyNumOfBuses--;
			}
		}
	}

	private void createBusDetailsDoc() {//Create bus details doc for exiting bus
		String busCode = currentBus.getBusCode();
		int passengersNumber = currentBus.getPassengersNumber();
		String destination = ((DepartingBus)currentBus).getDestination();
		long waitingInGate = this.sleepTime;
		BusDetails bD = new BusDetails(busCode, passengersNumber, destination, waitingInGate);
		managerQueue.insert(bD);
	}
}
