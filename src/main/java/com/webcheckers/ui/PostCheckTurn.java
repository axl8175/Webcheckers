package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.webcheckers.appl.PlayerServices;
import com.webcheckers.util.Message;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;
import static spark.Spark.halt;

import com.webcheckers.appl.GameCenter;

/**
 * The POST /checkTurn route handler.
 *
 */

public class PostCheckTurn implements Route{
    private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());

    private final TemplateEngine templateEngine;

    PostCheckTurn(TemplateEngine templateEngine) {
        LOG.config("PostCheckTurn is initialized.");

        this.templateEngine = templateEngine;
    }

    @Override
    public Message handle(Request request, Response response) {
        LOG.finer("PostCheckTurn is invoked.");

        PlayerServices current = request.session().attribute("PlayerServices");
		//Makes sure the user didn't open up the game page to start off with and cause server errors.
		if (current == null) {
			response.redirect(WebServer.HOME_URL);
            return Message.error("Invalid player!");
		} else if (current.getCurrentGame() == null) {
			response.redirect(WebServer.HOME_URL);
            return Message.error("Invalid game!");
		}
        boolean isTurn = current.isMyTurn();
        String isTurnString;
        if (isTurn) {
            isTurnString = "true";
        } else {
            isTurnString = "false";
        }
        Message message = Message.info(isTurnString);

        return message;
    }
}
