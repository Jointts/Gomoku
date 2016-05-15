package gomoku.strategies;

import gomoku.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static gomoku.SimpleBoard.PLAYER_WHITE;

public class JoonasStrategy implements ComputerStrategy {

    private int[][] board;
    private int players;
    private boolean initalized = false;
    private int opponent;
    private int me;

    @Override
    public Location getMove(SimpleBoard board, int player) {
        // let's operate on 2-d array
        //  lets not, i dont like arrays
        this.board = board.getBoard();

        if(!initalized){
            getPlayerTiles();
            initalized = true;
        }
        if(calculateMin().getBestScore() > calculateMax().getBestScore()){
            return calculateMin().getMove();
        }
        return calculateMax().getMove();
    }

    private void getPlayerTiles(){
        for(int x = 0; x < board[0].length; x++){
            for(int y = 0; y < board[0].length; y++){
                if(board[x][y] == SimpleBoard.PLAYER_WHITE){
                    opponent = SimpleBoard.PLAYER_WHITE;
                    me = SimpleBoard.PLAYER_BLACK;
                    return;
                }
            }
        }
        me = SimpleBoard.PLAYER_WHITE;
        opponent = SimpleBoard.PLAYER_BLACK;
    }

    private LocationType checkPossibleMoves(BestCoordinate bestCoordinate, String direction){
        List<Location> coordinates = bestCoordinate.getBestCoordinates();
        Location firstCoordinate = coordinates.get(0);
        Location lastCoordinate = coordinates.get(coordinates.size() - 1);
        switch (direction){
            case "cols":
                try{
                    if(board[firstCoordinate.getRow()][firstCoordinate.getColumn() - 1] == SimpleBoard.EMPTY){
                        return new LocationType(firstCoordinate.getRow(), firstCoordinate.getColumn() - 1, false);
                    }
                    if(board[lastCoordinate.getRow()][lastCoordinate.getColumn() + 1] == SimpleBoard.EMPTY){
                        return new LocationType(lastCoordinate.getRow(), lastCoordinate.getColumn() + 1, false);
                    }
                }catch(ArrayIndexOutOfBoundsException e){
                    return randomMove();
                }
                return randomMove();
            case "rows":
                try{
                    if(board[firstCoordinate.getRow() - 1][firstCoordinate.getColumn()] == SimpleBoard.EMPTY){
                        return new LocationType(firstCoordinate.getRow() - 1, firstCoordinate.getColumn(), false);
                    }
                    if(board[lastCoordinate.getRow() + 1][lastCoordinate.getColumn()] == SimpleBoard.EMPTY){
                        return new LocationType(lastCoordinate.getRow() + 1, lastCoordinate.getColumn(), false);
                    }
                }catch(ArrayIndexOutOfBoundsException e){
                    return randomMove();
                }
                return randomMove();
            default:
                return randomMove();
        }
    }

    public int checkButton(int player) {
        if(PLAYER_WHITE == -player) {
            randomMove();
        }
        if(SimpleBoard.PLAYER_BLACK == player) {
            randomMove();
        }
        return 0;
    }

    private BestCoordinate calculateMin(){
        BestCoordinate columnsBestCoordinates = evaluateColumns(opponent);
        BestCoordinate rowsBestCoordinates = evaluateRows(opponent);
        //  To get the best in row score
        //  To get the best in row possible move, random if not available
        if(columnsBestCoordinates.getBestScore() > rowsBestCoordinates.getBestScore()){
            return columnsBestCoordinates;
        }else{
            return rowsBestCoordinates;
        }
    }

    private BestCoordinate calculateMax(){
        BestCoordinate columnsBestCoordinates = evaluateColumns(me);
        BestCoordinate rowsBestCoordinates = evaluateRows(me);
        //  To get the best in row score
        //  To get the best in row possible move, random if not available
        if(columnsBestCoordinates.getBestScore() > rowsBestCoordinates.getBestScore()){
            return columnsBestCoordinates;
        }else{
            return rowsBestCoordinates;
        }
    }

    private BestCoordinate evaluateRows(int player){
        int highest = 0;
        Location move = new Location(0, 0);
        List<Location> highestCoordinates = new ArrayList<>();
        List<Location> currentCoordinates = new ArrayList<>();
        int score = 0;
        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board[0].length; row++) {
                if (board[row][col] == player) {
                    score+=1;
                    currentCoordinates.add(new Location(row, col));
                    System.out.println("Score is " + score);
                }else{
                    currentCoordinates = new ArrayList<>();
                    score = 0;
                }
                if(score > highest){
                    BestCoordinate coordinateToCheck = new BestCoordinate(currentCoordinates, score);
                    LocationType customLocation = checkPossibleMoves(coordinateToCheck, "rows");
                    if(!customLocation.isRandom()){
                        highestCoordinates = currentCoordinates;
                        highest = score;
                        move = customLocation.getCoordinate();
                    }
                }
            }
        }
        BestCoordinate bestCoordinate = new BestCoordinate(highestCoordinates, highest);
        bestCoordinate.setMove(move);
        return bestCoordinate;
    }

    private BestCoordinate evaluateColumns(int player){
        int highest = 0;
        Location move = new Location(0, 0);
        List<Location> highestCoordinates = new ArrayList<>();
        List<Location> currentCoordinates = new ArrayList<>();
        int score = 0;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col] == player) {
                    score+=1;
                    currentCoordinates.add(new Location(row, col));
                    System.out.println("Score is " + score);
                }else{
                    currentCoordinates = new ArrayList<>();
                    score = 0;
                }
                if(score > highest){
                    BestCoordinate coordinateToCheck = new BestCoordinate(currentCoordinates, score);
                    LocationType customLocation = checkPossibleMoves(coordinateToCheck, "cols");
                    if(!customLocation.isRandom()){
                        highestCoordinates = currentCoordinates;
                        highest = score;
                        move = customLocation.getCoordinate();
                    }
                }
            }
        }
        BestCoordinate bestCoordinate = new BestCoordinate(highestCoordinates, highest);
        bestCoordinate.setMove(move);
        return bestCoordinate;
    }

    public int evaluateDiagonals(){
        return 1;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public LocationType randomMove(){
        board = getBoard();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col] == SimpleBoard.EMPTY) {
                    // first empty location
                    return new LocationType(row, col, true);
                }
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return "Joonas strategy";
    }

}

class LocationType{

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

class BestCoordinate {

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