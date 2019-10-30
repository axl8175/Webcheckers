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
 * The UI Controller to POST the Spectator-Check-Turn route.
 *
 */
public class PostSpectatorCheckTurnRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostSpectatorCheckTurnRoute.class.getName());

    // FreeMarker
    private final TemplateEngine templateEngine;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /spectator/checkTurn} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     */
    public PostSpectatorCheckTurnRoute(final TemplateEngine templateEngine) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        //
        LOG.config("PostSpectatorCheckTurnRoute is initialized.");
    }

    /**
     * Returns a message stating if there are new actions needed to be updated
	 * for the spectator.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the message stating if there are new actions
     */
    @Override
    public Message handle(Request request, Response response) {
		Message message;
		PlayerServices current = request.session().attribute("PlayerServices");
		if (!current.getCurrentGame().getActiveColor().equals(current.getLastKnownColor())) {
			current.setLastKnownColor(current.getCurrentGame().getActiveColor());
			message = Message.info("true");
		}
		else {
			message = Message.info("false");
		}
        return message;
    }
}
