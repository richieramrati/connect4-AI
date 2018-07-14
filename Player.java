/**
 * A Connect Four player. Includes functionality for limiting the length of the
 * player's turn.
 * 
 * @author ssb
 */

public abstract class Player {

	protected GamePiece piece_;
	protected Move move_;
	protected long timeout_;
	protected boolean stop_;

	/**
	 * Create a player.
	 * 
	 * @param piece
	 *          the game piece the player is to play with
	 * @param timeout
	 *          the maximum time (in ms) that the player is allowed per turn
	 */
	protected Player ( GamePiece piece, long timeout ) {
		piece_ = piece;
		move_ = null;
		timeout_ = timeout;
		stop_ = false;
	}

	/**
	 * Choose the next move. The move is stored so that a subsequent getMove()
	 * will return it. The board is not changed.
	 * 
	 * @param board
	 *          the board on which the move is to be made
	 */
	public abstract void chooseMove ( ConnectFourBoard board );

	/**
	 * Retrieve the selected move.
	 * 
	 * @return the move selected by the most recent chooseMove
	 */
	public Move getMove () {
		return move_;
	}

	/**
	 * Get the player's name.
	 * 
	 * @return a name identifying this player
	 */
	public String getName () {
		return getClass().getName() + "-" + piece_.getAbbrev();
	}

	/**
	 * Get the player's piece.
	 * 
	 * @return the player's piece
	 */
	public GamePiece getPiece () {
		return piece_;
	}

	/**
	 * Get the player's allowed time per turn.
	 * 
	 * @return the timeout (in ms)
	 */
	public long getTimeout () {
		return timeout_;
	}

	/**
	 * Does the specified piece belong to the player?
	 * 
	 * @param piece
	 *          the piece
	 * @return true if it belongs to this player, false otherwise
	 */
	public boolean isMine ( GamePiece piece ) {
		return piece_ == piece;
	}

	/**
	 * Reset turn-taking things in preparation for choosing a new move.
	 */
	protected void reset () {
		stop_ = false;
		move_ = null;
	}

	/**
	 * Signal that the player should abort their move selection.
	 */
	public void stop () {
		stop_ = true;
	}

}
