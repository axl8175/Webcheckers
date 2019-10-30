package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.util.Message;

/**
 * The UI Controller to GET the Sign In  page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetSignInRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetSignInRoute.class.getName());

    private static final Message WELCOME_MSG = Message.info("Sign in.");

    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /signin} HTTP requests.
     *
	 * @param gameCenter
   *    The {@link GameCenter} for the application.
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetSignInRoute(final GameCenter gameCenter, final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        //
        LOG.config("GetSignInRoute is initialized.");
    }

    /**
     * Render the WebCheckers Sign In page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Sign In page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetSignInRoute is invoked.");
        //
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", WELCOME_MSG.getText());

        // display a user message in the Sign In page
        vm.put("message", WELCOME_MSG);

        // render the View
        return templateEngine.render(new ModelAndView(vm , "signin.ftl"));
    }
}
