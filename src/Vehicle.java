import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

public abstract class Vehicle implements Serializable {

    private double vehicleSize;
    private Route route;
    private Street currentStreet;
    private double currentLocation;
    private double totalDistanceTraveled;
    private boolean arrivedToDest;
    private boolean moving;
    private Date timeStartedMoving;
    private Date timeOfArrival;
    private Date timeStartedOnCurrentStreet;
    private static Vehicle max;
    private static Vehicle min;

    //Map Street to Minutes
    private HashMap<Street, Integer> routeTimeHistory = new HashMap<>();

    public abstract int getMaxSpeed();
    public abstract String getUID();

    public Vehicle(double vehicleSize){
        setVehicleSize(vehicleSize);
    }

    public double getVehicleSize() {
        return vehicleSize;
    }

    public Route getRoute() {
        return route;
    }

    public void arrive() {
        setArrivedToDest(true);
        setMoving(false);
        setTimeOfArrival(MakkahCity.getTimeMan().getCurrentTime());
        getCurrentStreet().getVehicles().remove(this);
        this.currentLocation = 0;
        maxArrived();
        minArrived();
        if (totalDistanceTraveled > route.getTotalLength()) totalDistanceTraveled = route.getTotalLength();
    }
    
    public void maxArrived() {
    	if (this instanceof Bus) {
    		if (max == null)
    			max = this;
    		else {
    			long thisMinutes = (getTimeOfArrival().getTime() - getTimeStartedMoving().getTime())/60000;
    			long maxMinutes = (max.getTimeOfArrival().getTime() - max.getTimeStartedMoving().getTime())/60000;
    			if (thisMinutes > maxMinutes)
    				max = this;
    		}
    	}
	}
    
    
    
    public void minArrived() {
    	if (this instanceof Bus) {
    		if (min == null)
    			min = this;
    		else {
    			long thisMinutes = (getTimeOfArrival().getTime() - getTimeStartedMoving().getTime())/60000;
    			long maxMinutes = (min.getTimeOfArrival().getTime() - min.getTimeStartedMoving().getTime())/60000;
    			if (thisMinutes < maxMinutes)
    				min = this;
    		}
    	}
	}
    
    public String timeToString() {
    	if (this == null)
    		return "null";
    	else {
		long minutes = (getTimeOfArrival().getTime() - getTimeStartedMoving().getTime())/60000;
		int hours =(int) minutes / 60;
		minutes %= 60;
		return String.format("%2d:%02d", hours,minutes);}

	}
 
    
    public static Vehicle getMaxArrived() {
		return max;
	}

	public static Vehicle getMinArrived() {
		return min;
	}
	
	public static void resetMaxArrived() {
		Vehicle.max = null;
	}
	
	public static void resetMinArrived() {
		Vehicle.min = null;
	}
	
	
    public void move(double distance) {
        if (!isMoving()) {
            setMoving(true);
            setTimeStartedMoving(MakkahCity.getTimeMan().getCurrentTime());
        }
        setCurrentLocation(getCurrentLocation() + distance);
        setTotalDistanceTraveled(getTotalDistanceTraveled() + distance);
    }

    public void setRoute(Route route) {
        this.route = route;
        this.arrivedToDest = false;
        this.routeTimeHistory.clear();
        this.timeStartedOnCurrentStreet = null;
        this.timeStartedMoving = null;
        this.timeOfArrival = null;
    }

    private void setVehicleSize(double vehicleSize) {
        if (vehicleSize <= 0) throw new IllegalArgumentException("Vehicle can not be negative in length!");
        else this.vehicleSize = vehicleSize;
    }

    public Street getCurrentStreet() {
        return currentStreet;
    }

    public double getCurrentLocation() {
        return currentLocation;
    }

    /**
     * @return Distance in meters to next car on same street. -1 if Can not calculate.
     */
    public double getDistanceToNextVehicle() {
        if (getCurrentStreet() != null) {
            int indexOfNxt = getCurrentStreet().getVehicles().indexOf(this) - 1; //next
            //Index may be out of bound if this car is the only or first (end if street)
            try {
                Vehicle nextVehicle = getCurrentStreet().getVehicles().get(indexOfNxt);
                return nextVehicle.getCurrentLocation() - this.getCurrentLocation();
            }
            catch (IndexOutOfBoundsException e) {return -1;}
        }
        return -1;//open or unlimited
    }

    public void setCurrentStreet(Street currentStreet) {
        if (this.currentStreet != null) {
            this.currentStreet.getVehicles().remove(this);
        }
        this.currentStreet = currentStreet;
        if (this.currentStreet != null)
            this.currentStreet.addVehicle(this);
        this.timeStartedOnCurrentStreet = MakkahCity.getTimeMan().getCurrentTime();
    }

    public void setCurrentLocation(double currentLocation) {
        this.currentLocation = currentLocation;
    }

    public double getTotalDistanceTraveled() {
        return totalDistanceTraveled;
    }

    public void setTotalDistanceTraveled(double totalDistanceTraveled) {
        this.totalDistanceTraveled = totalDistanceTraveled;
    }

    public boolean isArrivedToDest() {
        return arrivedToDest;
    }

    public void setArrivedToDest(boolean arrivedToDest) {
        this.arrivedToDest = arrivedToDest;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public Date getTimeStartedMoving() {
        return timeStartedMoving;
    }

    public void setTimeStartedMoving(Date timeStartedMoving) {
        this.timeStartedMoving = timeStartedMoving;
    }

    public Date getTimeOfArrival() {
        return timeOfArrival;
    }

    public void setTimeOfArrival(Date timeOfArrival) {
        this.timeOfArrival = timeOfArrival;
    }
    
    public void moveToNextStreet() {
        this.routeTimeHistory.put(this.currentStreet,
                (int) (MakkahCity.getTimeMan().getCurrentTime().getTime() - timeStartedOnCurrentStreet.getTime())/60000);
    	int nxtIndex = route.indexOf(this.getCurrentStreet()) + 1;
		if (nxtIndex <= route.getStreets().length - 1) {
			if (this.getRoute().getStreets()[nxtIndex].capcityPoint(0, 1000) < 1) {
			this.setCurrentStreet(route.getStreets()[nxtIndex]);
			this.setCurrentLocation(0);
			}
		}
		else
			this.arrive();
    }

    /**
     * Get value in minutes of time spent travling on street. If did not, return 0.
     * @param street
     * @return Time in minutes
     */
    public int getTimeOnStreet(Street street) {
        if (routeTimeHistory.containsKey(street)) {
            return routeTimeHistory.get(street);
        }
        return 0;
    }

    public String getProgress() {
        int p = (int)((totalDistanceTraveled/route.getTotalLength())*100);
        if (p > 100) p = 100;
        return String.format("%%%d", p);
    }

    public String getTripTime() {
        long minutes = 0;
        if (timeStartedMoving != null && timeOfArrival != null){
            minutes = (getTimeOfArrival().getTime() - getTimeStartedMoving().getTime())/60000;

        }
        else if (timeStartedMoving != null) { //compare to now
            minutes = (MakkahCity.getTimeMan().getCurrentTime().getTime() - getTimeStartedMoving().getTime())/60000;
        }
        return String.format("%d:%02d", minutes /60, minutes % 60);
    }

    public boolean hasCrossedStreet(Street street) {
        return routeTimeHistory.containsKey(street);
    }

    public String toString() {
        Street st = this.currentStreet;
        String streetString = "null";
        if (st != null) streetString = st.getName().name();
        return String.format("\nRoute: %s\nStreet: %s, Location: %.1f, Distance covered: %.1f, Progress: %s\n" +
                        "Arrived: %s\nStarting time: %s\nArrive Time:   %s\nTrip time: %s\n",
                this.route, streetString,
                this.getCurrentLocation(), this.totalDistanceTraveled, getProgress(), this.isArrivedToDest(),
                this.getTimeStartedMoving(), this.getTimeOfArrival(), getTripTime());
    }
}


