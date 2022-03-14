package com.github.zacharygriggs.chess.core;

import com.github.zacharygriggs.chess.data.ChessConstants;

import java.util.Objects;

/**
 * Represents a space on a main.java.chess board.
 * A space is uniquely identified by a "file", which is a column represented by a letter
 * between "a" and "h", and a "rank", which is a row represented by a number between 1 and 8
 * (both inclusive)
 */
public class ChessCoordinate {

    private int rank;
    private char file;

    public static boolean validFile(char file) {
        for(char f : ChessConstants.VALID_FILES) {
            if(file == f) {
                return true;
            }
        }
        return false;
    }

    public static boolean validRank(int rank) {
        for(int r : ChessConstants.VALID_RANKS_B_PERSPECTIVE) {
            if(rank == r) {
                return true;
            }
        }
        return false;
    }

    private void checkValidCoordinate() {
        if(!validRank(this.rank)) {
            throw new IllegalArgumentException("Invalid rank: " + rank);
        }
        if(!validFile(this.file)) {
            throw new IllegalArgumentException("Invalid file: " + file);
        }
    }

    public ChessCoordinate(char file, int rank) {
        this.rank = rank;
        this.file = Character.toLowerCase(file);
        checkValidCoordinate();
    }

    public ChessCoordinate(String coordinate) {
        if(coordinate.length() != 2) {
            throw new IllegalArgumentException("Invalid main.java.chess coordinate: " + coordinate);
        }
        this.rank = Integer.parseInt("" + coordinate.charAt(1));
        this.file = Character.toLowerCase(coordinate.charAt(0));
        checkValidCoordinate();
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public char getFile() {
        return file;
    }

    public void setFile(char file) {
        this.file = file;
    }

    public String toString() {
        return file + "" + rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessCoordinate that = (ChessCoordinate) o;
        return rank == that.rank &&
                file == that.file;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, file);
    }
}
