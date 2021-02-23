package ch.epl.tchu.game;

import java.util.List;

public enum Color {

    BLACK,
    VIOLET,
    BLUE,
    GREEN,
    YELLOW,
    ORANGE,
    RED,
    WHITE,
    NULL;

    //list of all possible colors
    public static final List<Color> ALL = List.of(Color.values());
    //amount of available colors
    public static final int COUNT = ALL.size();

    }

