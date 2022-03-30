package com.jvoperand.uno;

import com.jvoperand.uno.components.Card;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class ScreenManager {
    private static final String[] screensTitle = {
	    "Информация",
	    "Настройки игры",
	    "Игра"
	};
    public static short screen = 0;
    public static int gameSeed = 0;
    public static Scanner scanner = new Scanner(System.in);
    
    public static final short terminalRows = 60;
    public static final short terminalColumns = 30;
    public static final short cardLength = 10;
    public static final short playerLength = 10;
    public static final String spaceCardPlayer = " ".repeat(21 - cardLength);
    public static final String spaceVoidPlayer = spaceCardPlayer + " ".repeat(cardLength);
    
    public static final String colorOptionFirst = "\033[37;48;5;238m";
    public static final String colorOptionSecond = "\033[37;48;5;242m";
    public static final String colorOptionFirstDark = "\033[37;48;5;236m";
    public static final String colorOptionSecondDark = "\033[37;48;5;240m";
    public static final String colorTheme = "\033[37;48;5;25m";
    public static final String colorThemeDark = "\033[37;48;5;19m";
    public static final String colorClear = "\033[0m";
    public static final String colorBold = "\033[1m";
    public static final String colorBoldOff = "\033[22m";
    public static final String[] colormap = {
        "\033[37;48;5;160m", // red
        "\033[37;48;5;136m", // yellow
        "\033[37;48;5;19m", // blue
        "\033[37;48;5;28m", // green
        
        "\033[37;48;5;93m", // portal red
        "\033[37;48;5;95m", // portal yellow
        "\033[37;48;5;66m", // portal blue
        "\033[37;48;5;94m", // portal green
        
        "\033[40;37m", // black
        "\033[47;30m" // white
    };
    public static final short colormapPortalOffset = 4;
    public static final short colormapBlackIndex = 8;
    public static final String[] colormapRu = {
        "Красный",
        "Жёлтый",
        "Синий",
        "Зелёный",
        "Чёрный"
    };
    
    private String makeScreenTitle() {
        if (gameSeed != 0)
            return colorTheme + makeToCenter(terminalRows, screensTitle[screen] + ' ' + Integer.toString(gameSeed)) + colorClear + "\n";
        else
            return colorTheme + makeToCenter(terminalRows, screensTitle[screen]) + colorClear + "\n";
    }
    
    private String makeToCenter(short width, String text) {
        char[] charText = text.toCharArray();
        int textLength = charText.length;
        char[] resText = new char[width];
        int textStarts = (int) (width / 2 - textLength / 2);
        int textEnds = (int) (width - textStarts);
        for (short n = 0; n < width; n++) {
            if (n < textStarts || n >= textEnds)
                resText[n] = ' ';
            else
                resText[n] = charText[n - textStarts];
        }
        return String.valueOf(resText);
    }
    
    public String makeCard(short index, short color, String number, boolean isPortal) {
        short colorOffset = 0;
        if (isPortal)
            colorOffset += colormapPortalOffset;
        String resOut = String.format("%s. %s (%s)", colormap[color + colorOffset] + colorBold + String.valueOf(index + 1) + colorBoldOff, number, colormapRu[color]);
        resOut += " ".repeat(cardLength - (resOut.length() - 100)) + colorClear;
        return resOut;
    }
    
    public String makePlayer(short index, String name, short cardsCount) {
        if (index % 2 == 1)
            return String.format("%s - %s", colorOptionFirst + name, String.valueOf(cardsCount) + colorClear);
        else
            return String.format("%s - %s", colorOptionSecond + name, String.valueOf(cardsCount) + colorClear);
    }
    
    public String makeColorChoice(boolean isPortal, boolean blackAllowed) {
        short colorsLength = colormapPortalOffset;
        if (blackAllowed)
            colorsLength++;
        String[] resColors = new String[colorsLength];
        for (short n = 0; n < colorsLength; n++) {
            short colorn = 0;
            if (isPortal)
                colorn += colormapPortalOffset;
            if (blackAllowed && n == colorsLength - 1)
                colorn = colormapBlackIndex;
            resColors[n] = colormap[colorn] + Integer.toString(n) + ". " + colormapRu[n];
        }
        return String.join(" ", resColors);
    }
    
    public void clear(String text) {
        System.out.print("\033[H\033[2J" + makeScreenTitle() + text);
        System.out.flush();
    }
    
    public void update(List<Card> cards, String[] players, short[] playersCardsCount, boolean isPortal) {
        String resOut = "  Карты                Игроки\n";
        short cardsSize = (short) cards.size();
        short playersSize = (short) players.length;
        short maxSize;
        if (cardsSize < playersSize)
            maxSize = playersSize;
        else
            maxSize = cardsSize;
        Iterator<Card> iter = cards.iterator();
        for (short n = 0; n < maxSize; n++)
            if (cardsSize > n && playersSize > n) {
                Card card = iter.next();
                resOut += makeCard(n, card.color, card.numberNormal, isPortal) + spaceCardPlayer + makePlayer(n, players[n], playersCardsCount[n]) + "\n";
            } else if (cardsSize > n) {
                Card card = iter.next();
                resOut += makeCard(n, card.color, card.numberNormal, isPortal) + "\n";
            } else
                resOut += spaceVoidPlayer + makePlayer(n, players[n], playersCardsCount[n]) + "\n";
        clear(resOut);
    }
}