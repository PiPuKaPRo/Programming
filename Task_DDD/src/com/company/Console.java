package com.company;

import com.company.Card;
import com.company.Mast;
import com.company.Player;
import com.company.Table;

import java.util.List;
import java.util.Scanner;

public class Console {

    public static int input() {
        Scanner scn = new Scanner(System.in);
        return scn.nextInt();
    }

    public static String playerCardsToString(Table t) {
        StringBuilder playerCards = new StringBuilder("\n");
        for (int i = 0; i < t.getPlayers().size(); i++) {
            playerCards.append(getPlayerCardsForBattle(t, t.getPlayers().get(i)));
        }
        return playerCards.toString();
    }

    public static String getPlayerCardsForBattle(Table t, Player player) {
        List<Card> cards = player.getPlayersCards();
        StringBuilder playerCards = new StringBuilder("\n");
        Mast trump = t.getTrumpCard().getSuit();
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
