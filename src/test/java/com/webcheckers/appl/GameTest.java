package com.webcheckers.appl;

import com.webcheckers.model.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The unit test suite for the Game class.
 *
 */

@Tag("Application-tier")
public class GameTest {

    // The component under test
    private Game CuT;

    // friendly objects
    private GameCenter gameCenter;
    private Board board;
    private PlayerServices player1;
    private PlayerServices player2;

    /**
     * Setup new object for each test.
     */
    @BeforeEach
    public void setup() {
        gameCenter = new GameCenter();
        board = new Board();

        player1 = new PlayerServices("p1", gameCenter);
        player2 = new PlayerServices("p2", gameCenter);

        CuT = new Game(player1, player2, gameCenter);
    }

    /**
     * Test that the getAttributes method.
     */
    @Test
    public void attributes() {
        final Map<String, Object> vm = CuT.getAttributes(player1);
        assertEquals(player1, vm.get("PlayerServices"));
        assertEquals(player1, vm.get("Player1"));
        assertEquals("PLAY", vm.get("viewMode"));
        assertEquals(player2, vm.get("Player2"));
        assertEquals(board.getActiveColor(), vm.get("activeColor"));
    }

}
