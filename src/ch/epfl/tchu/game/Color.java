package ch.epfl.tchu.game;

import java.util.List;

/**
 * List of all possible colors for each card type (NULL being for
 * locomotives)
 *
 * @author Shin Urech (327245).
 */

public enum Color {

    BLACK,
    VIOLET,
    BLUE,
    GREEN,
    YELLOW,
    ORANGE,
    RED,
    WHITE;

    /**
     * List of all possible colors.
     */
    public static final List<Color> ALL = List.of(Color.values());

    /**
     * The amount of available colors.
     */
    public static final int COUNT = ALL.size();

    /**
     * Default constructor for Color class.
     */
    Color(){}

    }

