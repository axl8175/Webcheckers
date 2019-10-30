package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("UI-Tier")
public class GetReplayStopWatchingTest{

    // to component under test (CUT)
    private GetReplayStopWatchingRoute CuT;

    // friendly object
    private GameCenter gameCenter;

    // mock objects
    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine templateEngine;

    /**
     * Setup before each test
     */
    @BeforeEach
    public void setup() {
        // mock objects
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        templateEngine = mock(TemplateEngine.class);
        response = mock(Response.class);
        // friendly objects
        gameCenter = new GameCenter();
        //gameCenter.addPlayer(new PlayerServices("player1", gameCenter));

        CuT = new GetReplayStopWatchingRoute(templateEngine);
    }

    @Test
    public void signOutTest(){
        when(session.attribute("player1")).thenReturn(null);
        CuT.handle(request, response);

        assertEquals(0,gameCenter.getTotalPlayers());
    }
}

