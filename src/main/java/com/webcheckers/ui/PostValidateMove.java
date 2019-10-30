package com.webcheckers.ui;

import java.util.logging.Logger;

import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.Move;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;


/**
 * The POST /validateMove route handler.
 *
 */

public class PostValidateMove implements Route{
    private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());

    private final TemplateEngine templateEngine;

    PostValidateMove(TemplateEngine templateEngine) {
        LOG.config("PostValidateMove is initialized.");

        this.templateEngine = templateEngine;
    }

    @Override
    public Message handle(Request request, Response response) {
        LOG.finer("PostValidateMove is invoked.");

        // Move object is sent over in String format. Must be retrieved and parsed.
        String moveString = request.queryParams("actionData");
        Move move = Move.parseMove(moveString);

        PlayerServices current = request.session().attribute("PlayerServices");

        // send validateMove to model board for validity check
        boolean isValid = current.isValidMove(move);
        if (isValid) {
            return Message.info("Valid Move!");
        } else {
            return Message.error("Invalid move!");
        }
    }
}
