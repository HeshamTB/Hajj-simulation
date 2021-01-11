
public class Bus extends CivilVehicle {
	
	private int UID;
	private Campaign campaign;
	private static int numeberOfBuses;
    private final short TIME_TO_FIX = 20; //in minutes
    public static final short MAX_FORWARD = 900; //Meter/Min

    public static final double STD_BUS_SIZE = 10;

    public Bus() {
        super(STD_BUS_SIZE);
        generateUID();
    }

    @Override
    public int getMaxSpeed() {
        return MAX_FORWARD;
    }

    @Override
    public void arrive() {
        super.arrive();
        campaign.busArived(this);
    }

    @Override
    public int getTimeToFix() {
        return TIME_TO_FIX;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(String.format("ID: %s, Campaign ID: %s\n",this.getUID() , getCampaign().getUID()));
        s.append(super.toString());
        return s.toString();
    }

    private void generateUID() {
        numeberOfBuses++;
        this.UID = numeberOfBuses;
    }

    public String getUID(){
        return String.format("BUS%04d", UID);
    }

    public Campaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Campaign campaign) {
        this.campaign = campaign;
    }
}
