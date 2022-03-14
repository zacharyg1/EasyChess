package com.github.zacharygriggs.cli;

import com.github.zacharygriggs.chess.core.ChessBoard;
import com.github.zacharygriggs.chess.core.ChessCoordinate;
import com.github.zacharygriggs.chess.pieces.ChessPiece;

import java.util.Optional;
import java.util.Scanner;

public class ChessCLI {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String line;
        ChessBoard board = new ChessBoard();
        while(!board.isGameOver()) {
            try {
                System.out.println(board.toString());
                System.out.println("Current move: " + board.getMovingPlayer());
                System.out.println("You have " + board.getPosition().findLegalMoves(board.getMovingPlayer()).size() + " legal moves.");
                System.out.print("Enter a coordinate to move from: ");
                line = in.nextLine();
                ChessCoordinate coordinate = new ChessCoordinate(line);
                Optional<ChessPiece> piece = board.pieceAt(coordinate);
                if(piece.isEmpty()) {
                    System.out.println("No piece there!");
                    continue;
                }
                ChessPiece p = piece.get();
                System.out.println("Moving: " + p.toString());
                System.out.print("Enter a coordinate to move to: ");
                line = in.nextLine();
                ChessCoordinate dest = new ChessCoordinate(line);
                if(board.submitMove(coordinate, dest)) {
                    System.out.println("Move successful!");
                } else {
                    System.out.println("Invalid move.");
                }
            } catch (Exception ex) {
                System.out.println("Invalid coordinate!");
            }
        }
        System.out.println(board.getGameResult().toString());
    }
}
