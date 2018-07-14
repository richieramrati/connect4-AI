
/**
 * @author Richie
 */
public class SimpleEval implements EvalHeuristic {

	/*
	 * (non-Javadoc)
	 * @see EvalHeuristic#h(ConnectFourBoard, GamePiece)
	 */
	@Override
	public double h ( ConnectFourBoard board, GamePiece player ) {
		if ( board.getWinner().equals(player) ) {
			return 1;
		} else {
			return -1;
		}
	}

}
