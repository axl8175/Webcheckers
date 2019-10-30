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
 * The UI Controller to GET the Game In  page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class PostGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostGameRoute.class.getName());

    private static final Message WELCOME_MSG = Message.info("In Game.");

    private final GameCenter gameCenter;
    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /game} HTTP requests.
     *
     * @param gameCenter
     *    The {@link GameCenter} for the application.
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostGameRoute(final GameCenter gameCenter, final TemplateEngine templateEngine) {
        LOG.config("PostGameRoute is initialized.");
        //validation
        Objects.requireNonNull(gameCenter, "gameCenter must not be null");
        Objects.requireNonNull(templateEngine, "templateEngine must not be null");
        // state
        this.gameCenter = Objects.requireNonNull(gameCenter, "gameCenter is required");
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
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
        LOG.finer("PostGameRoute is invoked.");

        //start building the view model
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", WELCOME_MSG.getText());

		// retrieve PlayerServices from session
        PlayerServices current = request.session().attribute("PlayerServices");
		//This happens if/when the user refreshes on the game page
		if (current.getCurrentGame() != null) {
			// add JS attributes
            Game game = current.getCurrentGame();
			game.resetMoves(current);
            vm.putAll(game.getAttributes(current));
			return templateEngine.render(new ModelAndView(vm, "game.ftl"));
		}
        //retrieve the opponent
        String opponent = request.queryParams("opponent");

        //check if opponent is available
        if (!gameCenter.isPlayerAvailable(opponent)) {
            // this user clicked a busy user
            current.setEnteredBusy();
            // redirect back to home
            response.redirect(WebServer.HOME_URL);
            return templateEngine.render(new ModelAndView(vm, "home.ftl"));
        } else {
            // give each player the game
            String player = current.Id();
            PlayerServices opponentPlayer = gameCenter.getPlayerById(opponent);
            gameCenter.createGame(player, opponent);
            // add JS attributes
            Game game = current.getCurrentGame();
            vm.putAll(game.getAttributes(current));
            return templateEngine.render(new ModelAndView(vm, "game.ftl"));
        }
    }
}
