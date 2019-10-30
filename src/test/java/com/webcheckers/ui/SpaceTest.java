package com.webcheckers.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@Tag("UI-tier")
public class SpaceTest {
    private Space CuT;
    private Space CuT1;

    @BeforeEach
    public void testSetup(){
        CuT = new Space(0, true);
        assertNotNull(CuT);
        CuT1 = new Space(1, false);
        assertNotNull(CuT1);
    }

    @Test
    public void test_getCellIdx(){
        assertEquals(0, CuT.getCellIdx());
        assertEquals(1, CuT1.getCellIdx());
    }

    @Test
    public void test_isValid(){
        assertTrue(CuT.isValid());
        assertFalse(CuT1.isValid());
    }

    @Test
    void getPieceTest() {
        assertNull(CuT.getPiece());
        CuT.addPiece(Piece.Color.RED);
        assertNotNull(CuT.getPiece());
        assertEquals(Piece.Color.RED, CuT.getPiece().getColor());
    }

    @Test
    void addPieceTest() {
        CuT.addPiece(Piece.Color.RED);
        assertEquals(Piece.Color.RED, CuT.getPiece().getColor());
    }

    @Test
    void removePieceTest() {
        CuT.removePiece();
        assertNull(CuT.getPiece());
    }
}
