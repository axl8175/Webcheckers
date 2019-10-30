package com.webcheckers.ui;

import com.webcheckers.model.Board;
import com.webcheckers.model.Square;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A class that generates the board view based on the playerID
 */

public class BoardView implements Iterable<Row> {
    private List<Row> rows;
    private Board board;

    /**
     * The constructor of the board view that adds the rows and sets up the view based on the user id
     *
     * @param board the board to be viewed
     */
    public BoardView(Board board) {
        this.board = board;
        rows = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            rows.add(new Row(i));
        }

        translate(board);
    }

    /**
     * Getter for the board model
     *
     * @return Board model
     */
    public Board getBoard() {
        return board;
    }

    /**
     * The iterator that goes through the rows on the board
     *
     * @return the collection of rows on the board
     */
    @Override
    public Iterator<Row> iterator() {
        return rows.iterator();
    }

    /**
     * Translate the board to the board view
     *
     * @param board the board that is being played
     */
    private void translate(Board board) {
        Square[][] squares = board.getBoard();
        for(int j = 0; j < 8; ++j) {
            for(int k = 0; k < 8; ++k) {
                Square currSquare = squares[j][k];

                if (currSquare.getPiece() != null) {
                    Piece piece = currSquare.getPiece();
                    Row row = this.rows.get(j);
                    row.setPieceAt(k, piece);
					if (currSquare.getPieceType() == Piece.Type.KING) {
						row.kingPieceAt(k);
					}
                }
            }
        }
    }

    /**
     * flips the board so that each player's piece is at the bottom of the board for them
     * @return String that is used to test whether the flip correctly flips the board around
     */
    public String flip() {
        Square[][] b1 = board.getBoard();

        StringBuilder flippedBoard = new StringBuilder();
        for (int i = 7; i > -1; i--) {
            for (int j = 7; j > -1; j--) {
                if (b1[i][j].getColor() == Square.Color.BLACK) {
                    if (b1[i][j].getPiece() != null) {
                        flippedBoard.append("[(B)]");
                    } else {
                        flippedBoard.append("[B]");
                    }
                } else {
                    flippedBoard.append("[W]");
                }
            }
            flippedBoard.append("\n");
        }

        ArrayList<Space> spaces = new ArrayList<>();
        for (Row row : rows) {
            for (Space space : row.getSpaces()) {
                int id = space.getCellIdx();
                boolean b = space.isValid();
                Space s = new Space(id, b);
                if (space.getPiece() == null) {
                    spaces.add(s);
                } else {
                    if (space.getPieceColor() == Piece.Color.RED) {
                        s.addPiece(Piece.Color.RED);
                        if (space.getPieceType() == Piece.Type.KING) {
                            s.makePieceKing();
                        }
                        spaces.add(s);
                    } else {
                        s.addPiece(Piece.Color.WHITE);
                        if (space.getPieceType() == Piece.Type.KING) {
                            s.makePieceKing();
                        }
                        spaces.add(s);
                    }
                }
            }
        }

        int endIndex = 63;
        for (Row row : rows) {
            for (Space space : row.getSpaces()) {
                Piece piece = spaces.get(endIndex).getPiece();
                if (piece != null) {
                    space.addPiece(piece.getColor());
                    if (piece.getType() == Piece.Type.KING) {
                        space.makePieceKing();
                    }
                } else {
                    space.removePiece();
                }
                spaces.remove(endIndex);
                endIndex--;
            }
        }

        return flippedBoard.toString();
    }


}

