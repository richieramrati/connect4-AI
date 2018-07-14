/**
 * @author Richie
 */
public class SmartEval implements EvalHeuristic {

	/*
	 * (non-Javadoc)
	 * @see scoreHeuristic#h(ConnectFourBoard, GamePiece)
	 */
	@Override
	public double h ( ConnectFourBoard board, GamePiece player ) {
		String[][] state = init(board);
		state = boardToStringArray(board,state);
		int score = rate(board,player,state);
		return score;

	}

	/**
	 * @param state
	 * @return
	 */
	private String[][] init ( ConnectFourBoard board ) {
		int row = board.getNumRows();
		int col = board.getNumCols();
		String[][] state = new String[row][col];
		for ( int i = 0 ; i < row ; i++ ) {
			for ( int j = 0 ; j < col ; j++ ) {
				state[i][j] = "e";
			}
		}
		return state;
	}

	public int rate ( ConnectFourBoard b, GamePiece p, String[][] state ) {
		int sum = 0;
		int score = 0;
		int playerScore = 0;
		int enemyScore = 0;
		String playerColor = p.getAbbrev();
		String enemyColor = recognizeEnemy(p);
		int col = b.getNumCols();
		int row = b.getNumRows();
		for ( int i = row - 1 ; i >= 0 ; i-- ) {
			for ( int j = 0 ; j < col ; j++ ) {

				if ( j <= col - 4 ) {
					for ( int k = j ; k <= j + 3 ; k++ ) {
						if ( state[i][k].equalsIgnoreCase(playerColor) ) {
							playerScore++;
						} else if ( state[i][k].equalsIgnoreCase(enemyColor) ) {
							enemyScore++;
						}
					}

					score = ratingSystem(playerScore,enemyScore);
					if ( score == 4096 || score == -4096 ) {
						return score;
					}
					sum = sum + score;
					playerScore = 0;
					enemyScore = 0;
				}

				if ( i >= 3 ) {
					for ( int k = i ; k >= i - 3 ; k-- ) {
						if ( state[k][j].equalsIgnoreCase(playerColor) ) {
							playerScore++;
						} else if ( state[k][j].equalsIgnoreCase(enemyColor) ) {
							enemyScore++;
						}
					}

					score = ratingSystem(playerScore,enemyScore);
					if ( score == 4096 || score == -4096 ) {
						return score;
					}
					sum = sum + score;
					playerScore = 0;
					enemyScore = 0;
				}

				if ( (i >= 3) && (j <= col - 4) ) {
					int h = j;
					for ( int k = i ; k >= i - 3 ; k-- ) {
						if ( state[k][h].equalsIgnoreCase(playerColor) ) {
							playerScore++;
						} else if ( state[k][h].equalsIgnoreCase(enemyColor) ) {
							enemyScore++;
						}
						h++;
					}

					score = ratingSystem(playerScore,enemyScore);
					if ( score == 4096 || score == -4096 ) {
						return score;
					}
					sum = sum + score;
					playerScore = 0;
					enemyScore = 0;
				}

				if ( (i >= 3) && (j >= 3) ) {
					int h = j;
					for ( int k = i ; k >= i - 3 ; k-- ) {
						if ( state[k][h].equalsIgnoreCase(playerColor) ) {
							playerScore++;
						} else if ( state[k][h].equalsIgnoreCase(enemyColor) ) {
							enemyScore++;
						}
						h--;
					}

					score = ratingSystem(playerScore,enemyScore);
					if ( score == 4096 || score == -4096 ) {
						return score;
					}
					sum = sum + score;
					playerScore = 0;
					enemyScore = 0;
				}
			}
		}
		return sum;
	}

	/**
	 * @param state
	 */
	private void printState ( String[][] state, ConnectFourBoard board ) {
		for ( int i = 0 ; i < board.getNumRows() ; i++ ) {
			for ( int j = 0 ; j < board.getNumCols() ; j++ ) {
				System.out.print(state[i][j]);
			}
			System.out.println();
		}
	}

	/**
	 * @param playerScore
	 * @param enemyScore
	 * @return
	 */
	private int ratingSystem ( int playerScore, int enemyScore ) {
		if ( playerScore == 4 ) {
			return 4096;
		} else if ( playerScore == 3 && enemyScore == 0 ) {
			return 32;
		} else if ( playerScore == 2 && enemyScore == 0 ) {
			return 4;
		} else if ( playerScore == 1 && enemyScore == 0 ) {
			return 1;
		} else if ( enemyScore == 4 ) {
			return -4096;
		} else if ( enemyScore == 3 && playerScore == 0 ) {
			return -32;
		} else if ( enemyScore == 2 && playerScore == 0 ) {
			return -4;
		} else if ( enemyScore == 1 && playerScore == 0 ) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * @param p
	 * @return
	 */
	private String recognizeEnemy ( GamePiece p ) {
		if ( p.getAbbrev().equalsIgnoreCase("y") ) {
			return "r";
		} else {
			return "y";
		}

	}

	public String[][] boardToStringArray ( ConnectFourBoard b,
	                                       String[][] state ) {
		char[] temp2 = new char[7];
		String[] temp0 = b.toString().trim().split("/");
		for ( int i = 0 ; i < temp0.length ; i++ ) {
			temp2 = temp0[i].trim().toCharArray();
			for ( int j = 0 ; j < temp2.length ; j++ ) {
				if ( j % 2 == 0 ) {
					String temp = String.valueOf(temp2[j]);
					if ( temp.equalsIgnoreCase("r") || temp.equalsIgnoreCase("y") ) {
						state[i][j / 2] = temp;
					}
				}
			}
		}
		return state;
	}

}
