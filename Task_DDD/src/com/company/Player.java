package com.company;

import java.util.*;

public class Player {
    private List<Card> playersCards;
    private int number;

    public Player(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Player");
        sb.append(number);
        return sb.toString();
    }

    public List<Card> getPlayersCards() {
        return playersCards;
    }

    public void setPlayersCards(List<Card> playersCards) {
        this.playersCards = playersCards;
    }

    public int getNumber() {
        return number;
    }

    public static void dialCards(Table t) {   // раздача карт
        Map<Player, List<Card>> playersCards = new HashMap<>(); // лист с картами всех игорков

        for (Player p : t.getPlayers()) {
            List<Card> pc = new ArrayList<>();  // карты p-го игрока
            for (int i = 0; i < 6; i++) {
                pc.add(t.getCards().pop());
            }
            Collections.sort(pc);

            pc = addTrumpsToEnd(pc, t.getTrumpCard().getSuit());

            playersCards.put(p, pc);
            p.setPlayersCards(pc);// его карты сохранятся в лист с картами всех игроков
        }
    }

    public static List<Card> addTrumpsToEnd(List<Card> cards, Mast trump) { //перестевляем козыри в конец
        Card lastCard = cards.get(cards.size() - 1);
        int i = 0;
        while (cards.get(i) != lastCard) {
            if (cards.get(i).getSuit() == trump) {
                Card c = cards.remove(i);
                cards.add(c);
            } else {
                i++;
            }
        }
        if (cards.get(i).getSuit() == trump) {
            cards.add(cards.remove(i));
        }
        return cards;
    }

    public static boolean isActive(Table t, Player p) {
        return !p.getPlayersCards().isEmpty() || !Deck.isDeckEmpty(t);
    }
}
