/**
 * A move - a game piece and the column where it is to be played.
 * 
 *
 */
public class Move {

	private GamePiece piece_;
	private int col_;

	/**
	 * @param piece
	 * @param col
	 */
	public Move ( GamePiece piece, int col ) {
		super();
		piece_ = piece;
		col_ = col;
	}

	/**
	 * @return the col
	 */
	public int getCol () {
		return col_;
	}

	/**
	 * @return the piece
	 */
	public GamePiece getPiece () {
		return piece_;
	}

}
