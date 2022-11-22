package com.company;

public enum CardsNumberAndPower {
        ACE(14, "A"),
        KING(13, "K"),
        QUEEN(12, "Q"),
        JACK(11, "J"),
        TEN(10, "10"),
        NINE(9, "9"),
        EIGHT(8, "8"),
        SEVEN(7, "7"),
        SIX(6,"6");

         private final int power;
         private final String name;

    CardsNumberAndPower(int pow, String name) {
        this.power = pow;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getPower() {
        return power;
    }
}
