import java.util.Arrays;
import java.util.Observable;

/**
 * A game board for playing ConnectFour.
 * 
 * */
public class ConnectFourBoard extends Observable {

	private int rows_, cols_; // board dimensions
	private GamePiece[] board_; // row-major order, from the bottom
	private int numplayed_; // number of game pieces on the board

	/**
	 * Create an empty board with the specified dimensions.
	 * 
	 * @param rows
	 *          number of rows (>= 1)
	 * @param cols
	 *          number of columns (>= 1)
	 */
	public ConnectFourBoard ( int rows, int cols ) {
		super();

		if ( rows < 1 || cols < 1 ) {
			throw new IllegalArgumentException("rows and cols must be >= 1");
		}

		rows_ = rows;
		cols_ = cols;
		board_ = new GamePiece[rows * cols];
		clear();
	}

	/**
	 * Clear the game board.
	 */
	public void clear () {
		for ( int ctr = 0 ; ctr < board_.length ; ctr++ ) {
			board_[ctr] = GamePiece.NONE;
		}
		numplayed_ = 0;
	}

	/**
	 * Copy the board.
	 * 
	 * @return a copy of the game board
	 */
	public ConnectFourBoard copy () {
		ConnectFourBoard copy = new ConnectFourBoard(rows_,cols_);
		System.arraycopy(board_,0,copy.board_,0,rows_ * cols_);
		return copy;
	}

	/**
	 * Play a piece - drop the specified piece in the specified column.
	 * 
	 * @param piece
	 *          piece to drop (must be an actual piece, not null or
	 *          GamePiece.NONE)
	 * @param col
	 *          column to drop piece in (0 <= col < board.getNumCols())
	 * @throws GameRuleViolation
	 *           if the specified column is full
	 */
	public void drop ( GamePiece piece, int col ) throws GameRuleViolation {
		if ( piece == null ) {
			throw new IllegalArgumentException("piece cannot be null");
		}
		if ( piece == GamePiece.NONE ) {
			throw new IllegalArgumentException("piece cannot be NONE");
		}
		if ( col < 0 || col >= cols_ ) {
			throw new IllegalArgumentException("invalid column " + col
			    + "; must be >= 0 and < " + cols_);
		}
		if ( board_[(rows_ - 1) * cols_ + col] != GamePiece.NONE ) {
			throw new GameRuleViolation("column " + col + " is full");
		}

		for ( int row = 0 ; row < rows_ ; row++ ) {
			if ( board_[row * cols_ + col] == GamePiece.NONE ) {
				board_[row * cols_ + col] = piece;
				numplayed_++;
				setChanged();
				break;
			}
		}
		notifyObservers();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals ( Object obj ) {
		if ( this == obj ) return true;
		if ( obj == null ) return false;
		if ( getClass() != obj.getClass() ) return false;
		ConnectFourBoard other = (ConnectFourBoard) obj;
		if ( !Arrays.equals(board_,other.board_) ) return false;
		if ( cols_ != other.cols_ ) return false;
		if ( rows_ != other.rows_ ) return false;
		return true;
	}

	private GamePiece getColWinner ( int col ) {
		GamePiece last = GamePiece.NONE;
		int count = 0;
		// shortcut checking when there's not enough room left in the column to get
		// four in a row
		for ( int r = 0 ; r < rows_ - (4 - count) + 1 ; r++ ) {
			if ( board_[r * cols_ + col] == GamePiece.NONE ) {
				// have reached top of the filled part of the column without finding
				// four in a row
				return GamePiece.NONE;
			} else if ( board_[r * cols_ + col] == last ) {
				count++;
				if ( count == 4 ) {
					return last;
				}
			} else {
				last = board_[r * cols_ + col];
				count = 1;
			}
		}
		// have reached top of the column without finding four in a row
		return GamePiece.NONE;
	}

	private GamePiece getDiagWinner ( int row, int col, int coldir ) {
		GamePiece last = GamePiece.NONE;
		int count = 0;
		// shortcut checking when there's not enough room left on the diagonal to
		// get four in a row
		for ( int r = row, c = col ; r < rows_ - (4 - count) + 1
		    && ((coldir > 0 && c < cols_ - (4 - count) + 1)
		        || (coldir < 0 && c >= (4 - count) - 1)) ; r++, c += coldir ) {
			if ( board_[r * cols_ + c] == GamePiece.NONE ) {
				last = GamePiece.NONE;
				count = 0;
			} else if ( board_[r * cols_ + c] == last ) {
				count++;
				if ( count == 4 ) {
					return last;
				}
			} else {
				last = board_[r * cols_ + c];
				count = 1;
			}
		}
		// have reached end of row without finding four in a row
		return GamePiece.NONE;
	}

	/**
	 * Get a legal move.
	 * 
	 * @return column number for a legal move
	 * @throws InvalidStateException
	 *           if there are no legal moves possible (i.e. board is full)
	 */
	public int getLegalMove () {
		for ( int col = 0 ; col < cols_ ; col++ ) {
			if ( !isFull(col) ) {
				return col;
			}
		}
		throw new InvalidStateException("board is full - no legal moves");
	}

	/**
	 * Get the number of columns in the board.
	 * 
	 * @return number of columns in board
	 */
	public int getNumCols () {
		return cols_;
	}

	/**
	 * Get the number of rows in the board.
	 * 
	 * @return number of rows in board
	 */
	public int getNumRows () {
		return rows_;
	}

	/**
	 * Retrieve the piece at the specified location. Row 0 is the bottom row of
	 * the game board.
	 * 
	 * @param row
	 *          0 <= row < board.getNumRows()
	 * @param col
	 *          0 <= col < board.getNumCols()
	 * @return game piece
	 */
	public GamePiece getPiece ( int row, int col ) {
		if ( row < 0 || row >= rows_ ) {
			throw new IllegalArgumentException("invalid row " + row
			    + "; must be >= 0 and < " + rows_);
		}
		if ( col < 0 || col >= cols_ ) {
			throw new IllegalArgumentException("invalid column " + col
			    + "; must be >= 0 and < " + cols_);
		}

		return board_[row * cols_ + col];
	}

	private GamePiece getRowWinner ( int row ) {
		GamePiece last = GamePiece.NONE;
		int count = 0;
		// shortcut checking when there's not enough room left in the row to get
		// four in a row
		for ( int c = 0 ; c < cols_ - (4 - count) + 1 ; c++ ) {
			if ( board_[row * cols_ + c] == GamePiece.NONE ) {
				last = GamePiece.NONE;
				count = 0;
			} else if ( board_[row * cols_ + c] == last ) {
				count++;
				if ( count == 4 ) {
					return last;
				}
			} else {
				last = board_[row * cols_ + c];
				count = 1;
			}
		}
		// have reached end of row without finding four in a row
		return GamePiece.NONE;
	}

	/**
	 * Determine if someone has won the game. Note that this is <i>not</i> the
	 * same as testing for game over - it will return false both if there is a
	 * draw, and if the game is still in progress.
	 * 
	 * @return true if there is a winner, false if not
	 */
	public GamePiece getWinner () {
		GamePiece winner = GamePiece.NONE;
		for ( int col = 0 ; col < cols_ ; col++ ) {
			winner = getColWinner(col);
			if ( winner != GamePiece.NONE ) {
				return winner;
			}
			if ( col <= cols_ - 4 ) {
				winner = getDiagWinner(0,col,1);
				if ( winner != GamePiece.NONE ) {
					return winner;
				}
			}
			if ( col >= cols_ - 4 ) {
				winner = getDiagWinner(0,col,-1);
				if ( winner != GamePiece.NONE ) {
					return winner;
				}
			}
		}
		for ( int row = 0 ; row < rows_ ; row++ ) {
			winner = getRowWinner(row);
			if ( winner != GamePiece.NONE ) {
				return winner;
			}
			if ( row <= rows_ - 4 ) {
				winner = getDiagWinner(row,0,1);
				if ( winner != GamePiece.NONE ) {
					return winner;
				}
				winner = getDiagWinner(row,cols_ - 1,-1);
				if ( winner != GamePiece.NONE ) {
					return winner;
				}
			}
		}
		return GamePiece.NONE;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode () {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(board_);
		result = prime * result + cols_;
		result = prime * result + rows_;
		return result;
	}

	/**
	 * Is the board full?
	 * 
	 * @return true if the board is filled with pieces, false otherwise
	 */
	public boolean isFull () {
		return numplayed_ == rows_ * cols_;
	}

	/**
	 * Is the column full?
	 * 
	 * @return true if the specified column is full, false otherwise
	 */
	public boolean isFull ( int col ) {
		return board_[(rows_ - 1) * cols_ + col] != GamePiece.NONE;
	}

	/**
	 * Print out the board.
	 */
	public void print () {
		for ( int row = rows_ - 1 ; row >= 0 ; row-- ) {
			for ( int col = 0 ; col < cols_ ; col++ ) {
				System.out.print((col == 0 ? "" : " ")
				    + board_[row * cols_ + col].getAbbrev());
			}
			System.out.println();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString () {
		StringBuffer buffer = new StringBuffer();
		for ( int row = rows_ - 1 ; row >= 0 ; row-- ) {
			for ( int col = 0 ; col < cols_ ; col++ ) {
				buffer.append((col == 0 ? "" : " ")
				    + board_[row * cols_ + col].getAbbrev());
			}
			buffer.append(" / ");
		}
		return buffer.toString();
	}

	/**
	 * Remove the top piece from the specified column.
	 * 
	 * @param col
	 *          column (0 <= col < board.getNumCols())
	 * @exception InvalidStateException
	 *              if the column is empty
	 */
	public void undrop ( int col ) {
		if ( col < 0 || col >= cols_ ) {
			throw new IllegalArgumentException("invalid column " + col
			    + "; must be >= 0 and < " + cols_);
		}
		if ( board_[col] == GamePiece.NONE ) {
			throw new InvalidStateException("column " + col + " is empty");
		}

		for ( int row = rows_ - 1 ; row >= 0 ; row-- ) {
			if ( board_[row * cols_ + col] != GamePiece.NONE ) {
				board_[row * cols_ + col] = GamePiece.NONE;
				numplayed_--;
				setChanged();
				break;
			}
		}

		notifyObservers();
	}
}
