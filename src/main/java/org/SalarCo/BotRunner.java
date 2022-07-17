package org.SalarCo;

import javafx.application.Platform;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BotRunner {

    ProcessBuilder processBuilder = new ProcessBuilder();
   GameState getAnsFromBot() {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileInputStream(MainApp.outFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        char[][] ch = new char[8][8];
        String []s = new String[8];
        for (int i = 0; i < 8; i++)
            s[i] = scanner.nextLine();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                ch[i][j] = s[i].charAt(j);
        scanner.close();
        return new GameState(ch, Turn.White, 0);
    }
    void giveInputToBot(GameState gameState) {
        try {
            PrintStream printStream = new PrintStream(new FileOutputStream(MainApp.inputFile));
            printStream.println(1);
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    printStream.print(gameState.table[i][j]);
                }
                printStream.println();
            }
            printStream.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ArrayList<String> args = new ArrayList<>(List.of(MainApp.sysArgs.split("\n")));
        args.add(MainApp.jarFile.getAbsolutePath());
        args.add(MainApp.className);
        System.out.println(args);
        try {
            Process process = processBuilder.command(args).inheritIO().start();
            System.out.println(process.isAlive());
            new Thread(()->{
                try {
                    process.waitFor();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(()->{
                    MainApp.checkers.gameState = getAnsFromBot();
                    MainApp.checkers.reloadTable();
                });
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
