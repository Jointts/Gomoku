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
        switch (direction){
            case "cols":
                List<Location> coordinates = bestCoordinate.getBestCoordinates();
                Location leftMostCoordinate = coordinates.get(0);
                Location rightMostCoordinate = coordinates.get(coordinates.size() - 1);
                if(board[leftMostCoordinate.getRow()][leftMostCoordinate.getColumn() - 1] == SimpleBoard.EMPTY){
                    return new LocationType(leftMostCoordinate.getRow(), leftMostCoordinate.getColumn() - 1, false);
                }
                if(board[rightMostCoordinate.getRow()][rightMostCoordinate.getColumn() + 1] == SimpleBoard.EMPTY){
                    return new LocationType(rightMostCoordinate.getRow(), rightMostCoordinate.getColumn() + 1, false);
                }
                return randomMove();
            default:
                return randomMove();
        }
    }

    private Location calculateMin(){
        BestCoordinate columnsBestCoordinates = evaluateColumns(board);
        //  To get the best in row score
        columnsBestCoordinates.getBestScore();
        //  To get the best in row possible move, random if not available
        return columnsBestCoordinates.getMove();
    }

    public int evaluateRows(){
        return 1;
    }

    private BestCoordinate evaluateColumns(int [][] board){
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