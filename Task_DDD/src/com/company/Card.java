package com.company;

public record Card(Masti suit, CardsNumberAndPower rank) implements Comparable<Card> {

    public CardsNumberAndPower getRank() {
        return rank;
    }

    public Masti getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return "" + suit + rank;
    }

    @Override
    public int compareTo(Card second) {
        return this.getRank().getPow() - second.getRank().getPow();
    }
}
