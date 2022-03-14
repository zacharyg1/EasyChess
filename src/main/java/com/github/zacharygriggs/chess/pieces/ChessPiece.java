package com.github.zacharygriggs.chess.pieces;

import com.github.zacharygriggs.chess.core.ChessCoordinate;
import com.github.zacharygriggs.chess.core.ChessDisplay;
import com.github.zacharygriggs.chess.core.ChessPosition;
import com.github.zacharygriggs.chess.data.ChessPlayer;
import com.github.zacharygriggs.chess.core.ChessMove;
import javafx.scene.image.Image;
import com.github.zacharygriggs.util.ResourceManager;

import java.util.Optional;

import static com.github.zacharygriggs.chess.data.ChessPlayer.BLACK;
import static com.github.zacharygriggs.chess.data.ChessPlayer.WHITE;

/**
 * An abstract representation of a chess piece which can belong to either player,
 * move around the chess board, verify move legality, and capture pieces.
 */
public abstract class ChessPiece {

    private ChessPosition position;
    private ChessPlayer owner;
    private ChessCoordinate coordinate;
    private boolean hasMoved;

    /**
     * Creates a new main.java.chess piece.
     *
     * @param position   Board this piece will belong to
     * @param owner      Owning player
     * @param coordinate Starting coordinate
     */
    public ChessPiece(ChessPosition position, ChessPlayer owner, ChessCoordinate coordinate) {
        this.position = position;
        this.owner = owner;
        this.coordinate = coordinate;
    }

    /**
     * Creates a new main.java.chess piece.
     *
     * @param position   Board this piece will belong to
     * @param owner      Owning player
     * @param coordinate Starting coordinate
     */
    public ChessPiece(ChessPosition position, ChessPlayer owner, String coordinate) {
        this(position, owner, new ChessCoordinate(coordinate));
    }

    /**
     * Creates a new main.java.chess piece.
     *
     * @param position Board this piece will belong to
     * @param owner    Owning player
     * @param rank     Starting rank
     * @param file     Starting file
     */
    public ChessPiece(ChessPosition position, ChessPlayer owner, char file, int rank) {
        this(position, owner, new ChessCoordinate(file, rank));
    }

    /**
     * Copy constructor.
     *
     * @param old Piece to copy.
     */
    public ChessPiece(ChessPiece old) {
        this.position = old.position;
        this.owner = old.owner;
        this.coordinate = old.coordinate;
        this.hasMoved = old.hasMoved;
    }

    /**
     * Determines whether this piece can move to the destination.
     *
     * @param destination Desired destination
     * @return True if can move; false if not.
     */
    public final boolean canMove(ChessCoordinate destination) {
        return canMoveNoLegalCheck(destination) && legalPositionCheck(destination);
    }

    /**
     * Determines whether this piece can move to the destination.
     * Does not see if the move will leave you in check.
     *
     * @param destination Desired destination
     * @return True if can move; false if not.
     */
    public abstract boolean canMoveNoLegalCheck(ChessCoordinate destination);

    /**
     * Performs post-movement checks to ensure the move results
     * in a legal position.
     * <p>
     * For example - making sure the King won't be in check after this move.
     *
     * @param destination Coordinate that this piece is moving to
     * @return True if allowed; false if not.
     */
    public boolean legalPositionCheck(ChessCoordinate destination) {
        try {
            ChessMove incomingMove = new ChessMove();
            incomingMove.setFrom(getCoordinate());
            incomingMove.setTo(destination);
            ChessPosition nextPosition = position.positionAfterMove(incomingMove);
            nextPosition.updateAllPieces();
            if (nextPosition.inCheck(owner)) {
                return false;
            }
            return true;
        } catch (Exception ex) {
            System.out.println("Failed to evaluate move: " + destination);
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * Gets a string representation of this main.java.chess piece.
     *
     * @return A string representation of main.java.chess piece.
     */
    public abstract String identity();

    protected void setCoordinate(ChessCoordinate coordinate) {
        this.coordinate = coordinate;
        this.hasMoved = true;
    }

    /**
     * Captures a piece on the given square, removing it
     * from the game.
     *
     * @param whichSpace Space to capture on.
     */
    public void capture(ChessCoordinate whichSpace) {
        Optional<ChessPiece> isPieceAt = getPosition().pieceAt(whichSpace);
        if (isPieceAt.isPresent()) {
            ChessPiece whichPiece = isPieceAt.get();
            if (whichPiece instanceof King) {
                return;
            }
            getPosition().removePiece(whichPiece);
            getPosition().addCapturedPiece(getOwner(), whichPiece);
            getPosition().updateAllPieces();
        }
    }

    /**
     * Makes this piece move, regardless of any checks on whether or not it can.
     * This is used in moves like Castling, where the regular checks would fail because
     * it's a non-standard move. The checks for those moves are separate.
     *
     * @param destination Coordinate to move to
     */
    public void forceMove(ChessCoordinate destination) {
        setCoordinate(destination);
    }

    /**
     * Moves to a destination. Expects the move to be valid.
     *
     * @param destination Desired destination
     */
    public void move(ChessCoordinate destination) {
        if (!canMoveNoLegalCheck(destination)) {
            return;
        }
        if (getPosition().pieceAt(destination).isPresent()) {
            if (getPosition().pieceAt(destination).get().getOwner() == getOwner()) {
                throw new IllegalArgumentException("Attempted capturing same side piece on " + destination);
            }
            capture(destination);
        }
        setCoordinate(destination);
    }

    /**
     * Gets the owning player.
     *
     * @return Owning player
     */
    public ChessPlayer getOwner() {
        return owner;
    }

    /**
     * Gets the current coordinate of piece.
     *
     * @return Coordinate of piece
     */
    public ChessCoordinate getCoordinate() {
        return coordinate;
    }

    /**
     * Converts this piece into a representation that can be displayed
     * on the chess board. Contains player, chess piece id, and coordinate
     * <p>
     * Example: (Wh)Qa3
     * (Bl)Nf6
     *
     * @return
     */
    public String toString() {
        String playerColor = "";
        if (owner == WHITE) {
            playerColor = "(Wh)";
        } else if (owner == BLACK) {
            playerColor = "(Bl)";
        }
        return playerColor + identity() + coordinate.toString();
    }

    /**
     * Gets the identity of the owner, either w or b
     *
     * @return Player identity
     */
    public String getPlayerIdentity() {
        if (owner == WHITE) {
            return "w";
        } else {
            return "b";
        }
    }

    /**
     * Provides the path to an image which can render this piece.
     *
     * @return Resource image path
     */
    public abstract String resourcePath();

    /**
     * Renders this piece on a display.
     *
     * @param display Chess display to render on
     */
    public void render(ChessDisplay display) {
        Image img = ResourceManager.loadResourceAsCachedImage(resourcePath());
        display.drawAt(img, getCoordinate());
    }

    /**
     * Gets the linked position of this ches piece.
     *
     * @return Chess position object
     */
    public ChessPosition getPosition() {
        return position;
    }

    /**
     * Creates a copy of this piece
     * including the coordinate
     *
     * @return Copy of chess piece
     */
    public abstract ChessPiece copy();

    /**
     * Declares which ChessPosition object this piece is related to.
     *
     * @param position A chess position object
     */
    public void setPosition(ChessPosition position) {
        this.position = position;
    }

    /**
     * Provides the value of this chess piece
     * <p>
     * Pawns = 1
     * Minor piece = 3
     * Rook = 5
     * Queen = 9
     *
     * @return Value of piece
     */
    public abstract int getMaterialValue();

    public boolean hasMoved() {
        return hasMoved;
    }
}
