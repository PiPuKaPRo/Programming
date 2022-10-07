package com.company;

import java.util.*;

public class Deck {
    private final Queue<Card> d = new LinkedList<>();

    public void shuffle(){
        Collections.shuffle((List<?>) d);
    }

    public Masti getS(){
        return null;
    }

    public Card getCard() {
        return d.poll();
    }

    public Deck(){
        for(var mast : Masti.values()){
            for (var suit: CardsNumberAndPower.values()) {
                d.add(new Card(mast, suit));
            }
        }
        shuffle();
    }

    @Override
    public String toString() {
        return "Deck{" +
                "d=" + d +
                '}';
    }
}
