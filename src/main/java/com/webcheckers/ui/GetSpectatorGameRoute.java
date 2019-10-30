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
import com.webcheckers.util.Message;

/**
 * The UI Controller to GET the Spectator-Game page.
 * This Route will be accessed when someone chooses to spectate another player.
 *
 */
public class GetSpectatorGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetSpectatorGameRoute.class.getName());

	private final GameCenter gameCenter;
    // FreeMarker
    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /spectator/game} HTTP requests.
     *
	 * @param gameCenter
     *    The {@link GameCenter} for the application.
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetSpectatorGameRoute(final GameCenter gameCenter, final TemplateEngine templateEngine) {
		this.gameCenter = Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        //
        LOG.config("GetSpectatorGameRoute is initialized.");
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
        LOG.finer("GetSpectatorGameRoute is invoked.");
        // start building the view-model
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Game");

        // retrieve the PlayerServices from the session
        PlayerServices current = request.session().attribute("PlayerServices");
		//If the player enters this page without being signed in or in a valid game, 
		if (current == null) {
			vm.put("title", "Welcome!");
			//redirect back to home page
			response.redirect(WebServer.HOME_URL);
			return templateEngine.render(new ModelAndView(vm, "home.ftl"));
		}
		else if (current.getCurrentGame() == null) {
			String opponent = request.queryParams("opponent");
			PlayerServices opponentPlayer = gameCenter.getPlayerById(opponent);
			if (opponentPlayer == null || opponentPlayer.getCurrentGame() == null) {
				vm.put("title", "Welcome!");
				//redirect back to home page
				response.redirect(WebServer.HOME_URL);
				return templateEngine.render(new ModelAndView(vm, "home.ftl"));
			}
			else {
				current.addGame(opponentPlayer.getCurrentGame());
				current.toggleSpectate();
			}
		}
        // retrieve the game this player is entering
        Game game = current.getCurrentGame();
		current.setLastKnownColor(game.getActiveColor());

        // get required JS attributes from Game and add them to
        Map attributes = game.getAttributes(current);
        vm.putAll(attributes);
		

        // render the View
        return templateEngine.render(new ModelAndView(vm , "game.ftl"));
    }
}
