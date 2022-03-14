package com.github.zacharygriggs.chess.pieces;

import com.github.zacharygriggs.chess.core.ChessCoordinate;
import com.github.zacharygriggs.chess.core.ChessPosition;
import com.github.zacharygriggs.chess.data.ChessPlayer;
import com.github.zacharygriggs.chess.helper.MovementHelper;
import com.github.zacharygriggs.chess.data.ChessConstants;

public class Rook extends ChessPiece {


    public Rook(ChessPosition onBoard, ChessPlayer owner, ChessCoordinate coordinate) {
        super(onBoard, owner, coordinate);
    }

    public Rook(ChessPosition onBoard, ChessPlayer owner, String coordinate) {
        super(onBoard, owner, coordinate);
    }

    public Rook(ChessPosition onBoard, ChessPlayer owner, char file, int rank) {
        super(onBoard, owner, file, rank);
    }

    public Rook(Rook old) {
        super(old);
    }

    public void forceMove(ChessCoordinate destination) {
        super.forceMove(destination);
    }

    public void move(ChessCoordinate destination) {
        super.move(destination);
    }

    @Override
    public boolean canMoveNoLegalCheck(ChessCoordinate destination) {
        return MovementHelper.validRookMove(getCoordinate(), destination) &&
                MovementHelper.emptyBetween(getPosition(), getCoordinate(), destination) &&
                MovementHelper.emptyOrEnemy(getPosition(), getOwner(), destination);
    }

    @Override
    public String identity() {
        return "R";
    }

    @Override
    public String resourcePath() {
        return ChessConstants.IMAGE_PATH_PREFIX + getPlayerIdentity() + "_rook" + ChessConstants.IMAGE_PATH_EXTENSION;
    }

    public ChessPiece copy() {
        return new Rook(this);
    }

    public int getMaterialValue() {
        return 5;
    }
}
