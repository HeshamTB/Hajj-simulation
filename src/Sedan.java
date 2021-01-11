
public class Sedan extends CivilVehicle {

	private short UID;
	private static short numeberOfSedan;
    private final short TIME_TO_FIX = 15; //in minutes
    public static final short MAX_FORWARD = 1500; // Meter/Min

    public Sedan(double vehicleSize){
        super(vehicleSize);
        generateUID();
    }

    @Override
    public int getTimeToFix(){ return TIME_TO_FIX; }
    
    private void generateUID() {
    	numeberOfSedan++;
    	this.UID = numeberOfSedan;
    }
    
    public String getUID(){
        return String.format("Sedan%04d", UID);
    }

    @Override
    public int getMaxSpeed() {
        return MAX_FORWARD;
    }
}
