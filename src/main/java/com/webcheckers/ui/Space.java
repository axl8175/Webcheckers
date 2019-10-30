package com.webcheckers.ui;

/**
 * A class for the spaces in the game of Checkers
 */
public class Space {
    //the cell index of the space
    private int cellIdx;
    //the piece being put on the space
    private Piece piece;
    //the boolean to check if the space is a playable space or not
    private boolean valid;

    /**
     * The constructor that determines if a space is playable or not
     *
     * @param id the id of the space that is being created
     * @param valid determines if the space is a valid playable space or not
     */
    public Space(int id, boolean valid) {
        this.cellIdx = id;
        this.piece = null;
        this.valid = valid;
    }

    /**
     * Getter for the cell index
     *
     * @return the cell index
     */
    public int getCellIdx(){
        return this.cellIdx;
    }

    /**
     * Gets the color of the piece on the space
     *
     * @return the color of the piece on the space
     */
    public Piece.Color getPieceColor(){
        return this.piece.getColor();
    }

    /**
     * Makes the piece on the space a king
     */
    public void makePieceKing(){
        this.piece.makeKing();
    }

    /**
     * Gets the type the piece is on the space
     *
     * @return the type of piece is on the space
     */
    public Piece.Type getPieceType(){
        return this.piece.getType();
    }


    /**
     * Determines if a space is valid for movement dependent on if the space is valid and whether there is a piece in the space or not
     * @return true if the space is valid and there is not piece. false if there is a piece on the space or the space is not valid
     */
    public boolean isValid(){
        return getPiece() == null && valid == true;
    }

    /**
     * Getter for if there is a piece on the space
     * @return the piece on the space
     */
    public Piece getPiece(){
        return this.piece;
    }

    /**
     * Adds a new piece
     * @param color the color of the piece being added
     */
	public void addPiece(Piece.Color color) {
		this.piece = new Piece(color);
	}

    /**
     * remove any Piece that resides on this Space object.
     */
	public void removePiece() {
		this.piece = null;
	}
}
