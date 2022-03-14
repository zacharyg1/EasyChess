package com.github.zacharygriggs.chess.pieces;

import com.github.zacharygriggs.chess.core.ChessCoordinate;
import com.github.zacharygriggs.chess.core.ChessPosition;
import com.github.zacharygriggs.chess.data.ChessPlayer;
import com.github.zacharygriggs.chess.helper.MovementHelper;
import com.github.zacharygriggs.chess.core.ChessMove;
import com.github.zacharygriggs.chess.data.ChessConstants;

import java.util.List;

public class Knight extends ChessPiece {

    public Knight(ChessPosition onBoard, ChessPlayer owner, ChessCoordinate coordinate) {
        super(onBoard, owner, coordinate);
    }

    public Knight(ChessPosition onBoard, ChessPlayer owner, String coordinate) {
        super(onBoard, owner, coordinate);
    }

    public Knight(ChessPosition onBoard, ChessPlayer owner, char file, int rank) {
        super(onBoard, owner, file, rank);
    }

    public Knight(Knight old) {
        super(old);
    }

    @Override
    public boolean canMoveNoLegalCheck(ChessCoordinate destination) {
        return MovementHelper.validKnightMove(getCoordinate(), destination) &&
                MovementHelper.emptyOrEnemy(getPosition(), getOwner(), destination);
    }

    @Override
    public String identity() {
        return "N";
    }

    @Override
    public String resourcePath() {
        return ChessConstants.IMAGE_PATH_PREFIX + getPlayerIdentity() + "_knight" + ChessConstants.IMAGE_PATH_EXTENSION;
    }

    public ChessPiece copy() {
        return new Knight(this);
    }

    public int getMaterialValue() {
        return 3;
    }

    private void addIfValid(ChessCoordinate from, List<ChessMove> moves, char file, int rank) {
        if (ChessCoordinate.validFile(file) && ChessCoordinate.validRank(rank)) {
            moves.add(new ChessMove(from, new ChessCoordinate(file, rank)));
        }
    }
}
