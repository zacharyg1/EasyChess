package com.github.zacharygriggs.chess.helper;

import com.github.zacharygriggs.chess.data.ChessPlayer;

public class MiscHelper {

    private MiscHelper() {}

    public static ChessPlayer opposite(ChessPlayer original) {
        if(original == ChessPlayer.WHITE) {
            return ChessPlayer.BLACK;
        } else {
            return ChessPlayer.WHITE;
        }
    }
}
