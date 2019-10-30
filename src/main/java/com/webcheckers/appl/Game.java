package com.webcheckers.appl;

import com.webcheckers.model.Board;
import com.webcheckers.model.Move;
import com.webcheckers.ui.BoardView;
import com.webcheckers.ui.Piece;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * A class to represent a checkers game.
 */

public class Game {

    // player1 is the RED player
    private PlayerServices player1;
    // player2 is the WHITE player
    private PlayerServices player2;

    // the GameCenter manages this game
    private GameCenter gameCenter;

    // the UI BoardView
    private BoardView boardView;

    // the Model Board
    private Board board;

    // attributes used by User JS
    private final Map<String, Object> modeOptions;
    private Gson gson;

    // flag to indicate replay mode being active
    public boolean replayMode = false;


    /**
     * Create a new checkers game.
     *
     * @param player1 RED player
     * @param player2 WHITE player
     * @param gamecenter the GameCenter this game is assigned to
     */
    public Game(PlayerServices player1, PlayerServices player2, GameCenter gamecenter) {
        this.boardView = null;
        this.player1 = player1;
        this.player2 = player2;
        this.gameCenter = gamecenter;
        this.board = new Board();
        this.gson = new Gson();
        this.modeOptions = new HashMap<>(2);
        this.modeOptions.put("isGameOver", false);
        this.modeOptions.put("gameOverMessage", null);
    }

    /**
     * player1 getter
     *
     * @return RED player
     */
    public PlayerServices getPlayer1() {
        return this.player1;
    }

    /**
     * Returns the Id of the player that challenged
     *
     * @return Player 1 Id
     */
    public String getP1ID(){
        return getPlayer1().Id();
    }


    /**
     * player2 getter
     *
     * @return WHITE player
     */
    public PlayerServices getPlayer2() {
        return this.player2;
    }


    /**
     * Generate attributes used by the UI gameView.
     *
     * @param currentPlayer PlayerServices object looking for attributes.
     * @return HashMap of JS-readable attributes
     */
    public Map getAttributes(PlayerServices currentPlayer) {
        Map<String, Object> vm = new HashMap<>();

        vm.put("PlayerServices", currentPlayer);
        vm.put("Player1", this.player1);
        vm.put("Player2", this.player2);
        vm.put("activeColor", this.board.getActiveColor());

        BoardView boardView = new BoardView(this.board);
        if (currentPlayer.Id().equals(this.player1.Id()) || currentPlayer.isSpectating()){
            vm.put("board", boardView);
        } else {
            boardView.flip();
            vm.put("board", boardView);
        }

        vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));

        String currentPlayerID = currentPlayer.Id();
        if (this.replayMode) {
            vm.put("viewMode", "REPLAY");
            vm.put("activeColor", Piece.Color.RED);
        } else if (!currentPlayerID.equals(this.player1.Id()) && !currentPlayerID.equals(this.player2.Id())) {
            vm.put("viewMode", "SPECTATOR");
        } else {
            vm.put("viewMode", "PLAY");
        }

        return vm;
    }

    /**
     * Checks whose turn it is in the checkers game
     *
     * @param player a player
     * @return true if it is the player's turn, false if it is not
     */
    public boolean isMyTurn(PlayerServices player) {
        // player1 has called, should be RED's turn
        if (player.Id().equals(this.player1.Id())) {
            return (this.board.getActiveColor().equals(Piece.Color.RED));
        // player2 has called, should be WHITE's turn
        } else {
            return (this.board.getActiveColor().equals(Piece.Color.WHITE));
        }
    }

    /**
     * Checks with the Board class if the move made is under the American rules of checkers
     *
     * @param move the move made by the player
     * @return true if the move is valid and false if it is not
     */
    public boolean isValidMove(Move move) {
        return this.board.isValidMove(move);
    }

    /**
     * The method that uses the trySubmitTurn from the Board class
     *
     * @return Message to the player about their move when they click the submit button
     */
	public String trySubmitTurn() {
		return this.board.trySubmitTurn();
	}

    /**
     * The method that uses the backUpMove method in the board class
     *
     * @return Message about the player correctly backing up the move that they made
     */
    public String backUpMove() {
        String canBackUp = this.board.backUpMove();
        return canBackUp;
    }

    /**
     * Sets the game status to be over
     *
     * @param gameOverMessage  the string that sends the game over message to the game
     * @return true if the game is over and false if it is not
     */
    public boolean setGameOver(String gameOverMessage) {
        if(((boolean) modeOptions.get("isGameOver"))) {
            return false;
        }
        this.modeOptions.put("isGameOver", true);
        this.modeOptions.put("gameOverMessage", gameOverMessage);
        return true;
    }

    /**
     * Checks if the team is eliminated through the amount of pieces remaining
     *
     * @return true or false if the team is eliminated
     */
	public boolean teamIsEliminated() {
		return board.teamIsEliminated();
    }

    /**
     * Checks if the checkers game is over
     *
     * @return true or false if the game is over
     */
	public boolean checkIfGameOver() {
		return (boolean)this.modeOptions.get("isGameOver");
	}

    /**
     * Clears the move list that was generated during a player's turn
     *
     * @param player the player that just ended their turn
     * @return true or false if the move list has been cleared
     */
	public boolean resetMoves(PlayerServices player) {
		return this.board.resetMoves(player);
	}

    /**
     * Checks if a single piece needs to be made king
     */
	public void checkMakeKing() {
		this.board.checkMakeKing();
	}

    /**
     * Switches the turn from player to player
     *
     * @param player the player that just ended their turn
     */
    public void switchTurn(PlayerServices player) {
        if (player.Id().equals(this.player1.Id())) {
            this.board.changeActiveColor(Piece.Color.WHITE);
        } else {
            this.board.changeActiveColor(Piece.Color.RED);
        }
    }

    /**
     * Checks if the player can make any moves left in the checkers game and ends the game if they do not
     *
     * @return true if the player has moves left and false if they do not
     */
	public boolean hasMovesLeft() {
		return this.board.hasMovesLeft();
	}

	public Piece.Color getActiveColor() {
		return this.board.getActiveColor();
	}

    /**
     * Getter for this Game's Board
     *
     * @return this Game's Board
     */
    public Board getBoard() {
	    return this.board;
    }

    /**
     * Adds a configuration to a saved game.
     * Called when the opponent player submits a move.
     * This configuration must be added here to ensure correct config order.
     *
     * @param current PlayerServices object that calls the method.
     */
    public void giveOtherPlayerConfiguration(PlayerServices current) {
        if (current.Id().equals(player1.Id())) {
            this.player2.saveGame.addConfiguration(this.board);
        } else {
            this.player1.saveGame.addConfiguration(this.board);
        }
    }
}
