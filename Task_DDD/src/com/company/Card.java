package com.company;

public record Card(Mast suit, CardsNumberAndPower rank) implements Comparable<Card> {

    public CardsNumberAndPower getRank() {
        return rank;
    }

    public Mast getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return "" + suit + rank;
    }

    @Override
    public int compareTo(Card second) {
        return this.getRank().getPower() - second.getRank().getPower();
    }
}
