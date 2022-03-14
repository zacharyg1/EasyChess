package com.github.zacharygriggs.pgn;

import com.github.zacharygriggs.chess.core.ChessMove;

import java.util.List;

public class PgnUtility {

    public static String movesToMovetext(List<ChessMove> moves) {
        StringBuilder sb = new StringBuilder();
        int moveCount = 1;
        boolean inc = false;
        sb.append(moveCount).append(". ");
        for(ChessMove move : moves) {
            sb.append(move.toString()).append(" ");
            if(!inc) {
                inc = true;
            } else {
                inc = false;
                moveCount++;
                sb.append(moveCount).append(". ");
            }
        }
        return sb.toString();
    }

}
