package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Deck {
    public void addCardsInGame(Table g) {
        ArrayList<Card> allCards = new ArrayList<>();
        Stack<Card> cards = new Stack<>();
        for (CardsNumberAndPower rank : CardsNumberAndPower.values()) {
            for (Mast suit : Mast.values()) {
                allCards.add(new Card(suit, rank));
                Collections.shuffle(allCards);
            }
        }
        Card trump = allCards.remove(35);
        g.setTrumpCard(trump);
        cards.push(trump);
        for (Card card : allCards) {
            cards.push(card);
        }
        g.setCards(cards);
    } // в класс колоды

    public static boolean isDeckEmpty(Table t) {
        return t.getCards().isEmpty();
    }

    public static void addCardToFull(Table t, Player player) {
        if (player.getPlayersCards().size() > 6) return;

        List<Card> cards = player.getPlayersCards();
        Stack<Card> deck = t.getCards();
        int n = 6 - cards.size();
        for (int i = 0; i < n; i++) {
            if (deck.size() != 0) {
                addCardToPlayersList(t, cards, deck.pop());
            } else {
                break;
            }
        }
        player.setPlayersCards(cards);
    }

    public static void addCardToPlayer(Table t, Player player, Card card) {  //добавление карты игроку
        List<Card> cards = player.getPlayersCards();
        addCardToPlayersList(t, cards, card);
        player.setPlayersCards(cards);
    } // в класс колоды

    public static void addCardToPlayersList(Table t, List<Card> cards, Card card) {
        Mast trump = t.getTrumpCard().getSuit();

        int i = 0;
        if (card.getSuit() != trump) {
            while (i < cards.size() && cards.get(i).getSuit() != trump) {
                if (card.compareTo(cards.get(i)) <= 0) {
                    break;
                }
                i++;
            }
        } else {
            while (i < cards.size() && cards.get(i).getSuit() != trump) {
                i++;
            }
            while (i < cards.size()) {
                if (card.compareTo(cards.get(i)) <= 0) {
                    break;
                }
                i++;
            }
        }
        cards.add(i, card);
    }
}
