public class BusDetails {
	private String busCode;
	private int passengersNumber;
	private String destination;
	private double waitingInGate;
	private int cargo;
	private double repairCost;
	private double treatmentTime;
	private boolean suspiciousObject;
	private boolean isEnteringBus;

	// Constructor for doc for exiting bus
	public BusDetails(String busCode, int passengersNumber, String destination, double waitingInGate) {
		this.busCode = busCode;
		this.passengersNumber = passengersNumber;
		this.destination = destination;
		this.waitingInGate = waitingInGate;
		isEnteringBus = false;
	}

	// Constructor for doc for entering bus
	public BusDetails(String busCode, int passengersNumber, int cargo, double repairCost, double treatmentTime,
			boolean suspiciousObject) {
		this.busCode = busCode;
		this.passengersNumber = passengersNumber;
		this.cargo = cargo;
		this.repairCost = repairCost;
		this.treatmentTime = treatmentTime;
		this.suspiciousObject = suspiciousObject;
		isEnteringBus = true;
	}

	public String getBusCode() {
		return this.busCode;
	}

	public int getPassengersNumber() {
		return this.passengersNumber;
	}

	public String getDestination() {
		return this.destination;
	}

	public double getWaitingInGate() {
		return this.waitingInGate;
	}

	public int getCargo() {
		return this.cargo;
	}

	public double getRepairCost() {
		return this.repairCost;
	}

	public double getTreatmentTime() {
		return this.treatmentTime;
	}

	public boolean getSuspiciousObject() {
		return this.suspiciousObject;
	}

	public boolean getIsEnteringBus() {
		return this.isEnteringBus;
	}
}
