/**
 * Play a bunch of games of Connect Four (without a GUI) and track win/loss/draw
 * statistics.
 * 
 * @author ssb
 */

public class ConnectFourPounder {

	/**
	 * @param args
	 */
	public static void main ( String[] args ) {
		ConnectFourBoard board = new ConnectFourBoard(6,7);
		ConnectFourDriver driver = new ConnectFourDriver();

		Player player1 = new RandomPlayer(GamePiece.YELLOW,1000);
		Player player2 = new RandomPlayer(GamePiece.RED,1000);

		// number of wins for each player, and draws
		int win1 = 0, win2 = 0, draw = 0;

		for ( int rep = 1 ; rep <= 1000 ; rep++ ) {
			try {
				board.clear();
				Player winner = driver.playGame(player1,player2,board);
				// board.print();
				if ( winner == null ) {
					System.out.println("game is a draw!");
					draw++;
				} else {
					System.out.println("player " + winner.getName() + " wins!");
					if ( winner == player1 ) {
						win1++;
					} else {
						win2++;
					}
				}
				System.out.println(100 * win1 / rep + " " + 100 * win2 / rep + " "
				    + 100 * draw / rep);
			} catch ( GameRuleViolation e ) {
				e.printStackTrace();
			} catch ( InterruptedException e ) {
				e.printStackTrace();
			}
		}
	}

}
