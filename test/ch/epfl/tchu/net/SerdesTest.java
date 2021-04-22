package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Ticket;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class SerdesTest   {

    private String test = "Charles";

    private <T> void serdeTest(Serde<T> serde, T t){
        System.out.println("---------");
        System.out.println(serde.serialize(t));
        System.out.println(serde.deserialize(serde.serialize(t)));

          Assertions.assertEquals(t, serde.deserialize(serde.serialize(t)));
    }

    @Test
    public void integerSerdeTest(){
        serdeTest(Serdes.INTEGER_SERDE, 2021);
    }

    @Test
    public void stringSerdeTest(){
        serdeTest(Serdes.STRING_SERDE, test);
        Assertions.assertEquals(Serdes.STRING_SERDE.serialize(test), "Q2hhcmxlcw==");
    }

    @Test
    public void cardSerdeTest(){
        serdeTest(Serdes.CARD_SERDE, Card.VIOLET);
    }

    @Test
    public void routeSerdeTest(){
        serdeTest(Serdes.ROUTE_SERDE, ChMap.routes().get(0));
    }

    @Test
    public void ticketSerdeTest(){
        serdeTest(Serdes.TICKET_SERDE, ChMap.tickets().get(0));
    }

    @Test
    public void stringListSerde(){
        serdeTest(Serdes.STRING_LIST_SERDE, List.of(test, test));
    }

    @Test
    public void cardListSerdeTest(){
        serdeTest(Serdes.CARD_LIST_SERDE, List.of(Card.BLUE, Card.LOCOMOTIVE, Card.VIOLET));
    }

    @Test void routeListSerdeTest(){
        serdeTest(Serdes.ROUTE_LIST_SERDE, List.of(ChMap.routes().get(0), ChMap.routes().get(69)));
    }

    @Test
    public void cardSortedagSerdeTest(){
        List<Card> cardList = List.of(Card.BLUE, Card.BLUE, Card.LOCOMOTIVE, Card.BLACK, Card.GREEN, Card.BLACK);
        serdeTest(Serdes.CARD_SORTEDBAG_SERDE, SortedBag.of(cardList));
    }

    @Test
    public void ticketSortedBagSerdeTest(){

        serdeTest(Serdes.TICKET_SERDE, SortedBag.);
    }
}
