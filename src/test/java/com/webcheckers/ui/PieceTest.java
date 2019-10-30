package com.webcheckers.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * unit tests for the Piece class
 */
@Tag("UI-tier")
public class PieceTest {
    private Piece CuT;

    /**
     * Set up before each test
     */
    @BeforeEach
    public void testSetup(){
        CuT = new Piece(Piece.Color.WHITE);
        assertNotNull(CuT);
    }

    /**
     * test for the getType method
     */
    @Test
    public void test_getType(){
        assertEquals(Piece.Type.SINGLE, CuT.getType());
    }

    /**
     * test for the getColor method
     */
    @Test
    public void test_getColor(){
        assertEquals(Piece.Color.WHITE, CuT.getColor());
    }

    @Test
    void makeKingTest() {
        assertEquals(Piece.Type.SINGLE, CuT.getType());
        CuT.makeKing();
        assertEquals(Piece.Type.KING, CuT.getType());
    }
}
