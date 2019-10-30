package com.webcheckers.ui;

import com.webcheckers.appl.GameCenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.*;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * unit tests for the GetSignInRoute class
 */
@Tag("UI-tier")
public class GetSignInRouteTest {
    private GetSignInRoute CuT;
    private GameCenter gameCenter;

    private Request request;
    private Session session;
    private Response response;
    private TemplateEngine templateEngine;

    /**
     * Set up before each test
     */
    @BeforeEach
    public void testSetup(){
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        templateEngine = mock(TemplateEngine.class);
        response = mock(Response.class);
        // friendly objects
        gameCenter = new GameCenter();

        CuT = new GetSignInRoute(gameCenter, templateEngine);
    }

    /**
     * Tests if the display of the sign in route is proper
     */
    @Test
    public void validateCorrectDisplay(){
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(templateEngine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName("signin.ftl");
    }
}
