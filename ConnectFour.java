/**
 * Main program to play a single game of Connect Four.
 * 
/

public class ConnectFour {

	public static void main ( String[] args ) {
		ConnectFourBoard board = new ConnectFourBoard(6,7);
		// comment out the following line if you don't want a gui
		ConnectFourGUI gui = new ConnectFourGUI(board);
		ConnectFourDriver driver = new ConnectFourDriver();

		Player player1 = new AlphaBetaPrunerPlayer(GamePiece.RED,5000);
		Player player2 = new MoveOrderingPlayer(GamePiece.YELLOW,5000);

		try {
			Player winner = driver.playGame(player1,player2,board);
			// uncomment the following line if you don't use a gui
			// board.print();
			if ( winner == null ) {
				System.out.println("game is a draw!");
			} else {
				System.out.println("player " + winner.getName() + " wins!");
			}
		} catch ( GameRuleViolation e ) {
			e.printStackTrace();
		} catch ( InterruptedException e ) {
			e.printStackTrace();
		}

	}

}
