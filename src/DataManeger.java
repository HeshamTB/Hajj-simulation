import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;

public class DataManeger {

    private Path workingDir;

//    public DataManeger(Path path) {
//        this.seperator = File.separatorChar;
//        this.workingDir = path;
//    }

    public DataManeger(State state, Date time){
        Path dir = Paths.get("");
    }

    public DataManeger() {
        this(null, null);
    }

    public boolean stateAvailable(Date time) {
        File f = new File(String.format("0x%016X.bin", time.getTime()));
        return f.exists();
    }

    public  State loadState(Date time){
        State state = null;
        if (stateAvailable(time)){
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(
                        new FileInputStream(String.format("0x%016X.bin", time.getTime())));
                state = (State)objectInputStream.readObject();
                objectInputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return state;
    }

    public boolean saveState(State state, Date time){
        try {
            FileOutputStream fs = new FileOutputStream(String.format("0x%016X.bin", time.getTime()));
            BufferedOutputStream bfs = new BufferedOutputStream(fs);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(bfs);
            objectOutputStream.writeObject(state);
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

class State implements Serializable {

    //A better implementation is to make this more general or make Makkah an instance.

    private ArrayList<Campaign> listOfCampaigns;
    private ArrayList<Vehicle> listOfVehicles;
    private Route[] stdRoutes;
    private Street[] stdStreet;
    private Date allArrivedToArafatTime;
    private Date allArrivedToHotelsTime;

    public State(ArrayList<Campaign> listOfCampaigns, ArrayList<Vehicle> listOfVehicles, Route[] stdRoutes, Street[] stdStreet, Date allArrivedToArafatTime, Date allArrivedToHotelsTime) {
        //Make clones since values may change if this is running on a thread.
        this.listOfCampaigns = (ArrayList<Campaign>) listOfCampaigns.clone();
        this.listOfVehicles = (ArrayList<Vehicle>) listOfVehicles.clone();
        this.stdRoutes = stdRoutes.clone();
        this.stdStreet = stdStreet.clone();
        if (allArrivedToArafatTime != null) {
            this.allArrivedToArafatTime = (Date) allArrivedToArafatTime.clone();
        }
        if (allArrivedToHotelsTime != null) {
            this.allArrivedToHotelsTime = (Date) allArrivedToHotelsTime.clone();
        }
    }

    public ArrayList<Campaign> getListOfCampaigns() {
        return listOfCampaigns;
    }

    public ArrayList<Vehicle> getListOfVehicles() {
        return listOfVehicles;
    }

    public Route[] getStdRoutes() {
        return stdRoutes;
    }

    public Street[] getStdStreet() {
        return stdStreet;
    }

    public Date getAllArrivedToArafatTime() {
        return allArrivedToArafatTime;
    }

    public Date getAllArrivedToHotelsTime() {
        return allArrivedToHotelsTime;
    }
}
