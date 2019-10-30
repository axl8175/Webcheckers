package com.webcheckers.model;

import com.webcheckers.model.Move;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * unit tests for the Position class
 */
@Tag("Model-tier")
public class PositionTest {
    private Position CuT;
    private int row = 0;
    private int cell = 2;

    /**
     * Set up before each test
     */
    @BeforeEach
    public void testSetup(){
        CuT = new Position(row, cell);
        assertNotNull(CuT);
    }

    /**
     * test the getRow method
     */
    @Test
    public void test_getRow(){
        assertEquals(row, CuT.getRow());
    }

    /**
     * test the getCell method
     */
    @Test
    public void test_getCell(){
        assertEquals(cell, CuT.getCell());
    }
}
