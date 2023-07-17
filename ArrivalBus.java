
public class ArrivalBus extends Bus{
	private int cargoQuantity;
	private int technicalProblemNum;
	private double repairCost;
	private double treatmentTime; 
	private boolean suspiciousObject;
	
	//Constructor
	public ArrivalBus(String tripNumber, int passengersNumber ,int arrivalTime, Queue <Bus> busQueue, int cargoQuantity){ 
		super(tripNumber, passengersNumber,arrivalTime, busQueue);//Inherit Bus constructor
		this.cargoQuantity = cargoQuantity;
		this.technicalProblemNum =0;
		this.repairCost =0.0;
		this.treatmentTime = 0;
		this.suspiciousObject = false;
	}
	
	public boolean getPriority() {//Entering bus doesn't get priority
		return false;
	}
	
	public int getCargo() {
		return this.cargoQuantity;
	}
	public int getTechnicalProblemNum() {
		return this.technicalProblemNum;
	}
	
	public void setTechnicalProblemNum(int numOfTechProb) {//Classification of technical problems 
		this.technicalProblemNum = numOfTechProb;
	}
	
	public double getRepairCost() {
		return this.repairCost;
	}
	
	public void setRepairCost(double repairCost) {//Sum the cost of the repairs of this bus
		this.repairCost = this.repairCost + repairCost;
	}
	
	public double getTreatmentTime() {
		return this.treatmentTime;
	}
	
	public void setTreatmentTime(double addTreatmentTime) {//Sum the total treatment time of this bus
		this.treatmentTime = this.treatmentTime + addTreatmentTime; 
	}
	
	public boolean getSuspiciousObject() {
		return this.suspiciousObject;
	}
	
	public void setSuspiciousObject() {//If this bus had suspicious object
		this.suspiciousObject = true;
	}
}


