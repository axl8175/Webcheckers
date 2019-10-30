package com.webcheckers.model;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerServices;
import com.webcheckers.ui.BoardView;
import com.webcheckers.ui.Piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A class to represent a Game that a PlayerServices has saved.
 */

public class ReplayGame {

    // player1 is the RED player
    private PlayerServices player1;
    // player2 is the WHITE player
    private PlayerServices player2;

    // the UI BoardView
    private BoardView boardView;

    // collections of Board configurations
    private ArrayList<Board> boardConfigurations;
    // current configuration index
    private int currentConfigurationIndex = 0;

    // attributes used by User JS
    private final Map<String, Object> modeOptions;
    private Gson gson;

    // flag to indicate which button (next/previous) was clicked.
    private String button;

    public ReplayGame(PlayerServices player1, PlayerServices player2, Board initialConfiguration) {
        this.player1 = player1;
        this.player2 = player2;
        this.boardView = null;
        this.boardConfigurations = new ArrayList<>();
        this.modeOptions = new HashMap<>();
        this.modeOptions.put("isGameOver", false);
        this.gson = new Gson();
        this.button = "none";

        // the initial configuration is always a vanilla checkers Board
        addConfiguration(initialConfiguration);
    }

    /**
     * Get the JS attributes used for rendering a ReplayGame on game.ftl
     *
     * @param currentPlayer PlayerServices object that is replaying the ReplayGame
     * @return Collection of JS attributes.
     */
    public Map getAttributes(PlayerServices currentPlayer) {
        Map<String, Object> vm = new HashMap<>();
        vm.put("PlayerServices", currentPlayer);
        vm.put("Player1", this.player1);
        vm.put("Player2", this.player2);
        vm.put("viewMode", "REPLAY");
        // button will determine if configuration moves forward/backward
        if (this.button.equals("next")) {
            currentConfigurationIndex++;
        } else if (this.button.equals("previous")) {
            currentConfigurationIndex--;
        } else {
            ;
        }
        // the new configuration that will be rendered
        Board currentConfiguration = this.boardConfigurations.get(this.currentConfigurationIndex);
        // determine which buttons to activate
        if (hasNextConfigurations()) {
            this.modeOptions.put("hasNext", true);
        } else {
            this.modeOptions.put("hasNext", false);
        }
        if (hasPreviousConfigurations()) {
            this.modeOptions.put("hasPrevious", true);
        } else {
            this.modeOptions.put("hasPrevious", false);
        }
        vm.put("activeColor", currentConfiguration.getActiveColor());
        // create a new BoardView based on the current Configuration
        BoardView boardView = new BoardView(currentConfiguration);
        // may need to flip if white player
        if (currentPlayer.Id().equals(this.player1.Id())){
            vm.put("board", boardView);
        } else {
            boardView.flip();
            vm.put("board", boardView);
        }
        vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));

        return vm;
    }

    /**
     * Unique String creator to identify this ReplayGame
     * Used each PlayerServices object
     *
     * @return Player1VsPlayer2 unique String
     */
    public String getPlayerVsPlayer() {
        String s = this.player1.Id() + " vs. " + this.player2.Id();
        return s;
    }

    /**
     * Attempt to set the configuration to the next one in the collection.
     * The button flag is set to update the configuration in getAttributes().
     *
     * @return true
     */
    public boolean tryNextReplayMove() {
        this.button = "next";
        return true;
    }

    /**
     * Add a Board configuration to this ReplayGame collection.
     * Iterates through the passed in board and makes a deep copy of it.
     *
     * @param board Board to copy
     */
    public void addConfiguration(Board board) {
        Board newBoard = new Board();
        Square[][] b = board.getBoard();
        for (int row = 0; row < 8; row ++) {
            for (int col = 0; col < 8; col ++) {
                // remove any vanilla pieces that remain
                newBoard.getBoard()[row][col].removePiece();
                Square oldSquare = b[row][col];
                if (oldSquare.getColor() == Square.Color.WHITE) {
                    newBoard.getBoard()[row][col] = new Square(Square.Color.WHITE);
                } else {
                    if (oldSquare.getPiece() != null) {
                        Piece.Color pieceColor = oldSquare.getPieceColor();
                        newBoard.getBoard()[row][col].addPiece(pieceColor);
                        if (oldSquare.getPiece().getType() == Piece.Type.KING) {
                            newBoard.getBoard()[row][col].getPiece().makeKing();
                        }
                    }
                }
            }
        }
        this.boardConfigurations.add(newBoard);
    }

    /**
     * Determine if there is a next configuration in the collection.
     *
     * @return true if there is another config, false otherwise.
     */
    protected boolean hasNextConfigurations() {
        return (this.currentConfigurationIndex < this.boardConfigurations.size()-1);
    }

    /**
     * Determine if there is a previous configuration in the collection.
     *
     * @return true if there is a previous config, false otherwise.
     */
    protected boolean hasPreviousConfigurations() {
        return (this.currentConfigurationIndex > 0);
    }

    /**
     * Attempt to revert the configuration of this ReplayGame.
     *
     * @return true
     */
    public boolean tryPreviousReplayMove() {
        this.button = "previous";
        return true;
    }

    /**
     * Setter method for currentConfigurationIndex, used to maintain encapsulation
     *
     * @param currentConfigurationIndex the new currentConfigurationIndex
     */
    public void setCurrConfigIndex(int currentConfigurationIndex) {
        this.currentConfigurationIndex = currentConfigurationIndex;
    }

    /**
     * Getter method for currentConfigurationIndex, used to maintain encapsulation
     *
     * @return int - the currentConfigurationIndex
     */
    public int getCurrConfigIndex() {
        return currentConfigurationIndex;
    }

    /**
     * Getter method for button, used to maintain encapsulation
     *
     * @return String - the button
     */
    public String getButton() {
        return button;
    }

    /**
     * Setter method for button, used to maintain encapsulation
     *
     * @param button the new button string
     * @return boolean - true if the button was set, false if not
     */
    public boolean setButton(String button) {
        if (button.equals("none") || button.equals("next") || button.equals("previous")) {
            this.button = button;
            return true;
        } else {
            return false;
        }
    }
}
