package com.webcheckers.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import com.webcheckers.appl.PlayerServices;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;

import com.webcheckers.appl.GameCenter;

/**
 * The {@code POST /signin} route handler.
 *
 */
public class PostSignInRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostSignInRoute.class.getName());

  // Values used in the view-model map for rendering the page after the user submits their name.
  static final String INVALID_NAME = "Invalid name, please try another...";
  static final String TAKEN_NAME = "Name is already in use. Please try another...";

  //
  // Attributes
  //
  private final GameCenter gameCenter;
  private final TemplateEngine templateEngine;

  //
  // Constructor
  //
  /**
   * The constructor for the {@code POST /signin} route handler.
   *
   * @param gameCenter
   *    The {@link GameCenter} for the application.
   * @param templateEngine
   *    template engine to use for rendering HTML page
   *
   * @throws NullPointerException
   *    when the {@code templateEngine} parameter is null
   */
  PostSignInRoute(final GameCenter gameCenter, TemplateEngine templateEngine) {
    LOG.config("PostSignInRoute is initialized.");
    // validation
	Objects.requireNonNull(gameCenter, "gameCenter must not be null");
    Objects.requireNonNull(templateEngine, "templateEngine must not be null");
    //
	this.gameCenter = gameCenter;
    this.templateEngine = templateEngine;
  }

    /**
     * Check if a provided username is valid.
     * Can contain letters a-z, A-Z, and numbers 0-9.
     *
     * @param username String to test
     * @return true if username contains valid characters, false otherwise.
     */
  private boolean usernameIsValid(String username) {
      String stripped = username.replaceAll("\\s","");
      if (stripped.equals("")) {
          return false;
      }
      Pattern p = Pattern.compile("[^a-zA-Z0-9]");
      boolean hasSpecialChar = p.matcher(stripped).find();
      return !hasSpecialChar;
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
    LOG.finer("PostSignInRoute is invoked.");

    //start building the view model
    Map<String, Object> vm = new HashMap<>();
    vm.put("title", "Welcome!");

    //retrieve the username
      String username = request.queryParams("username");

      // check if name is valid
      if (usernameIsValid(username)) {
		  // check if name is taken
		  if (!gameCenter.nameIsTaken(username)) {
			  // create new player and add to session
			  PlayerServices ps = new PlayerServices(username, this.gameCenter);
			  request.session().attribute("PlayerServices", ps);
			  //redirect back to home page
			  response.redirect(WebServer.HOME_URL);
			  return templateEngine.render(new ModelAndView(vm, "home.ftl"));
		  }
		  else {
			  // error and redirect back to sign in
			  vm.put("Error", TAKEN_NAME);
			  return templateEngine.render(new ModelAndView(vm, "signin.ftl"));
		  }
      } else {
          // error and redirect back to sign in
          vm.put("Error", INVALID_NAME);
          return templateEngine.render(new ModelAndView(vm, "signin.ftl"));
      }
  }
}
