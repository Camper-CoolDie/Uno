package com.jvoperand.uno.components;

public class Card {
    public static short color;
    public static int number;
    public static String numberNormal;
    
    public static Card generate(short color, int number, String numberNormal) {
        Card c = new Card();
        c.color = color;
        c.number = number;
        c.numberNormal = numberNormal;
        return c;
    }
}