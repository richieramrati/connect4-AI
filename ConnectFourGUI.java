import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 * GUI for Connect Four.
 * 
 *  */
public class ConnectFourGUI implements Observer, MouseListener {

	class Square extends JPanel {

		private int row_, col_;
		private ConnectFourBoard board_;

		/**
		 * @param row
		 * @param col
		 * @param board
		 */
		public Square ( int row, int col, ConnectFourBoard board ) {
			super();
			row_ = row;
			col_ = col;
			board_ = board;
		}

		/**
		 * @return the col
		 */
		public int getCol () {
			return col_;
		}

		/*
		 * (non-Javadoc)
		 * @see javax.swing.JComponent#getMinimumSize()
		 */
		@Override
		public Dimension getMinimumSize () {
			return new Dimension(20,20);
		}

		/*
		 * (non-Javadoc)
		 * @see javax.swing.JComponent#getPreferredSize()
		 */
		@Override
		public Dimension getPreferredSize () {
			return new Dimension(80,80);
		}

		/**
		 * @return the row
		 */
		public int getRow () {
			return row_;
		}

		/*
		 * (non-Javadoc)
		 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
		 */
		@Override
		protected void paintComponent ( Graphics g ) {
			super.paintComponent(g);

			int w = getWidth(), h = getHeight();
			int dim = (int) Math.min(.8 * w,.8 * h);

			g.setColor(Color.BLACK);
			g.drawRect(0,0,w - 1,h - 1);

			GamePiece piece = board_.getPiece(row_,col_);
			if ( piece == GamePiece.NONE ) {
				g.setColor(Color.WHITE);
			} else {
				g.setColor(piece.getColor());
			}
			g.fillOval((w - dim) / 2,(h - dim) / 2,dim,dim);
			g.setColor(Color.BLACK);
			g.drawOval((w - dim) / 2,(h - dim) / 2,dim,dim);
		}

	}

	private JFrame frame_;

	private ConnectFourBoard board_;
	private Object turn_;

	private int move_;

	/**
	 * Create a GUI to display the specified board.
	 * 
	 * @param board
	 */
	public ConnectFourGUI ( ConnectFourBoard board ) {
		board_ = board;
		board_.addObserver(this);
		turn_ = new Object();
		move_ = -1;
		createGUI();
	}

	private void createGUI () {
		frame_ = new JFrame("ConnectFour");
		frame_.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		Container content = frame_.getContentPane();
		content.setLayout(new GridLayout(board_.getNumRows(),board_.getNumCols()));
		for ( int row = board_.getNumRows() - 1 ; row >= 0 ; row-- ) {
			for ( int col = 0 ; col < board_.getNumCols() ; col++ ) {
				Square sq = new Square(row,col,board_);
				sq.setBackground(new Color(100,100,255));
				sq.addMouseListener(this);
				content.add(sq);
			}
		}

		frame_.validate();
		frame_.pack();
		frame_.setVisible(true);
	}

	/**
	 * Get a move - waits for the player to click on a column of the game board.
	 * 
	 * @return the column of the desired move, or -1 if no move is to be made.
	 */
	public int getMove () {
		move_ = -1;
		try {
			synchronized ( turn_ ) {
				turn_.wait();
			}
		} catch ( InterruptedException e ) {}
		return move_;
	}

	@Override
	public void mouseClicked ( MouseEvent e ) {
		try {
			Square sq = (Square) e.getSource();
			move_ = sq.getCol();
			synchronized ( turn_ ) {
				turn_.notify();
			}
		} catch ( ClassCastException cce ) {}
	}

	@Override
	public void mouseEntered ( MouseEvent e ) {}

	@Override
	public void mouseExited ( MouseEvent e ) {}

	@Override
	public void mousePressed ( MouseEvent e ) {}

	@Override
	public void mouseReleased ( MouseEvent e ) {}

	@Override
	public void update ( Observable source, Object obj ) {
		frame_.repaint();
	}
}
