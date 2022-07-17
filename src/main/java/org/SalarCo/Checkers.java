package org.SalarCo;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Checkers {
    private static final int []dy = {-1, 1, -1, 1}, dxKing = {1, 1, -1, -1}, dxWhite={-1, -1};
    public Checkers(GameState gameState) {
        this.gameState = gameState;
    }
    GameState gameState;
    Rectangle[][] board;
    Pane stuff;
    List<Node> availableMoves;
    StackPane mainPane;
    boolean moved = false;
    double X_Start, Y_Start, cellSize;
    public void makeTable(Stage stage) {
        Pane pane = new Pane();
        mainPane = new StackPane();
        mainPane.getChildren().add(pane);
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        Scene scene = new Scene(mainPane,screenBounds.getWidth() / 2 , screenBounds.getHeight() / 2);
        scene.getStylesheets().add("styles/main.css");

        double tableSize = Math.min(scene.getHeight(), scene.getWidth()) - 10;
        X_Start = (scene.getWidth() - tableSize ) / 2.0;
        Y_Start = (scene.getHeight() - tableSize) / 2.0;
        cellSize = tableSize / 8.0;
        board = new Rectangle[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Rectangle(X_Start + j * cellSize, Y_Start + i * cellSize, cellSize, cellSize);
                board[i][j].setStroke(Color.TRANSPARENT);
                if ((i + j) % 2 == 0) {
                    board[i][j].setFill(Color.rgb(114, 60, 28, 1));
                } else {
                    board[i][j].setFill(Color.rgb(171, 112, 87, 1));
                }
                pane.getChildren().add(board[i][j]);
            }
        stage.setScene(scene);
    }

    void clearAvailableMoves() {
        stuff.getChildren().removeAll(availableMoves);
        availableMoves = new ArrayList<>();
    }
    public void reloadTable() {
        mainPane.getChildren().remove(stuff);
        availableMoves = new ArrayList<>();
        stuff = new Pane();
        moved = true;
        if (gameState.jumped) {
            Button endTurn = new Button("End Turn");
            stuff.getChildren().add(endTurn);
            endTurn.setOnAction(event -> {
                moved = true;
                gameState = new GameState(gameState.table, Turn.Black, gameState.steps+1);
                reloadTable();
            });
        }
        for (Coordinate i : gameState.blacks) {
            Circle circle = new Circle(X_Start + i.y * cellSize  + cellSize/2.0  , Y_Start + i.x * cellSize  + cellSize/2.0, cellSize/2.0 - 3);
            circle.setFill(Color.rgb(0, 0, 0, 1));
            stuff.getChildren().add(circle);
        }
        for (Coordinate i : gameState.blackKings) {
            Circle circle = new Circle(X_Start + i.y * cellSize + cellSize/2.0 , Y_Start + i.x * cellSize  + cellSize/2.0 , cellSize/2.0 - 3);
            circle.setFill(Color.rgb(0, 0, 0, 1));
            Label kingLabel = new Label("K");
            kingLabel.setStyle("-fx-font-size:12px;");
            kingLabel.setTextFill(Color.rgb(255, 255, 255, 1));
            kingLabel.setTranslateX(X_Start + i.y * cellSize + cellSize/2.0 - 4);
            kingLabel.setTranslateY(Y_Start + i.x * cellSize + cellSize/2.0 - 8);
            stuff.getChildren().add(circle);
            stuff.getChildren().add(kingLabel);
        }

        for (Coordinate i : gameState.whites) {
            Circle circle = new Circle(X_Start + i.y * cellSize  + cellSize/2.0 , Y_Start + i.x * cellSize + cellSize/2.0 , cellSize/2.0 - 3);
            circle.setFill(Color.rgb(255, 255, 255, 1));
            stuff.getChildren().add(circle);
            circle.setOnMouseClicked(event -> {
                if (gameState.turn == Turn.Black)
                    return;
                if (moved)
                    return;
                if (gameState.jumped) {
                    if (i != gameState.jumpedPiece) {
                        return;
                    }
                }
                clearAvailableMoves();
                for (int z = 0; z < 2; z++) {
                    int x = i.x + dxWhite[z], y = i.y + dy[z];
                    if (!gameState.isValid(x, y))
                        continue;
                    if (gameState.table[x][y] == 'E' && !gameState.jumped) {
                        Circle move = new Circle(X_Start + y * cellSize + cellSize/2.0 , Y_Start + x * cellSize  + cellSize/2.0,
                                10);
                        move.setFill(Color.rgb(0, 92, 255, 1));
                        availableMoves.add(move);
                        stuff.getChildren().add(move);
                        move.setOnMouseClicked(event1 -> {
                            char[][] tmp = gameState.cloneIt(gameState.table);
                            tmp[x][y] = 'w';
                            tmp[i.x][i.y] = 'E';
                            if (x == 0) {
                                tmp[x][y] = 'W';
                            }
                            moved = true;

                            gameState = new GameState(tmp,Turn.Black, gameState.steps+1);
                            reloadTable();
                        });
                        continue;
                    }
                    int xx = x + dxWhite[z], yy = y + dy[z];
                    if (!gameState.isValid(xx, yy))
                        continue;
                    if (gameState.table[xx][yy] != 'E')
                        continue;
                    if (gameState.table[x][y] != 'b' && gameState.table[x][y] != 'B')
                        continue;
                    Circle move = new Circle(X_Start + yy * cellSize + cellSize/2.0 , Y_Start + xx * cellSize  + cellSize/2.0,
                            10);
                    move.setFill(Color.rgb(0, 92, 255, 1));
                    availableMoves.add(move);
                    stuff.getChildren().add(move);
                    move.setOnMouseClicked(event1 -> {
                        char[][] tmp = gameState.cloneIt(gameState.table);
                        tmp[x][y] = 'E';
                        tmp[i.x][i.y] = 'E';
                        tmp[xx][yy] = 'w';
                        if (xx == 0)
                            tmp[xx][yy] = 'W';
                        moved = true;
                        if (!canJump(tmp, xx, yy))
                            gameState = new GameState(tmp,Turn.Black, gameState.steps+1);
                        else
                            gameState = new GameState(tmp, Turn.White, gameState.steps, xx, yy);
                        reloadTable();
                    });
                }
            });
        }

        for (Coordinate i : gameState.whiteKings) {
            Circle circle = new Circle(X_Start + i.y * cellSize + cellSize/2.0 , Y_Start + i.x * cellSize  + cellSize/2.0 , cellSize/2.0 - 3);
            circle.setFill(Color.rgb(255, 255, 255, 1));
            Label kingLabel = new Label("K");
            kingLabel.setStyle("-fx-font-size:12px;");
            kingLabel.setTextFill(Color.rgb(0, 0, 0, 1));
            kingLabel.setTranslateX(X_Start + i.y * cellSize + cellSize/2.0 - 4);
            kingLabel.setTranslateY(Y_Start + i.x * cellSize + cellSize/2.0 - 8);
            stuff.getChildren().add(circle);
            stuff.getChildren().add(kingLabel);
            circle.setOnMouseClicked(event -> {
                if (gameState.turn == Turn.Black)
                    return;
                if (moved)
                    return;
                if (gameState.jumped) {
                    if (i != gameState.jumpedPiece) {
                        return;
                    }
                }
                clearAvailableMoves();
                for (int z = 0; z < 4; z++) {
                    int x = i.x + dxKing[z], y = i.y + dy[z];
                    if (!gameState.isValid(x, y))
                        continue;
                    if (gameState.table[x][y] == 'E' && !gameState.jumped) {
                        Circle move = new Circle(X_Start + y * cellSize + cellSize/2.0 , Y_Start + x * cellSize  + cellSize/2.0,
                                10);
                        move.setFill(Color.rgb(0, 92, 255, 1));
                        availableMoves.add(move);
                        stuff.getChildren().add(move);
                        move.setOnMouseClicked(event1 -> {
                            char[][] tmp = gameState.cloneIt(gameState.table);
                            tmp[x][y] = 'W';
                            tmp[i.x][i.y] = 'E';
                            moved = true;

                            gameState = new GameState(tmp,Turn.Black, gameState.steps+1);
                            reloadTable();
                        });
                        continue;
                    }
                    int xx = x + dxKing[z], yy = y + dy[z];
                    if (!gameState.isValid(xx, yy))
                        continue;
                    if (gameState.table[xx][yy] != 'E')
                        continue;
                    if (gameState.table[x][y] != 'b' && gameState.table[x][y] != 'B')
                        continue;
                    Circle move = new Circle(X_Start + yy * cellSize + cellSize/2.0 , Y_Start + xx * cellSize  + cellSize/2.0,
                            10);
                    move.setFill(Color.rgb(0, 92, 255, 1));
                    availableMoves.add(move);
                    stuff.getChildren().add(move);
                    move.setOnMouseClicked(event1 -> {
                        char[][] tmp = gameState.cloneIt(gameState.table);
                        tmp[x][y] = 'E';
                        tmp[i.x][i.y] = 'E';
                        tmp[xx][yy] = 'W';

                        moved = true;
                        if (!canJump(tmp, xx, yy))
                            gameState = new GameState(tmp,Turn.Black, gameState.steps+1);
                        else
                            gameState = new GameState(tmp, Turn.White, gameState.steps);
                        reloadTable();
                    });
                }
            });
        }

        mainPane.getChildren().add(stuff);

        moved = false;
        if (gameState.turn == Turn.Black) {
            MainApp.botRunner.giveInputToBot(gameState);
        }
    }
    boolean canJump(char [][]table, int x_piece, int y_piece) {
        boolean res = false;
        if (table[x_piece][y_piece] == 'W') {
            for (int z = 0; z < 4; z++) {
                int x = x_piece + dxKing[z], y = y_piece + dy[z];
                if (!gameState.isValid(x, y))
                    continue;
                if (table[x][y] == 'E') {
                    continue;
                }
                int xx = x + dxKing[z], yy = y + dy[z];
                if (!gameState.isValid(xx, yy))
                    continue;
                if (table[xx][yy] != 'E')
                    continue;
                if (table[x][y] != 'b' && table[x][y] != 'B')
                    continue;
                res = true;
                break;
            }
        }
        else {
            for (int z = 0; z < 2; z++) {
                int x = x_piece + dxWhite[z], y = y_piece + dy[z];
                if (!gameState.isValid(x, y))
                    continue;
                if (table[x][y] == 'E') {
                    continue;
                }
                int xx = x + dxWhite[z], yy = y + dy[z];
                if (!gameState.isValid(xx, yy))
                    continue;
                if (table[xx][yy] != 'E')
                    continue;
                if (table[x][y] != 'b' && table[x][y] != 'B')
                    continue;
                res = true;
                break;
            }
        }
        return res;
    }

}
