package com.webcheckers.model;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.ui.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * unit tests for the Board class
 */
@Tag("Model-tier")
public class BoardTest {
    private Board CuT;
    private Square[][] board;
    private Move move;
    private Move move1;
    private PlayerServices player1;
    private PlayerServices player2;
    private GameCenter gameCenter;

    private Position p1 = new Position(0, 2);
    private Position p2 = new Position( 1, 1);
    private Position p3 = new Position(2,1);
    private Position p4 = new Position( 3, 0);

    /**
     * Set up before each test
     */
    @BeforeEach
    void testSetup(){
        move = new Move(p1, p2);
        move1 = new Move(p3, p4);
        CuT = new Board();
        gameCenter = new GameCenter();
        player1 = new PlayerServices( "Namor", gameCenter);
        player2 = new PlayerServices( "Aquaman", gameCenter);
        assertNotNull(CuT);

        //setting up for board comparison
        this.board = new Square[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (row % 2 == 0) {
                    if (col % 2 == 0) {
                        board[row][col] = new Square(Square.Color.WHITE);
                    } else {
                        board[row][col] = new Square(Square.Color.BLACK);
                        if (row <= 2) {
                            board[row][col].addPiece(Piece.Color.WHITE);
                        } else if (row >= 5) {
                            board[row][col].addPiece(Piece.Color.RED);
                        }
                    }
                } else {
                    if (col % 2 == 0) {
                        board[row][col] = new Square(Square.Color.BLACK);
                        if (row <= 2) {
                            board[row][col].addPiece(Piece.Color.WHITE);
                        } else if (row >= 5) {
                            board[row][col].addPiece(Piece.Color.RED);
                        }
                    } else {
                        board[row][col] = new Square(Square.Color.WHITE);
                    }
                }
            }
        }
    }

    /**
     * tests the isValid method
     */
    @Test
    void test_isValidMove(){
        assertFalse(CuT.isValidMove(move));
        assertTrue(CuT.isValidMove(move1));
    }

    @Test
    void getActiveColorTest() {
        assertNotNull(CuT.getActiveColor());
        assertEquals(Piece.Color.RED, CuT.getActiveColor());
    }

    @Test
    void changeActiveColorTest() {
        CuT.changeActiveColor(Piece.Color.WHITE);
        assertEquals(Piece.Color.WHITE, CuT.getActiveColor());
    }

    @Test
    void getWhitePiecesTest() {
        assertEquals(12, CuT.getWhitePieces());
    }

    @Test
    void getRedPiecesTest() {
        assertEquals(12, CuT.getRedPieces());
    }

    @Test
    void getActualMoveTest() {
        Move result = CuT.getActualMove(move1);
        assertNotNull(result);
        assertTrue(CuT.isValidMove(result));
        CuT.changeActiveColor(Piece.Color.WHITE);
        result = CuT.getActualMove(move1);
        assertFalse(CuT.isValidMove(result));
    }

    @Test
    void backUpMoveTest() {
        assertNotNull(CuT.backUpMove());

        assertEquals("moveList is empty, cannot backup the move.", CuT.backUpMove());
        CuT.isValidMove(move1);
        assertEquals("true", CuT.backUpMove());
    }

    @Test
    void getBoardTest() {
        assertNotNull(CuT.getBoard());

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals(board[row][col].getColor(), CuT.getBoard()[row][col].getColor());
            }
        }
    }

    @Test
    void toStringTest() {
        assertNotNull(CuT.toString());

        StringBuilder test = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getColor() == Square.Color.BLACK) {
                    if (board[i][j].getPiece() != null) {
                        test.append("[(B)]");
                    } else {
                        test.append("[B]");
                    }
                } else {
                    test.append("[W]");
                }
            }
            test.append("\n");
        }

        assertEquals(test.toString(), CuT.toString());
    }

    @Test
    void checkMakeKingTest() {
        CuT.getBoard()[0][1].addPiece(Piece.Color.RED);
        assertEquals(Piece.Type.SINGLE, CuT.getBoard()[0][1].getPiece().getType());
        CuT.checkMakeKing();
        assertEquals(Piece.Type.KING, CuT.getBoard()[0][1].getPiece().getType());
    }

    @Test
    void capturePiecesTest() {
        CuT.addPieceToBoard(3, 4, Piece.Color.WHITE);
        assertEquals(13, CuT.getWhitePieces());

        CuT.isValidMove(new Move(new Position(2, 3), new Position(4, 5)));

        CuT.capturePieces();
        assertEquals(12, CuT.getWhitePieces());
        assertEquals(12, CuT.getRedPieces());

        CuT.isValidMove(move1);
        CuT.capturePieces();

        //check that no pieces were captured
        assertEquals(12, CuT.getWhitePieces());
        assertEquals(12, CuT.getRedPieces());
    }

    @Test
    void capturePieceTest() {
        CuT.capturePiece(0,1);
        assertNull(CuT.getBoard()[0][1].getPiece());
        assertEquals(11, CuT.getWhitePieces());

        CuT.capturePiece(0,3);
        assertEquals(10, CuT.getWhitePieces());

        CuT.capturePiece(7,0);
        assertEquals(11, CuT.getRedPieces());

        //expecting nothing to have happened since no piece at 3,2
        CuT.capturePiece(3, 2);
        assertEquals(10, CuT.getWhitePieces());
        assertEquals(11, CuT.getRedPieces());
    }

    @Test
    void teamIsEliminatedTest() {
        Board copyCuT = new Board(CuT);
        assertFalse(CuT.teamIsEliminated());

        //eliminating white team
        CuT.capturePiece(0, 1);
        CuT.capturePiece(0, 3);
        CuT.capturePiece(0, 5);
        CuT.capturePiece(0, 7);
        CuT.capturePiece(1, 0);
        CuT.capturePiece(1, 2);
        CuT.capturePiece(1, 4);
        CuT.capturePiece(1, 6);
        CuT.capturePiece(2, 1);
        CuT.capturePiece(2, 3);
        CuT.capturePiece(2, 5);
        CuT.capturePiece(2, 7);

        assertTrue(CuT.teamIsEliminated());

        //eliminating red team
        copyCuT.capturePiece(5, 0);
        copyCuT.capturePiece(5, 2);
        copyCuT.capturePiece(5, 4);
        copyCuT.capturePiece(5, 6);
        copyCuT.capturePiece(6, 1);
        copyCuT.capturePiece(6, 3);
        copyCuT.capturePiece(6, 5);
        copyCuT.capturePiece(6, 7);
        copyCuT.capturePiece(7, 0);
        copyCuT.capturePiece(7, 2);
        copyCuT.capturePiece(7, 4);
        copyCuT.capturePiece(7, 6);

        assertTrue(copyCuT.teamIsEliminated());
    }

    @Test
    void resetMovesTest() {
        gameCenter.createGame(player1.Id(), player2.Id());

        assertFalse(CuT.resetMoves(null));
        assertTrue(CuT.resetMoves(player1));
        CuT.changeActiveColor(Piece.Color.WHITE);
        assertFalse(CuT.resetMoves(player1));
        assertTrue(CuT.resetMoves(player2));
        CuT.changeActiveColor(Piece.Color.RED);
        assertFalse(CuT.resetMoves(player2));
    }

    @Test
    void trySubmitTurnTest() {
        assertNotNull(CuT.trySubmitTurn());

        //will change test when changes to this function are made
        assertEquals("No moves!", CuT.trySubmitTurn());
        CuT.isValidMove(move1);
        assertEquals("true", CuT.trySubmitTurn());
    }

    @Test
    void hasMovesLeftTest() {
        assertTrue(CuT.hasMovesLeft());
    }


    @Test
    void addPieceToBoardTest() {
        assertFalse(CuT.addPieceToBoard(0,0, Piece.Color.RED));
        assertTrue(CuT.addPieceToBoard(p4.getRow(), p4.getCell(), Piece.Color.WHITE));
        assertFalse(CuT.addPieceToBoard(0, 1, Piece.Color.RED));
        assertTrue(CuT.addPieceToBoard(4, 1, Piece.Color.RED));
    }

    @Test
    void addPieceTypeToBoard() {
        assertFalse(CuT.addPieceTypeToBoard(0,0, Piece.Color.RED, Piece.Type.SINGLE));
        assertTrue(CuT.addPieceTypeToBoard(p4.getRow(), p4.getCell(), Piece.Color.WHITE, Piece.Type.SINGLE));
        assertFalse(CuT.addPieceTypeToBoard(0, 1, Piece.Color.RED, Piece.Type.SINGLE));
        assertTrue(CuT.addPieceTypeToBoard(4, 1, Piece.Color.RED, Piece.Type.SINGLE));
        assertTrue(CuT.addPieceTypeToBoard(3, 6, Piece.Color.WHITE, Piece.Type.KING));
        assertFalse(CuT.addPieceTypeToBoard(6,3, Piece.Color.RED, Piece.Type.KING));
    }
}
