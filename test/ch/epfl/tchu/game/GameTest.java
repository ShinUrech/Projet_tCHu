package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.*;

class GameTest {

    //this method tests the game method. not finished either, just did it for myself
    @Test
    public void gameWorks(){

        TestPlayer player1 = new TestPlayer(5, ChMap.routes());
        TestPlayer player2 = new TestPlayer(7, ChMap.routes());
        Map<PlayerId, Player> players = new EnumMap<>(PlayerId.class);
        Map<PlayerId, String> playersNames = new EnumMap<>(PlayerId.class);
        List<Ticket> tickets = ChMap.tickets();

        players.put(PlayerId.PLAYER_1, player1);
        players.put(PlayerId.PLAYER_2, player2);

        playersNames.put(PlayerId.PLAYER_1, "Shin");
        playersNames.put(PlayerId.PLAYER_2, "Aidas");


        Game.play(players, playersNames, SortedBag.of(tickets), player1.rng());
    }

    private static final class TestPlayer implements Player {

        private static final int TURN_LIMIT = 1000;

        private final Random rng;
        // Toutes les routes de la carte
        private final List<Route> allRoutes;

        private int turnCounter;
        private PlayerState ownState;
        private PublicGameState gameState;

        // Lorsque nextTurn retourne CLAIM_ROUTE
        private Route routeToClaim;
        private SortedBag<Card> initialClaimCards;

        public TestPlayer(long randomSeed, List<Route> allRoutes) {
            this.rng = new Random(randomSeed);
            this.allRoutes = List.copyOf(allRoutes);
            this.turnCounter = 0;
        }


        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
            System.out.println("player has been initialised");
        }

        //this method display's whether the info method works or not
        @Override
        public void receiveInfo(String info) {
            System.out.println("information transmitted");
        }


        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            this.gameState = newState;
            this.ownState = ownState;
        }

        //this method adds the initial 5 tickets to the player's hand
        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {

            ownState.withAddedTickets(tickets);
        }

        //this method chooses randomly an amount of cards between 3 and 5. Picks the nth first slots no matter what
        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            SortedBag.Builder builder = new SortedBag.Builder();

            int numberOfSelectedCards = rng.nextInt(3) + Constants.IN_GAME_TICKETS_COUNT;

            for(int i = 0; i < numberOfSelectedCards; ++i){
                builder.add(ownState.tickets().get(i));
            }

            return builder.build();

        }

        //i didnt made this method but i assume it decides which kind of turn will be played next
        @Override
        public TurnKind nextTurn() {
            turnCounter += 1;
            if (turnCounter > TURN_LIMIT)
                throw new Error("Trop de tours joués !");

            // Détermine les routes dont ce joueur peut s'emparer
            List<Route> claimableRoutes = this.claimableRoutes();
            if (claimableRoutes.isEmpty()) {
                return TurnKind.DRAW_CARDS;
            } else {
                int routeIndex = rng.nextInt(claimableRoutes.size());
                Route route = claimableRoutes.get(routeIndex);
                List<SortedBag<Card>> cards = ownState.possibleClaimCards(route);

                routeToClaim = route;
                initialClaimCards = cards.get(0);
                return TurnKind.CLAIM_ROUTE;
            }
        }


        //this method randomly chooses a ticket from many options randomly
        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
            return SortedBag.of(options.get(rng.nextInt(options.size())));
        }

        //this method returns a random slot index from which to pick a card
        @Override
        public int drawSlot() {
            return rng.nextInt(Constants.FACE_UP_CARDS_COUNT);
        }

        //this method returns which route is going to be claimed by the player
        @Override
        public Route claimedRoute() {
            return routeToClaim;
        }

        //this method returns which cards will be used to claim a route
        @Override
        public SortedBag<Card> initialClaimCards() {
            return initialClaimCards;
        }

        //this method chooses a random option to claim a combination of cards
        @Override
        public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
            return options.get(rng.nextInt(options.size()));
        }

        private List<Route> claimableRoutes(){

            ArrayList<Route> claimableRoutes = new ArrayList<>();

            for(Route route : allRoutes){

                if(ownState.canClaimRoute(route) && !ownState.routes().contains(route)){
                    claimableRoutes.add(route);
                }

            }


            return claimableRoutes;
        }

        public Random rng(){
            return rng;
        }
    }
}