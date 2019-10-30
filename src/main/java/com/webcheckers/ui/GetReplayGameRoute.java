package com.webcheckers.ui;

import com.webcheckers.appl.Game;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.ReplayGame;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * the UI Controller to GET the Replay-Game page.
 * This Route will be accessed when someone chooses to replay one of their previous games.
 *
 */

public class GetReplayGameRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetReplayGameRoute.class.getName());

    // FreeMarker
    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /replay/game} HTTP requests
     *
     * @param templateEngine the HTML template rendering engine
     */
    public GetReplayGameRoute(TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("GetReplayGameRoute is initialized.");
    }

    /**
     * Render the WebCheckers Game in page.
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the Game in page.
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetReplayGameRoute is invoked.");
        // start building the view-model
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Game");

        // retrieve the PlayerServices from the session
        PlayerServices current = request.session().attribute("PlayerServices");
        if (current == null) {
            vm.put("title", "Welcome!");
            // redirect back to home page
            response.redirect(WebServer.HOME_URL);
            return templateEngine.render(new ModelAndView(vm, "home.ftl"));
        }

        // retrieve the game this player is trying to spectate
        String gameString = request.queryParams("game");
        ReplayGame game = current.getSavedGame(gameString);

        // set the Game to spectate mode
        current.setReplayMode(gameString);

        // get required JS attributes from Game and add them to VM
        Map attributes = game.getAttributes(current);
        vm.putAll(attributes);

        return templateEngine.render(new ModelAndView(vm, "game.ftl"));
    }
}
