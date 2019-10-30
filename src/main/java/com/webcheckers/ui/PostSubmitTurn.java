package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.webcheckers.appl.PlayerServices;
import com.webcheckers.model.Move;
import com.webcheckers.util.Message;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;
import spark.TemplateEngine;
import static spark.Spark.halt;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.Game;

/**
 * The POST /submitTurn route handler.
 *
 */

public class PostSubmitTurn implements Route{
    private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());

    private final TemplateEngine templateEngine;

    PostSubmitTurn(TemplateEngine templateEngine) {
        LOG.config("PostSubmitTurn is initialized.");

        this.templateEngine = templateEngine;
    }

    @Override
    public Message handle(Request request, Response response) {
        LOG.finer("PostSubmitTurn is invoked.");

        PlayerServices current = request.session().attribute("PlayerServices");
		//get move recent move from current Player
        // submit it to Board for advanced validation
        // if valid: continue on to end of game check below and return Message info
        // if invalid: return error
		String turnString = current.trySubmitTurn();
		if (!turnString.equals("true")) {
			return Message.error(turnString);
		}
		
		//This only should happen after the players move is determined to be valid.
		Game currentGame = current.getCurrentGame();
		currentGame.checkMakeKing();
		current.addReplayConfiguration();
		if (currentGame.teamIsEliminated()) {
			String gameOverMessage = current.Id() + " has captured all pieces.";
			currentGame.setGameOver(gameOverMessage);
			current.saveCurrentGame();
			return Message.info("true");
		}
		currentGame.switchTurn(current);
		
		String losingPlayerID;
		String p1ID = currentGame.getP1ID();
		if (current.Id().equals(p1ID)) {
			losingPlayerID = currentGame.getPlayer2().Id();
		} else {
			losingPlayerID = p1ID;
		}
		//Check if the player has any possible moves, if not, end the game.
		if (!current.getCurrentGame().hasMovesLeft() && !current.currentGameIsOver()) {
			//Player has no moves left
			String gameOverMessage = losingPlayerID + " has no moves left.";
			current.getCurrentGame().setGameOver(gameOverMessage);
			current.saveCurrentGame();
		}
		
		return Message.info("true");
    }
}
