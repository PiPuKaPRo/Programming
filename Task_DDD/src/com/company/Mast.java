package com.company;

public enum Mast {
    HEARTS("♥"),
    DIAMONDS("♦"),
    CLUBS("♣"),
    SPADES("♠");

    private final String mast;

    Mast(String mast) {
        this.mast = mast;
    }

    @Override
    public String toString() {
        return "" + mast;
    }
}


// https://refactoring.guru/ru/design-patterns/strategy