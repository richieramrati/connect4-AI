import java.util.Random;

/**
 * A Connect Four player who chooses moves randomly.
 * 
 * */
public class RandomPlayer extends Player {

	private static Random random_ = new Random();

	/**
	 * Create a new player.
	 * 
	 * @param piece
	 *          piece the player will use
	 * @param timeout
	 *          maximum time allowed for player's turn
	 */
	public RandomPlayer ( GamePiece piece, long timeout ) {
		super(piece,timeout);
	}

	@Override
	public void chooseMove ( ConnectFourBoard board ) {
		reset();
		for ( ; move_ == null && !stop_ ; ) {
			int col = Math.abs(random_.nextInt(board.getNumCols()));
			if ( !board.isFull(col) ) {
				move_ = new Move(piece_,col);
			}
		}
		// we've been directed to stop
		if ( move_ == null ) {
			move_ = new Move(piece_,board.getLegalMove());
		}
	}
}
