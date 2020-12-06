import java.io.Serializable;

public class Route implements Travelable, Serializable {

    private Street[] streets;
    private District hotelArea;
    private Mashier mashier;

    public Route(Street[] streets, District hotelArea, Mashier mashier) {
        setStreets(streets);
        setHotelArea(hotelArea);
        setMashier(mashier);
    }

    public Route(Street street){
        this.streets = new Street[1];
        this.streets[0] = street;
    }

    public int indexOf(Street street){
        for (int i = 0; i < streets.length; i++) {
            if (street == streets[i]) return i;
        }
        return -1;
    }

    public Street[] getStreets() {
        return this.streets;
    }

    public double getTotalLength() {
        double totalLength = 0;
        for (Street st : streets){
            totalLength += st.getLength();
        }
        return totalLength;
    }

    public String getFastestTimeOfTravel(Vehicle vehicle) {
        double totalLength = getTotalLength();
        int maxSpeed = vehicle.getMaxSpeed();
        int totalTime = (int) (totalLength/maxSpeed);
        String result = String.format("%02d:%02d",totalTime / 60, totalTime % 60);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(super.toString())
                .append("\n").append(String.format("%s:%s",getHotelArea(),getMashier()))
                .append("\n")
                .append("Length: ").append(getTotalLength())
                .append("\n")
                .append("Streets: ");
        for (Street street : this.getStreets())
            s.append(street.getName().name()).append(" ");
        s.append("\nBest Time: ").append(getFastestTimeOfTravel(new Bus())).append("\n");
        return s.toString();
    }

    public District getHotelArea() {
        return hotelArea;
    }

    public Mashier getMashier() {
        return mashier;
    }

    private void setStreets(Street[] streets) {
        if (streets != null) this.streets = streets;
        else throwIllegal();
    }

    private void setHotelArea(District hotelArea) {
        if (hotelArea != null) this.hotelArea = hotelArea;
        else throwIllegal();
    }

    private void setMashier(Mashier mashier) {
        if (mashier != null) this.mashier = mashier;
        else throwIllegal();
    }

    private void throwIllegal() {
        throw new IllegalArgumentException();
    }
    
}
