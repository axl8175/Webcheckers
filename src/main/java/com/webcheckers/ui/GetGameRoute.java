package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.appl.Game;
import com.webcheckers.appl.PlayerServices;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import com.webcheckers.appl.GameCenter;

/**
 * The UI Controller to GET the Game page.
 * This Route will only be accessed when a user is redirected after being selected.
 *
 */
public class GetGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());

    // this sessions associated GameCenter
    private final GameCenter gameCenter;
    // FreeMarker
    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /game} HTTP requests.
     *
     * @param gameCenter
     *    The {@link GameCenter} for the application.
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetGameRoute(final GameCenter gameCenter, final TemplateEngine templateEngine) {
        this.gameCenter = Objects.requireNonNull(gameCenter, "gameCenter is required");
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        //
        LOG.config("GetGameRoute is initialized.");
    }

    /**
     * Render the WebCheckers Game In page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Game In page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetGameRoute is invoked.");
        // start building the view-model
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "In Game.");

        // retrieve the PlayerServices from the session
        PlayerServices current = request.session().attribute("PlayerServices");
		//If the player enters this page without being signed in or in a valid game, 
		if (current == null) {
			vm.put("title", "Welcome!");
			//redirect back to home page
			response.redirect(WebServer.HOME_URL);
			return templateEngine.render(new ModelAndView(vm, "home.ftl"));
		}
		else if (current.getCurrentGame() == null && !current.isReplaying) {
			vm.put("title", "Welcome!");
			//redirect back to home page
			response.redirect(WebServer.HOME_URL);
			return templateEngine.render(new ModelAndView(vm, "home.ftl"));
		}
        // retrieve the game this player is entering
        Game game = current.getCurrentGame();

        // get required JS attributes from Game and add them to
        Map attributes = game.getAttributes(current);
        vm.putAll(attributes);
		

        // render the View
        return templateEngine.render(new ModelAndView(vm , "game.ftl"));
    }
}
