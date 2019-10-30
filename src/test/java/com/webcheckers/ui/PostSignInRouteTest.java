package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;

import com.webcheckers.appl.PlayerServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The unit test suite for the PostSignInRoute class.
 */

@Tag("UI-tier")
public class PostSignInRouteTest {

    // to component under test (CUT)
    private PostSignInRoute CuT;

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

        CuT = new PostSignInRoute(gameCenter, templateEngine);
    }

    /**
     * Test when a user enters a username with invalid characters
     */
    @Test
    public void invalidCharacterUsername() {
        when(request.queryParams(eq("username"))).thenReturn("%");
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("Error", "Invalid name, please try another...");
        testHelper.assertViewName("signin.ftl");
    }

    /**
     * Test when a user enters a whitespace username
     */
    @Test
    public void noCharacterUsername() {
        when(request.queryParams(eq("username"))).thenReturn("   ");
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("Error", "Invalid name, please try another...");
        testHelper.assertViewName("signin.ftl");
    }

    /**
     * Test when a username is already taken
     */
    @Test
    public void takenUsername() {
        gameCenter.addPlayer(new PlayerServices("player1", gameCenter));

        when(request.queryParams(eq("username"))).thenReturn("player1");
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("Error", "Name is already in use. Please try another...");
        testHelper.assertViewName("signin.ftl");
    }

    /**
     * Test a valid username
     */
    @Test
    public void validUsername() {
        when(request.queryParams(eq("username"))).thenReturn("player1");
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("home.ftl");
    }

}
