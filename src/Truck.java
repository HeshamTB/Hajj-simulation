
public class Truck extends CivilVehicle {
	private short UID;
	private static short numeberOfTruck;
    private final short TIME_TO_FIX = 20; //in minutes

    @Override
    public int getMaxSpeed() {
        return Bus.MAX_FORWARD;
    }

    public Truck(double vehicleSize){
        super(vehicleSize);
        generateUID();
    }

    public int getTimeToFix(){
    	return TIME_TO_FIX; 
    }
    private void generateUID() {
    	numeberOfTruck++;
    	this.UID = numeberOfTruck;
    }

    public String getUID(){
        return String.format("Truck%04d", UID);
    }

}


