package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.collections.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObservableGameState {

    private final int TOTAL_TICKETS_COUNT = 46;
    private final int TOTAL_CARDS_COUNT = 110;

    private final PlayerId playerId;
    private PublicGameState currentGameState;
    private PlayerState currentPlayerState;

    private final IntegerProperty ticketsPercentage;
    private final IntegerProperty cardsPercentage;
    private final List<ObjectProperty<Card>> faceUpCards;
    private final List<ObjectProperty<PlayerId>> routes;

    private final Map<PlayerId, IntegerProperty> playersTickets;
    private final Map<PlayerId, IntegerProperty> playersCards;
    private final Map<PlayerId, IntegerProperty> playersWagons;
    private final Map<PlayerId, IntegerProperty> playersConstrPoints;

    private final ObservableList<Ticket> tickets;
    private final Map<Card, IntegerProperty> cards;
    private final List<BooleanProperty> canSeize;


    public ObservableGameState(PlayerId playerId){
        this.playerId = playerId;
        currentGameState = null;
        currentPlayerState = null;
        ticketsPercentage = new SimpleIntegerProperty();
        cardsPercentage = new SimpleIntegerProperty();
        faceUpCards = createFaceUpCards();
        routes = createRoutes();
        playersTickets = createPlayersTickets();
        playersCards = createPlayersCards();
        playersWagons = createPlayersWagons();
        playersConstrPoints = createPlayersConstrPoints();
        tickets = FXCollections.observableArrayList();
        cards = createCards();
        canSeize = createCanSeize();

    }

    public void setState(PublicGameState newGameState, PlayerState playerState){

        currentGameState = newGameState;
        currentPlayerState = playerState;
        PublicPlayerState statePlayer1 = currentGameState.playerState(PlayerId.PLAYER_1);
        PublicPlayerState statePlayer2 = currentGameState.playerState(PlayerId.PLAYER_2);

        ticketsPercentage.set(currentGameState.ticketsCount() / TOTAL_TICKETS_COUNT * 100);
        cardsPercentage.set(currentGameState.cardState().totalSize() / TOTAL_CARDS_COUNT * 100);

        for(Route route : ChMap.routes()){
            if(statePlayer1.routes().contains(route)){
                //System.out.println("player1 has this route");
                routes.get(ChMap.routes().indexOf(route)).set(PlayerId.PLAYER_1);
                //System.out.println(routes.get(ChMap.routes().indexOf(route)));
            }
            else if(statePlayer2.routes().contains(route)){
                routes.get(ChMap.routes().indexOf(route)).set(PlayerId.PLAYER_2);
            }
            else if(currentGameState.playerState(playerId).equals(currentGameState.currentPlayerState())
                    && currentPlayerState.canClaimRoute(route)){
                Boolean canBeClaimed = true;
                for(Route a : currentGameState.claimedRoutes()){
                    if(a.station1().equals(route.station1()) && a.station2().equals(route.station2())
                            && !route.equals(a)){
                        canBeClaimed = false;
                        break;
                    }
                }
                canSeize.get(ChMap.routes().indexOf(route)).set(canBeClaimed);
            }
        }

        for(int slot : Constants.FACE_UP_CARD_SLOTS) {
            Card newCard = newGameState.cardState().faceUpCard(slot);
            faceUpCards.get(slot).set(newCard);
        }

        for(PlayerId player : PlayerId.ALL){
            playersTickets.get(player).set(currentGameState.playerState(player).ticketCount());
            playersCards.get(player).set(currentGameState.playerState(player).cardCount());
            playersWagons.get(player).set(currentGameState.playerState(player).carCount());
            playersConstrPoints.get(player).set(currentGameState.playerState(player).claimPoints());
        }

        tickets.setAll(currentPlayerState.tickets().toList());

        for(Card card : Card.ALL){
            cards.get(card).set(currentPlayerState.cards().countOf(card));
        }


    }

    private static List<ObjectProperty<Card>> createFaceUpCards(){
        List<ObjectProperty<Card>> faceUpCards = new ArrayList<>();
        for(int slot : Constants.FACE_UP_CARD_SLOTS){
            faceUpCards.add(new SimpleObjectProperty<Card>());
        }
        return faceUpCards;
    }

    private static List<ObjectProperty<PlayerId>> createRoutes(){
        List<ObjectProperty<PlayerId>> routes = new ArrayList<ObjectProperty<PlayerId>>();
        for(Route a : ChMap.routes()){
            routes.add(new SimpleObjectProperty<PlayerId>());
            //System.out.println(routes.get(ChMap.routes().indexOf(a)));
        }
        return routes;
    }

    private static Map<PlayerId, IntegerProperty> createPlayersTickets(){
        Map<PlayerId, IntegerProperty> tickets = new HashMap<>();
        for(PlayerId player : PlayerId.ALL){
            tickets.put(player, new SimpleIntegerProperty());
        }
        return tickets;
    }

    private static Map<PlayerId, IntegerProperty> createPlayersCards(){
        Map<PlayerId, IntegerProperty> cards = new HashMap<>();
        for(PlayerId player : PlayerId.ALL){
            cards.put(player, new SimpleIntegerProperty());
        }
        return cards;
    }

    private static Map<PlayerId, IntegerProperty> createPlayersWagons(){
        Map<PlayerId, IntegerProperty> wagons = new HashMap<>();
        for(PlayerId player : PlayerId.ALL){
            wagons.put(player, new SimpleIntegerProperty());
        }
        return wagons;
    }

    private static Map<PlayerId, IntegerProperty> createPlayersConstrPoints(){
        Map<PlayerId, IntegerProperty> points = new HashMap<>();
        for(PlayerId player : PlayerId.ALL){
            points.put(player, new SimpleIntegerProperty());
        }
        return points;
    }

    private static Map<Card, IntegerProperty> createCards(){
        Map<Card, IntegerProperty> cards = new HashMap<>();
        for(Card card : Card.ALL){
            cards.put(card, new SimpleIntegerProperty());
        }
        return cards;
    }

    private static List<BooleanProperty> createCanSeize(){
        List<BooleanProperty> canSeize = new ArrayList<>();
        for(Route route : ChMap.routes()){
            canSeize.add(new SimpleBooleanProperty());
        }
        return canSeize;
    }

    public ReadOnlyObjectProperty<Card> faceUpCard(int slot){
        return faceUpCards.get(slot);
    }

    public ReadOnlyIntegerProperty ticketsPercentage(){
        return ticketsPercentage;
    }

    public ReadOnlyIntegerProperty cardsPercentage(){
        return cardsPercentage;
    }

    public ReadOnlyObjectProperty<PlayerId> route(Route route){
        return routes.get(ChMap.routes().indexOf(route));
    }

    public ReadOnlyIntegerProperty playersTickets(PlayerId player){
        return playersTickets.get(player);
    }

    public ReadOnlyIntegerProperty playersCards(PlayerId player){
        return playersCards.get(player);
    }

    public ReadOnlyIntegerProperty playersWagons(PlayerId player){
        return playersWagons.get(player);
    }

    public ReadOnlyIntegerProperty playersConstrPoints(PlayerId player){
        return playersConstrPoints.get(player);
    }

    public ObservableList<Ticket> tickets(){
        return FXCollections.unmodifiableObservableList(tickets);
    }

    public ReadOnlyIntegerProperty card(Card card){
        return cards.get(card);
    }

    public ReadOnlyBooleanProperty canSeize(Route route){
        return canSeize.get(ChMap.routes().indexOf(route));
    }

    public Boolean canDrawTickets(){
        return currentGameState.canDrawTickets();
    }

    public Boolean canDrawCards(){
        return currentGameState.canDrawCards();
    }

    public List<SortedBag<Card>> possibleClaimCards(Route route){
        return currentPlayerState.possibleClaimCards(route);
    }
}
