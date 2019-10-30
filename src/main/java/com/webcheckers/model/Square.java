package com.webcheckers.model;

import com.webcheckers.ui.Piece;

/**
 * A class to represent a square on a checkers Board.
 */

public class Square {
    //enum for the color of the square
    public enum Color {
        WHITE, BLACK
    }
    //Color of the square
    private Color color;
    //a piece for a square to handle
    private Piece piece;
    //the position of the square
    private Position position;

    /**
     * Constructor for the square
     *
     * @param color the color of the square
     */
    public Square(Color color) {
        this.color = color;
        this.piece = null;
    }

    /**
     * Gets the color od the square
     *
     * @return the color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Gets the piece that is on the square if there is one
     *
     * @return the piece or null
     */
    public Piece getPiece() {
        return this.piece;
    }

    /**
     * Gets the color of the piece on the square
     *
     * @return the color of the piece
     */
    public Piece.Color getPieceColor(){
        return this.piece.getColor();
    }

    /**
     * Gets the type of the piece on the square
     *
     * @return the type of the piece
     */
    public Piece.Type getPieceType(){
        return this.piece.getType();
    }

    /**
     * Adds a piece to the square
     *
     * @param color the color of the piece that is being added
     */
    public void addPiece(Piece.Color color) {
        Piece addedPiece = new Piece(color);
        this.piece = addedPiece;
    }

    /**
     * Removes the piece from the square
     */
	public void removePiece() {
		this.piece = null;
	}

    /**
     * Checks if the square is playable based on the color of the square and if there is a piece on the square
     *
     * @return true if the square is available for play
     */
    public boolean isPlayable() {
        // square must be BLACK, and empty
        if (this.color == Color.BLACK) {
            if (this.piece == null) {
                return true;
            }
        }
        return false;
    }

}
