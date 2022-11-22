package com.company;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private Player source;
    private Player target;
    private List<Battle> battles = new ArrayList();
    private boolean isPickedUp = false;
    private List<CardsNumberAndPower> allRanksInRound = new ArrayList<>();

    public Round(Player source, Player target) {
        this.source = source;
        this.target = target;
    }

    public Player getSource() {
        return source;
    }

    public Player getTarget() {
        return target;
    }


    public List<Battle> getBattles() {
        return battles;
    }

    // добавляем все бои за 1 раунд
    public void setBattles(List<Battle> battles) {
        this.battles = battles;
    }

    public void setPickedUp(boolean pickedUp) {
        isPickedUp = pickedUp;
    }

    public void addRank(CardsNumberAndPower rank) {    //добавляем номинал карты
        if (!this.nominalInRound(rank)) {
            allRanksInRound.add(rank);      //если нет, то добавляем
        }
    }

    public boolean nominalInRound(CardsNumberAndPower rank) {  //проверяем наличие номинала rank в раунде
        for (CardsNumberAndPower r: allRanksInRound) {         //цикл по всем сохранённым номиналам
            if (r.equals(rank)) return true;             //если rank совпадает с r, то такой номинал есть
        }
        return false;
    }
}

