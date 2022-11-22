package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Table {
    private Card trumpCard;
    private List<Player> players;
    private Stack<Card> cards;
    private List<Round> rounds;

    public Stack<Card> getCards() {
        return cards;
    }

    public void setCards(Stack<Card> cards) {
        this.cards = cards;
    }


    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Card getTrumpCard() {
        return trumpCard;
    }

    public void setTrumpCard(Card trumpCard) {
        this.trumpCard = trumpCard;
    }

    public void addRound(Round round) {
        rounds.add(round);
    }

    public Table() {
        rounds = new ArrayList<>();
    }
}
