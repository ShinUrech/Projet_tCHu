package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SerdesTest_1 {


    private String test = "Charles";

    private List<Card> fu = List.of(Card.RED, Card.WHITE, Card.BLUE, Card.BLACK, Card.RED);

    private PublicCardState cs = new PublicCardState(fu, 30, 31);

    private List<Route> rs1 = ChMap.routes().subList(0, 2);

    private Map<PlayerId, PublicPlayerState> ps = Map.of(
                PlayerId.PLAYER_1, new PublicPlayerState(10, 11, rs1),
                PlayerId.PLAYER_2, new PublicPlayerState(20, 21, List.of()));

    private PublicGameState gs =
            new PublicGameState(40, cs, PlayerId.PLAYER_2, ps, null);

    private <T> T serdeAuto(Serde<T> serde, T t){
        return serde.deserialize(serde.serialize(t));
    }




    private <T> void serdeTest(Serde<T> serde, T t){
          Assertions.assertEquals(t, serde.deserialize(serde.serialize(t)));
    }

    @Test
    public void integerSerdeWorks(){
        serdeTest(Serdes.INTEGER_SERDE, -2021);
    }

    @Test
    public void stringSerdeWorks(){
        serdeTest(Serdes.STRING_SERDE, test);
    }

    @Test
    public void playerIdSerdeWorks(){
        serdeTest(Serdes.PLAYER_ID_SERDE, PlayerId.PLAYER_1);
        serdeTest(Serdes.PLAYER_ID_SERDE, PlayerId.PLAYER_2);
    }

    @Test
    public void turnKindSerdeWorks(){
        serdeTest(Serdes.TURN_KIND_SERDE, Player.TurnKind.ALL.get(0));
    }

    @Test
    public void cardSerdeWorks(){
        serdeTest(Serdes.CARD_SERDE, Card.VIOLET);
    }

    @Test
    public void routeSerdeWorks(){
        serdeTest(Serdes.ROUTE_SERDE, ChMap.routes().get(0));
    }

    @Test
    public void ticketSerdeWorks(){
        serdeTest(Serdes.TICKET_SERDE, ChMap.tickets().get(0));
    }

    @Test
    public void stringListSerdeWorks(){
        serdeTest(Serdes.STRING_LIST_SERDE, List.of(test, test));
    }

    @Test
    public void cardListSerdeWorks(){
        serdeTest(Serdes.CARD_LIST_SERDE, List.of());
    }

    @Test void routeListSerdeWorks(){
        serdeTest(Serdes.ROUTE_LIST_SERDE, List.of(ChMap.routes().get(0), ChMap.routes().get(69)));
    }

    @Test
    public void cardSortedagSerdeWorks(){
        List<Card> cardList = List.of(Card.BLUE, Card.BLUE, Card.LOCOMOTIVE, Card.BLACK, Card.GREEN, Card.BLACK);
        serdeTest(Serdes.CARD_SORTEDBAG_SERDE, SortedBag.of(cardList));
    }

    @Test
    public void ticketSortedBagSerdeWorks(){
        serdeTest(Serdes.TICKET_SORTEDBAG_SERDE, SortedBag.of(List.of(ChMap.tickets().get(4), ChMap.tickets().get(2), ChMap.tickets().get(0), ChMap.tickets().get(10), ChMap.tickets().get(4), ChMap.tickets().get(2))));
    }

    @Test
    public void cardSortedBagListSerdeWorks(){

        SortedBag<Card> cardSortedBag_1 = SortedBag.of(List.of(Card.BLUE, Card.BLUE, Card.LOCOMOTIVE, Card.BLACK, Card.GREEN, Card.BLACK));
        SortedBag<Card> cardSortedBag_2 = SortedBag.of(List.of(Card.LOCOMOTIVE, Card.LOCOMOTIVE, Card.GREEN, Card.BLUE, Card.BLUE, Card.GREEN));
        SortedBag<Card> cardSortedBag_3 = SortedBag.of(List.of(Card.GREEN, Card.BLUE, Card.LOCOMOTIVE, Card.GREEN,Card.BLUE, Card.ORANGE, Card.VIOLET));

        serdeTest(Serdes.CARD_SORTEDBAG_LIST_SERDE, List.of(cardSortedBag_1, cardSortedBag_2, cardSortedBag_3));

    }

    @Test
    public void publicCardStateSerdeWorks(){

        PublicCardState serded = serdeAuto(Serdes.PUBLIC_CARD_STATE_SERDE, cs);

        Assertions.assertEquals(cs.faceUpCards(), serded.faceUpCards());
        Assertions.assertEquals(cs.deckSize(), serded.deckSize());
        Assertions.assertEquals(cs.discardsSize(), serded.discardsSize());

    }

    @Test
    public void publicPlayerStateSerdeWorks(){

        PublicPlayerState serded = serdeAuto(Serdes.PUBLIC_PLAYER_STATE_SERDE, ps.get(PlayerId.PLAYER_1));

        Assertions.assertEquals(ps.get(PlayerId.PLAYER_1).ticketCount(), serded.ticketCount());
        Assertions.assertEquals(ps.get(PlayerId.PLAYER_1).cardCount(), serded.cardCount());
        Assertions.assertEquals(ps.get(PlayerId.PLAYER_1).routes(), serded.routes());

    }

    @Test
    public void playerStateSerdeWorks(){

        PlayerState testPS = PlayerState.initial(SortedBag.of(List.of(Card.BLUE, Card.BLUE, Card.LOCOMOTIVE, Card.VIOLET)));
        PlayerState serded = serdeAuto(Serdes.PLAYER_STATE_SERDE , testPS);

        Assertions.assertEquals(testPS.tickets(), serded.tickets());
        Assertions.assertEquals(testPS.cards(), serded.cards());
        Assertions.assertEquals(testPS.routes(), serded.routes());

    }

    @Test
    public void publicGameStateSerdeWorks(){

        PublicGameState serded = serdeAuto(Serdes.PUBLIC_GAME_STATE_SERDE, gs);

        Assertions.assertEquals(gs.ticketsCount(), serded.ticketsCount());
        Assertions.assertEquals(gs.cardState(), gs.cardState());
        Assertions.assertEquals(gs.playerState(PlayerId.PLAYER_1), serded.playerState(PlayerId.PLAYER_1));
        Assertions.assertEquals(gs.lastPlayer(), serded.lastPlayer());
    }
}
