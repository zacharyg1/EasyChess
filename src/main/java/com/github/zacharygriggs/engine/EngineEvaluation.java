package com.github.zacharygriggs.engine;

import com.github.zacharygriggs.chess.core.ChessMove;

/**
 * Model class for the evaluation of the chess engine
 * Stores the selected move and the position score
 */
public class EngineEvaluation {

    private ChessMove bestMove;
    private double eval;

    public ChessMove getBestMove() {
        return bestMove;
    }

    public void setBestMove(ChessMove bestMove) {
        this.bestMove = bestMove;
    }

    public double getEval() {
        return eval;
    }

    public void setEval(double eval) {
        this.eval = eval;
    }
}
