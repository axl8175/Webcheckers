package com.webcheckers.ui;

import com.webcheckers.appl.PlayerServices;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class GetReplayStopWatchingRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetReplayStopWatchingRoute.class.getName());

    // FreeMarker
    private final TemplateEngine templateEngine;

    /**
     * Create the Spare Route (UI controller) to handle all {@code GET /replay/stopWatching} HTTP requests.
     *
     * @param templateEngine the HTML template rendering engine
     */
    public GetReplayStopWatchingRoute(TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        LOG.config("etReplayStopWatchingRoute is initialized.");
    }

    /**
     *
     * @param request the HTTP request
     * @param response the HTTP response
     * @return the rendered HTML for the home page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetReplayStopWatchingRoute is invoked.");

        // end the ReplayGame that this Player was watching
        PlayerServices current = request.session().attribute("PlayerServices");
        current.endCurrentGame();
        current.replayingGame.setCurrConfigIndex(0);
        current.replayingGame.setButton("none");

        // build VM
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Welcome!");

        response.redirect(WebServer.HOME_URL);
        return templateEngine.render(new ModelAndView(vm, "home.ftl"));
    }
}
