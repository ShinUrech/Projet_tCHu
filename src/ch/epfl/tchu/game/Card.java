package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum to represent all types of cards.
 *
 * @author Shin Urech (327245)
 */

public enum Card {

    BLACK(Color.BLACK),
    VIOLET(Color.VIOLET),
    BLUE(Color.BLUE),
    GREEN(Color.GREEN),
    YELLOW(Color.YELLOW),
    ORANGE(Color.ORANGE),
    RED(Color.RED),
    WHITE(Color.WHITE),
    LOCOMOTIVE(null);


    /**
     * List of all possible cards.
     */
    public static final List<Card> ALL = List.of(Card.values());

    /**
     * This attribute counts the number of cards in the list ALL.
     */
    public static final int COUNT = ALL.size();

    /**
     * List of all the different kinds of cards that are required to play except
     * locomotives (CarList() is defined below and it allows us to create a list of all
     * cards without locomotives).
     */
    public static final List<Card> CARS = CarList();


    private final Color cardColor;

    /**
     * This constructor is associating each card to each color.
     */
   private Card(Color cardColor){
        this.cardColor = cardColor;
    }

    /**
     * This method returns the type of card according to which color we choose.
     *
     * @param color
     * a chosen color.
     *
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

        default:
            cardType = LOCOMOTIVE;
        }

        return cardType;

    }

    /**
     * Returns the color of a chosen card.
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
