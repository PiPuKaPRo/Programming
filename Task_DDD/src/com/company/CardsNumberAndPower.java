package com.company;

public enum CardsNumberAndPower {;

    private int pow;
    private String name;

    CardsNumberAndPower(int pow, String name) {
        this.pow = pow;
        this.name = name;
    }

    public int getPow() {
        return pow;
    }

    public String getName() {
        return name;
    }
}
