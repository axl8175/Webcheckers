package com.webcheckers.appl;

import com.webcheckers.model.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The unit test suite for the PlayerServices class.
 *
 */

@Tag("Application-tier")
public class PlayerServicesTest {

    // The component under test
    private PlayerServices CuT;
    private GameCenter gameCenter;
    private String username;
    private Game currentGame;

    /**
     * Setup new object for each test.
     */
    @BeforeEach
    public void setup() {
        gameCenter = new GameCenter();
		username = "Player";
		currentGame = null;
    }

    /**
     * Test that the constructor
     */
    @Test
    public void constructorTest() {
        CuT = new PlayerServices(username, gameCenter);
		
        assertEquals(CuT.Id(), username);
        assertEquals(CuT.getCurrentGame(), currentGame);
    }
	
	 /**
     * Test add game functionality
     */
    @Test
    public void addGameTest() {
        CuT = new PlayerServices(username, gameCenter);
		Game game = new Game(CuT, new PlayerServices("player2", gameCenter), gameCenter);
		CuT.addGame(game);
		
        assertEquals(game, CuT.getCurrentGame());
    }

	/**
     * Test to make sure it correctly communicates with the Game class
	 * when trying to ask for it's turn.
     */
    @Test
    public void checkTurnTest() {
        CuT = new PlayerServices(username, gameCenter);
		Game game = new Game(CuT, new PlayerServices("player2", gameCenter), gameCenter);
		CuT.addGame(game);
		
        assertEquals(game.isMyTurn(CuT), CuT.isMyTurn());
    }

}
