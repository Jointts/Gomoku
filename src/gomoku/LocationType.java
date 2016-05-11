package gomoku;

/**
 * Created by pagulane on 11.05.16.
 */
public class LocationType{

    private Location coordinate;
    private boolean random;

    public LocationType(int x, int y, boolean random){
        this.coordinate = new Location(x, y);
        this.random = random;
    }

    public Location getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Location coordinate) {
        this.coordinate = coordinate;
    }

    public boolean isRandom() {
        return random;
    }

    public void setRandom(boolean random) {
        this.random = random;
    }
}
