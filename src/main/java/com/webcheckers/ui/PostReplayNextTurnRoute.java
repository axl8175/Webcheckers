package com.webcheckers.ui;

import com.webcheckers.appl.Game;
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

public class PostReplayNextTurnRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostReplayNextTurnRoute.class.getName());

    // FreeMarker
    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /replay/NextTurn} HTTP requests
     *
     * @param templateEngine the HTML template rendering engine
     */
    public PostReplayNextTurnRoute(TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("PostReplayNextTurnRoute is initialized.");
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
    public Object handle(Request request, Response response) {
        LOG.finer("GetReplayNextTurnRoute is invoked.");
        // start building the view-model
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Game");

        // retrieve the PlayerServices from the session
        PlayerServices current = request.session().attribute("PlayerServices");
        String gameString = request.queryParams("gameID");
        ReplayGame game = current.getSavedGame(gameString);

        // attempt to perform the next move on the saved game
        boolean next = current.tryNextReplayMove();
        if (next) {
            return Message.info("true");
        }
        return Message.error("Error in getting next turn.");
    }
}
