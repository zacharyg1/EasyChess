package com.github.zacharygriggs.chess.core;

import com.github.zacharygriggs.chess.data.ChessPlayer;
import com.github.zacharygriggs.chess.data.ChessResult;
import com.github.zacharygriggs.chess.helper.MiscHelper;
import com.github.zacharygriggs.chess.pieces.*;

import java.util.*;

/**
 * Represents a main.java.chess board which contains
 * pieces and chess squares, and can play a game.
 */
public class ChessBoard {

    private ChessPosition currentPosition;
    private ChessPlayer movingPlayer;
    private int fiftyMoveRuleCounter = 0;
    private List<ChessMove> history;

    /**
     * Creates a chess board with the starting position.
     */
    public ChessBoard() {
        this.history = new ArrayList<>();
        this.currentPosition = ChessPosition.startingPosition();
        this.movingPlayer = ChessPlayer.WHITE;
    }

    /**
     * Finds the piece at the given coordinate, or empty
     * if there's no piece there.
     *
     * @param coordinate Coordinate to check
     * @return Optional of piece, or empty optional.
     */
    public Optional<ChessPiece> pieceAt(ChessCoordinate coordinate) {
        return currentPosition.pieceAt(coordinate);
    }

    /**
     * Determines if the game's over.
     * Stubbed.
     *
     * @return True if game's over; false if not.
     */
    public boolean isGameOver() {
        return getGameResult() != ChessResult.ONGOING;
    }

    /**
     * Determines if either of the players have sufficient material to deliver a checkmate.
     * <p>
     * Rules:
     * - If there's a pawn on either side, it's enough material because the pawn can promote into a queen
     * - If there's a queen or a rook, it's enough
     * - A Bishop and a Knight can checkmate, though it is extremely difficult
     * - Two Bishop is enough
     * - Two Knights is enough. Even though checkmate cannot be forced, it is possible if the other player makes bad moves.
     * - One Knight OR one Bishop is not sufficient material.
     *
     * @return  True if either player has material to checkmate. False if not (and it is a draw)
     */
    private boolean hasSufficientMaterial() {

        int whiteBishopCount = 0;
        int whiteKnightCount = 0;
        int blackBishopCount = 0;
        int blackKnightCount = 0;
        for (ChessPiece piece : currentPosition.getPieces()) {
            if (piece instanceof Queen || piece instanceof Pawn || piece instanceof Rook) {
                // Any of these on any side can force checkmate.
                return true;
            }
            if (piece instanceof Knight) {
                if (piece.getOwner() == ChessPlayer.WHITE) {
                    whiteKnightCount++;
                } else {
                    blackKnightCount++;
                }
            }
            if (piece instanceof Bishop) {
                if (piece.getOwner() == ChessPlayer.WHITE) {
                    whiteBishopCount++;
                } else {
                    blackBishopCount++;
                }
            }
        }
        // Two of any minor piece can checkmate.
        if (whiteBishopCount > 1 || whiteKnightCount > 1 || blackBishopCount > 1 || blackKnightCount > 1) {
            return true;
        }
        // One of each minor piece can checkmate
        if ((whiteBishopCount >= 1 && whiteKnightCount >= 1) || (blackBishopCount >= 1 && blackKnightCount >= 1)) {
            return true;
        }
        // Insufficient material to checkmate in any circumstance.
        return false;
    }

    /**
     * Gets the chess result of the current position.
     *
     * @return A result saying whether the game is ongoing, over, etc.
     */
    public ChessResult getGameResult() {
        ChessResult res = ChessResult.ONGOING; // Assume ongoing by default.
        // Check for a victory or stalemate by either side.
        // If it's a player's turn and they have no moves, the game is over.
        // If the player with no moves is in check, then it's a checkmate.
        // If the player isn't in check it's a stalemate.
        if (movingPlayer == ChessPlayer.WHITE) {
            if (currentPosition.noLegalMoves(ChessPlayer.WHITE)) {
                if (currentPosition.inCheck(ChessPlayer.WHITE)) {
                    res = ChessResult.BLACK_WINS;
                } else {
                    res = ChessResult.STALEMATE; // Stalemate
                }
            }
        }
        if (movingPlayer == ChessPlayer.BLACK) {
            if (currentPosition.noLegalMoves(ChessPlayer.BLACK)) {
                if (currentPosition.inCheck(ChessPlayer.BLACK)) {
                    res = ChessResult.WHITE_WINS;
                } else {
                    res = ChessResult.STALEMATE; // Stalemate
                }
            }
        }

        // If neither side has material enough to win the game, it's a book draw.
        // An example is King + Bishop vs King.
        // Cannot checkmate under any circumstance, therefore we'll call it a draw now.
        if (!hasSufficientMaterial()) {
            res = ChessResult.INSUFFICIENT_MATERIAL;
        }

        // 50-Move Rule: if there are 50 moves in a row without a pawn move or a capture
        // Then the game is drawn.
        if (fiftyMoveRuleCounter >= 50) {
            res = ChessResult.FIFTY_MOVE_DRAW;
        }

        // Threefold repetition - if the last 6 moves are the same (3 from each side)
        // Then it's a draw.
        // The logic is a bit weird here...
        if (history.size() > 7) {
            int lastHistory = history.size() - 1;
            if (history.get(lastHistory).equals(history.get(lastHistory - 2)) &&
                    history.get(lastHistory - 1).equals(history.get(lastHistory - 3)) &&
                    history.get(lastHistory - 4).equals(history.get(lastHistory - 6))) {
                res = ChessResult.REPETITION_DRAW;
            }
        }
        return res;
    }

    /**
     * Submits a move to be played.
     *
     * @param one Original coordinate
     * @param two Destination coordinate
     * @return True if valid move; false if not.
     */
    public boolean submitMove(ChessCoordinate one, ChessCoordinate two) {
        ChessMove move = new ChessMove();
        move.setFrom(one);
        move.setTo(two);
        return submitMove(move);
    }

    /**
     * Tries to submit a move for the current player to make.
     *
     * @param move Desired move
     * @return True if the move is accepted and played; false if not.
     */
    public boolean submitMove(ChessMove move) {
        // If there's no piece where the player is trying to move from,
        // then it's not a valid move (reject)
        Optional<ChessPiece> isPieceAt = pieceAt(move.getFrom());
        boolean isThisACapture = pieceAt(move.getTo()).isPresent();
        if (isPieceAt.isEmpty()) {
            return false;
        }
        // Find if there's a piece at the desired destination.
        // If the player already has a piece there, it's not valid (cannot self capture)
        ChessPiece movingPiece = isPieceAt.get();
        if (movingPiece.getOwner() != movingPlayer) {
            return false;
        }
        // If the piece doesn't move that way or is blocked, not a valid move.
        if (!movingPiece.canMove(move.getTo())) {
            return false;
        }
        // It's a valid move. Set metadata for the chess move - this is used in
        // generating the PGN report after the game.
        move.setCapture(isThisACapture);
        move.setPieceIdentity(isPieceAt.get().identity());
        // Execute the move, update the position.
        currentPosition = currentPosition.positionAfterMove(move);
        currentPosition.updateAllPieces();
        // Update metadata that's only known after the move.
        if (getGameResult() == ChessResult.BLACK_WINS ||
                getGameResult() == ChessResult.WHITE_WINS) {
            move.setCheckmate(true);
        }
        if (currentPosition.inCheck(MiscHelper.opposite(movingPlayer))) {
            move.setCheck(true);
        }
        // Switch moving player
        movingPlayer = MiscHelper.opposite(movingPlayer);
        // Account for the 50 move rule.
        fiftyMoveRuleCounter++;
        if (movingPiece instanceof Pawn || isThisACapture) {
            fiftyMoveRuleCounter = 0;
        }
        // Track this for the game's report generation
        history.add(move);
        currentPosition.setTurn(currentPosition.getTurn() + 1);
        return true;
    }

    /**
     * Gets the current player who should move now.
     *
     * @return The current moving player.
     */
    public ChessPlayer getMovingPlayer() {
        return movingPlayer;
    }

    /**
     * Creates a String representation of this main.java.chess position.
     *
     * @return Chess board String
     */
    public String toString() {
        return currentPosition.toString();
    }

    /**
     * Provides the current position on the board.
     *
     * @return  Current chess position
     */
    public ChessPosition getPosition() {
        return currentPosition;
    }

    /**
     * Renders the chess board on the display.
     *
     * @param display   Chess display
     */
    public void renderPieces(ChessDisplay display) {
        for (ChessPiece piece : currentPosition.getPieces()) {
            piece.render(display);
        }
    }

    /**
     * Provides the move history of the game.
     *
     * @return  Move history
     */
    public List<ChessMove> getHistory() {
        return history;
    }
}
