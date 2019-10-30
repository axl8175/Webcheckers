package com.webcheckers.model;

import com.webcheckers.model.Square;

import com.webcheckers.ui.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@Tag("Model-tier")
public class SquareTest {
    private Square CuT;
    private Square CuT1;
    private Square CuT2;
    private Square CuT3;
    private Square.Color color;
    private Piece piece;

    /**
     * Set up before each test
     */
    @BeforeEach
    public void testSetup(){
        this.color = Square.Color.BLACK;
        this.piece = new Piece(Piece.Color.WHITE);
        CuT = new Square(color);
        CuT1 = new Square(Square.Color.WHITE);
        CuT2 = new Square(color);
        CuT3 = new Square(color);
        assertNotNull(CuT);
        assertNotNull(CuT1);
    }

    /**
     * test for the getColor method
     */
    @Test
    public void test_getColor(){
        assertEquals(Square.Color.BLACK, CuT.getColor());
    }

    /**
     * test for the getPiece method
     */
    @Test
    public void test_getPiece(){
        CuT.addPiece(Piece.Color.WHITE);
        assertNotNull(CuT.getPiece());
        assertNull(CuT1.getPiece());
    }

    /**
     * test for the isPlayable method
     */
    @Test
    public void test_isPlayable(){
        assertTrue(CuT.isPlayable());
        assertFalse(CuT1.isPlayable());
        assertTrue(CuT2.isPlayable());
        CuT3.addPiece(Piece.Color.RED);
        assertFalse(CuT3.isPlayable());
    }
}
