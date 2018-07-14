/**
 * Human player. The player's move is specified by clicking on the desired
 * column on the game board.
 */
public class HumanPlayer extends Player {

	private ConnectFourGUI gui_;

	/**
	 * Create a human player.
	 * 
	 * @param piece
	 *          the game piece the player will use
	 * @param gui
	 *          the GUI for getting the player's move
	 */
	public HumanPlayer ( GamePiece piece, ConnectFourGUI gui ) {
		super(piece,Long.MAX_VALUE);
		gui_ = gui;
	}

	@Override
	public void chooseMove ( ConnectFourBoard board ) {
		System.out.println(getName() + ", it's your turn");
		reset();
		for ( ; move_ == null && !stop_ ; ) {
			int col = gui_.getMove();
			if ( col != -1 && !board.isFull(col) ) {
				move_ = new Move(piece_,col);
			} else {
				if ( board.isFull(col) ) {
					System.out.println("column is full; choose another");
				}
			}
		}
		// we were directed to stop before the player made a move
		if ( move_ == null ) {
			System.out.println("no move made; using default move");
			move_ = new Move(piece_,board.getLegalMove());
		}
	}

}
