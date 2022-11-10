package com.company;

import java.util.List;
import java.util.Scanner;

public class Console {

    private int cardNumberInHand;
    private static Scanner s = new Scanner(System.in);

    public Console() {
        this.cardNumberInHand = input();
    }

    public static int input() {
        return s.nextInt();
    }

    public static String playerCardsToString(Table g) {
        StringBuilder playerCards = new StringBuilder("\n");
        for (int i = 0; i < g.getPlayers().size(); i++) {
            playerCards.append(getPlayerCardsForBattle(g, g.getPlayers().get(i)));
        }
        return playerCards.toString();
    }

    public static String getPlayerCardsForBattle(Table t, Player player) {
        List<Card> cards = player.getPlayersCards();
        StringBuilder playerCards = new StringBuilder("\n");
        Masti trump = t.getTrumpCard().getSuit();
        if (cards.size() == 0) {
            playerCards.append("none");
        } else {
            playerCards.append(player).append(" = [ ");
            for (int cardNum = 0; cardNum < cards.size() - 1; cardNum++) {
                playerCards.append(cardNum + " = ");
                if (cards.get(cardNum).getSuit() == trump) {
                    playerCards.append((char) 27 + "[46m").append(cards.get(cardNum)).append((char) 27).append("[0m ");
                } else {
                    playerCards.append(cards.get(cardNum)).append(" ");
                }
            }
            Card lastCard = cards.get(cards.size() - 1);
            playerCards.append(cards.size() - 1 + " = ");
            if (lastCard.getSuit() == trump) {
                playerCards.append((char) 27 + "[46m").append(lastCard).append((char) 27).append("[0m");
            } else {
                playerCards.append(lastCard);
            }
            playerCards.append(" ]");
        }
        return playerCards.toString();
    }
}
