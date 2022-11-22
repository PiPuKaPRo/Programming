package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Table t = new Table();
        Logic logic = new Logic();
        logic.addCardsInGame(t);
        System.out.println("Enter the number of players: ");
        int players = Console.input();
        logic.addPlayersInGame(t, players);
        logic.play(t);
    }
}
