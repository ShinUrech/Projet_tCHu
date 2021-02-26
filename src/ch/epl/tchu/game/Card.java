package ch.epl.tchu.game;

import java.util.ArrayList;
import java.util.List;

/**
 * enum to represent all types of cards.
 *
 * @author Shin Urech (327245)
 */

public enum Card {

    BLACK,
    VIOLET,
    BLUE,
    GREEN,
    YELLOW,
    ORANGE,
    RED,
    WHITE,
    LOCOMOTIVE;

    /**
     * list of all the different kinds of cards that are required to play the game except
     * locomotives (CarList() is defined below and it allows us to create a list of all
     * cards without locomotives).
     */
    public static final List<Card> CARS = CarList();

    /**
     * this attribute counts the number of cards in the list CARS.
     */
    public static final int COUNT = CARS.size();


    private final Color cardColor;

    /**
     * this constructor is associating each card to each color.
     */
    Card(){
        switch(this) {

            case BLACK:
                cardColor = Color.BLACK;
                break;

            case VIOLET:
                cardColor = Color.VIOLET;
                break;

            case BLUE:
                cardColor = Color.BLUE;
                break;

            case GREEN:
                cardColor = Color.GREEN;
                break;

            case YELLOW:
                cardColor = Color.YELLOW;
                break;

            case ORANGE:
                cardColor = Color.ORANGE;
                break;

            case RED:
                cardColor = Color.RED;
                break;

            case WHITE:
                cardColor = Color.WHITE;
                break;

            case LOCOMOTIVE:
                cardColor = Color.NULL;
                break;

            default:
                cardColor = null;
        }
    }

    /**
     * this method returns the type of card according to which color we choose.
     *
     * @param color
     * a chosen color.
     * @return the type of card of the selected color
     *
     */
    public static Card of(Color color){

        Card cardType;

        switch(color) {

        case BLACK:
            cardType = BLACK;
            break;

        case VIOLET:
            cardType = VIOLET;
            break;

        case BLUE:
            cardType = BLUE;
            break;

        case GREEN:
            cardType = GREEN;
            break;

        case YELLOW:
            cardType = YELLOW;
            break;

        case ORANGE:
            cardType = ORANGE;
            break;

        case RED:
            cardType = RED;
            break;

        case WHITE:
            cardType = WHITE;
            break;

        case NULL:
            cardType = LOCOMOTIVE;
            break;

        default:
            cardType = null;
        }

        return cardType;

    }

    /**
     * Returns the color of a chosen card
     * @return the color of a card.
     */
    public Color color(){
        return cardColor;
    }

    private static List<Card> CarList(){

        List <Card> CARS = new ArrayList<Card>();
        CARS.add(Card.BLACK);
        CARS.add(Card.VIOLET);
        CARS.add(Card.BLUE);
        CARS.add(Card.GREEN);
        CARS.add(Card.YELLOW);
        CARS.add(Card.ORANGE);
        CARS.add(Card.RED);
        CARS.add(Card.WHITE);

        return CARS;
    }


}
