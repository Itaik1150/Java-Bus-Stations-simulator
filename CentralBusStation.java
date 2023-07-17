import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CentralBusStation {
	private PriorityQueue<Bus> gateKeeperQueue;
	private Queue<ArrivalBus> logisticsAttendantQueue;
	private Queue<ArrivalBus> cleanerQueue;
	private BoundedQueue<ArrivalBus> fuelAttendantQueue;
	private Queue<ArrivalBus> technicalAttendantQueue;
	private Queue<BusDetails> managerQueue;
	private int numOfTechnicalAttendant;
	private double cleanerWorkRate;
	public static int dailyNumOfBuses;

	public CentralBusStation(String Bus, int numOfTechnicalAttendant, double cleanerWorkRate) {// Constructor
		gateKeeperQueue = new PriorityQueue<Bus>();
		logisticsAttendantQueue = new Queue<ArrivalBus>();
		cleanerQueue = new Queue<ArrivalBus>();
		fuelAttendantQueue = new BoundedQueue<>(8);
		technicalAttendantQueue = new Queue<ArrivalBus>();
		managerQueue = new Queue<BusDetails>();
		dailyNumOfBuses = 0;
		inputFile(Bus);
		this.numOfTechnicalAttendant = numOfTechnicalAttendant;
		this.cleanerWorkRate = cleanerWorkRate;

	}

	private void inputFile(String path) { // Read from a file
		BufferedReader inFile = null;
		try {
			FileReader file = new FileReader(path);
			inFile = new BufferedReader(file);
			path.contains(path);
			String line = inFile.readLine();
			while (line != null) {
				line = inFile.readLine();
				insertBusQueue(line);
			}
		} catch (FileNotFoundException exception) {
			System.out.println("The file " + path + " was not found.");
		} catch (IOException exception) {
			System.out.println(exception);
		} finally {
			try {
				inFile.close();
			} catch (IOException exception) {
				exception.printStackTrace();
			}
		}

	}

	private void insertBusQueue(String line) {// Insert to a queue the information from the file
		if (line != null) {
			String[] arrOfStr = line.split("	");
			String code = arrOfStr[0];
			int numOfPassengers = Integer.parseInt(arrOfStr[1]);
			int arrivalTime = Integer.parseInt(arrOfStr[2]);
			String destination = arrOfStr[3];
			Thread t;
			if (destination.equals("")) {// If its an entering bus
				int cargo = Integer.parseInt(arrOfStr[4]);
				Bus arrivalBus = new ArrivalBus(code, numOfPassengers, arrivalTime, gateKeeperQueue, cargo);
				dailyNumOfBuses++;
				t = new Thread(arrivalBus);
			} else {// If its an exiting bus
				Bus departingBus = new DepartingBus(code, numOfPassengers, arrivalTime, gateKeeperQueue, destination);
				dailyNumOfBuses++;
				t = new Thread(departingBus);
			}
			t.start();
		}
	}

	private void createGateKeeper() {// Create the gate keepers and run threads
		for (int i = 0; i < 2; i++) {
			GateKeeper gK = new GateKeeper(gateKeeperQueue, logisticsAttendantQueue, managerQueue);
			Thread t = new Thread(gK);
			t.start();
		}
	}

	private void createLogisticsAttendant() {// Create the logistics attendants and run threads
		for (int i = 0; i < 3; i++) {
			double workRate = 0.03 + (i * 0.02);
			LogisticsAttendant lA = new LogisticsAttendant(String.valueOf(i + 1), workRate, logisticsAttendantQueue,
					cleanerQueue, technicalAttendantQueue);
			Thread t = new Thread(lA);
			t.start();
		}
	}

	private void createCleaner() {// Create the cleaners and run threads
		for (int i = 0; i < 3; i++) {
			Cleaner c = new Cleaner(i + 1, cleanerWorkRate, cleanerQueue, fuelAttendantQueue, technicalAttendantQueue);
			Thread t = new Thread(c);
			t.start();
		}
	}

	private void createFuelAttendant() {// Create the fuel attendants and run threads
		for (int i = 0; i < 2; i++) {
			int MaxFuelCapacity = 2000 - (i * 1000);
			FuelAttendant fA = new FuelAttendant(String.valueOf(i + 1), MaxFuelCapacity, fuelAttendantQueue,
					technicalAttendantQueue, managerQueue);
			Thread t = new Thread(fA);
			t.start();
		}
	}

	private void createTechnicalAttendant() {// Create the technical attendants by number given by GUI and run threads
		for (int i = 0; i < numOfTechnicalAttendant; i++) {
			TechnicalAttendant tA = new TechnicalAttendant(String.valueOf(i + 1), cleanerQueue, fuelAttendantQueue,
					technicalAttendantQueue, managerQueue);
			Thread t = new Thread(tA);
			t.start();
		}
	}

	private void createManager() {// Create the manager and run thread
		Manager m = new Manager("Itai", managerQueue, gateKeeperQueue, logisticsAttendantQueue, cleanerQueue,
				fuelAttendantQueue, technicalAttendantQueue);
		Thread t = new Thread(m);
		t.start();
	}

	public void start() {// Initiate
		createGateKeeper();
		createLogisticsAttendant();
		createCleaner();
		createFuelAttendant();
		createTechnicalAttendant();
		createManager();
	}
}
