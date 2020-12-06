import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Campaign implements Serializable {

    private String UID;
    private int numberOfAriivedBuses;
    //private int housingNumber;
    //private String name;
    private District hotelDistrict;

    private Route route;

    private ArrayList<Vehicle> vehicles = new ArrayList<>();
    private ArrayList<Vehicle> arrivedVehicles = new ArrayList<>();

    private Date timeToLeaveToDest;
    private Date timeToLeaveToHousing;

    private static int numeberOfCampains;

    public Campaign(District hotelDistrict, int numberofBusses) {
        this.hotelDistrict = hotelDistrict;
        generateBusses(numberofBusses);
        generateUID();
    }

    public Campaign(District hotelDistrict, ArrayList<Vehicle> vehicles) {
        this.hotelDistrict = hotelDistrict;
        setVehicles(vehicles);
        generateUID();
    }

    public void busArived(Bus bus) {
        if(numberOfAriivedBuses < vehicles.size())
            arrivedVehicles.add(bus);
            numberOfAriivedBuses++;
    }

    public boolean isDone() {
        return numberOfAriivedBuses == vehicles.size();
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
        for (Vehicle vehicle : vehicles) {
            vehicle.setRoute(route);
        }
        //since a route is set, reset buses and clear arrivedBussesList
        this.arrivedVehicles.clear();
        this.numberOfAriivedBuses = 0;
    }

    public District getHotelDistrict(){ return this.hotelDistrict; }

    public Date getTimeToLeaveToDest() {
        return timeToLeaveToDest;
    }

    public void setTimeToLeaveToDest(Date timeToLeaveToDest) throws OutOfSimulationTimeException {
        if(PDate.isWithInTimeline(timeToLeaveToDest, MakkahCity.getTimeMan()))
            this.timeToLeaveToDest = timeToLeaveToDest;
        else throw new OutOfSimulationTimeException();
    }

    public Date getTimeToLeaveToHousing() {
        return timeToLeaveToHousing;
    }

    public void setTimeToLeaveToHousing(Date timeToLeaveToHousing) throws OutOfSimulationTimeException {
        if(PDate.isWithInTimeline(timeToLeaveToHousing, MakkahCity.getTimeMan()))
            this.timeToLeaveToHousing = timeToLeaveToHousing;
        else throw new OutOfSimulationTimeException();
    }

    public int getNumberOfBusses() {
        int busses = 0;
        for (Vehicle vehicle : vehicles){
            if (vehicle instanceof Bus) busses++;
        }
        return busses;
    }

    public int getNumberOfArrivedBuses() {
        return getArrivedVehicles().size();
    }

    //Use this in Makkah to optimize later
    public ArrayList<Vehicle> getArrivedVehicles() {
        return arrivedVehicles;
    }

    public ArrayList<Vehicle> getVehicles() {
		return vehicles;
	}

	public void setVehicles(ArrayList<Vehicle> vehicles) {
		if (vehicles != null)
		this.vehicles = vehicles;
	}

    private void generateBusses(int number){
    	for (int i = 1; i <= number; i++) {
    	    Bus bus = new Bus();
    		vehicles.add(bus);
    		bus.setCampaign(this);
    	}
    }

    private void generateUID() {
        numeberOfCampains++;
        this.UID = String.format("CAMP%04d", numeberOfCampains);
    }

    public String getUID(){
        return this.UID;
    }

    public int getPercentArrived() {
        return ((numberOfAriivedBuses/vehicles.size()) *100);
    }

    public String toString(){
        return String.format("ID: %s\n" +
                "District: %s\n" +
                "Bueses: %d, Arrived buses: %d\n",
                getUID(),
                getHotelDistrict().name(),
                getNumberOfBusses(), getNumberOfArrivedBuses());
    }
}
