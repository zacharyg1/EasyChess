package com.github.zacharygriggs.chess.pieces;

import com.github.zacharygriggs.chess.core.ChessCoordinate;
import com.github.zacharygriggs.chess.core.ChessPosition;
import com.github.zacharygriggs.chess.data.ChessPlayer;
import com.github.zacharygriggs.chess.helper.MovementHelper;
import com.github.zacharygriggs.chess.data.ChessConstants;

public class Bishop extends ChessPiece {

    public Bishop(ChessPosition onBoard, ChessPlayer owner, ChessCoordinate coordinate) {
        super(onBoard, owner, coordinate);
    }

    public Bishop(ChessPosition onBoard, ChessPlayer owner, String coordinate) {
        super(onBoard, owner, coordinate);
    }

    public Bishop(ChessPosition onBoard, ChessPlayer owner, char file, int rank) {
        super(onBoard, owner, file, rank);
    }

    public Bishop(Bishop old) {
        super(old);
    }

    @Override
    public boolean canMoveNoLegalCheck(ChessCoordinate destination) {
        if (!MovementHelper.validBishopMove(getCoordinate(), destination)) {
            return false;
        }
        if (!MovementHelper.emptyBetween(getPosition(), getCoordinate(), destination)) {
            return false;
        }
        if (!MovementHelper.emptyOrEnemy(getPosition(), getOwner(), destination)) {
            return false;
        }
        return true;
    }

    @Override
    public String identity() {
        return "B";
    }

    @Override
    public String resourcePath() {
        return ChessConstants.IMAGE_PATH_PREFIX + getPlayerIdentity() + "_bishop" + ChessConstants.IMAGE_PATH_EXTENSION;
    }

    public ChessPiece copy() {
        return new Bishop(this);
    }

    public int getMaterialValue() {
        return 3;
    }
}
