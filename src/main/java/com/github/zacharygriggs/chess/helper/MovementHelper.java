package com.github.zacharygriggs.chess.helper;

import com.github.zacharygriggs.chess.core.ChessCoordinate;
import com.github.zacharygriggs.chess.data.ChessPlayer;
import com.github.zacharygriggs.chess.core.ChessPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to determine if a move is valid for a type of piece.
 * Does not handle logic of blocking pieces and such,
 * only whether it can ever be a legal move.
 * <p>
 * Pawn movements/capture is not included here because it depends
 * on which player the pawn belongs to.
 */
public class MovementHelper {

    private MovementHelper() {}

    /**
     * Determines if moving from one to two is a valid Rook move.
     *
     * @param one   First coordinate
     * @param two   Second coordinate
     * @return      True if possibly valid; false if not.
     */
    public static boolean validRookMove(ChessCoordinate one, ChessCoordinate two) {
        int rankDist = Math.abs(one.getRank() - two.getRank());
        int fileDist = Math.abs(one.getFile() - two.getFile());
        return (!one.equals(two) && (rankDist == 0 || fileDist == 0));
    }

    /**
     * Determines if moving from one to two is a valid Bishop move.
     *
     * @param one   First coordinate
     * @param two   Second coordinate
     * @return      True if possibly valid; false if not.
     */
    public static boolean validBishopMove(ChessCoordinate one, ChessCoordinate two) {
        int rankDist = Math.abs(one.getRank() - two.getRank());
        int fileDist = Math.abs(one.getFile() - two.getFile());
        return (!one.equals(two) && (rankDist == fileDist));
    }

    /**
     * Determines if moving from one to two is a valid Knight move.
     *
     * @param one   First coordinate
     * @param two   Second coordinate
     * @return      True if possibly valid; false if not.
     */
    public static boolean validKnightMove(ChessCoordinate one, ChessCoordinate two) {
        int rankDist = Math.abs(one.getRank() - two.getRank());
        int fileDist = Math.abs(one.getFile() - two.getFile());
        return (!one.equals(two) && ((rankDist == 1 && fileDist == 2)) || (rankDist == 2 && fileDist == 1));
    }

    /**
     * Determines if moving from one to two is a valid Queen move.
     *
     * @param one   First coordinate
     * @param two   Second coordinate
     * @return      True if possibly valid; false if not.
     */
    public static boolean validQueenMove(ChessCoordinate one, ChessCoordinate two) {
        return validRookMove(one, two) || validBishopMove(one, two);
    }

    /**
     * Determines if moving from one to two is a valid King move.
     *
     * @param one   First coordinate
     * @param two   Second coordinate
     * @return      True if possibly valid; false if not.
     */
    public static boolean validKingMove(ChessCoordinate one, ChessCoordinate two) {
        int rankDist = Math.abs(one.getRank() - two.getRank());
        int fileDist = Math.abs(one.getFile() - two.getFile());
        return (!one.equals(two) && (rankDist <= 1 && fileDist <= 1));
    }

    /**
     * Gets all coordinates between two points on a diagonal.
     *
     * @param one   First space on a diagonal
     * @param two   Second space on a diagonal
     * @return      Spaces between one -> two
     */
    public static List<ChessCoordinate> spacesBetweenDiagonal(ChessCoordinate one, ChessCoordinate two) {
        if(!validBishopMove(one, two)) {
            throw new RuntimeException("Not a valid diagonal.");
        }
        List<ChessCoordinate> coordinates = new ArrayList<>();
        int fileDist = one.getFile() - two.getFile();
        int rankDist = one.getRank() - two.getRank();
        int fileInc = 0;
        int rankInc = 0;
        if(fileDist < 0) {
            fileInc = 1;
        } else {
            fileInc = -1;
        }
        if(rankDist < 0) {
            rankInc = 1;
        } else {
            rankInc = -1;
        }
        ChessCoordinate coord = one;
        for(int i = 0; i < Math.abs(fileDist) - 1; i++) {
            coord = new ChessCoordinate((char)(coord.getFile() + fileInc), coord.getRank() + rankInc);
            coordinates.add(coord);
        }
        return coordinates;
    }

    /**
     * Gets all coordinates between two points on a horizontal/vertical line.
     *
     * @param one   First space on a line
     * @param two   Second space on a line
     * @return      Spaces between one -> two
     */
    public static List<ChessCoordinate> spacesBetweenLine(ChessCoordinate one, ChessCoordinate two) {
        if(!validRookMove(one, two)) {
            throw new RuntimeException("Not a valid horizontal/vertical.");
        }
        List<ChessCoordinate> coordinates = new ArrayList<>();
        int fileDist = one.getFile() - two.getFile();
        int rankDist = one.getRank() - two.getRank();
        int fileInc = 0;
        int rankInc = 0;
        if(fileDist < 0) {
            fileInc = 1;
        } else if(fileDist > 0) {
            fileInc = -1;
        }
        if(rankDist < 0) {
            rankInc = 1;
        } else if(rankDist > 0) {
            rankInc = -1;
        }
        ChessCoordinate coord = one;
        for(int i = 0; i < Math.max(Math.abs(fileDist), Math.abs(rankDist)) - 1; i++) {
            coord = new ChessCoordinate((char)(coord.getFile() + fileInc), coord.getRank() + rankInc);
            coordinates.add(coord);
        }
        return coordinates;
    }

    /**
     * Gets all coordinates between two points on either a line or a diagonal
     *
     * @param one   First space
     * @param two   Second space
     * @return      Spaces between one -> two
     */
    public static List<ChessCoordinate> spacesBetween(ChessCoordinate one, ChessCoordinate two) {
        if(validBishopMove(one, two)) {
            return spacesBetweenDiagonal(one, two);
        } else if(validRookMove(one, two)) {
            return spacesBetweenLine(one, two);
        } else {
            throw new RuntimeException("Not a valid between calculation");
        }
    }

    public static boolean emptyOrEnemy(ChessPosition board, ChessPlayer owner, ChessCoordinate destination) {
        return board.pieceAt(destination).isEmpty() ||
                board.pieceAt(destination).get().getOwner() != owner;
    }

    public static boolean emptyBetween(ChessPosition board, ChessCoordinate coordinate, ChessCoordinate destination) {
        try {
            for (ChessCoordinate coord : spacesBetween(coordinate, destination)) {
                if (board.pieceAt(coord).isPresent()) {
                    return false;
                }
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
