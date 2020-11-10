import java.util.Date;

public class Accident {

    private Date date;
    private Breakable[] involvedCars;
    private Street location;
    //Add expext fixed by (Date) object.


    public Accident(Date date, Breakable[] involvedCars, Street location) {
        setDate(date);
        setInvovledCars(involvedCars);
        this.location = location;
    }

    private void setDate(Date date){
        this.date = date;
    }

    private void setInvovledCars(Breakable[] cars){
        if (cars.length > 1) this.involvedCars = cars;
        else throw new IllegalArgumentException("Can not make an accident with one car");
    }

    public Date getDate() {
        return date;
    }

    public Breakable[] getInvolvedCars() {
        return involvedCars;
    }

    public Street getLocation(){
        return this.location;
    }

    public int getTimeToFix(){
        int max = 0;
        for (Breakable car : involvedCars){
            if (car.getTimeToFix() > max) max = car.getTimeToFix();
        }
        return max;
    }

    public boolean isDone(){
        for (Breakable car : this.involvedCars){
            /*
            if broken flag is set and currAcc is ref to this instance
            then the accident is not done.
            The ref to accident should be null if fix() of car is invoked
             */
            if (car.isBroken() &&
                    ((CivilVehicle)car).getCurrentAccident() == this) {
                return false;
            }
        }
        return true;
    }
}
