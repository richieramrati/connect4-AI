
/**
 * @author Richie
 */
public class MinimaxPlayer extends Player {

	private int bestMove_ = 0;
	private SimpleEval simpleEval_ = new SimpleEval();
	private int idealCol_;
	private int worstMove_;

	/**
	 * @param piece
	 * @param timeout
	 */
	protected MinimaxPlayer ( GamePiece piece, long timeout ) {
		super(piece,timeout);
	}

	/*
	 * (non-Javadoc)
	 * @see Player#chooseMove(ConnectFourBoard)
	 */
	@Override
	public void chooseMove ( ConnectFourBoard board ) {
		reset();
		for ( ; move_ == null && !stop_ ; ) {
			minimax(board);
		}

	}

	/**
	 * @param board
	 */
	private void minimax ( ConnectFourBoard board ) {
		bestMove_ = maxValue(0,8,board);
		move_ = new Move(piece_,idealCol_);
	}

	/**
	 * @param board
	 * @return
	 */
	private int maxValue ( int currentDepth, int maxDepth,
	                       ConnectFourBoard board ) {
		int max = 0;
		if ( stop_ ) {
			return bestMove_;
		}
		if ( currentDepth >= maxDepth ) {
			max = (int) simpleEval_.h(board,piece_);
		} else {
			max = Integer.MIN_VALUE;
			for ( int i = 0 ; i < board.getNumCols() ; i++ ) {
				if ( !board.isFull(i) ) {
					try {
						board.drop(piece_,i);
					} catch ( GameRuleViolation e ) {
						e.printStackTrace();
					}
					max = Math.max(max,(minValue(currentDepth + 1,maxDepth,board)));
					board.undrop(i);
					if ( max > bestMove_ ) {
						bestMove_ = max;
						idealCol_ = i;
					}
				}
			}
		}
		return max;
	}

	/**
	 * @param board
	 * @return
	 */
	private int minValue ( int currentDepth, int maxDepth,
	                       ConnectFourBoard board ) {
		int min = 0;
		if ( stop_ ) {
			return worstMove_;
		}
		if ( currentDepth >= maxDepth ) {
			min = (int) simpleEval_.h(board,piece_.other());
		} else {
			min = Integer.MAX_VALUE;
			for ( int i = 0 ; i < board.getNumCols() ; i++ ) {
				if ( !board.isFull(i) ) {
					try {
						board.drop(piece_.other(),i);
					} catch ( GameRuleViolation e ) {
						e.printStackTrace();
					}
					min = Math.min(min,maxValue(currentDepth + 1,maxDepth,board));
					board.undrop(i);
					if ( worstMove_ >= min ) {
						worstMove_ = min;
					}
				}
			}
		}
		return min;
	}
}
