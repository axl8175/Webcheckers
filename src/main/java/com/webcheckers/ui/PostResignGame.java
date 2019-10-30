package com.webcheckers.ui;

import java.util.logging.Logger;

import com.webcheckers.appl.PlayerServices;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

/**
 * The POST /resignGame route handler.
 *
 */

public class PostResignGame implements Route{
    private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());

    private final TemplateEngine templateEngine;

    PostResignGame(TemplateEngine templateEngine) {
        LOG.config("PostResignGame is initialized.");

        this.templateEngine = templateEngine;
    }

    @Override
    public Message handle(Request request, Response response) {
        LOG.finer("PostResignGame is invoked.");

        PlayerServices current = request.session().attribute("PlayerServices");

        if (current == null) {
            return Message.error("Error: no Player initialized.");
        }

        String gameOverMessage = current.Id() + " has resigned.";

        if(current.getCurrentGame().setGameOver(gameOverMessage)) {
            current.endCurrentGame();
            return Message.info("true");
        // it is possible that the opponent player has resigned just before this player did.
        } else {
            return Message.error("Your opponent has resigned before you! Refresh page to exit game.");
        }

    }
}
