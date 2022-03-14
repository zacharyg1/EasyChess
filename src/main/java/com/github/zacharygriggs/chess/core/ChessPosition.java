package com.github.zacharygriggs.chess.core;

import com.github.zacharygriggs.chess.data.ChessConstants;
import com.github.zacharygriggs.chess.data.ChessPlayer;
import com.github.zacharygriggs.chess.pieces.*;

import java.util.*;

public class ChessPosition {

    private List<ChessPiece> pieces;
    private Map<ChessPlayer, List<ChessPiece>> capturedPieces;
    private int turn = 1;

    /**
     * Initializes a new (empty) position.
     */
    public ChessPosition() {
        pieces = new ArrayList<>();
        capturedPieces = new HashMap<>();
        capturedPieces.put(ChessPlayer.WHITE, new ArrayList<>());
        capturedPieces.put(ChessPlayer.BLACK, new ArrayList<>());
    }

    public ChessPosition(ChessPosition old) {
        pieces = new ArrayList<>();
        capturedPieces = new HashMap<>();
        for(ChessPiece piece : old.pieces) {
            ChessPiece newPiece = piece.copy();
            newPiece.setPosition(this);
            pieces.add(newPiece);
        }
        for(ChessPlayer player : old.capturedPieces.keySet()) {
            List<ChessPiece> capturedPiecesList = new ArrayList<>();
            for(ChessPiece p :old.capturedPieces.get(player)) {
                capturedPiecesList.add(p.copy());
            }
            capturedPieces.put(player, capturedPiecesList);
        }
        this.turn = old.turn;
    }

    /**
     * Adds a pawn to the board.
     *
     * @param player        Owning player
     * @param coordinate    Coordinate to add to
     */
    private void addPawn(ChessPlayer player, String coordinate) {
        pieces.add(new Pawn(this, player, coordinate));
    }

    /**
     * Adds a knight to the board.
     *
     * @param player        Owning player
     * @param coordinate    Coordinate to add to
     */
    private void addKnight(ChessPlayer player, String coordinate) {
        pieces.add(new Knight(this, player, coordinate));
    }

    /**
     * Adds a bishop to the board.
     *
     * @param player        Owning player
     * @param coordinate    Coordinate to add to
     */
    private void addBishop(ChessPlayer player, String coordinate) {
        pieces.add(new Bishop(this, player, coordinate));
    }

    /**
     * Adds a queen to the board.
     *
     * @param player        Owning player
     * @param coordinate    Coordinate to add to
     */
    private void addQueen(ChessPlayer player, String coordinate) {
        pieces.add(new Queen(this, player, coordinate));
    }

    /**
     * Adds a king to the board.
     *
     * @param player        Owning player
     * @param coordinate    Coordinate to add to
     */
    private void addKing(ChessPlayer player, String coordinate) {
        pieces.add(new King(this, player, coordinate));
    }

    /**
     * Adds a rook to the board.
     *
     * @param player        Owning player
     * @param coordinate    Coordinate to add to
     */
    private void addRook(ChessPlayer player, String coordinate) {
        pieces.add(new Rook(this, player, coordinate));
    }

    /**
     * Calculates what the position will be after a specific
     * move is made. The move may be illegal and it will still
     * provide back a position.
     *
     * @param move  Move to make
     * @return      New Chess Position after that move.
     */
    public ChessPosition positionAfterMove(ChessMove move) {
        ChessPosition newPos = new ChessPosition(this);
        Optional<ChessPiece> optPiece = newPos.pieceAt(move.getFrom());
        optPiece.ifPresent(chessPiece -> chessPiece.move(move.getTo()));
        return newPos;
    }

    /**
     * Creates the starting chess position.
     *
     * @return  Starting chess position.
     */
    public static ChessPosition startingPosition() {
        ChessPosition pos = new ChessPosition();
        for(char file: ChessConstants.VALID_FILES) {
            pos.addPawn(ChessPlayer.WHITE, file + "2");
            pos.addPawn(ChessPlayer.BLACK, file + "7");
        }
        pos.addRook(ChessPlayer.WHITE, "a1");
        pos.addRook(ChessPlayer.WHITE, "h1");
        pos.addKnight(ChessPlayer.WHITE, "b1");
        pos.addKnight(ChessPlayer.WHITE, "g1");
        pos.addBishop(ChessPlayer.WHITE, "c1");
        pos.addBishop(ChessPlayer.WHITE, "f1");
        pos.addQueen(ChessPlayer.WHITE, "d1");
        pos.addKing(ChessPlayer.WHITE, "e1");

        pos.addRook(ChessPlayer.BLACK, "a8");
        pos.addRook(ChessPlayer.BLACK, "h8");
        pos.addKnight(ChessPlayer.BLACK, "b8");
        pos.addKnight(ChessPlayer.BLACK, "g8");
        pos.addBishop(ChessPlayer.BLACK, "c8");
        pos.addBishop(ChessPlayer.BLACK, "f8");
        pos.addQueen(ChessPlayer.BLACK, "d8");
        pos.addKing(ChessPlayer.BLACK, "e8");
        return pos;
    }

    public List<ChessPiece> getPieces() {
        return pieces;
    }

    public void setPieces(List<ChessPiece> pieces) {
        this.pieces = pieces;
    }

    /**
     * Finds the piece at the given coordinate, or empty
     * if there's no piece there.
     *
     * @param coordinate    Coordinate to check
     * @return              Optional of piece, or empty optional.
     */
    public Optional<ChessPiece> pieceAt(ChessCoordinate coordinate) {
        for(ChessPiece piece: pieces) {
            if(piece.getCoordinate().equals(coordinate)) {
                return Optional.of(piece);
            }
        }
        return Optional.empty();
    }

    public void addCapturedPiece(ChessPlayer owner, ChessPiece whichPiece) {
        capturedPieces.get(owner).add(whichPiece);
    }

    /**
     * Creates a String representation of this main.java.chess position.
     *
     * @return  Chess board String
     */
    public String toString() {
        Map<ChessCoordinate, ChessPiece> pieceMapping = new HashMap<>();
        for(ChessPiece piece : pieces) {
            pieceMapping.put(piece.getCoordinate(), piece);
        }
        StringBuilder boardString = new StringBuilder();
        // Add header with all of the files on it.
        boardString.append("     |    ");
        for(char file : ChessConstants.VALID_FILES) {
            boardString.append(file).append("    |    ");
        }
        boardString.append("\n");
        for(int rank: ChessConstants.VALID_RANKS_W_PERSPECTIVE) {

            boardString.append("  ").append(rank).append("  |");
            for(char file : ChessConstants.VALID_FILES) {
                String pieceHere = "         ";
                ChessCoordinate thisCoordinate = new ChessCoordinate(file, rank);
                if(pieceMapping.containsKey(thisCoordinate)) {
                    pieceHere = " " + pieceMapping.get(thisCoordinate).toString() + " ";
                }
                boardString.append(pieceHere).append("|");
            }
            boardString.append("\n");
        }
        return boardString.toString();
    }

    /**
     * Locates the king of a specified player.
     *
     * @param owner Player to find king of.
     * @return      King, or exception thrown if king is missing.
     */
    public ChessPiece getKing(ChessPlayer owner) {
        for(ChessPiece piece : pieces) {
            if(piece instanceof King && piece.getOwner() == owner) {
                return piece;
            }
        }
        System.out.println("Current position lacks a king:");
        System.out.println(toString());
        throw new RuntimeException("No king for player " + owner);
    }

    /**
     * Gets all pieces that are attacking another piece.
     *
     * @param whichPiece    Piece possibly under attack
     * @return              List of attackers. Empty list if not attacked.
     */
    public List<ChessPiece> getAttackers(ChessPiece whichPiece) {
        List<ChessPiece> attackers = new ArrayList<>();
        for(ChessPiece piece : pieces) {
            if(piece.getOwner() != whichPiece.getOwner() && piece.canMoveNoLegalCheck(whichPiece.getCoordinate())) {
                attackers.add(piece);
            }
        }
        return attackers;
    }

    /**
     * Gets all pieces that are attacking a square
     *
     * @param square        Square possibly under attack
     * @param whichPlayer The player who can attack this (opposite of owner)
     * @return              List of attackers. Empty list if not attacked.
     */
    public List<ChessPiece> getAttackers(ChessCoordinate square, ChessPlayer whichPlayer) {
        List<ChessPiece> attackers = new ArrayList<>();
        for(ChessPiece piece : pieces) {
            if(piece.getOwner() == whichPlayer && piece.canMoveNoLegalCheck(square)) {
                attackers.add(piece);
            }
        }
        return attackers;
    }

    /**
     * Determines if a player is in check.
     *
     * @param whichPlayer   Player to determine for
     * @return              True if in check; false if not.
     */
    public boolean inCheck(ChessPlayer whichPlayer) {
        ChessPiece king = getKing(whichPlayer);
        return !getAttackers(king).isEmpty();
    }

    /**
     * Gets a list of legal moves for the player.
     *
     * @param whichPlayer   Player to get legal moves for
     * @return              List of legal moves
     */
    public List<ChessMove> findLegalMoves(ChessPlayer whichPlayer) {
        List<ChessMove> moves = new ArrayList<>();
        for(ChessPiece piece : pieces) {
            if(whichPlayer == piece.getOwner()) {
                for(char file : ChessConstants.VALID_FILES) {
                    for(int rank: ChessConstants.VALID_RANKS_W_PERSPECTIVE) {
                        if(piece.canMove(new ChessCoordinate(file, rank))) {
                            ChessMove move = new ChessMove();
                            move.setFrom(piece.getCoordinate());
                            move.setTo(new ChessCoordinate(file, rank));
                            moves.add(move);
                        }
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Determines if a player has no legal moves.
     *
     * @param whichPlayer   Player to check for legal moves
     * @return              True if there are no legal moves. False otherwise.
     */
    public boolean noLegalMoves(ChessPlayer whichPlayer) {
        // Code duplicated intentionally for early exit.
        for(ChessPiece piece : pieces) {
            if(whichPlayer == piece.getOwner()) {
                for(char file : ChessConstants.VALID_FILES) {
                    for(int rank: ChessConstants.VALID_RANKS_W_PERSPECTIVE) {
                        try {
                            if (piece.canMove(new ChessCoordinate(file, rank))) {
                                return false;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Removes a piece from the board.
     *
     * @param whichPiece    Piece to remove.
     */
    public void removePiece(ChessPiece whichPiece) {
        pieces.remove(whichPiece);
    }

    public void updateAllPieces() {
        for(ChessPiece piece: pieces) {
            piece.setPosition(this);
        }
    }

    public void promote(Pawn pawn) {
        ChessCoordinate coord = pawn.getCoordinate();
        ChessPlayer owner = pawn.getOwner();
        this.removePiece(pawn);
        this.pieces.add(new Queen(this, owner, coord));
        updateAllPieces();
    }

    /**
     * Gets numeric value of material for a player.
     *
     * @param who   Who to calculate material for
     * @return      Total material
     */
    public int countMaterial(ChessPlayer who) {
        int mat = 0;
        for(ChessPiece piece : pieces) {
            if(piece.getOwner() == who) {
                mat += piece.getMaterialValue();
            }
        }
        return mat;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
}
