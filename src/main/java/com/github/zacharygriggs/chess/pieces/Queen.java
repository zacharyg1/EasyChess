package com.github.zacharygriggs.chess.pieces;

import com.github.zacharygriggs.chess.core.ChessCoordinate;
import com.github.zacharygriggs.chess.core.ChessPosition;
import com.github.zacharygriggs.chess.data.ChessPlayer;
import com.github.zacharygriggs.chess.helper.MovementHelper;

import static com.github.zacharygriggs.chess.data.ChessConstants.IMAGE_PATH_EXTENSION;
import static com.github.zacharygriggs.chess.data.ChessConstants.IMAGE_PATH_PREFIX;

public class Queen extends ChessPiece {

    public Queen(ChessPosition onBoard, ChessPlayer owner, ChessCoordinate coordinate) {
        super(onBoard, owner, coordinate);
    }

    public Queen(ChessPosition onBoard, ChessPlayer owner, String coordinate) {
        super(onBoard, owner, coordinate);
    }

    public Queen(ChessPosition onBoard, ChessPlayer owner, char file, int rank) {
        super(onBoard, owner, file, rank);
    }

    public Queen(Queen old) {
        super(old);
    }

    @Override
    public boolean canMoveNoLegalCheck(ChessCoordinate destination) {
        return MovementHelper.validQueenMove(getCoordinate(), destination) &&
                MovementHelper.emptyBetween(getPosition(), getCoordinate(), destination) &&
                MovementHelper.emptyOrEnemy(getPosition(), getOwner(), destination);
    }

    @Override
    public String identity() {
        return "Q";
    }

    @Override
    public String resourcePath() {
        return IMAGE_PATH_PREFIX + getPlayerIdentity() + "_queen" + IMAGE_PATH_EXTENSION;
    }

    public ChessPiece copy() {
        return new Queen(this);
    }

    public int getMaterialValue() {
        return 9;
    }

}
