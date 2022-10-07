package com.company;

public enum CardsNumberAndPower {
        ACE(14, "A"),
        KING(13, "13"),
        QUEEN(12, "Q"),
        JACK(11, "J"),
        TEN(10, "10"),
        NINE(9, "9"),
        EIGHT(8, "8"),
        SEVEN(7, "7"),
        SIX(6,"6");

         private final int pow;
         private final String name;

    CardsNumberAndPower(int pow, String name) {
        this.pow = pow;
        this.name = name;
    }

    @Override
    public String toString() {
        return "CardsNumberAndPower{" +
                "pow=" + pow +
                ", name='" + name + '\'' +
                '}';
    }

    public int getPow() {
        return pow;
    }

    public String getName() {
        return name;
    }
}
