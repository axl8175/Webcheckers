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
 * The UI Controller to GET the Spectator-Stop-Watching route.
 *
 */
public class GetSpectatorStopWatchingRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetSpectatorStopWatchingRoute.class.getName());

    // FreeMarker
    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /spectator/game} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public GetSpectatorStopWatchingRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        //
        LOG.config("GetSpectatorStopWatchingRoute is initialized.");
    }

    /**
     * 
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the home page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetSpectatorStopWatchingRoute is invoked.");
		PlayerServices current = request.session().attribute("PlayerServices");
		current.endCurrentGame();
		current.setLastKnownColor(null);
		if (current.isSpectating()) {
			current.toggleSpectate();
		}
        // start building the view-model
        Map<String, Object> vm = new HashMap<>();
		vm.put("title", "Welcome!");
		//redirect back to home page
		response.redirect(WebServer.HOME_URL);
		return templateEngine.render(new ModelAndView(vm, "home.ftl"));
    }
}
