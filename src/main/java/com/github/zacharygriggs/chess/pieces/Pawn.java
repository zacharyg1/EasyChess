package com.github.zacharygriggs.chess.pieces;

import com.github.zacharygriggs.chess.core.ChessCoordinate;
import com.github.zacharygriggs.chess.core.ChessPosition;
import com.github.zacharygriggs.chess.data.ChessPlayer;
import com.github.zacharygriggs.chess.helper.MovementHelper;
import com.github.zacharygriggs.chess.data.ChessConstants;

import java.util.Optional;

public class Pawn extends ChessPiece {

    private boolean canBeEnPassanted = false;

    public Pawn(ChessPosition onBoard, ChessPlayer owner, ChessCoordinate coordinate) {
        super(onBoard, owner, coordinate);
    }

    public Pawn(ChessPosition onBoard, ChessPlayer owner, String coordinate) {
        super(onBoard, owner, coordinate);
    }

    public Pawn(ChessPosition onBoard, ChessPlayer owner, char file, int rank) {
        super(onBoard, owner, file, rank);
    }

    public Pawn(Pawn old) {
        super(old);
        this.canBeEnPassanted = old.canBeEnPassanted;
    }

    /**
     * Moves to a destination. Expects the move to be valid.
     *
     * @param destination Desired destination
     */
    @Override
    public void move(ChessCoordinate destination) {
        // We do need some custom logic here, to track whether the pawn
        // can be en-passanted by another pawn (possible only if moves 2 spaces)
        // and to mark that this pawn has already moved and will not be able
        // to move 2 spaces again.
        int dist = Math.abs(destination.getRank() - getCoordinate().getRank());
        if (dist == 2) {
            canBeEnPassanted = true;
        } else if (canBeEnPassanted) {
            canBeEnPassanted = false;
        }


        // This method is called for both moving and capturing.
        // If capturing, we need to perform capture code.
        ChessPiece capturablePiece = findCapturablePieceInternal(destination);
        if (capturablePiece != null) {
            capture(capturablePiece.getCoordinate()); // This is not necessarily the same as destination due to en passant!
        }

        // Set the destination as our new coordinate
        setCoordinate(destination);

        // Now we need to check if this pawn should promote.
        // White promotes on reaching the 8th rank
        // Black promotes on reaching the 1st rank
        int promotionRank;
        if (getOwner() == ChessPlayer.WHITE) {
            promotionRank = 8;
        } else {
            promotionRank = 1;
        }
        if (getCoordinate().getRank() == promotionRank) {
            // Auto-promote to a Queen for right now.
            getPosition().promote(this);
        }
    }

    private boolean canMoveInternal(ChessCoordinate destination) {
        // Pawns can only move one space ahead of where they are currently.
        // They cannot capture on this space.
        // The only exception is the first time this pawn is moved,
        // where they may move two spaces.
        // A White pawn should move forward, and a Black pawn should move backward
        int range = 1;
        if (!hasMoved()) {
            range = range * 2; // Allow moving two spaces
        }
        int moveRangeAttempted = destination.getRank() - getCoordinate().getRank();
        if (Math.abs(moveRangeAttempted) <= range) {
            if (getPosition().pieceAt(destination).isPresent()) {
                return false; // Tried to move onto another piece in a line.
            }
            for (ChessCoordinate between : MovementHelper.spacesBetween(getCoordinate(), destination)) {
                if (getPosition().pieceAt(between).isPresent()) {
                    return false; // Tried to move past another piece.
                }
            }
        } else {
            return false; // Tried to move too far.
        }
        return true; // Nothing wrong with move.
    }

    private ChessPiece findCapturablePieceInternal(ChessCoordinate destination) {
        ChessPiece desiredCapture;
        if (getPosition().pieceAt(destination).isEmpty()) {
            // There was no piece to capture.
            // However there COULD be a piece to take with en passant.
            // An example of en passant:
            //    If there's a pawn belonging to White on d5
            //    Black plays e7 -> e5
            //    White captures dxe6
            // We can generalize this by saying:
            // If there is a pawn one space relatively behind (-1 for white, +1 for black)
            // And this pawn has moved two spaces in the last turn, then we allow capture.
            int direction;
            if (getOwner() == ChessPlayer.WHITE) {
                direction = -1;
            } else {
                direction = 1;
            }
            ChessCoordinate enPassantDestination = new ChessCoordinate(destination.getFile(), destination.getRank() + direction);
            Optional<ChessPiece> isPieceAt = getPosition().pieceAt(enPassantDestination);
            if (isPieceAt.isEmpty()) {
                return null; // There was no piece to attempt to en passant.
            }
            ChessPiece pieceAt = isPieceAt.get();
            if (!(pieceAt instanceof Pawn)) {
                return null; // You may only en passant pawns.
            }
            Pawn checkedPawn = (Pawn) pieceAt;
            if (!checkedPawn.canBeEnPassanted) {
                return null; // The attempt was too late.
            }
            desiredCapture = checkedPawn; // Everything OK - we can play en passant.
        } else {
            desiredCapture = getPosition().pieceAt(destination).get();
        }
        if (desiredCapture.getOwner() == this.getOwner()) {
            // Can't capture your own piece.
            return null;
        }
        return desiredCapture; // There was nothing wrong with the move.
    }

    @Override
    public boolean canMoveNoLegalCheck(ChessCoordinate destination) {
        // Pawn movement is weird.
        // First, we should see whether we're trying to move or capture
        // Since they are different for pawns.
        int moveRangeAttempted = destination.getRank() - getCoordinate().getRank();
        if (moveRangeAttempted == 0) {
            return false; // Can't move to same space it's already on.
        }
        // Pawns cannot move backwards. Note the opposite sign when checking white vs black.
        if (moveRangeAttempted < 0 && getOwner() == ChessPlayer.WHITE) {
            return false;
        }
        if (moveRangeAttempted > 0 && getOwner() == ChessPlayer.BLACK) {
            return false;
        }
        if (getCoordinate().getFile() == destination.getFile()) {
            // Same file, for example e -> e. The move is legal as long as they only moved one (or possibly 2 squares)
            return canMoveInternal(destination);
        } else if (Math.abs(getCoordinate().getFile() - destination.getFile()) == 1 &&
                Math.abs(getCoordinate().getRank() - destination.getRank()) == 1) {
            // They attempted to move one file and rank over, for example e2 -> c3 which could be a valid capture
            return findCapturablePieceInternal(destination) != null;
        } else {
            // They tried to do something that doesn't work ever.
            return false;
        }
    }

    @Override
    public String identity() {
        return "P";
    }

    @Override
    public String resourcePath() {
        return ChessConstants.IMAGE_PATH_PREFIX + getPlayerIdentity() + "_pawn" + ChessConstants.IMAGE_PATH_EXTENSION;
    }

    public ChessPiece copy() {
        return new Pawn(this);
    }

    public int getMaterialValue() {
        return 1;
    }
}
