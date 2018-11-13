package client.game;

import client.Point;

public class Game {
  private Boolean turn;
  private GameBoard gameBoard;

  private static final String startingBoard = "rcCrcDrcErdCkdDrdEreCreDreERhHRhIRhJRiHKiIRiJRjHRjIRjJ";

  /**
   * Creates a new Game with the default starting board and Whites getTurn.
   */
  public Game() {
    this(startingBoard, false);
  }

  /**
   * Creates a Game from the given board string and getTurn.
   * @param board A string representation of a game board.
   * @param turn Which players getTurn it is. False for black, True for white.
   */
  public Game(String board, boolean turn){
    this.gameBoard = new GameBoard(board);
    this.turn = turn;
  }

  /**
   * Which players turn it is.
   * @return True for black, False for white.
   */
  public Boolean getTurn(){
    return turn;
  }

  /**
   * Moves a Piece at from to the space to, If that is a valid move for that piece.
   * @param from String representation of the space to move a piece from.
   * @param to String representation of the space to move a piece to.
   * @return True if the piece was moved, false otherwise.
   */
  public Boolean move(String from, String to){
    if(gameBoard.MovePiece(new Point(from), new Point(to), turn)){
      this.turn = !turn;
      return true;
    }
    return false;
  }

  /**
   * Gets the set of valid moves for a given location.
   * @param location The String representation of the tile to get valid moves for.
   * @return A String representation of the set of valid locations to move to.
   */
  public String validMoves(String location){
    return gameBoard.getMoves(new Point(location), turn);
  }

  /**
   * Gets the String representation of the current GameBoard.
   * @return String representation of the current GameBoard.
   */
  public String getBoard(){
    return gameBoard.getBoard();
  }
}
