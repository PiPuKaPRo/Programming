package com.company;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private boolean isWindow;

    public void setWindow(boolean window) {
        isWindow = window;
    }

    public boolean getIsWindow() {
        return isWindow;
    }

    public void addPlayersInGame(Table g, int playersCount) {
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= playersCount; i++) {
            players.add(new Player(i));
        }
        g.setPlayers(players);
    }

    public static Player getPlayerWhoMovedFirst(Table t) { // кто ходит первым
        Card currentTrump = t.getTrumpCard(); // текущий козырь
        Player playerWhoMovedFirst = null;

        if (currentTrump.getRank().getPower() > 10) {
            playerWhoMovedFirst = searchMinTrumpInGame(t, t.getTrumpCard().getSuit());
            System.out.println("The youngest trump card is " + playerWhoMovedFirst.toString() + ", so he goes first");
        } else {
            playerWhoMovedFirst = searchMaxTrumpInGame(t, t.getTrumpCard().getSuit());
            System.out.println("The most senior trump card is " + playerWhoMovedFirst.toString() + ", so he goes first");
        }
        if (playerWhoMovedFirst == null) {
            playerWhoMovedFirst = searchMaxCardInGame(t);
            System.out.println("The players have no trumps in their hands, the highest card is" + playerWhoMovedFirst);
        }
        return playerWhoMovedFirst;
    }

    private static Player searchMaxTrumpInGame(Table t, Mast currentTrump) {
        Player playerWithMaxTrump = null;  // номер игрока с самым большим козырем
        Card maxTrumpInGame = null;   // самый старший козырь из карт в игре
        for (int i = 0; i < t.getPlayers().size(); i++) {
            Player iPlayer = t.getPlayers().get(i);  // i-ый игрок
            List<Card> cardsIPlayer = iPlayer.getPlayersCards();  // карты i-го игроко
            Card maxTrumpInPlayer = null;   // наибольший козырь у игрока
            for (int j = cardsIPlayer.size() - 1; j > -1; j--) {
                if (cardsIPlayer.get(i).getSuit() == currentTrump) {
                    if (maxTrumpInPlayer == null) {
                        maxTrumpInPlayer = cardsIPlayer.get(i);
                        break;
                    }
                }
            }
            for (Card card : cardsIPlayer) {
                if (card.getSuit() == currentTrump) {
                    if (maxTrumpInPlayer == null) {
                        maxTrumpInPlayer = card;
                        break;
                    }
                }
            }
            if (maxTrumpInPlayer != null) {
                if (maxTrumpInGame == null) {
                    maxTrumpInGame = maxTrumpInPlayer;
                    playerWithMaxTrump = iPlayer;
                } else {
                    if (maxTrumpInGame.compareTo(maxTrumpInPlayer) < 0) {   //если меньше 0, то заменяем
                        maxTrumpInGame = maxTrumpInPlayer;
                        playerWithMaxTrump = iPlayer;
                    }
                }
            }
        }

        //если ни у кого нет козыря
        return playerWithMaxTrump;
    }

    private static Player searchMinTrumpInGame(Table t, Mast currentTrump) { // search - поиск
        Player playerWithMinTrump = null;   // игрок с самым маленьким козырем
        Card minTrumpInGame = null;   // самый младший козырь из карт в игре
        for (int i = 0; i < t.getPlayers().size(); i++) {
            Player iPlayer = t.getPlayers().get(i);   // i-ый игрок
            List<Card> cardsIPlayer = iPlayer.getPlayersCards();  // карты i-го игрока
            Card minTrumpInPlayer = null;  // наименьший козырь у игрока
            for (Card card : cardsIPlayer) {
                if (card.getSuit() == currentTrump) {
                    if (minTrumpInPlayer == null) {
                        minTrumpInPlayer = card;
                        break;
                    }
                }
            }
            if (minTrumpInPlayer != null) {
                if (minTrumpInGame == null) {
                    minTrumpInGame = minTrumpInPlayer;
                    playerWithMinTrump = iPlayer;
                } else {
                    if (minTrumpInGame.compareTo(minTrumpInPlayer) > 0) { //если больше 0, то заменяем
                        minTrumpInGame = minTrumpInPlayer;
                        playerWithMinTrump = iPlayer;
                    }
                }
            }
        }
        return playerWithMinTrump;
    }

    private static Player searchMaxCardInGame(Table t) {
        List<Player> players = t.getPlayers();
        Player playerWithMaxCard = players.get(0);

        List<Card> cards = playerWithMaxCard.getPlayersCards();
        Card maxCard = cards.get(cards.size() - 1);
        for (int i = 1; i < players.size(); i++) {
            cards = players.get(i).getPlayersCards();
            Card maxPlayerCard = cards.get(cards.size() - 1);
            if (maxCard.compareTo(maxPlayerCard) < 0) {
                maxCard = maxPlayerCard;
                playerWithMaxCard = players.get(i);
            }
        }
        return playerWithMaxCard;
    }

    public static boolean isGameActive(Table t) {
        int count = 0;
        for (Player p : t.getPlayers()) {
            if (Player.isActive(t, p)) {
                count++;

                if (count > 1) return true;
            }
        }
        return false;
    }
}
