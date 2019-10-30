package com.webcheckers.ui;

import com.webcheckers.ui.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;


@Tag("UI-tier")
public class RowTest {
    private Row row1;
    private Row row2;

    private Space space;

    /**
     * Setup before each test
     */
    @BeforeEach
    public void testSetup() {
        space = mock(Space.class);
        row1 = new Row(7);
        assertNotNull(row1);
        row2 = new Row(0);
        assertNotNull(row2);
    }

    /**
     * Tests the get index function to notify which row is being made
     */
    @Test
    public void test_getIndex(){
        assertEquals(0, row2.getIndex());
    }

    /**
     * Tests the iterator of the Row class
     */
    @Test
    public void test_iterator(){
        assertNotNull(row1.iterator());
    }
}
