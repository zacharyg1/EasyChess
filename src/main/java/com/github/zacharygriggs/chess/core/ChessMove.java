package com.github.zacharygriggs.chess.core;

import java.util.Objects;

public class ChessMove {

    /**
     * Declared data of the chess move - where it's from and to
     */
    private ChessCoordinate from;
    private ChessCoordinate to;

    /**
     * Metadata set by the chess board itself.
     * Whether it's a capture or not, check, checkmate.
     * What piece was moving.
     * These should not be set outside of the Board and will be
     * ignored if they are...
     */
    private boolean isCapture;
    private String pieceIdentity;
    private boolean isCheckmate;
    private boolean isCheck;

    public ChessMove() {

    }

    public ChessMove(ChessCoordinate from, ChessCoordinate to) {
        setFrom(from);
        setTo(to);
    }

    public ChessCoordinate getFrom() {
        return from;
    }

    public void setFrom(ChessCoordinate from) {
        this.from = from;
    }

    public ChessCoordinate getTo() {
        return to;
    }

    public void setTo(ChessCoordinate to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(from, chessMove.from) &&
                Objects.equals(to, chessMove.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        // Leave out the piece identity if it's a pawn since it's assumed.
        if(pieceIdentity != null && !pieceIdentity.equalsIgnoreCase("P")) {
            sb.append(pieceIdentity.toUpperCase());
        }
        sb.append(from.toString());
        if(isCapture) {
            sb.append("x");
        }
        sb.append(to.toString());
        if(isCheckmate) {
            sb.append("#");
        } else if(isCheck) {
            sb.append("+");
        }
        return sb.toString();
    }

    public void setCapture(boolean isCapture) {
        this.isCapture = isCapture;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public void setCheckmate(boolean isCheckmate) {
        this.isCheckmate = isCheckmate;
    }

    public void setPieceIdentity(String pieceIdentity) {
        this.pieceIdentity = pieceIdentity;
    }
}
