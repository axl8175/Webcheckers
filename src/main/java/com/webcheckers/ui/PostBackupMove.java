package com.webcheckers.ui;

import java.util.logging.Logger;

import com.webcheckers.appl.PlayerServices;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

/**
 * The POST /backupMove route handler.
 *
 */

public class PostBackupMove implements Route{
    private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());

    private final TemplateEngine templateEngine;

    PostBackupMove(TemplateEngine templateEngine) {
        LOG.config("PostBackupMove is initialized.");

        this.templateEngine = templateEngine;
    }

    @Override
    public Message handle(Request request, Response response) {
        LOG.finer("PostBackupMove is invoked.");

        PlayerServices current = request.session().attribute("PlayerServices");
        String canBackUp = current.backUpMove();
        if (canBackUp.equals("true")) {
            return Message.info("Your move was reverted.");
        } else {
            return Message.error(canBackUp);
        }
    }
}
