package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import com.webcheckers.appl.GameCenter;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.util.Message;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 */
public class GetHomeRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  private static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");

  private final GameCenter gameCenter;
  private final TemplateEngine templateEngine;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /home} HTTP requests.
   *
   * @param gameCenter
   *    The {@link GameCenter} for the application.
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetHomeRoute(final GameCenter gameCenter, final TemplateEngine templateEngine) {
	this.gameCenter = Objects.requireNonNull(gameCenter, "gameCenter is required");
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    //
    LOG.config("GetHomeRoute is initialized.");
  }

  /**
   * Render the WebCheckers Home page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("GetHomeRoute is invoked.");

    //start building the view model
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", WELCOME_MSG.getText());
	vm.put("totalPlayers", gameCenter.getTotalPlayers());

	// if the user is singed in
    if (request.session().attributes().contains("PlayerServices")) {
      // retrieve the PlayerServices from the session
      PlayerServices ps = request.session().attribute("PlayerServices");
	  if (ps.getCurrentGame() != null && ps.currentGameIsOver()) {
		  ps.endCurrentGame();
	  }
      vm.put("PlayerServices", ps);
	  vm.put("Players", gameCenter.getPlayers());
	  vm.put("savedGames", ps.savedGames);
	  // if the home page is rendering after user clicked a busy player
	  if (ps.enteredBusy) {
        vm.put("Error", "Player is busy.");
      }
    }
    return templateEngine.render(new ModelAndView(vm, "home.ftl"));
  }
}
