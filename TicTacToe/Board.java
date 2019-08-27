package TicTacToe;

import java.util.*;

class Point {
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "[" + (x+1) + ", " + (y+1) + "]";
    }
}

class PointsAndScores {
    int score;
    Point point;

    PointsAndScores(int score, Point point) {
        this.score = score;
        this.point = point;
    }
}

class Board { 
    List<Point> availablePoints;
    Scanner scan = new Scanner(System.in);
    int[][] board = new int[3][3];

    public Board() {
    }

    //checks whether the game has been won by a player or if there is a draw
    public boolean isGameOver() {
        return (hasXWon() || hasOWon() || getAvailablePoints().isEmpty());
    }

    //checks all the instances where the AI player would win
    public boolean hasXWon() {
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 1) || (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 1)) {
            return true;
        }
        for (int i = 0; i < 3; ++i) {
            if (((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 1)
                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 1))) {
                return true;
            }
        }
        return false;
    }

    //checks all the instances where the opponent player would win
    public boolean hasOWon() {
        if ((board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] == 2) || (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] == 2)) {
            return true;
        }
        for (int i = 0; i < 3; ++i) {
            if ((board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] == 2)
                    || (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] == 2)) {
                return true;
            }
        }
        return false;
    }

    //points on the board currently free
    public List<Point> getAvailablePoints() {
        availablePoints = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (board[i][j] == 0) {
                    availablePoints.add(new Point(i, j));
                }
            }
        }
        return availablePoints;
    }
    
    //return whether the point is taken with the given point argument
    public int getState(Point point){
    	return board[point.x][point.y];
    }

    //add a point to the board
    public void placeAMove(Point point, int player) {
        board[point.x][point.y] = player;   
    }

    //print out the board with either noughts, crosses or dots
    public void displayBoard() {
        System.out.println();

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
 		if (board[i][j]==1)           
                    System.out.print("X ");
                else if (board[i][j]==2)
                    System.out.print("O ");
                else
                    System.out.print(". ");
            }
            System.out.println();
        }
    }

    Point AIPlayer; 
    
    public int minimax(int depth, int turn) {  
        //if AIPlayer wins, return +1 (win)
    	if (hasXWon()) 
        	return +1; 
        
        //if opponent wins, return -1 (loss)
        if (hasOWon()) 
        	return -1;

        List<Point> pointsAvailable = getAvailablePoints();
        
        //if there are no more available points on the board return a 0 (draw) 
        if (pointsAvailable.isEmpty()) 
        	return 0; 
 
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
         
        for (int i = 0; i < pointsAvailable.size(); ++i) {  
            Point point = pointsAvailable.get(i); 
            
            //When it is the AI players turn. Place a move, to attempt to maximize the opponents turn.
            if (turn == 1) { 
            	
            	//place a move for the AI player
                placeAMove(point, 1); 
                
                //use a recursive function to form a search tree of the possible moves, which could lead to either a win or a loss.
                int avaliablePoint = minimax(depth + 1, 2);
                max = Math.max(avaliablePoint, max);
                
                //if the depth of the search tree is 0 and the available point is greater than 0, place a point for the AI player. 
                if(avaliablePoint >= 0 && depth == 0)
                	AIPlayer = point;

                //if the available point is 1, change the point in the current position to 0 and break out of the FOR loop.
                if(avaliablePoint == 1)
                {
                	board[point.x][point.y] = 0; 
                	break;
                } 
                
                //if the FOR loop is at the last available point on the board and the max is less than zero, place a point for the AI player.
                if((i == pointsAvailable.size()-1 && max < 0) && depth == 0)
                	AIPlayer = point;

                
            } 
            
            //When it is the oppenent's turn. Place a move, to attempt to minimise the AI player's turn.
            else if (turn == 2) {
            	//place a move for the opponent
                placeAMove(point, 2); 
                
                //use a recursive function to form a search tree of the possible moves, which could lead to either a win or a loss.
                int avaliablePoint = minimax(depth + 1, 1);
                min = Math.min(avaliablePoint, min); 
                
              //if the minimum is -1 place a point, to minimise the AI Players move and break out of the FOR loop.
                if(min == -1){
                	board[point.x][point.y] = 0;
                	break;
                }
            }
            board[point.x][point.y] = 0; //Reset the point, so it can be used again without holding previous values.
        } 
        
        //if its the AI Players turn, return the max. Else, return the min.
        if (turn == 1)
        	return max;
        else 
        	return min;
    } 

}
