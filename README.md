# Easy Chess

This project is a Java-based Chess board and a simple AI opponent to play against. 
All special moves (castling, en passant) are implemented. Underpromotion is not implemented.
You can move a piece either by dragging/dropping or clicking the piece and then clicking the destination.

# Running from source

After cloning source, run LaunchGUI with VM Options set to your JavaFX folder as such:
```
--module-path "C:\Program Files (x86)\javafx-sdk-17.0.1\lib" --add-modules javafx.controls,javafx.fxml
```

## AI

The Chess AI runs a low-depth material & positional calculation.

It does not value passed pawns, king safety, etc. and as such it's not difficult to beat the AI. I believe
it plays around the level of a 900.