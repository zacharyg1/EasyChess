package com.github.zacharygriggs.chess.core;

import com.github.zacharygriggs.chess.data.ChessConstants;
import com.github.zacharygriggs.chess.data.ChessPlayer;
import com.github.zacharygriggs.chess.data.ChessResult;
import com.github.zacharygriggs.engine.ChessEngine;
import com.github.zacharygriggs.engine.EngineEvaluation;
import com.github.zacharygriggs.engine.EngineSettings;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import com.github.zacharygriggs.pgn.PgnUtility;
import com.github.zacharygriggs.util.ImageHelper;
import com.github.zacharygriggs.util.ResourceManager;

import java.util.Timer;
import java.util.TimerTask;

public class ChessDisplay extends Canvas {

    private static final double RED_COLORING = 0.15;
    private static final double GREEN_COLORING = 0.10;
    private static final double BLUE_COLORING = 0.00;

    private Image darkSquare;
    private Image lightSquare;
    private ChessPlayer whichPlayer;

    private final int widthPerSquare;
    private final int heightPerSquare;

    private ChessBoard board;
    private ChessEngine engine;

    private int sequence = 0;
    private ChessCoordinate from;
    private ChessCoordinate to;
    private boolean gameOver = false;

    private int selectedX = -1;
    private int selectedY = -1;

    private int lastPlayerMoveFromX = -1;
    private int lastPlayerMoveFromY = -1;

    private int lastPlayerMoveToX = -1;
    private int lastPlayerMoveToY = -1;

    private int lastOpponentMoveFromX = -1;
    private int lastOpponentMoveFromY = -1;

    private int lastOpponentMoveToX = -1;
    private int lastOpponentMoveToY = -1;

    public ChessDisplay(int width, int height) {
        super(width, height);
        whichPlayer = ChessPlayer.WHITE;
        heightPerSquare = height / ChessConstants.VALID_RANKS_W_PERSPECTIVE.length;
        widthPerSquare = width / ChessConstants.VALID_FILES.length;
        engine = new ChessEngine(3, EngineSettings.BEST_MOVE);
        initialize();
    }

    private void initialize() {
        clear();
        this.darkSquare = ImageHelper.resize(
                ResourceManager.loadResourceAsCachedImage(
                        ChessConstants.IMAGE_PATH_PREFIX + "brown_dark" + ChessConstants.IMAGE_PATH_EXTENSION),
                widthPerSquare, heightPerSquare);
        this.lightSquare = ImageHelper.resize(
                ResourceManager.loadResourceAsCachedImage(
                        ChessConstants.IMAGE_PATH_PREFIX + "brown_light" + ChessConstants.IMAGE_PATH_EXTENSION),
                widthPerSquare, heightPerSquare);
        drawSquares();
    }

    private void drawSquares() {
        boolean light = true;
        GraphicsContext gc = getGraphicsContext2D();
        for(int i = 0; i < ChessConstants.VALID_RANKS_W_PERSPECTIVE.length; i++) {
            for(int j = 0; j < ChessConstants.VALID_FILES.length; j++) {
                Image which;
                if(light) {
                    which = lightSquare;
                } else {
                    which = darkSquare;
                }
                if(i == lastOpponentMoveFromX && j == lastOpponentMoveFromY) {
                    which = ImageHelper.recolor(which, RED_COLORING, GREEN_COLORING, BLUE_COLORING);
                }
                if(i == lastOpponentMoveToX && j == lastOpponentMoveToY) {
                    which = ImageHelper.recolor(which, RED_COLORING, GREEN_COLORING, BLUE_COLORING);
                }
                if(i == lastPlayerMoveFromX && j == lastPlayerMoveFromY) {
                    which = ImageHelper.recolor(which, RED_COLORING, GREEN_COLORING, BLUE_COLORING);
                }
                if(i == lastPlayerMoveToX && j == lastPlayerMoveToY) {
                    which = ImageHelper.recolor(which, RED_COLORING, GREEN_COLORING, BLUE_COLORING);
                }
                light = !light;
                gc.drawImage(which, i * widthPerSquare, j * heightPerSquare);
            }
            light = !light;
        }
    }

    public void clear() {
        getGraphicsContext2D().clearRect(0, 0, getWidth(), getHeight());
    }

    public void drawAt(Image resource,
                       ChessCoordinate position) {
        resource = ImageHelper.resize(resource,
                (int)(widthPerSquare * 0.66),
                (int)(heightPerSquare * 0.66));
        // Find top-left coordinate of square.
        int x = widthPerSquare * (position.getFile() - 'a');
        int y = heightPerSquare * (7 - (position.getRank() - 1));
        // Center it.
        x += (widthPerSquare / 6);
        y += (heightPerSquare / 6);
        getGraphicsContext2D().drawImage(resource, x, y);
    }

    public void link(ChessBoard board) {
        this.board = board;
    }

    public void redraw() {
        clear();
        drawSquares();
        if(board != null) {
            board.renderPieces(this);
        }
    }

    private void clearSelection() {
        sequence = 0;
        from = null;
        to = null;
        selectedX = -1;
        selectedY = -1;
        redraw();
    }

    private void checkGameOver() {
        if(gameOver) {
            return;
        }
        if(board.isGameOver()) {
            this.gameOver = true;
            ChessResult res = board.getGameResult();
            if(res == ChessResult.WHITE_WINS) {
                System.out.println("White wins, by checkmate");
            } else if(res == ChessResult.BLACK_WINS) {
                System.out.println("Black wins, by checkmate");
            } else if(res == ChessResult.STALEMATE) {
                System.out.println("Draw, by stalemate");
            } else if(res == ChessResult.FIFTY_MOVE_DRAW) {
                System.out.println("Draw, by fifty move rule");
            } else if(res == ChessResult.REPETITION_DRAW) {
                System.out.println("Draw, by repetition");
            } else if(res == ChessResult.INSUFFICIENT_MATERIAL) {
                System.out.println("Draw, by insufficient material");
            } else {
                System.err.println("Result: " + res);
            }
            System.out.println(PgnUtility.movesToMovetext(board.getHistory()));
        }
    }

    public void click(double x, double y, String evType) {
        try {
            if (whichPlayer == ChessPlayer.BLACK) {
                return; // use engine
            }
            // Find which square was clicked.
            selectedX = (int) (x / widthPerSquare);
            selectedY = (int) (y / heightPerSquare);
            char file = ChessConstants.VALID_FILES[selectedX];
            int rank = ChessConstants.VALID_RANKS_W_PERSPECTIVE[selectedY];
            if (sequence == 0) {
                // selecting a square.
                lastPlayerMoveFromX = (int) (x / widthPerSquare);
                lastPlayerMoveFromY = (int) (y / heightPerSquare);
                from = new ChessCoordinate(file, rank);
                if (board.pieceAt(from).isPresent() && board.pieceAt(from).get().getOwner() == whichPlayer) {
                    sequence = 1;
                } else {
                    clearSelection();
                }

            } else if (sequence == 1 && evType.equalsIgnoreCase("MOUSE_CLICKED")) {
                // selecting a destination
                to = new ChessCoordinate(file, rank);
                System.out.println("Move: " + from + " -- " + to);
                lastPlayerMoveToX = (int) (x / widthPerSquare);
                lastPlayerMoveToY = (int) (y / heightPerSquare);
                lastPlayerMoveFromX = -1;
                lastPlayerMoveFromY = -1;

                resetOpponentColoring();

                if (board.submitMove(from, to)) {
                    System.out.println("Move OK");
                    if (whichPlayer == ChessPlayer.WHITE) {
                        whichPlayer = ChessPlayer.BLACK;
                        // Use engine for black's move.
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                engineMove();
                            }
                        }, 1000);
                    } else {
                        whichPlayer = ChessPlayer.WHITE;
                    }
                    checkGameOver();
                } else {
                    resetPlayerColoring();
                }
                clearSelection();
            }
            redraw();
        } catch (Exception ex) {
            // Possibly the user clicked somewhere that wasn't counted as a valid square.
            System.out.println(ex.getMessage());
        }
    }

    private void resetOpponentColoring() {
        lastOpponentMoveFromX = -1;
        lastOpponentMoveFromY = -1;
        lastOpponentMoveToX = -1;
        lastOpponentMoveToY = -1;
    }

    private void engineMove() {
        if(gameOver) {
            return;
        }
        if(whichPlayer == ChessPlayer.BLACK) {
            EngineEvaluation eval = engine.evaluate(board.getPosition(), whichPlayer);
            board.submitMove(eval.getBestMove());
            System.out.println("Made Engine move: " + eval.getBestMove().getFrom() + " - " + eval.getBestMove().getTo());
            lastOpponentMoveFromX = (eval.getBestMove().getFrom().getFile() - 'a');
            lastOpponentMoveFromY = 8 - (eval.getBestMove().getFrom().getRank());
            lastOpponentMoveToX = (eval.getBestMove().getTo().getFile() - 'a');
            lastOpponentMoveToY = 8 - (eval.getBestMove().getTo().getRank());
            whichPlayer = ChessPlayer.WHITE;
            resetPlayerColoring();
            checkGameOver();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    redraw();
                }
            });
        }
    }

    private void resetPlayerColoring() {
        lastPlayerMoveFromX = -1;
        lastPlayerMoveFromY = -1;
        lastPlayerMoveToX = -1;
        lastPlayerMoveToY = -1;
    }

    public void changeDifficulty(EngineSettings setting) {
        this.engine.changeDifficulty(setting);
    }
}
