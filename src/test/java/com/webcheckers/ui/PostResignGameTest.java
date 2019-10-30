package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.webcheckers.appl.PlayerServices;
import spark.*;


import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * unit tests for the PostResignGame class
 */
@Tag("UI-tier")
class PostResignGameTest {

    private PostResignGame CuT;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine templateEngine;
    private GameCenter gameCenter;
    private PlayerServices p1;
    private PlayerServices p2;

    /**
     * Setup before each test
     */
    @BeforeEach
    public void setup() {
        gameCenter = new GameCenter();
        p1 = new PlayerServices("Donald", gameCenter);
        p2 = new PlayerServices("Daffy", gameCenter);

        gameCenter.addPlayer(p1);
        gameCenter.addPlayer(p2);
        gameCenter.createGame(p1.Id(), p2.Id());

        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        templateEngine = mock(TemplateEngine.class);
        response = mock(Response.class);


        CuT = new PostResignGame(templateEngine);


    }

    @Test
    void handleTest() {
        HashSet hs = new HashSet<>();
        hs.add("PlayerServices");
        when(request.session().attributes()).thenReturn(hs);
        when(request.session().attribute("PlayerServices")).thenReturn(p1);

        Message message = CuT.handle(request, response);
        assertNotNull(message);
        assertTrue(message.isSuccessful());
        assertTrue(p1.currentGameIsOver());
        assertFalse(p2.isMyTurn());
    }
}