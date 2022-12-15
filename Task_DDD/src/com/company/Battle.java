package com.company;

public record Battle(Card attackCard, Card defendCard) {
    public Card getAttackCard() {
        return attackCard;
    }

    public Card getDefendCard() {
        return defendCard;
    }


    public boolean isCovered() { // если карта отбита
        return defendCard != null;
    }
}
