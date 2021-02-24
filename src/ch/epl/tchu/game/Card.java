package ch.epl.tchu.game;

import java.util.ArrayList;
import java.util.List;

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

    public static final List<Card> CARS = CarList();
    public static final int COUNT = CARS.size();

    private final Color cardColor;


    /*Card constructor. In order to make it work you have to use the name of
    the card you wanna build */

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
