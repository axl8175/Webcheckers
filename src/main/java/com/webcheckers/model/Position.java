package com.webcheckers.model;


/**
 * A class to represent a space on the Board.
 */

public class Position {
    //the row of the position
    private int row;
    //the cell of the position
    private int cell;

    /**
     * The Position constructor
     *
     * @param row the row of the position
     * @param cell the cell of the position
     */
    public Position(int row, int cell) {
        this.row = row;
        this.cell = cell;
    }

    /**
     * Gets the row of the position
     *
     * @return the row of the position
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Gets the cell of the position
     *
     * @return the cell of the position
     */
    public int getCell() {
        return this.cell;
    }
}
