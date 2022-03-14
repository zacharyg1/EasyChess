package com.github.zacharygriggs.chess.data;

/**
 * Constants for the game.
 */
public interface ChessConstants {

    char[] VALID_FILES = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    int[] VALID_RANKS_B_PERSPECTIVE = {1, 2, 3, 4, 5, 6, 7, 8};
    int[] VALID_RANKS_W_PERSPECTIVE = {8, 7, 6, 5, 4, 3, 2, 1};

    String IMAGE_PATH_PREFIX = "images/";
    String IMAGE_PATH_EXTENSION = ".png";
}
