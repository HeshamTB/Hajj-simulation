
public class SUV extends CivilVehicle {
	private short UID;
	private static short numeberOfSUV;
    private final short TIME_TO_FIX = 15; //in minutes
    public static final short MAX_FORWARD = 1300;

    public SUV(double vehicleSize){
        super(vehicleSize);
        generateUID();
    }

    @Override
    public int getMaxSpeed() {
        return MAX_FORWARD;
    }

    public int getTimeToFix(){
    	return TIME_TO_FIX; 
    }
    
    private void generateUID() {
    	numeberOfSUV++;
    	this.UID = numeberOfSUV;
    }
    
    public String getUID(){
        return String.format("SUV%04d", UID);
    }
    


}
