package com.github.zacharygriggs.pgn;

public class Pgn {

    private PgnHeader header;

    private String movetext;

    public PgnHeader getHeader() {
        return header;
    }

    public void setHeader(PgnHeader header) {
        this.header = header;
    }

    public String getMovetext() {
        return movetext;
    }

    public void setMovetext(String movetext) {
        this.movetext = movetext;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[Event \"").append(header.getEvent()).append("\"]\n");
        sb.append("[Site \"").append(header.getSite()).append("\"]\n");
        sb.append("[Date \"").append(header.getDate()).append("\"]\n");
        sb.append("[Round \"").append(header.getRound()).append("\"]\n");
        sb.append("[White \"").append(header.getWhite()).append("\"]\n");
        sb.append("[Black \"").append(header.getBlack()).append("\"]\n");
        sb.append("[Result \"").append(header.getResult()).append("\"]\n");
        sb.append("\n");
        sb.append(movetext);
        return sb.toString();
    }
}
