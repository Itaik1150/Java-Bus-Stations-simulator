import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Manager implements Runnable {
	private String identifier;
	private int dailyNumOfBuses;
	public static boolean dayOver;
	private BusDetails currentDoc;
	private int sumPassengers;
	private int sumCargoQuantity;
	private String commonDestination;
	private double sumRepairCost;
	private int sumUsedFuel;
	private int sumSuspiciousObject;
	private Queue<BusDetails> managerQueue;
	private PriorityQueue<Bus> gateKeeperQueue;
	private Queue<ArrivalBus> logisticsAttendantQueue;
	private Queue<ArrivalBus> cleanerQueue;
	private BoundedQueue<ArrivalBus> fuelAttendantQueue;
	private Queue<ArrivalBus> technicalAttendantQueue;
	private Vector<BusDetails> enteringBusDetailsVec;
	private Vector<BusDetails> exitingBusDetailsVec;
	private SortedMap<String, Integer> destinationsCount;

	// constructor
	public Manager(String identifier, Queue<BusDetails> managerQueue, PriorityQueue<Bus> gateKeeperQueue,
			Queue<ArrivalBus> logisticsAttendantQueue, Queue<ArrivalBus> cleanerQueue,
			BoundedQueue<ArrivalBus> fuelAttendantQueue, Queue<ArrivalBus> technicalAttendantQueue) {
		this.identifier = identifier;
		this.dailyNumOfBuses = CentralBusStation.dailyNumOfBuses;
		this.managerQueue = managerQueue;
		exitingBusDetailsVec = new Vector<BusDetails>();
		enteringBusDetailsVec = new Vector<BusDetails>();
		this.gateKeeperQueue = gateKeeperQueue;
		this.logisticsAttendantQueue = logisticsAttendantQueue;
		this.cleanerQueue = cleanerQueue;
		this.fuelAttendantQueue = fuelAttendantQueue;
		this.technicalAttendantQueue = technicalAttendantQueue;
		destinationsCount = new TreeMap<String, Integer>();
		sumPassengers = 0;
		sumCargoQuantity = 0;
		commonDestination = "";
		sumRepairCost = 0;
		sumUsedFuel = 0;
		sumSuspiciousObject = 0;
		dayOver = false;
	}

	public void run() {
		while (!Manager.dayOver) {// While the day is not over
			currentDoc = managerQueue.extract();
			sumPassengers = sumPassengers + currentDoc.getPassengersNumber();// Sum number of all passengers from all
																				// the buses
			dailyNumOfBuses--;
			// Add this doc to right vector (entering buses or exiting)
			if (currentDoc.getIsEnteringBus())
				enteringBusDetailsVec.add(currentDoc);
			else {
				exitingBusDetailsVec.add(currentDoc);
				countDestinations(currentDoc.getDestination());// Count how much buses were from each destination
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
			if (currentDoc.getIsEnteringBus()) {// If this doc is of entering bus - sum
				sumCargoQuantity = sumCargoQuantity + currentDoc.getCargo();
				sumRepairCost = sumRepairCost + currentDoc.getRepairCost();
				sumUsedFuel = sumUsedFuel + 200;
				if (currentDoc.getSuspiciousObject())
					sumSuspiciousObject++;
			}
			printMidDayReport();// Print mid day report
			if (dailyNumOfBuses == 0) {// If there are no more bus docs
				writeFileEnteringBuses();// Write to a file of entering buses
				writeFileExitingBuses();// Write to a file of exiting buses
				dayOver();
				threadsFinish();// Notify all threads so the could die
				printEndDayReport();// Print end of day report
			}
		}
	}

	public String getIdentifier() {
		return this.identifier;
	}

	private void dayOver() {// End of day notification in order to kill the threads
		Manager.dayOver = true;
	}

	private void threadsFinish() {// Notify all threads so the could die
		gateKeeperQueue.insert(null);
		logisticsAttendantQueue.insert(null);
		cleanerQueue.insert(null);
		fuelAttendantQueue.insert(null);
		technicalAttendantQueue.insert(null);
	}

	private void printMidDayReport() {// Print mid day report
		System.out.println("Bus code: " + currentDoc.getBusCode());
		System.out.println("This bus time in central station: " + currentDoc.getTreatmentTime());
		System.out.println("This bus treatment cost: " + currentDoc.getRepairCost() + "\n");
	}

	private void printEndDayReport() {// Print end of day report
		System.out.println("End of the day report:");
		System.out.println("Total passengers number (entering & exiting): " + sumPassengers);
		System.out.println("Total cargo quantity: " + sumCargoQuantity);
		System.out.println("Most common destination: " + findMostCommonDestination());
		System.out.println("Total treatment cost: " + sumRepairCost);
		System.out.println("Total fuel quantity: " + sumUsedFuel);
		System.out.println("Total suspicious Objects found: " + sumSuspiciousObject);
	}

	private void writeFileEnteringBuses() {// Write to a file of entering buses
		File file1 = new File("Entering" + ".txt");
		PrintWriter output = null;
		try {
			output = new PrintWriter(new FileWriter(file1));
		} catch (IOException exception) {
			System.err.println(exception.getMessage());
		}
		output.println(
				"code\t" + "passengers\t" + "cargo\t" + "treatment cost\t" + "suspicious object\t" + "treatment time");
		for (BusDetails d : this.enteringBusDetailsVec) {
			output.println(d.getBusCode() + "\t" + d.getPassengersNumber() + "\t" + d.getCargo() + "\t"
					+ d.getRepairCost() + "\t" + d.getSuspiciousObject() + "\t" + d.getTreatmentTime());
		}
		output.close();
	}

	private void writeFileExitingBuses() {// Write to a file of exiting buses
		File file1 = new File("Exiting" + ".txt");
		PrintWriter output = null;
		try {
			output = new PrintWriter(new FileWriter(file1));
		} catch (IOException exception) {
			System.err.println(exception.getMessage());
		}
		output.println("code\t" + "passengers\t" + "destination\t" + "time waiting at the gate");
		for (BusDetails d : this.exitingBusDetailsVec) {
			output.println(d.getBusCode() + "\t" + d.getPassengersNumber() + "\t" + d.getDestination() + "\t"
					+ d.getWaitingInGate());
		}
		output.close();
	}

	private void countDestinations(String destination) {// Count how much buses were from each destination
		if (destinationsCount.containsKey(destination))// If the current destination is already in the sortedMap
			destinationsCount.put(destination, destinationsCount.get(destination) + 1);// Add one more to value
		else// If the current destination isn't in the sortedMap
			destinationsCount.put(destination, 1);// Add it to the sortedMap
	}

	private String findMostCommonDestination() {// Find the most common destination
		int maxCount = 0;
		for (String dest : destinationsCount.keySet()) {// Run over all keys in the sortedMap
			if (destinationsCount.get(dest) > maxCount) {// Take the destination with the highest value
				commonDestination = dest;
				maxCount = destinationsCount.get(dest);
			}
		}
		return commonDestination;
	}
}