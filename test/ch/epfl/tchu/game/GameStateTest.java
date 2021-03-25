package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GameStateTest {

    @Test
    void withMoreDiscardedCardsWorks(){

        GameState initialGameState = GameState.initial(SortedBag.of(1, TestMap.LAU_STG), new Random());
        GameState testedGameState1 = initialGameState.withMoreDiscardedCards(SortedBag.of(5, Card.LOCOMOTIVE,
                11, Card.BLUE));

        assertTrue(!initialGameState.equals(testedGameState1));

        GameState testedGameState2 = initialGameState.withMoreDiscardedCards(SortedBag.of());

        assertTrue(!initialGameState.equals(testedGameState2));
    }

    @Test
    void withCardsDeckRecreatedIfNeededWorksWhenDeckIsNotEmpty(){

        GameState initialGameState = GameState.initial(SortedBag.of(1, TestMap.LAU_STG), new Random());

        assertEquals(initialGameState, initialGameState.withCardsDeckRecreatedIfNeeded(new Random()));

    }

    @Test
    void withCardsDeckRecreatedIfNeededWorksWhenDeckIsEmpty(){

        GameState initialGameState = GameState.initial(SortedBag.of(1, TestMap.LAU_STG), new Random());

        while(!initialGameState.cardState().isDeckEmpty()){
            initialGameState = initialGameState.withoutTopCard();
        }

        initialGameState = initialGameState.withMoreDiscardedCards(SortedBag.of(11, Card.LOCOMOTIVE));
        initialGameState = initialGameState.withCardsDeckRecreatedIfNeeded(new Random());

        assertEquals(11, initialGameState.cardState().deckSize());
    }

    @Test
    void withInitiallyChosenTicketsWorksWithInitialGameState(){
        GameState initialGameState = GameState.initial(SortedBag.of(1, TestMap.LAU_STG), new Random());
        GameState withTicketGameState = initialGameState.withInitiallyChosenTickets(PlayerId.PLAYER_1,
                SortedBag.of(TestMap.LAU_BER));
        GameState withTicketGameState2 = initialGameState.withInitiallyChosenTickets(PlayerId.PLAYER_1,
                SortedBag.of());

        assertFalse(initialGameState.equals(withTicketGameState));
        assertEquals(initialGameState.playerState(PlayerId.PLAYER_1).tickets(),
                withTicketGameState2.playerState(PlayerId.PLAYER_1).tickets());
    }

    @Test
    void withInitiallyChosenTicketsWorksWithNonInitialGameState(){
        GameState initialGameState = GameState.initial(SortedBag.of(1, TestMap.LAU_STG), new Random());
        GameState withTicketGameState = initialGameState.withInitiallyChosenTickets(PlayerId.PLAYER_1,
                SortedBag.of(TestMap.LAU_BER));

        assertThrows(IllegalArgumentException.class,
                () -> withTicketGameState.withInitiallyChosenTickets(PlayerId.PLAYER_1, SortedBag.of()));
        assertThrows(IllegalArgumentException.class,
                () -> withTicketGameState.withInitiallyChosenTickets(PlayerId.PLAYER_1, SortedBag.of(TestMap.LAU_BER)));

    }

    @Test
    void withChosenAdditionalTicketsThrowsCorrectly(){

        GameState initialGameState = GameState.initial(SortedBag.of(2, TestMap.BER_STG, 1, TestMap.DE1_IT2), new Random());
        GameState withTicketGameState = initialGameState.withInitiallyChosenTickets(initialGameState.currentPlayerId(),
                SortedBag.of(TestMap.LAU_STG));

        assertThrows(IllegalArgumentException.class, ()-> withTicketGameState.withChosenAdditionalTickets
                (SortedBag.of(1, TestMap.BER_STG, 1, TestMap.DE1_IT2), SortedBag.of(TestMap.LAU_STG)));

    }

    @Test
    void withChosenAdditionalTicketsWorks(){

        GameState initialGameState = GameState.initial(SortedBag.of(2, TestMap.BER_STG, 1, TestMap.DE1_IT2), new Random());
        GameState withTicketGameState = initialGameState.withInitiallyChosenTickets(initialGameState.currentPlayerId(),
                SortedBag.of(TestMap.LAU_STG));

        withTicketGameState = withTicketGameState.withChosenAdditionalTickets
                (SortedBag.of(1, TestMap.BER_STG, 1, TestMap.DE1_IT2), SortedBag.of(TestMap.DE1_IT2));


        assertFalse(initialGameState.equals(withTicketGameState));
        assertNotEquals(initialGameState.currentPlayerState(),
                withTicketGameState.currentPlayerState());
        assertEquals(2, withTicketGameState.currentPlayerState().tickets().size());
        assertEquals(1, withTicketGameState.ticketsCount());

    }

    @Test
    void withDrawnFaceUpCardWorks(){

        GameState initialGameState = GameState.initial(SortedBag.of(2, TestMap.BER_STG, 1, TestMap.DE1_IT2), new Random());

        GameState changedGameState = initialGameState.withDrawnFaceUpCard(3);

        assertEquals(initialGameState.cardState().deckSize()-1, changedGameState.cardState().deckSize());
        assertNotEquals(initialGameState.currentPlayerState().cards(), changedGameState.currentPlayerState().cards());
        assertEquals(initialGameState.currentPlayerState().cards().size() + 1,
                changedGameState.currentPlayerState().cards().size());

    }

    @Test
    void withDrawnFaceUpCardThrowsIllegalArgumentExceptionWhenNeeded(){

        GameState initialGameState = GameState.initial(SortedBag.of(2, TestMap.BER_STG, 1, TestMap.DE1_IT2), new Random());

        while(initialGameState.cardState().deckSize() > 3){
            initialGameState = initialGameState.withoutTopCard();
        }

        GameState finalGameState = initialGameState;

        assertThrows(IllegalArgumentException.class, ()-> finalGameState.withDrawnFaceUpCard(3));

    }

    @Test
    void withBlindlyDrawnCardWorks(){

        GameState initialGameState = GameState.initial(SortedBag.of(2, TestMap.BER_STG, 1, TestMap.DE1_IT2), new Random());

        GameState changedGameState = initialGameState.withBlindlyDrawnCard();

        assertEquals(initialGameState.cardState().deckSize()-1, changedGameState.cardState().deckSize());
        assertNotEquals(initialGameState.currentPlayerState().cards(), changedGameState.currentPlayerState().cards());
        assertEquals(initialGameState.currentPlayerState().cards().size() + 1,
                changedGameState.currentPlayerState().cards().size());

    }

    @Test
    void withBlindlyDrawnCardsThrowsIllegalArgumentExceptionWhenNeeded(){

        GameState initialGameState = GameState.initial(SortedBag.of(2, TestMap.BER_STG, 1, TestMap.DE1_IT2), new Random());

        while(initialGameState.cardState().deckSize() > 3){
            initialGameState = initialGameState.withoutTopCard();
        }

        GameState finalGameState = initialGameState;

        assertThrows(IllegalArgumentException.class, ()-> finalGameState.withBlindlyDrawnCard());

    }

    @Test
    void withClaimedRouteWorks(){

        GameState initialGameState = GameState.initial(SortedBag.of(2, TestMap.BER_STG, 1, TestMap.DE1_IT2), new Random());

        GameState withRoadGameState = initialGameState.withClaimedRoute(TestMap.route1, SortedBag.of(4, Card.BLACK));


        assertFalse(initialGameState.equals(withRoadGameState));
        assertNotEquals(initialGameState.currentPlayerState(),
                withRoadGameState.currentPlayerState());
        assertEquals(initialGameState.currentPlayerState().withClaimedRoute(TestMap.route1, SortedBag.of(4, Card.BLACK)).routes(),
                withRoadGameState.currentPlayerState().routes());
        assertEquals(initialGameState.currentPlayerState().withClaimedRoute(TestMap.route1, SortedBag.of(4, Card.BLACK)).cards(),
                withRoadGameState.currentPlayerState().cards());

    }

    @Test
    void LastTurnBeginsWorks(){

        GameState initialGameState = GameState.initial(SortedBag.of(2, TestMap.BER_STG, 1, TestMap.DE1_IT2), new Random());

        GameState withRoadGameState = initialGameState.withClaimedRoute(TestMap.route2, SortedBag.of(4, Card.BLACK));

        withRoadGameState = withRoadGameState.withClaimedRoute(TestMap.route2, SortedBag.of(4, Card.BLACK));
        withRoadGameState = withRoadGameState.withClaimedRoute(TestMap.route2, SortedBag.of(4, Card.BLACK));
        withRoadGameState = withRoadGameState.withClaimedRoute(TestMap.route2, SortedBag.of(4, Card.RED));
        withRoadGameState = withRoadGameState.withClaimedRoute(TestMap.route2, SortedBag.of(4, Card.BLUE));
        withRoadGameState = withRoadGameState.withClaimedRoute(TestMap.route2, SortedBag.of(4, Card.BLUE));
        withRoadGameState = withRoadGameState.withClaimedRoute(TestMap.route1, SortedBag.of(4, Card.BLUE));

        assertTrue(withRoadGameState.lastTurnBegins());
        assertFalse(initialGameState.lastTurnBegins());
    }

    @Test
    void ForNextTurnWorks(){
        GameState initialGameState = GameState.initial(SortedBag.of(2, TestMap.BER_STG, 1, TestMap.DE1_IT2), new Random());

        GameState withRoadGameState = initialGameState.withClaimedRoute(TestMap.route2, SortedBag.of(4, Card.BLACK));

        withRoadGameState = withRoadGameState.withClaimedRoute(TestMap.route2, SortedBag.of(4, Card.BLACK));
        withRoadGameState = withRoadGameState.withClaimedRoute(TestMap.route2, SortedBag.of(4, Card.BLACK));
        withRoadGameState = withRoadGameState.withClaimedRoute(TestMap.route2, SortedBag.of(4, Card.RED));
        withRoadGameState = withRoadGameState.withClaimedRoute(TestMap.route2, SortedBag.of(4, Card.BLUE));
        withRoadGameState = withRoadGameState.withClaimedRoute(TestMap.route2, SortedBag.of(4, Card.BLUE));
        withRoadGameState = withRoadGameState.withClaimedRoute(TestMap.route1, SortedBag.of(4, Card.BLUE));

        assertNotEquals(withRoadGameState, withRoadGameState.forNextTurn());
        assertEquals(initialGameState, initialGameState.forNextTurn());
    }

    private static final class TestMap {

        // Stations - cities
        public static final Station BER = new Station(0, "Berne");
        public static final Station LAU = new Station(1, "Lausanne");
        public static final Station STG = new Station(2, "Saint-Gall");

        // Stations - countries
        public static final Station DE1 = new Station(3, "Allemagne");
        public static final Station DE2 = new Station(4, "Allemagne");
        public static final Station DE3 = new Station(5, "Allemagne");
        public static final Station AT1 = new Station(6, "Autriche");
        public static final Station AT2 = new Station(7, "Autriche");
        public static final Station IT1 = new Station(8, "Italie");
        public static final Station IT2 = new Station(9, "Italie");
        public static final Station IT3 = new Station(10, "Italie");
        public static final Station FR1 = new Station(11, "France");
        public static final Station FR2 = new Station(12, "France");

        // Countries
        public static final List<Station> DE = List.of(DE1, DE2, DE3);
        public static final List<Station> AT = List.of(AT1, AT2);
        public static final List<Station> IT = List.of(IT1, IT2, IT3);
        public static final List<Station> FR = List.of(FR1, FR2);

        public static final Ticket LAU_STG = new Ticket(LAU, STG, 13);
        public static final Ticket LAU_BER = new Ticket(LAU, BER, 2);
        public static final Ticket DE1_IT2 = new Ticket(DE1, IT2, 2);
        public static final Ticket BER_STG = new Ticket(BER, STG, 4);
        public Ticket BER_NEIGHBORS = ticketToNeighbors(List.of(BER), 6, 11, 8, 5);
        public Ticket FR_NEIGHBORS = ticketToNeighbors(FR, 5, 14, 11, 0);

        public static final Route route1 = new Route("AT1_STG_1",BER, LAU, 3,Route.Level.UNDERGROUND, null);
        public static final Route route2 = new Route("AT1_STG_1",LAU, STG, 6,Route.Level.UNDERGROUND, null);


        private Ticket ticketToNeighbors(List<Station> from, int de, int at, int it, int fr) {
            var trips = new ArrayList<Trip>();
            if (de != 0) trips.addAll(Trip.all(from, DE, de));
            if (at != 0) trips.addAll(Trip.all(from, AT, at));
            if (it != 0) trips.addAll(Trip.all(from, IT, it));
            if (fr != 0) trips.addAll(Trip.all(from, FR, fr));
            return new Ticket(trips);
        }
    }
}
