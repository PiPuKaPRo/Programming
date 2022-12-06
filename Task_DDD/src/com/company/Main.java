package com.company;

import com.company.JSwing.GamePanel;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Choose type of the game: 1-Console; 2-WindowGame: ");
        int gameName = Console.input();
        if (gameName == 1){
        Table t = new Table();
            Logic logic = new Logic();
            Game g = new Game();
            Deck dk = new Deck();
            dk.addCardsInGame(t);
            System.out.println("Enter the number of players: ");
            int players = Console.input();
            g.addPlayersInGame(t, players);
            logic.play(t);
        } else if (gameName == 2){
            GamePanel gm = new GamePanel();
            gm.show();
        }
    }
}
