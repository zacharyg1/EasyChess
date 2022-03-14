package com.github.zacharygriggs.engine;

import com.github.zacharygriggs.chess.core.ChessPosition;
import com.github.zacharygriggs.chess.data.ChessPlayer;
import com.github.zacharygriggs.chess.helper.MiscHelper;
import com.github.zacharygriggs.chess.core.ChessMove;
import com.github.zacharygriggs.chess.pieces.Bishop;
import com.github.zacharygriggs.chess.pieces.ChessPiece;
import com.github.zacharygriggs.chess.pieces.Knight;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * A chess evaluator engine.
 * Attempts to evaluate a chess position and determine the best move.
 * Supports some simple options.
 */
public class ChessEngine {

    private int depth;
    private EngineSettings settings;
    private Random random;

    private static final int DEFAULT_DEPTH = 3;
    private static final EngineSettings DEFAULT_SETTINGS = EngineSettings.BEST_MOVE;

    /**
     * Creates a chess engine with specified settings.
     *
     * @param depth    How many moves ahead to evaluate a position.
     *                 Larger depth means more evaluation time.
     * @param settings How strong the engine should be.
     */
    public ChessEngine(int depth, EngineSettings settings) {
        this.depth = depth;
        this.settings = settings;
        this.random = new Random();
    }

    /**
     * Creates a chess engine with a depth of 3 and best move setting.
     */
    public ChessEngine() {
        this(DEFAULT_DEPTH, DEFAULT_SETTINGS);
    }

    /**
     * Evaluates a position, determining how well the engine believes each
     * player is doing a selected move for the current turn.
     *
     * @param pos       The chess position to evaluate
     * @param whoseMove Who will move on this turn
     * @return An evaluation, containing an integer score and a selected move.
     */
    public EngineEvaluation evaluate(ChessPosition pos, ChessPlayer whoseMove) {
        try {
            if (settings == EngineSettings.RANDOM_MOVES) {
                return randomEval(pos, whoseMove);
            } else if (settings == EngineSettings.BEST_MOVE) {
                return bestMove(pos, whoseMove);
            } else if (settings == EngineSettings.BAD_MOVE) {
                return badMove(pos, whoseMove);
            } else {
                return null;
            }
        } catch (Exception ex) {
            // If something goes wrong in the evaluation, default to a random move
            // The random move selector is very simple and should never fail.
            // This allows the game to go on even for weird edge cases.
            ex.printStackTrace();
            return randomEval(pos, whoseMove);
        }
    }

    /**
     * Returns a flagged invalid evaluation
     *
     * @return An invalid evaluation
     */
    private void invalidEval() {
        throw new IllegalArgumentException("Needed to select a legal move, but none existed.");
    }

    /**
     * Finds a move that gives the engine the worst position possible
     *
     * @param pos       The chess position to evaluate
     * @param whoseMove Who will move on this turn
     * @return An evaluation, containing an integer score and a selected move.
     */
    private EngineEvaluation badMove(ChessPosition pos, ChessPlayer whoseMove) {
        EngineEvaluation eval = new EngineEvaluation();
        eval.setEval(Integer.MIN_VALUE);
        eval.setBestMove(null);
        List<ChessMove> moves = pos.findLegalMoves(whoseMove);
        if (moves.isEmpty()) {
            invalidEval();
        }
        for (ChessMove move : moves) {
            double thisMoveValue = evalDepthZero(pos, whoseMove);
            // What gives the opponent material?
            if (thisMoveValue > eval.getEval()) {
                eval.setBestMove(move);
                eval.setEval(thisMoveValue);
            }
        }
        return eval;
    }

    /**
     * Simple move selector to find a move to play
     * Finds a random legal move.
     *
     * @param pos       Current chess position to calculate
     * @param whoseMove Whose turn it is
     * @return A random move
     */
    private EngineEvaluation randomEval(ChessPosition pos, ChessPlayer whoseMove) {
        EngineEvaluation eval = new EngineEvaluation();
        List<ChessMove> moves = pos.findLegalMoves(whoseMove);
        if (moves.isEmpty()) {
            invalidEval();
        }
        ChessMove move = moves.get(random.nextInt(moves.size()));
        eval.setBestMove(move);
        eval.setEval(evalDepthZero(pos, whoseMove));
        return eval;
    }

    /**
     * Attempts to find the best evaluation and move.
     *
     * @param pos       Current chess position to calculate
     * @param whoseMove Whose turn it is
     * @return The attempted best move
     */
    private EngineEvaluation bestMove(ChessPosition pos, ChessPlayer whoseMove) {
        EngineEvaluation eval = new EngineEvaluation();

        ChessMove bestMove = null;
        double bestValue = Integer.MIN_VALUE;
        List<ChessMove> moves = pos.findLegalMoves(whoseMove);
        if (moves.isEmpty()) {
            invalidEval();
        }
        // Go through each possible move and find best one.
        for (ChessMove move : moves) {
            double currentVal = calculate(
                    pos.positionAfterMove(move),
                    MiscHelper.opposite(whoseMove),
                    whoseMove,
                    depth);

            Optional<ChessPiece> piece = pos.pieceAt(move.getFrom());
            // Give higher value to castling.
            // TODO: Why doesnt this work

//            if (piece.isPresent()) {
//                if (piece.get() instanceof King) {
//                    if (Math.abs(move.getFrom().getRank() - move.getTo().getRank()) > 1
//                            && move.getFrom().getFile() == 'e' && (move.getFrom().getRank() == 1
//                            || move.getFrom().getRank() == 8)) {
//                        System.out.println("Assigning higher value to castle");
//                        currentVal += 1.00;
//                    } else {
//                        currentVal -= 0.20;
//                    }
//                }
//            }

            // Prioritize piece development
            if (piece.isPresent()) {
                if (pos.getTurn() >= 3
                        && !piece.get().hasMoved()
                        && (piece.get() instanceof Knight || piece.get() instanceof Bishop)) {
                    double adjustment = 0.25 + (0.10 * (pos.getTurn() - 3));
                    if(adjustment > 1.50) {
                        adjustment = 1.50;
                    }
                    currentVal += adjustment;
                    System.out.println("Updating: " + adjustment);
                }
            }

            if (currentVal > bestValue) {
                bestValue = currentVal;
                bestMove = move;
            }
        }
        eval.setEval(bestValue);
        eval.setBestMove(bestMove);
        return eval;
    }

    /**
     * Evaluates a position at depth zero and provides a value, either negative
     * or positive, representing which player is winning.
     *
     * @param pos       Current chess position to calculate
     * @param whoseMove Whose turn it is
     * @return Evaluates the position from the perspective of the moving player.
     */
    private double evalDepthZero(ChessPosition pos, ChessPlayer whoseMove) {
        double eval = 0;
        // First priority is material.
        int myMaterial = pos.countMaterial(whoseMove);
        int enemyMaterial = pos.countMaterial(MiscHelper.opposite(whoseMove));
        eval += (myMaterial - enemyMaterial);
        // TODO: Add factors for passed pawns and king safety
        // Apply a small random factor so every game is different.
        eval += (random.nextDouble() / 2.0) - (random.nextDouble() / 2.0);
        return eval;
    }

    /**
     * Calculates the score of the current position recursively taking depth into account
     * This algorithm assumes both players play their best moves, by the engine's idea of best.
     *
     * @param pos            Current chess position to calculate
     * @param whoseMove      Whose turn it is
     * @param optimizeFor    Which player should be optimized for
     * @param depthRemaining How much depth is remaining for the calculation, ends at 0
     * @return Position score
     */
    private double calculate(ChessPosition pos, ChessPlayer whoseMove, ChessPlayer optimizeFor, int depthRemaining) {
        if (depthRemaining == 0) {
            return evalDepthZero(pos, optimizeFor);
        }
        if (whoseMove == optimizeFor) {
            return optimizeFor(pos, whoseMove, optimizeFor, depthRemaining);
        } else {
            return optimizeAgainst(pos, whoseMove, optimizeFor, depthRemaining);
        }
    }

    /**
     * Attempts to find the move that provides the highest score available.
     *
     * @param pos            Current chess position to calculate
     * @param whoseMove      Whose turn it is
     * @param optimizeFor    Which player should be optimized for
     * @param depthRemaining How much depth is remaining for the calculation, ends at 0
     * @return Highest position score
     */
    private double optimizeFor(ChessPosition pos, ChessPlayer whoseMove, ChessPlayer optimizeFor, int depthRemaining) {
        List<ChessMove> moves = pos.findLegalMoves(whoseMove);
        if (moves.isEmpty()) {
            return Integer.MAX_VALUE; // found the end...
        }
        // Go through each possible move and find best one.
        double bestValue = Integer.MIN_VALUE;
        for (ChessMove move : moves) {
            double currentVal = calculate(pos.positionAfterMove(move), MiscHelper.opposite(whoseMove),
                    optimizeFor, depthRemaining - 1);
            if (currentVal > bestValue) {
                bestValue = currentVal;
            }
        }
        return bestValue;
    }

    /**
     * Attempts to find the move that provides the lowest score available.
     *
     * @param pos            Current chess position to calculate
     * @param whoseMove      Whose turn it is
     * @param optimizeFor    Which player should be optimized for
     * @param depthRemaining How much depth is remaining for the calculation, ends at 0
     * @return Lowest position score
     */
    private double optimizeAgainst(ChessPosition pos, ChessPlayer whoseMove, ChessPlayer optimizeFor, int depthRemaining) {
        List<ChessMove> moves = pos.findLegalMoves(whoseMove);
        if (moves.isEmpty()) {
            return Integer.MAX_VALUE; // found the end...
        }
        // Go through each possible move and find best one.
        double worstValue = Integer.MAX_VALUE;
        for (ChessMove move : moves) {
            double currentVal = calculate(pos.positionAfterMove(move), MiscHelper.opposite(whoseMove),
                    optimizeFor, depthRemaining - 1);
            if (currentVal < worstValue) {
                worstValue = currentVal;
            }
        }
        return worstValue;
    }

    /**
     * Changes the difficulty of the engine.
     *
     * @param setting Engine setting
     */
    public void changeDifficulty(EngineSettings setting) {
        this.settings = setting;
    }
}
