package org.SalarCo;

import java.util.ArrayList;
import java.util.LinkedList;

public class GameState implements Comparable<GameState> {
    public final ArrayList<Coordinate> whites;
    public final ArrayList<Coordinate> whiteKings;
    public final ArrayList<Coordinate> blacks;
    public final ArrayList<Coordinate> blackKings;
    public int score = 0;

    public boolean jumped;
    public Coordinate jumpedPiece;
    public final char [][]table;
    public int steps;
    public Coordinate [][]coordinates;
    public Turn turn;
    public boolean playerTurnCanMove;

    public Coordinate playerTurnCanMoveCord;

    public Coordinate playerTurnCanMoveEndCord;
    boolean isValid(int x, int y) {
        return Math.max(x, y) < 8 && Math.min(x, y)>= 0;
    }

    private static final int []dy = {-1, 1, -1, 1}, dxKing = {1, 1, -1, -1}, dxWhite={-1, -1},
        dxBlack={1,1};
    public GameState(char [][]a, Turn turn, int steps) {
        this.steps = steps;
        this.turn =turn;
        whites = new ArrayList<>();
        whiteKings = new ArrayList<>();
        blacks = new ArrayList<>();
        blackKings = new ArrayList<>();
        table = cloneIt(a);
        coordinates = new Coordinate[8][8];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                coordinates[i][j] = new Coordinate(i, j);
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                if (table[i][j] != 'E') {
                    if (table[i][j] == 'W' || table[i][j] == 'w') {
                        if (table[i][j] == 'w')
                            whites.add(coordinates[i][j]);
                        else
                            whiteKings.add(coordinates[i][j]);
                    }
                    else {
                        if (table[i][j] == 'b')
                            blacks.add(coordinates[i][j]);
                        else
                            blackKings.add(coordinates[i][j]);
                    }
                }
            }
        endCreation();
    }
    public GameState(char [][]a, Turn turn, int steps, int Xjumped, int Yjumped) {
        this.steps = steps;
        this.turn =turn;
        whites = new ArrayList<>();
        whiteKings = new ArrayList<>();
        blacks = new ArrayList<>();
        blackKings = new ArrayList<>();
        table = cloneIt(a);
        coordinates = new Coordinate[8][8];
        jumped = true;
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                coordinates[i][j] = new Coordinate(i, j);
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                if (table[i][j] != 'E') {
                    if (table[i][j] == 'W' || table[i][j] == 'w') {
                        if (table[i][j] == 'w')
                            whites.add(coordinates[i][j]);
                        else
                            whiteKings.add(coordinates[i][j]);
                    }
                    else {
                        if (table[i][j] == 'b')
                            blacks.add(coordinates[i][j]);
                        else
                            blackKings.add(coordinates[i][j]);
                    }
                }
            }
        jumpedPiece = coordinates[Xjumped][Yjumped];
    }

    public void endCreation() {
        playerTurnCanMove = false;
        if (Turn.White == turn) {
            for (Coordinate i : whites) {
                if (playerTurnCanMove)
                    break;
                for (int z = 0; z < 2; z++) {
                    if (playerTurnCanMove)
                        break;
                    int x = i.x + dxWhite[z], y = i.y + dy[z];
                    if (!isValid(x, y))
                        continue;
                    if (table[x][y] == 'E') {
                        playerTurnCanMoveCord = i;
                        playerTurnCanMoveEndCord = coordinates[x][y];
                        playerTurnCanMove = true;
                        break;
                    }
                    int xx = x + dxWhite[z], yy = y + dy[z];
                    if (!isValid(xx, yy))
                        continue;
                    if (table[xx][yy] != 'E')
                        continue;
                    if (table[x][y] != 'b' && table[x][y] != 'B')
                        continue;
                    playerTurnCanMoveCord = i;
                    playerTurnCanMoveEndCord = coordinates[xx][yy];
                    playerTurnCanMove = true;
                    break;
                }
            }
            for (Coordinate i : whiteKings) {
                if (playerTurnCanMove)
                    break;
                for (int z = 0; z < 4; z++) {
                    if (playerTurnCanMove)
                        break;
                    int x = i.x + dxKing[z], y = i.y + dy[z];
                    if (!isValid(x, y))
                        continue;
                    if (table[x][y] == 'E') {
                        playerTurnCanMoveCord = i;
                        playerTurnCanMoveEndCord = coordinates[x][y];
                        playerTurnCanMove = true;
                        break;
                    }
                    int xx = x + dxKing[z], yy = y + dy[z];
                    if (!isValid(xx, yy))
                        continue;
                    if (table[xx][yy] != 'E')
                        continue;
                    if (table[x][y] != 'b' && table[x][y] != 'B')
                        continue;
                    playerTurnCanMoveCord = i;
                    playerTurnCanMoveEndCord = coordinates[xx][yy];
                    playerTurnCanMove = true;
                    break;
                }
            }
        }
        else {
            for (Coordinate i : blacks) {
                if (playerTurnCanMove)
                    break;
                for (int z = 0; z < 2; z++) {
                    if (playerTurnCanMove)
                        break;
                    int x = i.x + dxBlack[z], y = i.y + dy[z];
                    if (!isValid(x, y))
                        continue;
                    if (table[x][y] == 'E') {
                        playerTurnCanMoveCord = i;
                        playerTurnCanMoveEndCord = coordinates[x][y];
                        playerTurnCanMove = true;
                        break;
                    }
                    int xx = x + dxBlack[z], yy = y + dy[z];
                    if (!isValid(xx, yy))
                        continue;
                    if (table[xx][yy] != 'E')
                        continue;
                    if (table[x][y] != 'w' && table[x][y] != 'W')
                        continue;
                    playerTurnCanMoveCord = i;
                    playerTurnCanMoveEndCord = coordinates[xx][yy];
                    playerTurnCanMove = true;
                    break;
                }
            }
            for (Coordinate i : blackKings) {
                if (playerTurnCanMove)
                    break;
                for (int z = 0; z < 4; z++) {
                    if (playerTurnCanMove)
                        break;
                    int x = i.x + dxKing[z], y = i.y + dy[z];
                    if (!isValid(x, y))
                        continue;
                    if (table[x][y] == 'E') {
                        playerTurnCanMoveCord = i;
                        playerTurnCanMoveEndCord = coordinates[x][y];
                        playerTurnCanMove = true;
                        break;
                    }
                    int xx = x + dxKing[z], yy = y + dy[z];
                    if (!isValid(xx, yy))
                        continue;
                    if (table[xx][yy] != 'E')
                        continue;
                    if (table[x][y] != 'w' && table[x][y] != 'W')
                        continue;
                    playerTurnCanMove = true;
                    playerTurnCanMoveEndCord = coordinates[xx][yy];
                    playerTurnCanMoveCord = i;
                    break;
                }
            }
        }
    }


    @Override
    public int compareTo(GameState o) {
        return Long.compare(score, o.score);
    }

    public States getState() {

        if (blacks.size() + blackKings.size() == 0)
            return States.Lose;
        if (whites.size() + whiteKings.size() == 0)
            return States.Win;
        if (!playerTurnCanMove) {
            if (turn == Turn.Black)
                return States.Lose;
            else
                return States.Win;
        }
        return States.Pending;
    }

    public void print() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(table[i][j]);
            }
            System.out.println();
        }
    }

    public char[][] cloneIt(char[][] toBeCopied) {
        char [][] res = new char[toBeCopied.length][toBeCopied[0].length];
        for (int i = 0; i < toBeCopied.length; i++)
            System.arraycopy(toBeCopied[i], 0, res[i], 0, toBeCopied[i].length);
        return res;
    }
}
