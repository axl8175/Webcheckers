package com.webcheckers.ui;

/**
 * A class that handles the pieces in the checkers game
 */
public class Piece {

    /**
     * Defining the types of pieces in the checkers game
     */
    public enum Type {
        SINGLE, KING
    }
    //the type the piece is
    private Type type;

    /**
     * Defining the color of the piece to differentiate between the players
     */
    public enum Color {
        RED, WHITE
    }
    //the color of the piece
    private Color color;

    /**
     * The constructor to create a Piece for the checkers game
     *
     * @param color the color that the piece should be
     */
    public Piece(Color color) {
        this.type = Type.SINGLE;
        this.color = color;
    }

    /**
     * Getter for what type the Piece is
     *
     * @return the type of the piece
     */
    public Type getType() {
        return this.type;
    }

    /**
     * Getter for the color of the piece
     *
     * @return the color of the piece
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Makes a piece a king
     */
	public void makeKing() {
		this.type = Type.KING;
	}
}
