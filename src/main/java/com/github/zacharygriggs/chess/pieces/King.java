package com.github.zacharygriggs.chess.pieces;


import com.github.zacharygriggs.chess.core.ChessCoordinate;
import com.github.zacharygriggs.chess.core.ChessPosition;
import com.github.zacharygriggs.chess.data.ChessPlayer;
import com.github.zacharygriggs.chess.helper.MovementHelper;
import com.github.zacharygriggs.chess.data.ChessConstants;

import java.util.Optional;

public class King extends ChessPiece {

    // G = Short castle
    // B = log castle

    public King(ChessPosition onBoard, ChessPlayer owner, ChessCoordinate coordinate) {
        super(onBoard, owner, coordinate);
    }

    public King(ChessPosition onBoard, ChessPlayer owner, String coordinate) {
        super(onBoard, owner, coordinate);
    }

    public King(ChessPosition onBoard, ChessPlayer owner, char file, int rank) {
        super(onBoard, owner, file, rank);
    }

    public King(King old) {
        super(old);
    }

    private void doCastle(ChessCoordinate destination) {
        if (canLongCastle() && (destination.getFile() == 'b' || destination.getFile() == 'c' || destination.getFile() == 'd')) {
            doLongCastle();
        } else {
            doShortCastle();
        }
    }

    private void doLongCastle() {
        int kingsRank = this.getCoordinate().getRank();
        ChessPiece castledRook = getPosition().pieceAt(new ChessCoordinate('a', kingsRank)).get();
        castledRook.forceMove(new ChessCoordinate('d', kingsRank));
        this.forceMove(new ChessCoordinate('c', kingsRank));
    }

    private void doShortCastle() {
        int kingsRank = this.getCoordinate().getRank();
        ChessPiece castledRook = getPosition().pieceAt(new ChessCoordinate('h', kingsRank)).get();
        castledRook.forceMove(new ChessCoordinate('f', kingsRank));
        this.forceMove(new ChessCoordinate('g', kingsRank));
    }

    @Override
    public void forceMove(ChessCoordinate destination) {
        // Determine if player's trying to castle or just to move.
        super.forceMove(destination);
    }

    /**
     * Moves to a destination. Expects the move to be valid.
     * Handle castling
     *
     * @param destination Desired destination
     */
    @Override
    public void move(ChessCoordinate destination) {
        // Determine if player's trying to castle or just to move.
        if (!hasMoved()) {
            if (validCastle(destination)) {
                doCastle(destination);
                return;
            }
        }
        super.move(destination);
    }

    private boolean pieceOn(char file, int rank) {
        return getPosition().pieceAt(new ChessCoordinate(file, rank)).isPresent();
    }

    private boolean underAttack(char file, int rank) {
        ChessPlayer attacker;
        if (getOwner() == ChessPlayer.WHITE) {
            attacker = ChessPlayer.BLACK;
        } else {
            attacker = ChessPlayer.WHITE;
        }
        return (!(getPosition().getAttackers(new ChessCoordinate(file, rank), attacker).isEmpty()));
    }

    private boolean canShortCastle() {
        // In order to short castle, the King will move from
        // e1 (or e8) to g1 (or g8)
        // The rook must be present on the h file
        // There must be no pieces on f and g file
        // The f and g file must not be under attack
        // The king must not be in check
        // Rooks cannot have moved
        int kingsRank = this.getCoordinate().getRank();
        if (pieceOn('f', kingsRank) || pieceOn('g', kingsRank)) {
            return false;
        }
        Optional<ChessPiece> rookShouldBeHere = getPosition().pieceAt(new ChessCoordinate('h', kingsRank));
        if (rookShouldBeHere.isEmpty()) {
            return false;
        }
        ChessPiece shouldBeRook = rookShouldBeHere.get();
        if (!(shouldBeRook instanceof Rook)) {
            return false;
        }
        if (shouldBeRook.hasMoved()) {
            return false;
        }
        if (underAttack('f', kingsRank) || underAttack('g', kingsRank)) {
            return false;
        }
        if (getPosition().inCheck(getOwner())) {
            return false;
        }
        // Nothing wrong with castle. So allowed.
        return true;
    }

    private boolean canLongCastle() {
        // In order to short castle, the King will move from
        // e1 (or e8) to c1 (or c8)
        // The rook must be present on the a file
        // There must be no pieces on b, c, and d file
        // The b, c, and d file must not be under attack
        // The king must not be in check
        // Rooks cannot have moved
        int kingsRank = this.getCoordinate().getRank();
        if (pieceOn('b', kingsRank) || pieceOn('c', kingsRank) || pieceOn('d', kingsRank)) {
            return false;
        }
        Optional<ChessPiece> rookShouldBeHere = getPosition().pieceAt(new ChessCoordinate('a', kingsRank));
        if (rookShouldBeHere.isEmpty()) {
            return false;
        }
        ChessPiece shouldBeRook = rookShouldBeHere.get();
        if (!(shouldBeRook instanceof Rook)) {
            return false;
        }
        if (shouldBeRook.hasMoved()) {
            return false;
        }
        if (underAttack('b', kingsRank) || underAttack('c', kingsRank) || underAttack('d', kingsRank)) {
            return false;
        }
        if (getPosition().inCheck(getOwner())) {
            return false;
        }
        // Nothing wrong with castle. So allowed.
        return true;
    }

    private boolean validCastle(ChessCoordinate destination) {
        if (hasMoved()) {
            return false; // Cannot castle after move, even if you move back onto starting square
        }
        if (destination.getRank() != this.getCoordinate().getRank()) {
            return false; // Can't castle to another rank..
        }
        if (destination.getFile() == 'b' || destination.getFile() == 'c' || destination.getFile() == 'd') {
            return canLongCastle();
        } else if (destination.getFile() == 'g' || destination.getFile() == 'h') {
            return canShortCastle();
        }
        return false;
    }

    @Override
    public boolean canMoveNoLegalCheck(ChessCoordinate destination) {
        if (validCastle(destination)) {
            return true;
        }
        return MovementHelper.validKingMove(getCoordinate(), destination) &&
                MovementHelper.emptyOrEnemy(getPosition(), getOwner(), destination);
    }

    @Override
    public String identity() {
        return "K";
    }

    @Override
    public String resourcePath() {
        return ChessConstants.IMAGE_PATH_PREFIX + getPlayerIdentity() + "_king" + ChessConstants.IMAGE_PATH_EXTENSION;
    }

    public ChessPiece copy() {
        return new King(this);
    }

    public int getMaterialValue() {
        return 0;
    }
}
