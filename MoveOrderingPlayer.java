import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Richie
 */
public class MoveOrderingPlayer extends Player {
	private ArrayList<Integer> bestFound_ =
	    new ArrayList<Integer>(Arrays.asList(0,0,0,0,0,0,0));
	private int bestMove_ = 0;
	private SmartEval smartEval_ = new SmartEval();

	private int worstMove_ = 0;
	private int idealCol_;

	/**
	 * @param piece
	 * @param timeout
	 */
	protected MoveOrderingPlayer ( GamePiece piece, long timeout ) {
		super(piece,timeout);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * @see Player#chooseMove(ConnectFourBoard)
	 */
	@Override
	public void chooseMove ( ConnectFourBoard board ) {
		reset();
		minimax(board);
	}

	/**
	 * @param board
	 */
	private void minimax ( ConnectFourBoard board ) {
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;

		if ( !board.toString().contains("r") && !board.toString().contains("y") ) {
			move_ = new Move(piece_,3);
			return;
		}

		bestMove_ = maxValue(0,10,board,alpha,beta);
		move_ = new Move(piece_,idealCol_);
	}

	/**
	 * @param board
	 * @param beta
	 * @param alpha
	 * @return
	 */
	private int maxValue ( int currentDepth, int maxDepth, ConnectFourBoard board,
	                       int alpha, int beta ) {
		int max = 0;
		if ( stop_ ) {
			return bestMove_;
		}
		if ( currentDepth == maxDepth ) {
			max = (int) smartEval_.h(board,piece_);
		} else {
			max = Integer.MIN_VALUE;
			if ( currentDepth == 0 ) {
				for ( int i = 0 ; i < board.getNumCols() ; i++ ) {
					if ( !board.isFull(i) ) {
						try {
							board.drop(piece_,i);
						} catch ( GameRuleViolation e ) {
							e.printStackTrace();
						}
						max = Math
						    .max(max,
						         (minValue(currentDepth + 1,maxDepth,board,alpha,beta)));
						board.undrop(i);
						if ( max >= bestMove_ ) {
							idealCol_ = i;
							bestMove_ = max;
						}
						if ( max >= beta ) {
							idealCol_ = i;
							return max;
						}
						alpha = Math.max(max,alpha);
					}
				}
			} else {
				ArrayList<Integer> moveOrdering =
				    (ArrayList<Integer>) bestFound_.clone();
				Collections.sort(moveOrdering);
				for ( int i = 0 ; i < board.getNumCols() ; i++ ) {
					int x = bestFound_.indexOf(moveOrdering.get(i));
					if ( !board.isFull(x) ) {
						try {
							board.drop(piece_,x);
						} catch ( GameRuleViolation e ) {
							e.printStackTrace();
						}
						max = Math
						    .max(max,
						         (minValue(currentDepth + 1,maxDepth,board,alpha,beta)));
						board.undrop(x);
						if ( max >= bestMove_ ) {
							idealCol_ = x;
							bestMove_ = max;
						}
						if ( max >= beta ) {
							idealCol_ = x;
							return max;
						}
						alpha = Math.max(max,alpha);
					}
				}
			}
		}
		return max;
	}

	/**
	 * @param board
	 * @param beta
	 * @param alpha
	 * @return
	 */
	private int minValue ( int currentDepth, int maxDepth, ConnectFourBoard board,
	                       int alpha, int beta ) {
		int min = 0;
		if ( stop_ ) {
			return worstMove_;
		}
		if ( currentDepth >= maxDepth ) {
			min = (int) smartEval_.h(board,piece_.other());
		} else {
			min = Integer.MAX_VALUE;
			for ( int i = 0 ; i < board.getNumCols() ; i++ ) {
				if ( !board.isFull(i) ) {
					try {
						board.drop(piece_.other(),i);
					} catch ( GameRuleViolation e ) {
						e.printStackTrace();
					}
					min = Math.min(min,
					               maxValue(currentDepth + 1,maxDepth,board,alpha,beta));
					board.undrop(i);
					worstMove_ = Math.min(worstMove_,min);
					if ( min <= alpha ) {
						return min;
					}
					beta = Math.min(min,beta);
				}
			}
		}
		return min;
	}
}