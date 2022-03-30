package com.jvoperand.uno;

import com.jvoperand.uno.ScreenManager;
import com.jvoperand.uno.components.Card;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Core {
	private static ScreenManager manager = new ScreenManager();
	private static Random random;
    
	private static List<Card> cards = new ArrayList<Card>();
	private static boolean isPortal = false;
	
	private static final short playerStartCards = 7;
	private static final String[] players = {"Jwego", "Suno"};
	private static final short[] playersCardsCount = new short[players.length];
	private static final String[] cardNumbers = {
	    "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
	    "+2", "+4", "Цвет", "Портал"
	};
	private static final short actionCardsOffset = 10;
	
	private static final String[] validFalse = {
	    "нет",
	    "н",
	    "no",
	    "n",
	    "-",
	    "0"
	};
	private static final String[] validTrue = {
	    "да",
	    "д",
	    "yes",
	    "y",
	    "+",
	    "1"
	};
	
	private short settingNameLength = 13;
	private static final String[] gameSettingsRu = {
	    "Семя",
	    "Макс. ходов",
	    "Чёрные карты"
	};
	private static final String[] gameSettingsDefaultv = {
	    "0",
	    "0",
	    "Да"
	};
	private static final short[] gameSettingsTypes = {
	    0,
	    0,
	    1
	};
    
	public void start() {
		String infoText = "    javaUNO – Игра в консоли, написанная на Java. С помощью ANSI-кодов каждому объекту давался свой цвет и в целом, используя некоторые коды, сам процесс игры становился более приятным.\n\n    В дополнение к стандартной игре UNO были добавлены новые карты и правила. К примеру: карта-портал, которая меняет первую колоду на вторую (и обратно) у каждого игрока.\n\nЧтобы создать новую игру, введите Enter.";
		manager.clear(infoText);
		manager.scanner.nextLine();
	    gameSettingsInit();
	}
	
	private void gameSettingsInit() {
	    manager.screen = 1;
	    String[] resopts = gameSettingsDefaultv;
	    boolean[] resoptsSets = new boolean[resopts.length];
	    boolean errorSetopt = false;
        while (true) {
            String resOut = "";
            for (short n = 0; n < gameSettingsRu.length; n++)
	            resOut += makeSetting((short) (n + 1), gameSettingsRu[n], resopts[n]);
	        resOut += manager.colorClear;
	        manager.clear(resOut);
	        if (errorSetopt) {
	            System.out.println("Вы ввели неверный индекс.");
	            errorSetopt = false;
	        }
            System.out.print("\nЧтобы продолжить, введите Enter.\nЧтобы изменить значение, введите его индекс: ");
            String input = manager.scanner.nextLine();
            try {
                boolean breaks = false;
                while (!breaks) {
                    breaks = true;
	                int optindex = Integer.parseInt(input) - 1;
                    System.out.print(String.format("Введите значение для \"%s\": ", gameSettingsRu[optindex]));
                    String optvalue = manager.scanner.nextLine();
                    switch (gameSettingsTypes[optindex]) {
                        case (0):
                            try {
                                resopts[optindex] = String.valueOf(Integer.parseInt(optvalue));
                            } catch (NumberFormatException e) {
                                System.out.println("Вы ввели неверные данные для числа.");
                                breaks = false;
                            }
                            resoptsSets[optindex] = true;
                            break;
                        case (1):
                            optvalue = optvalue.trim().toLowerCase();
                            boolean changed = false;
                            for (short n = 0; n < validFalse.length; n++)
                                if (optvalue.equals(validFalse[n])) {
                                    resopts[optindex] = "Нет";
                                    resoptsSets[optindex] = true;
                                    changed = true;
                                    break;
                                }
                            if (!changed)
                                for (short n = 0; n < validTrue.length; n++)
                                    if (optvalue.equals(validTrue[n])) {
                                        resopts[optindex] = "Да";
                                        resoptsSets[optindex] = true;
                                        changed = true;
                                        break;
                                    }
                            if (!changed) {
                                System.out.println("Вы ввели неверные данные для Boolean.");
                                breaks = false;
                            }
                            break;
                        case (2):
                            resopts[optindex] = optvalue;
                            resoptsSets[optindex] = true;
                            break;
                    }
                }
            } catch (NumberFormatException e) {
                manager.screen = 2;
                break;
            } catch (ArrayIndexOutOfBoundsException e) {
                errorSetopt = true;
            }
        }
        gameInit(resopts, resoptsSets);
	}
	
	private String makeSetting(short index, String name, String defaultv) {
	    if (index % 2 == 1)
	        return String.format("%s. %s %s", manager.colorOptionSecond + manager.colorBold + Integer.toString(index) + manager.colorBoldOff, name + " ".repeat(settingNameLength - name.length()) + manager.colorOptionSecondDark, defaultv + "\n");
        else
            return String.format("%s. %s %s", manager.colorOptionFirst + manager.colorBold + Integer.toString(index) + manager.colorBoldOff, name + " ".repeat(settingNameLength - name.length()) + manager.colorOptionFirstDark, defaultv + "\n");
	}
	
	private void gameInit(String[] resopts, boolean[] resoptsSets) {
	    manager.screen = 2;
	    if (resoptsSets[0] == true)
	        manager.gameSeed = Integer.parseInt(resopts[0]);
	    else
            manager.gameSeed = (int) System.currentTimeMillis();
        random = new Random(manager.gameSeed);
        for (short n = 0; n < playerStartCards; n++)
            cards.add(randCard());
        manager.update(cards, players, playersCardsCount, isPortal);
	}
	
	private Card randCard() {
	    int number = random.nextInt(cardNumbers.length);
	    return Card.generate((short) random.nextInt(manager.colormapPortalOffset), number, cardNumbers[number]);
	}
}