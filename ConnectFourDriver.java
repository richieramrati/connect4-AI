/**
 * Game logic for a game of Connect Four, with time limits on each player's
 * move.
 * 
 * */
public class ConnectFourDriver {

	class TurnRunner implements Runnable {

		private Player player_;
		private ConnectFourBoard board_;

		/**
		 * @param player
		 * @param board
		 */
		TurnRunner ( Player player, ConnectFourBoard board ) {
			super();
			player_ = player;
			board_ = board;
		}

		@Override
		public void run () {
			player_.chooseMove(board_);
		}

	}

	/**
	 * Play one game of ConnectFour using the specified board. The game proceeds
	 * from whatever state the provided game board is in (clear it first, if
	 * desired), and the board is left in whatever end-of-game state it ends up in
	 * when playGame returns.
	 * 
	 * @param player1
	 *          player 1
	 * @param player2
	 *          player 2
	 * @param board
	 *          the game board
	 * @return the winner, or null if the game ended in a draw
	 * @throws GameRuleViolation
	 *           if a move is attempted that violates the game rules
	 * @throws InterruptedException
	 *           if this thread is interrupted
	 */
	public Player playGame ( Player player1, Player player2,
	                         ConnectFourBoard board )
	    throws GameRuleViolation, InterruptedException {
		Player[] players = { player1, player2 };
		for ( int curplayer = 0 ; true ; curplayer = (curplayer + 1) % 2 ) {
			// check for game over
			if ( board.isFull() ) {
				return null;
			}
			GamePiece winner = board.getWinner();
			if ( winner != GamePiece.NONE ) {
				for ( Player player : players ) {
					if ( player.isMine(winner) ) {
						return player;
					}
				}
			}

			// take turn
			Thread turn = new Thread(new TurnRunner(players[curplayer],board));
			turn.start();
			// wait for the player to move, but only up to a time limit
			turn.join(players[curplayer].getTimeout());
			// player's turn is done
			// System.out.println("requesting stop");
			players[curplayer].stop();
			turn.interrupt();
			// wait for player to finish stopping
			turn.join();
			// get the player's move
			Move move = players[curplayer].getMove();
			if ( move == null ) {
				throw new IllegalStateException("player " + players[curplayer].getName()
				    + " did not make a move");
			}
			board.drop(move.getPiece(),move.getCol());
		}
	}

}
