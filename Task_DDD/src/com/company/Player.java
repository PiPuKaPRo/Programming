package com.company;

import java.util.List;
import java.util.Map;

public class Player {
    private List<Card> playersCards;
    private String number;

    public Player(String number) {
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

    public String getNumber() {
        return number;
    }
}
