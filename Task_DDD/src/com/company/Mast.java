package com.company;

public enum Mast {
    HEARTS("H"),
    DIAMONDS("D"),
    CLUBS("C"),
    SPADES("S");

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