package gomoku;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pagulane on 11.05.16.
 */
public class BestCoordinate {

    private List<Location> bestCoordinates = new ArrayList<>();
    private int bestScore;
    private Location move;

    public BestCoordinate(List<Location> bestCoordinates, int bestScore){
        this.bestCoordinates = bestCoordinates;
        this.bestScore = bestScore;
    }

    public List<Location> getBestCoordinates() {
        return bestCoordinates;
    }

    public void setBestCoordinates(List<Location> bestCoordinates) {
        this.bestCoordinates = bestCoordinates;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public Location getMove() {
        return move;
    }

    public void setMove(Location move) {
        this.move = move;
    }
}
