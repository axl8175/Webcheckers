package com.webcheckers.model;

import com.webcheckers.ui.Piece;
import java.util.ArrayList;
import com.webcheckers.appl.PlayerServices;

/**
 * A class to represent the checkers board for a Game.
 */

public class Board {
	//the 2D array that is the board
    private Square[][] board;
	//the color of the player whose turn it is
    private Piece.Color activeColor;
	//a list of moves
    private ArrayList<Move> moveList;

    //the amount of white pieces in the checkers game
	private int whitePieces;
	//the amount of red pieces in the checkers game
	private int redPieces;


	/**
	 * Constructor for the checkers board that also places the pieces in their initial starting positions
	 */
    public Board() {
        this.board = new Square[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                // if row is even, white first
                if (row % 2 == 0) {
                    if (col % 2 == 0) {
                        board[row][col] = new Square(Square.Color.WHITE);
                    } else {
                    	// black squares may have pieces on them
                        board[row][col] = new Square(Square.Color.BLACK);
                        if (row <= 2) {
                            board[row][col].addPiece(Piece.Color.WHITE);
                        } else if (row >= 5) {
                            board[row][col].addPiece(Piece.Color.RED);
                        }
                    }
                } else {
                    if (col % 2 == 0) {
                    	// black squares may have pieces on them
                        board[row][col] = new Square(Square.Color.BLACK);
                        if (row <= 2) {
                            board[row][col].addPiece(Piece.Color.WHITE);
                        } else if (row >= 5) {
                            board[row][col].addPiece(Piece.Color.RED);
                        }
                    } else {
                        board[row][col] = new Square(Square.Color.WHITE);
                    }
                }
            }
        }
        this.activeColor = Piece.Color.RED;
		this.whitePieces = 12;
		this.redPieces = 12;
		this.moveList = new ArrayList<>();
    }

	/**
	 * Copy constructor for Board used for making a new board based on the passed board
	 * @param board the board to be copied
	 */
	public Board(Board board) {
    	this.board = board.board;
    	this.activeColor = board.activeColor;
    	this.whitePieces = board.whitePieces;
    	this.redPieces = board.redPieces;
    	this.moveList = board.moveList;
	}

	/**
	 * Add a piece to the board at the position on the board that is passed with the color that was passed
	 *
	 * @param row the passed row where the new piece should be added
	 * @param col the passed column where the new piece should be added
	 * @param color the color of the new piece that is being added to the board
	 * @return boolean - true if the piece was successfully added to the board, false if not
	 */
	public boolean addPieceToBoard(int row, int col, Piece.Color color) {
		if (this.board[row][col].getPiece() == null && this.board[row][col].isPlayable()) {
			this.board[row][col].addPiece(color);
			if (color.equals(Piece.Color.WHITE)) {
				this.whitePieces++;
			} else {
				this.redPieces++;
			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Add a piece to the board at the position on the board that is passed with the color that was passed
	 * and the type that was passed
	 *
	 * @param row the passed row where the new piece should be added
	 * @param col the passed column where the new piece should be added
	 * @param color the color of the new piece that is being added to the board
	 * @param type the type of the new piece that is being added to the board
	 * @return boolean - true if the piece was successfully added to the board, false if not
	 */
	public boolean addPieceTypeToBoard(int row, int col, Piece.Color color, Piece.Type type) {
		if (this.board[row][col].getPiece() == null && this.board[row][col].isPlayable()) {
			this.board[row][col].addPiece(color);
			if	(type == Piece.Type.KING) {
				this.board[row][col].getPiece().makeKing();
			}

			if (color.equals(Piece.Color.WHITE)) {
				this.whitePieces++;
			} else {
				this.redPieces++;
			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * A getter method for the whitePieces that is used for testing the capturePieces methods
	 *
	 * @return int the number of white pieces left on the board
	 */
	public int getWhitePieces() {
		return whitePieces;
	}

	/**
	 * A getter method for the redPieces that is used for testing the capturePieces methods
	 *
	 * @return int the number of red pieces left on the board
	 */
	public int getRedPieces() {
		return redPieces;
	}

	/**
	 * Gets the color of whose turn it is
	 *
	 * @return RED or WHITE based on whose turn it is
	 */
	public Piece.Color getActiveColor() {
        return this.activeColor;
    }

	/**
	 * Changes the active color to the other played
	 *
	 * @param activeColor the player's color
	 */
    public void changeActiveColor(Piece.Color activeColor) {
        this.activeColor = activeColor;
    }

	/**
	 * Flips the move if for the white player to see the move through their point of view
	 *
	 * @param move the move being made
	 * @return the move made
	 */
	public Move getActualMove(Move move) {
		//If the piece is red, don't flip it.
		if (this.activeColor == Piece.Color.RED) {
			return move;
		}
		Position start = move.getStart();
        int startRow = 7 - start.getRow();
        int startCol = 7 - start.getCell();
		Position end = move.getEnd();
        int endRow = 7 - end.getRow();
        int endCol = 7 - end.getCell();
		Position actualStart = new Position(startRow, startCol);
		Position actualEnd = new Position(endRow, endCol);
		Move actualMove = new Move(actualStart, actualEnd);
		return actualMove;
	}

	/**
	 * Checks if the move made by the player is a valid move
	 *
	 * @param move the move input the player made
	 * @return true if the player is able to make that move
	 */
    public boolean isValidMove(Move move) {
		Move actualMove = getActualMove(move);
		
        Position start = actualMove.getStart();
        Position end = actualMove.getEnd();
        int endRow = end.getRow();
        int endCol = end.getCell();
        Square endSquare = this.board[endRow][endCol];

        // is the move onto a valid square
        boolean validSquare = endSquare.isPlayable();
		// End is the same place as the inititial start
		boolean isBackOnSameTile = this.board[endRow][endCol].getPiece() == getMovingPiece(move);
		// validity check to see if move can be made
		boolean actuallyIsValid = isActuallyValid(getMovingPiece(actualMove), start, end);

        if ((validSquare || isBackOnSameTile) && actuallyIsValid) {
			if (!this.moveList.isEmpty()) {
				Move recentMove = this.moveList.get(this.moveList.size() - 1);
				Position recentStart = recentMove.getStart();
				Position recentEnd = recentMove.getEnd();
				if (isJump(recentStart, recentEnd, false) && !isJump(start, end, true)) {
					return false;
				} else if (isDiagonal(recentStart, recentEnd)) {
					return false;
				}
			}
            this.moveList.add(actualMove);
            return true;
        } else {
            return false;
        }

    }

	/**
	 * Gets the piece that is being moved by the player
	 *
	 * @param move the move made by the player
	 * @return the piece moving
	 */
	private Piece getMovingPiece(Move move) {
		Move firstMove;
		if (this.moveList.isEmpty()) {
			if (move == null) {
				return null;
			}
			firstMove = move;
		} else {
			firstMove = this.moveList.get(0);
		}
		int firstRow = firstMove.getStart().getRow();
		int firstCol = firstMove.getStart().getCell();
		return board[firstRow][firstCol].getPiece();
	}

	/**
	 * Checks if the piece that is being moved is set to a valid place for the pieces type
	 *
	 * @param movingPiece the piece being moved
	 * @param start the starting position of the piece
	 * @param end the ending position of the piece
	 * @return true if the move is allowed within the rules of the piece
	 */
	private boolean isActuallyValid(Piece movingPiece, Position start, Position end) {
		if (movingPiece == null) {
			return false;
		}

		boolean diagonalMove = isDiagonal(start, end);
		boolean jumpMove = isJump(start, end, true);
		int startRow = start.getRow();
        int endRow = end.getRow();
		//Do check for king stuff
		if (movingPiece.getType() == Piece.Type.KING) {
			return diagonalMove || jumpMove;
		} else {
			if (diagonalMove) {
				if (movingPiece.getColor() == Piece.Color.RED) {
					if (startRow - endRow != 1) {
						return false;
					} else {
						return true;
					}
				} else {
					if (startRow - endRow != -1) {
						return false;
					} else {
						return true;
					}
				}
			} else if (jumpMove) {
				if (movingPiece.getColor() == Piece.Color.RED) {
					if (startRow - endRow != 2) {
						return false;
					} else {
						return true;
					}
				} else {
					if (startRow - endRow != -2) {
						return false;
					} else {
						return true;
					}
				}
			} else {
				return false;
			}
		}
	}

	/**
	 * Checks if there is a jump valid on the board
	 *
	 * @param start the starting position of the piece
	 * @param end the ending position of the piece
	 * @param isNewMove checked if the piece has jumped before
	 * @return true if the piece can jump
	 */
    private boolean isJump(Position start, Position end, boolean isNewMove){
		int xMovement = start.getCell() - end.getCell();
        int yMovement = start.getRow() - end.getRow();
        if (Math.abs(xMovement) != 2) {
			return false;
		}
		if (Math.abs(yMovement) != 2) {
			return false;
		}
		int jumpedOverCol = start.getCell() - (xMovement/2);
		int jumpedOverRow = start.getRow() - (yMovement/2);
		boolean jumpedOverNonNull = board[jumpedOverRow][jumpedOverCol].getPiece() != null;
		boolean jumpedPieceIsOppositeColor = false;
		boolean hasJumpedBefore = false;
		if (isNewMove) {
			hasJumpedBefore = jumpedBefore(jumpedOverRow, jumpedOverCol);
		}
		// if a piece was jumped over, then check its color
		if (jumpedOverNonNull) {
			jumpedPieceIsOppositeColor = board[jumpedOverRow][jumpedOverCol].getPiece().getColor() != this.activeColor;
		}
		if (jumpedOverNonNull && jumpedPieceIsOppositeColor && !hasJumpedBefore) {
			int endCol = end.getCell();
			int endRow = end.getRow();
			return this.board[endRow][endCol].getPiece() == null 
				   || this.board[endRow][endCol].getPiece() == getMovingPiece(new Move(start,end));
		}
		return false;
    }

	/**
	 * Checks if the piece has jumped before
	 *
	 * @param jumpedOverRow the row that has been jumped
	 * @param jumpedOverCol the column that has been jumped
	 * @return true if the piece has jumped
	 */
	private boolean jumpedBefore(int jumpedOverRow, int jumpedOverCol) {
		for (Move move : this.moveList) {
			Position start = move.getStart();
			Position end = move.getEnd();
			int xMovement = start.getCell() - end.getCell();
			int yMovement = start.getRow() - end.getRow();
			if (Math.abs(xMovement) != 2) {
				continue;
			}
			if (Math.abs(yMovement) != 2) {
				continue;
			}
			int otherJumpedOverCol = start.getCell() - (xMovement/2);
			int otherJumpedOverRow = start.getRow() - (yMovement/2);
			if  (jumpedOverRow == otherJumpedOverRow && jumpedOverCol == otherJumpedOverCol) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if the move made by the player is a diagonal move
	 *
	 * @param start the starting position of the piece
	 * @param end the ending position of the piece
	 * @return true if the move is diagonal
	 */
    private boolean isDiagonal(Position start, Position end) {
        int xMovement = start.getCell() - end.getCell();
        int yMovement = start.getRow() - end.getRow();
        if (Math.abs(xMovement) != 1) {
            return false;
        }
        if (Math.abs(yMovement) != 1) {
            return false;
        }
        return true;
    }

	/**
	 * Method that is implemented to support when a player hits the backup button in the game
	 *
	 * @return a string that determines if the player can make the backup move or not and backsup the move
	 */
	public String backUpMove() {
        // do some further checks on move
        // check for multi jumps and stuff
        boolean canBackUp = true; // change this flag if not possible
        if (canBackUp) {
			if (this.moveList.size() > 0) {
				this.moveList.remove(this.moveList.size() - 1);
				return "true";
			}
			else {
				return "moveList is empty, cannot backup the move.";
			}
        } else {
            String errorMessage = ""; // provide error message if not possible
            return errorMessage;
        }
    }

	/**
	 * Gets the board
	 * @return the board
	 */
    public Square[][] getBoard() {
        return board;
    }

	/**
	 * Returns the checkers board in a 2D array
	 * @return the game in a 2D array
	 */
	@Override
    public String toString() {
        String s = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j].getColor() == Square.Color.BLACK) {
                    if (board[i][j].getPiece() != null) {
                        s += "[(B)]";
                    } else {
                        s += "[B]";
                    }
                } else {
                    s += "[W]";
                }
            }
            s += "\n";
        }
        return s;
    }

	/**
	 * Checks if a piece is on the king row and needs to be kinged
	 */
	public void checkMakeKing() {
		for (int i = 0; i < 8; i++) {
			Piece current = this.board[0][i].getPiece();
			if (current != null) {
				if (current.getType() != Piece.Type.KING && current.getColor() == Piece.Color.RED) {
					current.makeKing();
				}
			}
			current = this.board[7][i].getPiece();
			if (current != null) {
				if (current.getType() != Piece.Type.KING && current.getColor() == Piece.Color.WHITE) {
					current.makeKing();
				}
			}
		}
	}

	/**
	 * Handles when a piece has been jumped and needs to be taken off the board
	 */
	public void capturePieces() {
		for (Move move : this.moveList) {
			Position start = move.getStart();
			Position end = move.getEnd();
			int xMovement = start.getCell() - end.getCell();
			int yMovement = start.getRow() - end.getRow();
			if (Math.abs(xMovement) != 2) {
				continue;
			}
			if (Math.abs(yMovement) != 2) {
				continue;
			}
			int jumpedOverCol = start.getCell() - (xMovement/2);
			int jumpedOverRow = start.getRow() - (yMovement/2);
			capturePiece(jumpedOverRow, jumpedOverCol);
		}
	}

	/**
	 * Modifies the count of pieces proper to the captured piece
	 *
	 * @param row the row the captured piece is on
	 * @param col the cell the captured piece is on
	 */
	public void capturePiece(int row, int col) {
		if (this.board[row][col].getPiece() == null) {
			return;
		}
		Piece.Color color = this.board[row][col].getPieceColor();
		if (color == Piece.Color.RED) {
			this.redPieces--;
		} else {
			this.whitePieces--;
		}
		System.out.println("Red pieces: " + redPieces + " White pieces: " + whitePieces);
		this.board[row][col].removePiece();
	}

	/**
	 * Checks if a team is out of pieces and therefore eliminated from the game
	 *
	 * @return true if a team is eliminated
	 */
	public boolean teamIsEliminated() {
		return whitePieces == 0 || redPieces == 0;
	}

	/**
	 * Resets the move list that was made during the player's turn
	 *
	 * @param player the player
	 * @return true when the move list has been cleared
	 */
	public boolean resetMoves(PlayerServices player) {
		if (player == null) {
			return false;
		}

		if (player.Player1Id().equals(player.Id())) {
			if (this.activeColor == Piece.Color.WHITE) {
				return false;
			}
		} else {
			if (this.activeColor == Piece.Color.RED) {
				return false;
			}
		}
		this.moveList.clear();
		return true;
	}

	/**
	 * Checks if the player has taken all the jumps that was available
	 *
	 * @return true if the player has taken all the jumps available to them
	 */
	private boolean tookAllJumps() {
		boolean mustJump = checkForJumps();
		if (this.moveList.size() > 0) {
			Move firstMove = this.moveList.get(0);
			if (mustJump && !isJump(firstMove.getStart(), firstMove.getEnd(), false)) {
				return false;
			} else if (isJump(firstMove.getStart(), firstMove.getEnd(), false)) {
				Move lastMove = this.moveList.get(this.moveList.size() - 1);
				int lastRow = lastMove.getEnd().getRow();
				int lastCol = lastMove.getEnd().getCell();
				ArrayList<Move> possibleJumps;
				Piece piece = getMovingPiece(firstMove);
				if (piece.getType() == Piece.Type.KING) {
					possibleJumps = checkForKingJumps(lastRow, lastCol);
				} else if (piece.getColor() == Piece.Color.RED) {
					possibleJumps = checkForRedJumps(lastRow, lastCol);
				} else if (piece.getColor() == Piece.Color.WHITE) {
					possibleJumps = checkForWhiteJumps(lastRow, lastCol);
				} else {
					return false;
				}
				if (!possibleJumps.isEmpty()) {
					for (Move move : possibleJumps) {
						if (isJump(move.getStart(), move.getEnd(), true))
							return false;
					}
				}
			}
		}
		return true;
	}


	/**
	 * Secondary Move validity check when attempting to submit a turn.
	 * The moveList should be executable in the order they are added in.
	 *
	 * @return String representation of boolean regarding if Move was successful.
	 */
	public String trySubmitTurn() {
		if (tookAllJumps()) {
			if (this.moveList.size() > 0) {
				// remove all pieces that have been captured with a Move
				capturePieces();
				// change the Position of the Moved piece on the Board
				Position pieceInitialPosition = this.moveList.get(0).getStart();
				Position pieceEndingPosition = this.moveList.get(this.moveList.size() - 1).getEnd();
				int initialRow = pieceInitialPosition.getRow();
				int initialCol = pieceInitialPosition.getCell();
				Piece initialPiece = this.board[initialRow][initialCol].getPiece();
				int endRow = pieceEndingPosition.getRow();
				int endCol = pieceEndingPosition.getCell();
				this.board[endRow][endCol].addPiece(initialPiece.getColor());

				if (initialPiece.getType() == Piece.Type.KING) {
					this.board[endRow][endCol].getPiece().makeKing();
				}
				if (initialRow != endRow || initialCol != endCol) {
					this.board[initialRow][initialCol].removePiece();
				}
			}
			else {
				return "No moves!";
			}
			moveList.clear();
			return "true";
		}
		return "Invalid move! Did not take a required jump move.";
	}

	/**
	 * Creates an array list of the possible jumps a player can make
	 *
	 * @return true if there are any jumps available
	 */
	private boolean checkForJumps() {
        ArrayList<Move> possibleMoves = createPossibleJumpList(this.activeColor);
        if (possibleMoves.size() == 0) {
			return false;
		} else {
        	return true;
		}
    }

	/**
	 * Makes a list of all the possible jumps that a player can make
	 * Iterate through each piece on the Board that matched the activeColor
	 *
	 * @param color the color of the piece that is the active color
	 * @return the list of jumps that are available
	 */
	private ArrayList<Move> createPossibleJumpList(Piece.Color color) {
    	ArrayList<Move> possibleMoves = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square s = this.board[row][col];
                if (s.getPiece() != null) {
                    if (s.getPieceColor() == color) {
						if (s.getPieceType() == Piece.Type.KING) {
							possibleMoves.addAll(checkForKingJumps(row, col));
						} else {
							if (color == Piece.Color.RED) {
								possibleMoves.addAll(checkForRedJumps(row, col));
							} else {
								possibleMoves.addAll(checkForWhiteJumps(row, col));
							}
						}
                    }
                }
            }
        }
        return possibleMoves;
    }

	/**
	 * Checks for moves that a single piece can make
	 *
	 * @return true if there are any moves a single piece can make
	 */
	private boolean checkForSingleMoves() {
		ArrayList<Move> possibleMoves = createPossibleSingleMovesList(this.activeColor);
        if (possibleMoves.size() == 0) {
			return false;
		} else {
        	return true;
		}
	}

	/**
	 * Creates an array list of moves that a single piece can make
	 * Iterate through all pieces on the Board that match the activeColor.
	 *
	 * @param color the color that the list is being made for
	 * @return the list of moves the single pieces can make
	 */
	private ArrayList<Move> createPossibleSingleMovesList(Piece.Color color) {
		ArrayList<Move> possibleMoves = new ArrayList<>();
		for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square s = this.board[row][col];
                if (s.getPiece() != null) {
                    if (s.getPieceColor() == color) {
						if (s.getPieceType() == Piece.Type.KING) {
							possibleMoves.addAll(checkForKingSingleMoves(row, col));
						} else {
							if (this.activeColor == Piece.Color.RED) {
								possibleMoves.addAll(checkForRedSingleMoves(row, col));
							} else {
								possibleMoves.addAll(checkForWhiteSingleMoves(row, col));
							}
						}
                    }
                }
            }
        }
		return possibleMoves;
	}

	/**
	 * Checks for moves that a king can make
	 *
	 * @param row the row the king is on
	 * @param col the column the king is on
	 * @return list of moves that a king can make
	 */
	private ArrayList<Move> checkForKingSingleMoves(int row, int col) {
		ArrayList<Move> possibleMoves = new ArrayList<>();
    	// check North-East move
    	if (row >= 1 && col <= 6) {
    		// if the landing piece (1x1 move) is empty
    		if (this.board[row - 1][col + 1].getPiece() == null) {
    			// then there is a move
				Move m = new Move(new Position(row, col), new Position(row - 1,col + 1));
				possibleMoves.add(m);
			}
		}

    	// check South-East move
    	if (row <= 6 && col <= 6) {
			// if the landing piece (1x1 move) is empty
			if (this.board[row + 1][col + 1].getPiece() == null) {
				// then there is a move
				Move m = new Move(new Position(row, col), new Position(row + 1,col + 1));
				possibleMoves.add(m);
			}
		}

    	// check South-West move
    	if (row <= 6 && col >= 1) {
			// if the landing piece (1x1 move) is empty
			if (this.board[row + 1][col - 1].getPiece() == null) {
				// then there is a move
				Move m = new Move(new Position(row, col), new Position(row + 1,col - 1));
				possibleMoves.add(m);
			}
		}

    	// check North-West move
    	if (row >= 1 && col >= 1) {
			// if the landing piece (1x1 move) is empty
			if (this.board[row - 1][col - 1].getPiece() == null) {
				// then there is a move
				Move m = new Move(new Position(row, col), new Position(row - 1,col - 1));
				possibleMoves.add(m);
			}
		}
		return possibleMoves;
	}

	/**
	 * Makes a list of available moves a red piece has
	 *
	 * @param row the row the red piece is on
	 * @param col the column the red piece is on
	 * @return the lsit of moves that the red piece can make
	 */
	private ArrayList<Move> checkForRedSingleMoves(int row, int col) {
		ArrayList<Move> possibleMoves = new ArrayList<>();
		// check North-East move
		if (row >= 1 && col <= 6) {
			// if the landing piece (1x1 move) is empty
			if (this.board[row - 1][col + 1].getPiece() == null) {
				// then there is a move
				Move m = new Move(new Position(row, col), new Position(row - 1,col + 1));
				possibleMoves.add(m);
			}
		}

		// check North-West move
		if (row >= 1 && col >= 1) {
			// if the landing piece (1x1 move) is empty
			if (this.board[row - 1][col - 1].getPiece() == null) {
				// then there is a move
				Move m = new Move(new Position(row, col), new Position(row - 1,col - 1));
				possibleMoves.add(m);
			}
		}
		return possibleMoves;
	}

	/**
	 * Makes a list of available moves a white piece has
	 *
	 * @param row the row the white piece is on
	 * @param col the column the white piece is on
	 * @return the list of moves that the white piece can make
	 */
	private ArrayList<Move> checkForWhiteSingleMoves(int row, int col) {
		ArrayList<Move> possibleMoves = new ArrayList<>();
		// check South-East move
		if (row <= 6 && col <= 6) {
			// if the landing piece (1x1 move) is empty
			if (this.board[row + 1][col + 1].getPiece() == null) {
				// then there is a move
				Move m = new Move(new Position(row, col), new Position(row + 1,col + 1));
				possibleMoves.add(m);
			}
		}

		// check South-West move
		if (row <= 6 && col >= 1) {
			// if the landing piece (1x1 move) is empty
			if (this.board[row + 1][col - 1].getPiece() == null) {
				// then there is a move
				Move m = new Move(new Position(row, col), new Position(row + 1,col - 1));
				possibleMoves.add(m);
			}
		}
		return possibleMoves;
	}


	/**
	 * Makes a list for all the jumps that a king can make in its move
	 *
	 * @param row the row the king is on
	 * @param col the column the king is on
	 * @return a list of available jumps for the king
	 */
    private ArrayList<Move> checkForKingJumps(int row, int col) {
    	ArrayList<Move> possibleJumps = new ArrayList<>();
    	// check North-East move
    	Square ne = null;
    	if (row >= 2 && col <= 5) {
    		ne = this.board[row - 1][col + 1];
    		// if NE has a piece
    		if (ne.getPiece() != null) {
    			// if the piece is opposite color
    			if (ne.getPieceColor() != this.activeColor) {
    				// if the landing piece (2x2 jump) is empty
    				if (this.board[row - 2][col + 2].getPiece() == null) {
    					// then there is a jump
						Move m = new Move(new Position(row, col), new Position(row - 2,col + 2));
						possibleJumps.add(m);
					}
				}
			}
		}

    	// check South-East move
    	Square se = null;
    	if (row <= 5 && col <= 5) {
    		se = this.board[row + 1][col + 1];
			// if SE has a piece
			if (se.getPiece() != null) {
				// if the piece is opposite color
				if (se.getPieceColor() != this.activeColor) {
					// if the landing piece (2x2 jump) is empty
					if (this.board[row + 2][col + 2].getPiece() == null) {
						// then there is a jump
						Move m = new Move(new Position(row, col), new Position(row + 2,col + 2));
						possibleJumps.add(m);
					}
				}
			}
		}

    	// check South-West move
    	Square sw = null;
    	if (row <= 5 && col >= 2) {
    		sw = this.board[row + 1][col - 1];
			// if SW has a piece
			if (sw.getPiece() != null) {
				// if the piece is opposite color
				if (sw.getPieceColor() != this.activeColor) {
					// if the landing piece (2x2 jump) is empty
					if (this.board[row + 2][col - 2].getPiece() == null) {
						// then there is a jump
						Move m = new Move(new Position(row, col), new Position(row + 2,col - 2));
						possibleJumps.add(m);
					}
				}
			}
		}

    	// check North-West move
    	Square nw = null;
    	if (row >= 2 && col >= 2) {
    		nw = this.board[row - 1][col - 1];
			// if NW has a piece
			if (nw.getPiece() != null) {
				// if the piece is opposite color
				if (nw.getPieceColor() != this.activeColor) {
					// if the landing piece (2x2 jump) is empty
					if (this.board[row - 2][col - 2].getPiece() == null) {
						// then there is a jump
						Move m = new Move(new Position(row, col), new Position(row - 2,col - 2));
						possibleJumps.add(m);
					}
				}
			}
		}
    	return possibleJumps;
	}

	/**
	 * Makes a list for the jumps a red piece can make
	 *
	 * @param row the row the piece is on
	 * @param col the column the piece is on
	 * @return a list of the jumps a red piece can make
	 */
	private ArrayList<Move> checkForRedJumps(int row, int col) {
    	ArrayList<Move> possibleJumps = new ArrayList<>();
		// check North-East move
		Square ne = null;
		if (row >= 2 && col <= 5) {
			ne = this.board[row - 1][col + 1];
			// if NE has a piece
			if (ne.getPiece() != null) {
				// if the piece is opposite color
				if (ne.getPieceColor() != this.activeColor) {
					// if the landing piece (2x2 jump) is empty
					if (this.board[row - 2][col + 2].getPiece() == null) {
						// then there is a jump
						Move m = new Move(new Position(row, col), new Position(row - 2,col + 2));
						possibleJumps.add(m);
					}
				}
			}
		}

		// check North-West move
		Square nw = null;
		if (row >= 2 && col >= 2) {
			nw = this.board[row - 1][col - 1];
			// if NW has a piece
			if (nw.getPiece() != null) {
				// if the piece is opposite color
				if (nw.getPieceColor() != this.activeColor) {
					// if the landing piece (2x2 jump) is empty
					if (this.board[row - 2][col - 2].getPiece() == null) {
						// then there is a jump
						Move m = new Move(new Position(row, col), new Position(row - 2,col - 2));
						possibleJumps.add(m);
					}
				}
			}
		}
		return possibleJumps;
	}

	/**
	 * Makes a list for the jumps a white piece can make
	 *
	 * @param row the row the piece is on
	 * @param col the column the piece is on
	 * @return a list of the jumps a white piece can make
	 */
	private ArrayList<Move> checkForWhiteJumps(int row, int col) {
    	ArrayList<Move> possibleJumps = new ArrayList<>();
		// check South-East move
		Square se = null;
		if (row <= 5 && col <= 5) {
			se = this.board[row + 1][col + 1];
			// if SE has a piece
			if (se.getPiece() != null) {
				// if the piece is opposite color
				if (se.getPieceColor() != this.activeColor) {
					// if the landing piece (2x2 jump) is empty
					if (this.board[row + 2][col + 2].getPiece() == null) {
						// then there is a jump
						Move m = new Move(new Position(row, col), new Position(row + 2,col + 2));
						possibleJumps.add(m);
					}
				}
			}
		}

		// check South-West move
		Square sw = null;
		if (row <= 5 && col >= 2) {
			sw = this.board[row + 1][col - 1];
			// if SW has a piece
			if (sw.getPiece() != null) {
				// if the piece is opposite color
				if (sw.getPieceColor() != this.activeColor) {
					// if the landing piece (2x2 jump) is empty
					if (this.board[row + 2][col - 2].getPiece() == null) {
						// then there is a jump
						Move m = new Move(new Position(row, col), new Position(row + 2,col - 2));
						possibleJumps.add(m);
					}
				}
			}
		}
		return possibleJumps;
	}

	/**
	 * Checks if a player has any moves left that they can make
	 *
	 * @return true if the player has moves left and false if they don't
	 */
	public boolean hasMovesLeft() {
		boolean hasJumps = checkForJumps();
		boolean hasSingleMoves = checkForSingleMoves();
		return hasJumps || hasSingleMoves;
	}

}
