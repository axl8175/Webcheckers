package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.appl.PlayerServices;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import com.webcheckers.appl.GameCenter;

/**
 * The {@code POST /signout} route handler.
 *
 */
public class PostSignOutRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostSignOutRoute.class.getName());

  //
  // Attributes
  //
  private final GameCenter gameCenter;
  private final TemplateEngine templateEngine;

  //
  // Constructor
  //
  /**
   * The constructor for the {@code POST /signout} route handler.
   *
   * @param gameCenter
   *    The {@link GameCenter} for the application.
   * @param templateEngine
   *    template engine to use for rendering HTML page
   *
   * @throws NullPointerException
   *    when the {@code templateEngine} parameter is null
   */
  PostSignOutRoute(final GameCenter gameCenter, TemplateEngine templateEngine) {
    LOG.config("PostSignOutRoute is initialized.");
    // validation
	Objects.requireNonNull(gameCenter, "gameCenter must not be null");
    Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    //
	this.gameCenter = gameCenter;
    this.templateEngine = templateEngine;
  }

  //
  // TemplateViewRoute method
  //
  /**
   * {@inheritDoc}
   *
   */
  @Override
  public String handle(Request request, Response response) {
    LOG.finer("PostSignOutRoute is invoked.");
	
	//Remove the player from the GameCenter and the Session
	PlayerServices current = request.session().attribute("PlayerServices");
	this.gameCenter.removePlayer(current);
	request.session().attribute("PlayerServices", null);

	Map<String, Object> vm = new HashMap<>();
	vm.put("title", "Welcome!");
	//redirect back to home page
	response.redirect(WebServer.HOME_URL);
	return templateEngine.render(new ModelAndView(vm, "home.ftl"));
  }
}
