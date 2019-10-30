package com.webcheckers.ui;

import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.ReplayGame;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class PostReplayPreviousTurnRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostReplayPreviousTurnRoute.class.getName());

    // FreeMarker
    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST/replay/PreviousTurn} HTTP requests
     *
     * @param templateEngine the HTML template rendering engine
     */
    public PostReplayPreviousTurnRoute(TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("PostReplayPreviousTurnRoute is initialized.");
    }

    /**
     * Render the WebCheckers Game in page.
     * Performs the next saved move in the game.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the Game in page.
     */
    @Override
    public Object handle(Request request, Response response) throws Exception {
        LOG.finer("GetReplayPreviousTurnRoute is invoked.");
        // start building the view-model
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Game");

        // retrieve the PlayerServices from the session
        PlayerServices current = request.session().attribute("PlayerServices");
        String gameString = request.queryParams("gameID");
        ReplayGame game = current.getSavedGame(gameString);

        // attempt to revert the configuration on the saved game
        boolean previous = current.tryPreviousReplayMove();
        if (previous) {
            return Message.info("true");
        }
        return Message.error("Error in getting previous turn.");
    }
}
