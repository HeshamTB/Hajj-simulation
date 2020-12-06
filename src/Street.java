import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Street implements Travelable, Serializable {

    private double length;
    private int numberOfLanes;
    private ArrayList<Vehicle> vehicles; //Current
    private StreetNames name;
  
  

    public Street(double length, int numberOfLanes, StreetNames name) {
        vehicles = new ArrayList<>();
        setLength(length);
        setNumberOfLanes(numberOfLanes);
        this.name = name;
    }

    private void setLength(double length) {
        if (length >= 0) this.length = length;
        else throw new IllegalArgumentException("Can not make a negative length street");
    }

    private void setNumberOfLanes(int numberOfLanes) {
        if (numberOfLanes >= 1) this.numberOfLanes = numberOfLanes;
        else throw new IllegalArgumentException("Street can not have less then 1 lane");
    }

    public double getLength() {
        return length;
    }

    /**
     * Gets total length of one lane * number of lanes
     * As if it is a sigle longer lane.
     * @return Total length of all lanes combined
     */
    public double getCombinedLength() {
        return this.length * this.numberOfLanes;
    }

    public int getNumberOfLanes() {
        return numberOfLanes;
    }

    public ArrayList<Vehicle> getVehicles() {
        return vehicles;
    }

    public StreetNames getName() {
        return name;
    }

    public double capcity() {
        double totalLength =  length * numberOfLanes;
        double totalLenthofCar=0;
        for(int i=0;i<vehicles.size();i++) {
            totalLenthofCar+=vehicles.get(i).getVehicleSize();
        }
        int percent = 0;
        if (totalLenthofCar > totalLength){
            percent = (int) (totalLength/totalLenthofCar);
            percent *= 100;
            percent +=  totalLength - (totalLength % totalLenthofCar);
        }
        else {
            percent = (int)(totalLength - totalLenthofCar);
        }

        return percent;
        //return totalLength - totalLenthofCar;
    }

    public int getPercentRemainingCapacity() {
        return (int) (100 - (capcity()/(this.length*this.numberOfLanes)*100));
    }
    
    public boolean canTakeVehicles( Vehicle vehicle ) {
    	if ( vehicle.getVehicleSize() < capcity() )
    		return true;
    	else
    		return false;
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public double capcityPoint(double min, double max) {
        double totalLength =  (max - min) * numberOfLanes;
        double totalLenthofCar=0;
        for(int i=0;i<vehicles.size();i++) {
            if (vehicles.get(i).getCurrentLocation() >= min &&
                    vehicles.get(i).getCurrentLocation() <= max)
                totalLenthofCar+=vehicles.get(i).getVehicleSize();
        }
        double capcity = totalLenthofCar / totalLength;
        if (capcity > 1)
        	return 1;
        else if (capcity < 0 )
        	return 0;
        else
        	return capcity;
    }
    
    public double capcityPoint(double min, double max, Vehicle vehicle) {
    	
    	if(max > getLength() && getNextStreet(vehicle) != null) {
    		double reamingLength = max - getLength();
    		double y = capcityPoint(min, getLength(),vehicle);
    		double z = getNextStreet(vehicle).capcityPoint(0, reamingLength, vehicle);
                return (((getLength() - min)/(max - min)) * y) + (((reamingLength)/(max - min)) * z);
    	}
        double totalLength =  (max - min) * numberOfLanes;
        double totalLenthofCar=0;
        for(int i=0;i<vehicles.size();i++) {
            if (vehicles.get(i).getCurrentLocation() >= min &&
                    vehicles.get(i).getCurrentLocation() <= max)
                totalLenthofCar+=vehicles.get(i).getVehicleSize();
        }
        double capcity = totalLenthofCar / totalLength;
        if (capcity > 1)
        	return 1;
        else if (capcity < 0 )
        	return 0;
        else
        	return capcity;
    }
    
    public Street getNextStreet(Vehicle vehicle) {
        int nextIndex = vehicle.getRoute().indexOf(vehicle.getCurrentStreet()) +1 ;
        if(vehicle.getRoute().getStreets().length > nextIndex)
        return 	(vehicle.getRoute().getStreets()[nextIndex]);
            else
                return null;
    }

    public int getNumberOfBuses() {
        int number = 0;
        for (Vehicle vehicle : this.getVehicles()) {
            if (vehicle instanceof Bus) number++;
        }
        return number;
    }

    public int getNumberOfLocalCars() {
        int number = 0;
        for (Vehicle vehicle : this.getVehicles()) {
            if (vehicle instanceof CivilVehicle && !(vehicle instanceof Bus)) number++;
        }
        return number;
    }

    public String toString(){
        StringBuilder printedStreet = new StringBuilder();
        //Imagine steert is 32 units in lengths (scale down)
        for (int i = 0; i < 32; i++){
            printedStreet.append("----");
        }
        printedStreet.append("\n");
        //This is vary bad. Should copy vehicles list and reduce when a vehicle is counted;
        for (int i = 0; i < 32; i++){
            int cars = 0;
            for (Vehicle vehicle : vehicles){
                double realLocation = vehicle.getCurrentLocation();
                int scaledLocation = (int)((realLocation*32) / this.length);
                if (scaledLocation == i) cars++;
            }
            //if 'cars' is 4 digits or more it will distort the output
            printedStreet.append(String.format("%03d ", cars));
        }
        printedStreet.append("\n");
        for (int i = 0; i < 32; i++){
            printedStreet.append("----");
        }
        return String.format("Street name: %s, Length: %.2f, Lanes: %d, Vehicles: %d, Capacity: %%%s\nDensity:\n%s\n",
                name.name(),
                length,
                numberOfLanes,
                vehicles.size(),
                getPercentRemainingCapacity(),
                printedStreet.toString());
    }

    public boolean isContainsBuses() {
    	for (Vehicle vehicle : this.vehicles) {
    		if (vehicle instanceof Bus)
    			return true;
    	}
    	return false;
	}

	public String getFastestTimeOfTravel(Vehicle vehicle) {
        double totalLength = length;
        int maxSpeed = vehicle.getMaxSpeed();
        int totalTime = (int) (totalLength/maxSpeed);
        return String.format("%02d:%02d",totalTime / 60, totalTime % 60);
    }
}
