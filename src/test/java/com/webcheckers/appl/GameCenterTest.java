package com.webcheckers.appl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The unit test suite for the GameCenter class.
 *
 */

@Tag("Application-Tier")
class GameCenterTest {
    //component under test
    private GameCenter CuT;

    //friendly objects
    private ArrayList<PlayerServices> playerServices;

    private int player_count;


    @BeforeEach
    public void setup() {
        playerServices = new ArrayList<>();
        CuT = new GameCenter();
        player_count = 0;

        for (int i = 1; i < 4; ++i) {
            playerServices.add(new PlayerServices("player" + i, CuT));
            ++player_count;
        }
    }

    @Test
    void addPlayerTest() {
        assertEquals(CuT.getTotalPlayers(), player_count);

        playerServices.add(new PlayerServices("player4", CuT));
        ++player_count;

        //assertNotEquals(player_count, CuT.getTotalPlayers());

        //CuT.addPlayer(new PlayerServices("player4", CuT));

        assertEquals(player_count, CuT.getTotalPlayers());
        assertEquals(CuT.getPlayerById("player4").Id(), playerServices.get(3).Id());
    }

    @Test
    void nameIsTakenTest() {
        assertTrue(CuT.nameIsTaken("player1"));
        assertFalse(CuT.nameIsTaken("player999"));
    }

    @Test
    void getTotalPlayersTest() {
        assertEquals(player_count, CuT.getTotalPlayers());
    }

    @Test
    void getPlayersTest() {
        assertEquals(playerServices, CuT.getPlayers());
    }

    @Test
    void getPlayerByIdTest() {
        assertEquals(playerServices.get(0), CuT.getPlayerById("player1"));
    }

    @Test
    void isPlayerAvailableTest() {
        assertTrue(CuT.isPlayerAvailable("player3"));
        assertEquals(playerServices.get(2).isAvailable(), CuT.isPlayerAvailable("player3"));

        Game game = new Game(CuT.getPlayerById("player1"),CuT.getPlayerById("player2"), CuT);
        CuT.getPlayerById("player1").addGame(game);
        CuT.getPlayerById("player2").addGame(game);

        assertFalse(CuT.isPlayerAvailable("player1"));
    }

    @Test
    void createGameTest() {
        CuT.createGame("player1", "player2");
        assertEquals("player2", CuT.getPlayerById("player1").getCurrentGame().getPlayer2().Id());
        assertFalse(CuT.isPlayerAvailable("player1"));
        assertTrue(CuT.isPlayerAvailable("player3"));
    }
}
