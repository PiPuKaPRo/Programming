package com.company;

public enum Masti {
    HEARTS("♥"),
    DIAMONDS("♦"),
    CLUBS("♣"),
    SPADES("♠");

    private final String mast;

    Masti(String mast) {
        this.mast = mast;
    }

    @Override
    public String toString() {
        return "" + mast;
    }
}


// https://refactoring.guru/ru/design-patterns/strategy