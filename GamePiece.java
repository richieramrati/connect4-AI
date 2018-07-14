import java.awt.Color;

/**
 * A game piece for ConnectFour.
 * 
 *
 */
public enum GamePiece {
	NONE("none"," ",null), RED("red","r",Color.RED), YELLOW("yellow","y",
	                                                        Color.YELLOW);

	/**
	 * Retrieve the game piece, if any, with the specified label.
	 * 
	 * @param label
	 *          game piece label
	 * @return the game piece with the specified label
	 * @throws IllegalArgumentException
	 *           if there is no such game piece
	 */
	public static GamePiece getInstance ( String label ) {
		for ( GamePiece piece : GamePiece.values() ) {
			if ( piece.label_.equals(label) ) {
				return piece;
			}
		}
		throw new IllegalArgumentException("no such ConnectFourPiece '" + label
		    + "'");
	}

	private String label_;
	private String abbrev_;

	private Color color_;

	private GamePiece ( String label, String abbrev, Color color ) {
		label_ = label;
		abbrev_ = abbrev;
		color_ = color;
	}

	/**
	 * A one-letter abbreviation for the game piece.
	 * 
	 * @return one-letter abbreviation for the game piece
	 */
	public String getAbbrev () {
		return abbrev_;
	}

	/**
	 * The color for drawing the game piece.
	 * 
	 * @return game piece color
	 */
	public Color getColor () {
		return color_;
	}

	/**
	 * Get the other game piece e.g. if this one is RED, returns BLACK. Assumes
	 * there are only two non-NONE game pieces.
	 * 
	 * @return the other player's game piece, or GamePiece.NONE if this piece is
	 *         GamePiece.NONE
	 */
	public GamePiece other () {
		if ( this == GamePiece.NONE ) {
			return GamePiece.NONE;
		}
		for ( GamePiece piece : GamePiece.values() ) {
			if ( piece == this || piece == GamePiece.NONE ) {
				continue;
			}
			return piece;
		}
		return null;
	}

	@Override
	public String toString () {
		return label_;
	}

}
