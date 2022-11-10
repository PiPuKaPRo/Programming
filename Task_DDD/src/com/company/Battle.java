package com.company;

public record Battle(Card down, Card up) {
    public Card getDown() {
        return down;
    }

    public Card getUp() {
        return up;
    }

    public boolean isCovered() { // если карта отбита
        return up != null;
    }
}
