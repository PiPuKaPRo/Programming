package com.company;

import java.util.*;

import static com.company.Deck.addCardToFull;
import static com.company.Player.isActive;

public class Table {
    private Card trumpCard;
    private List<Player> players;
    private Stack<Card> cards;
    private List<Round> rounds;

    public List<Round> getRounds() {
        return rounds;
    }

    public Stack<Card> getCards() {
        return cards;
    }

    public void setCards(Stack<Card> cards) {
        this.cards = cards;
    }

    public String[][] getStringPlayers(List<Player> lst){
        String[][] array = new String[lst.size()][1];
        int index = 0;
        for (Player value : lst) {
            array[index][0] = value.toString();
            index++;
        }
        return array;
    }

    public List<Player> getPlayers() {
        return players;
    }
    public String getPlayersSize() {
        return String.valueOf(getPlayers().size());
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Card getTrumpCard() {
        return trumpCard;
    }

    public void setTrumpCard(Card trumpCard) {
        this.trumpCard = trumpCard;
    }

    public void addRound(Round round) {
        rounds.add(round);
    }

    public Table() {
        rounds = new ArrayList<>();
    }

    public static Player getNextPlayingPlayer(Table t, Player playerSource) { // возвращает следующего по очереди игрока, который идет после сурса
        int playerSourceNumber = Integer.valueOf(playerSource.getNumber());
        List<Player> players = t.getPlayers();
        Player nextPlayer;
        if (playerSourceNumber == players.size()) {
            nextPlayer = searchNextPlayer(t, players, 0, players.size() - 1);
        }  else {
            nextPlayer = searchNextPlayer(t, players, playerSourceNumber, players.size());
        }
        if (nextPlayer == null) {
            nextPlayer = searchNextPlayer(t, players, 0, playerSourceNumber - 1);
        }
        return nextPlayer;
    } // в класс тейбл

    private static Player searchNextPlayer(Table t, List<Player> players, int start, int finish) {
        Player nextPlayer = null;
        for (int i = start; i < finish; i++) {
            if (isActive(t, players.get(i))) {
                nextPlayer = players.get(i);
                break;
            }
        }
        return nextPlayer;
    } // в класс тейбл

    public static Player getLastPlayer(Table t) {
        for (int i = 0; i < t.getPlayers().size(); i++) {
            Player lastPlayer = t.getPlayers().get(i);
            if (isActive(t, lastPlayer)) return lastPlayer;
        }
        return null;
    }

    public static Player getNextPlayer(Table t, Round r, Battle b) {
        if (!b.isCovered()) {
            System.out.println(r.getTarget().toString() + " takes");
            r.setPickedUp(false);

            Round.addRoundCardsToPlayer(t, r);

            addCardToFull(t, r.getSource());

            if (Game.isGameActive(t)) return getNextPlayingPlayer(t, r.getTarget());

            System.out.println(Console.playerCardsToString(t));
            return null;

        } else {

            System.out.println(b.getDefendCard().toString());

            r.setPickedUp(true);


            addCardToFull(t, r.getSource());
            addCardToFull(t, r.getTarget());

            if (isActive(t, r.getTarget())) {
                return r.getTarget();
            } else {
                return getNextPlayingPlayer(t, r.getTarget());
            }
        }
    } // в класс тейбл
}
