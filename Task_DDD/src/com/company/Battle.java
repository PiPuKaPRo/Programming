package com.company;

public class Battle {
    private Card attackCard;
    private Card defendCard;

    public Battle(Card attackCard, Card defendCard) {
        this.attackCard = attackCard;
        this.defendCard = defendCard;
    }

    public Battle() {
    }

    public void setAttackCard(Card attackCard) {
        this.attackCard = attackCard;
    }

    public void setDefendCard(Card defendCard) {
        this.defendCard = defendCard;
    }

    public Card getDefendCard() {
        return defendCard;
    }

    public Card getAttackCard() {
        return attackCard;
    }


    public boolean isCovered() { // если карта отбита
        return defendCard != null;
    }
}
