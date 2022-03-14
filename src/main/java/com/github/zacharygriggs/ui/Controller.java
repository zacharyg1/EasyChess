package com.github.zacharygriggs.ui;

import com.github.zacharygriggs.chess.core.ChessBoard;
import com.github.zacharygriggs.chess.core.ChessDisplay;
import com.github.zacharygriggs.pgn.PgnUtility;
import com.github.zacharygriggs.engine.EngineSettings;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;

import javax.swing.*;

/**
 * Controller for the chess UI
 */
public final class Controller {

    private ChessDisplay display;
    private ChessBoard board;
    private ComboBox<String> difficulties;

    /**
     * Sets up the game using the controls.
     *
     * @param display   Chess UI/display
     */
    public void setup(ChessDisplay display, ComboBox<String> difficulties) {
        this.display = display;
        this.board = new ChessBoard();
        this.display.link(this.board);
        this.display.redraw();
        this.difficulties = difficulties;

        display.setOnMouseClicked(this::handle);
        display.setOnMouseDragged(this::handle);
    }

    /**
     * Passes through a mouse event to the chess display.
     *
     * @param ev    Mouse event
     */
    private void handle(MouseEvent ev) {
        display.click(ev.getX(), ev.getY(), ev.getEventType().getName());
    }

    /**
     * Changes the engine's difficulty.
     *
     * @param actionEvent   Ignored
     */
    public void changeDifficulty(ActionEvent actionEvent) {
        EngineSettings newDifficulty = EngineSettings.RANDOM_MOVES;
        String desired = difficulties.getSelectionModel().getSelectedItem();
        if(desired.equalsIgnoreCase("max")) {
            newDifficulty = EngineSettings.BEST_MOVE;
        } else if(desired.equalsIgnoreCase("random")) {
            newDifficulty = EngineSettings.RANDOM_MOVES;
        } else if(desired.equalsIgnoreCase("low")) {
            newDifficulty = EngineSettings.BAD_MOVE;
        }
        display.changeDifficulty(newDifficulty);
    }

    public void restart(ActionEvent actionEvent) {
        int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to restart?");
        if(option == JOptionPane.OK_OPTION) {
            System.out.println(PgnUtility.movesToMovetext(board.getHistory()));
            this.board = new ChessBoard();
            this.display.link(this.board);
            this.display.redraw();
        }
    }

    public void engineTakeOver(ActionEvent actionEvent) {
    }
}
