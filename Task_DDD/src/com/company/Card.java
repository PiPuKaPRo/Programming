package com.company;

public record Card (Masti m, CardsNumberAndPower p){
    public  Masti m(){
        return m;
    }

    public CardsNumberAndPower p(){
        return p;
    }

    @Override
    public String toString() {
        return "Card{" +
                "m=" + m +
                ", p=" + p +
                '}';
    }
}
