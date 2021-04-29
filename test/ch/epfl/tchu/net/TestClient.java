package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.epfl.tchu.game.ChMap;

public final class TestClient {
    public static void main(String[] args) {
        System.out.println("Starting client!");
        RemotePlayerClient playerClient = new RemotePlayerClient(new TestPlayer(), "localhost", 5108);
        playerClient.run();
        System.out.println("Client done!");
    }

    private final static class TestPlayer implements Player {
        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> names) {

            System.out.printf("ownId:%s \n", ownId);
            System.out.printf("playerNames:%s \n", names);
            System.out.println();
        }

        @Override
        public void receiveInfo(String info) {
            System.out.println("info received: ");
            System.out.println(info);
            System.out.println();
        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            System.out.println("State has been updated: ");
            System.out.printf("PlayerState: %s", ownState.toString());
            System.out.println();
            System.out.printf("New game state: %s \n", newState.toString());
            System.out.println();
        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
            System.out.println("Choose initial tickets from: ");
            System.out.println(tickets);
            System.out.println();
        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            System.out.println("Player has chosen these initial tickets: ");

            SortedBag.Builder<Ticket> builder = new SortedBag.Builder<>();
            builder.add(1, ChMap.tickets().get(0));
            builder.add(1, ChMap.tickets().get(1));
            builder.add(1, ChMap.tickets().get(2));

            SortedBag<Ticket> initialTickets = builder.build();
            System.out.println(initialTickets);
            System.out.println();
            return initialTickets;
        }

        @Override
        public TurnKind nextTurn() {
            System.out.println("Player chooses next turn to be: ");
            System.out.println(TurnKind.DRAW_CARDS.name());
            System.out.println();
            return TurnKind.DRAW_CARDS;
        }

        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
            System.out.println("Player has to choose tickets from: ");
            System.out.println(options);
            System.out.println("Player has chosen: ");

            SortedBag.Builder<Ticket> builder = new SortedBag.Builder<>();
            builder.add(1, ChMap.tickets().get(3));
            builder.add(1, ChMap.tickets().get(4));
            builder.add(1, ChMap.tickets().get(5));

            SortedBag<Ticket> tickets = builder.build();

            System.out.println(tickets);
            System.out.println();

            return tickets;
        }

        @Override
        public int drawSlot() {
            System.out.printf("Player has chosen to draw car no. %s \n", 2);
            System.out.println();
            return 2;
        }

        @Override
        public Route claimedRoute() {
            System.out.printf("Player has claimed a route %s \n", ChMap.routes().get(1));
            System.out.println();
            return ChMap.routes().get(1);
        }

        @Override
        public SortedBag<Card> initialClaimCards() {
            System.out.println("Player has used these cards to claim a route: ");
            System.out.println(SortedBag.of(2, Card.YELLOW, 1, Card.BLUE));
            System.out.println();
            return SortedBag.of(2, Card.YELLOW, 1, Card.BLUE);
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
            System.out.println("Player has to choose additional cards from: ");
            System.out.println(options);
            System.out.println("Player has chosen: ");
            System.out.println(SortedBag.of(2, Card.YELLOW, 1, Card.BLUE));
            System.out.println();
            return SortedBag.of(2, Card.YELLOW, 1, Card.BLUE);
        }

    }
}