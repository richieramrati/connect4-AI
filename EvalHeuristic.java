/**
 * Board position evaluation heuristic.
 * 
 * */
public interface EvalHeuristic {

	/**
	 * Evaluate the board from the perspective of the player.
	 * 
	 * @param board
	 *          the game board
	 * @param player
	 *          the player
	 * @return the quality of the game board from the perspective of the player
	 */
	public double h ( ConnectFourBoard board, GamePiece player );

}
