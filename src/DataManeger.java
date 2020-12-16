import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataManeger {

    private File workingDir;

    public DataManeger(){
        workingDir = new File("./data/");
        workingDir.mkdir();
        clearData();
    }

    public boolean stateAvailable(Date time) {
        File f = new File(String.format("./data/%s.bin", time.getTime()));
        return f.exists();
    }

    public State loadState(Date time){
        State state = null;
        if (stateAvailable(time)){
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(
                        new FileInputStream(String.format("./data/%s.bin", time.getTime())));
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
            FileOutputStream fs = new FileOutputStream(String.format("./data/%s.bin", time.getTime()));
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

    public File[] savedStateFiles() {
        return workingDir.listFiles();
    }

    public Date[] savedStatesTimes() {
        File[] files = savedStateFiles();
        ArrayList<Date> timesList = new ArrayList<>();
        for (File file : files){
            if (file.getName().contains(".bin")){
                String timeInName = file.getName().replaceAll(".bin", "").trim();
                if ("".equals(timeInName)) continue;
                long time = Long.parseLong(timeInName);
                timesList.add(new HijriDate(time));
            }
        }
        Date[] times = new Date[timesList.size()];
        times = timesList.toArray(times);
        return times;
    }

    public List<State> getStates(){
        List<State> list = new ArrayList<>();
        for (File file : workingDir.listFiles()) {
            if (file.getName().contains(".bin")){
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                    ObjectInputStream objectInputStream = new ObjectInputStream(bufferedInputStream);
                    State state = (State) objectInputStream.readObject();
                    list.add(state);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    private void clearData() {
        for (File file : savedStateFiles()) {
            if (file.getName().contains(".bin")){
                file.delete();
            }
        }
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
    private Date stateTime;

    public State(ArrayList<Campaign> listOfCampaigns,
                 ArrayList<Vehicle> listOfVehicles,
                 Route[] stdRoutes,
                 Street[] stdStreet,
                 Date allArrivedToArafatTime,
                 Date allArrivedToHotelsTime,
                 Date stateTime) {
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
        this.stateTime = stateTime;
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

	public Date getStateTime() {
		return stateTime;
	}
    
}
