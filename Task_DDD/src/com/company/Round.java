package com.company;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private Player source; // кто ходит
    private Player target; // кто отбивается
    private List<Battle> fights = new ArrayList(); // борьба
    private boolean isPickedUp = false;  // бито или стянул
    private List<CardsNumberAndPower> allRanksInRound = new ArrayList<>(); //номиналы всех карт, используемых в раунде

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


    public List<Battle> getFights() {
        return fights;
    }

    // добавляем все бои за 1 раунд
    public void setFights(List<Battle> fights) {
        this.fights = fights;
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
            if (r.equals(rank)) {               //если rank совпадает с r, то такой номинал есть
                return true;                    //иначе такого номинала нет
            }
        }
        return false;
    }
}

