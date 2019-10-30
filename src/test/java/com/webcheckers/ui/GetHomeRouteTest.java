package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;

import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The unit test suite for the GetHomeRoute class.
 */

@Tag("UI-tier")
public class GetHomeRouteTest {

    // to component under test (CUT)
    private GetHomeRoute CuT;

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

        CuT = new GetHomeRoute(gameCenter, templateEngine);
    }

    /**
     * Test to make sure the number of online players is shown when the user
	 * enters the home page without being logged in.
     */
    @Test
    public void validateCorrectFirstDisplay() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("totalPlayers", gameCenter.getTotalPlayers());
        testHelper.assertViewName("home.ftl");
    }
	
	/**
     * Test to make sure the player services of the signed in player is correctly
	 * put into the view model.
     */
    @Test
    public void validatePlayerServices() {
		PlayerServices ps = new PlayerServices("player", gameCenter);
		HashSet hs = new HashSet<>();
		hs.add("PlayerServices");
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
		when(request.session().attributes()).thenReturn(hs);
		when(request.session().attribute("PlayerServices")).thenReturn(ps);
		
        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
		testHelper.assertViewModelAttribute("PlayerServices", ps);
        testHelper.assertViewName("home.ftl");
    }
	
	/**
     * Test to make sure the player gets the correct error message when trying to
	 * enter a game with a busy player.
     */
    @Test
    public void validateBusyError() {
		PlayerServices ps = new PlayerServices("player", gameCenter);
		ps.setEnteredBusy();
		HashSet hs = new HashSet<>();
		hs.add("PlayerServices");
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
		when(request.session().attributes()).thenReturn(hs);
		when(request.session().attribute("PlayerServices")).thenReturn(ps);
		
        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
		testHelper.assertViewModelAttribute("Error", "Player is busy.");
        testHelper.assertViewName("home.ftl");
    }	
}
