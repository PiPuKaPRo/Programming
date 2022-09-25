package com.company;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Card {

    public static final String[] ranks = {
            "6",
            "7",
            "8",
            "9",
            "10",
            "Валет",
            "Королева",
            "Король",
            "Туз"
    };


    // Rank values
    public static final Map<String, Integer> values;
    static {
        Map<String, Integer> m = new HashMap<String, Integer>();
        m.put("6", 6);
        m.put("7", 7);
        m.put("8", 8);
        m.put("9", 9);
        m.put("10", 10);
        m.put("Валет", 11);
        m.put("Королева", 12);
        m.put("Король", 13);
        m.put("Туз", 14);
        values = Collections.unmodifiableMap(m);
    }

    // Suit constants:
    public static final String[] suits = {
            "Пики",
            "Черви",
            "Крести",
            "Бубны"
    };
}
