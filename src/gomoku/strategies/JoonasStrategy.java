package gomoku.strategies;

import gomoku.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoonasStrategy implements ComputerStrategy {

    private int[][] board;

    @Override
    public Location getMove(SimpleBoard board, int player) {
        // let's operate on 2-d array
        //  lets not, i dont like arrays
        this.board = board.getBoard();
        return calculateMin();
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

    private Location calculateMin(){
        BestCoordinate columnsBestCoordinates = evaluateColumns();
        BestCoordinate rowsBestCoordinates = evaluateRows();
        //  To get the best in row score
        rowsBestCoordinates.getBestScore();
        //  To get the best in row possible move, random if not available
        return rowsBestCoordinates.getMove();
    }

    private BestCoordinate evaluateRows(){
        int highest = 0;
        Location move = new Location(0, 0);
        List<Location> highestCoordinates = new ArrayList<>();
        List<Location> currentCoordinates = new ArrayList<>();
        int score = 0;
        for (int col = 0; col < board.length; col++) {
            for (int row = 0; row < board[0].length; row++) {
                if (board[row][col] == SimpleBoard.PLAYER_WHITE) {
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

    private BestCoordinate evaluateColumns(){
        int highest = 0;
        Location move = new Location(0, 0);
        List<Location> highestCoordinates = new ArrayList<>();
        List<Location> currentCoordinates = new ArrayList<>();
        int score = 0;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col] == SimpleBoard.PLAYER_WHITE) {
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
        return "Dummy strategy";
    }

}