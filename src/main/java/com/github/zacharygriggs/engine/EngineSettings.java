package com.github.zacharygriggs.engine;

/**
 * Settings for how strong the chess engine should be.
 */
public enum EngineSettings {
    /**
     * Makes a random legal move.
     */
    RANDOM_MOVES,
    /**
     * Plays the best move, to the engine's knowledge.
     */
    BEST_MOVE,
    /**
     * Plays a move that the engine believes will make it lose as fast as possible.
     * This mode can be fun to practice attacking.
     */
    BAD_MOVE,

}
