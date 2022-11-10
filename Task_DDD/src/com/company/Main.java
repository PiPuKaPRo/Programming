package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Table t = new Table();
        Logic s = new Logic();
        s.addCardsInGame(t);//добавление колоды
        System.out.println("Enter the number of players: ");
        int players = sc.nextInt();
        s.addPlayersInGame(t, players);    //добавление игроков
        s.play(t);
    }
}
