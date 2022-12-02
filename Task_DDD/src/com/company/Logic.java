package com.company;

import java.util.*;

import static com.company.Round.playingRound;
import static com.company.Table.getNextPlayingPlayer;

public class Logic {

    public void play(Table t) {
        Player.dialCards(t);  //раздаем карты
        System.out.println((char) 27 + "[46m" + t.getTrumpCard() + (char) 27 + "[0m");
        System.out.println(Console.playerCardsToString(t));
        System.out.println(t.getCards().size());

        Player source = Game.getPlayerWhoMovedFirst(t);  // кто ходит первым
        System.out.print("---1 Round---\nPlaying " + source.toString());
        Player target = getNextPlayingPlayer(t, source);  // кто отбивает
        System.out.println(" and " + target.toString());

        Round firstRound = new Round(source, target);  // создаем 1ый раунд
        source = playingRound(t, firstRound, true);  // играем 1 раунд и возвращаем игрока, который ходит следующим
        target = getNextPlayingPlayer(t, source);   // находим игрока, который будет отбиваться следующим
        int i = 2;
        while (Game.isGameActive(t)) {
            System.out.println("---| " + i + " Round |---\nPlaying " + source.toString() + " and " + target.toString());
            Round round = new Round(source, target);   // создаем новый раунд
            source = playingRound(t, round, false);
            t.addRound(round);  // сохраняем инфрмацию о раунде
            // играем раунд и возвращаем игрока, который ходит следующим
            if (source == null) {
                break;
            }
            target = getNextPlayingPlayer(t, source);   // находим игрока, который будет отбиваться следующим
            if (target == null) {
                break;
            }
            i++;
        }
        Player stupid = Table.getLastPlayer(t);
        if (stupid == null) {
            System.out.println("draw");
        } else {
            System.out.println("----| " + stupid + " lose |----");
        }
        System.out.println((char) 27 + "[31m|||---||| Game  end |||---|||" + (char) 27 + "[0m");
    }

    public static Card attackersMove(boolean isFirstBattle, Round round, Player source, Table t) {
        StringBuilder sb = new StringBuilder();

        List<Card> cards = source.getPlayersCards();
        if (!isFirstBattle) sb.append("-1 - bito ");
        sb.append(Console.getPlayerCardsForBattle(t, source));
        System.out.println(sb);

        if (isFirstBattle) {
            System.out.println("Choose card:");
        } else {
            System.out.println("Choose card or say bito: ");
        }

        Card cardForMove = null;
        int action;

        while (true) {
            action = Console.input();
            if (action == -1) {
                if (isFirstBattle) {
                    System.out.println("You cannot say bito. Choose card: ");
                } else {
                    break;
                }
            } else if (round.nominalInRound(cards.get(action).getRank()) || isFirstBattle) {
                cardForMove = cards.get(action);
                break;
            } else {
                System.out.println("You cannot move this card. Choose another card:");
            }
        }

        if (cardForMove != null) {
            round.addRank(cardForMove.getRank());
            cards.remove(action);
        }

        return cardForMove;
    }

    public static Card defendersMove(Table t, Player player, Round round, Card down) {
        System.out.println(Console.getPlayerCardsForBattle(t, player));
        List<Card> cards = player.getPlayersCards();
        int numberCard;
        Card cardForMove = null;


            System.out.println("Choose card or say take: ");

        while (true) {
            numberCard = Console.input();

            if (numberCard == -1) break;

            if (cards.get(numberCard).compareTo(down) > 0) {
                if (cards.get(numberCard).getSuit() == down.getSuit()) {
                    cardForMove = cards.get(numberCard);
                    break;
                }
            }

            if (down.getSuit() != t.getTrumpCard().getSuit()) {
                if (cards.get(numberCard).getSuit() == t.getTrumpCard().getSuit()) {
                    cardForMove = cards.get(numberCard);
                    break;
                }
            }
        }

        if (cardForMove != null) {
            round.addRank(cardForMove.getRank());
            cards.remove(numberCard);
        }

        return cardForMove;
    }

}