package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;

public class PublicPlayerState {

    private final int ticketCount;
    private final int cardCount;
    private final List<Route> routes;
    private final int claimPoints;
    private final int carCount;

    /**
     * this is the public constructor for the public player state
     * @param ticketCount is the amount of tickets a player owns
     * @param cardCount is the amount of cards a player owns
     * @param routes is the list of all claimed routes by the player
     */
    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes){

        Preconditions.checkArgument(cardCount >= 0 || ticketCount >= 0);

        this.ticketCount = ticketCount;
        this.cardCount = cardCount;
        this.routes = List.copyOf(routes);
        this.carCount = getCardCount(routes);
        this.claimPoints = getClaimPoints(routes);

    }

    /**
     * this method returns the amount of tickets the player has
     * @return an int that is the amount of tickets left on a players hand
     */
    public int ticketCount(){
        return this.ticketCount;
    }

    /**
     * this method returns the amount of cards the player has
     * @return an int that is the amount of tickets left on a player's hand
     */
    public int cardCount(){
        return this.cardCount;
    }

    /**
     * this method returns a list with all routes claimed by the player
     * @return the list with all claimed routes
     */
    public List<Route> routes(){
        return this.routes;
    }

    /**
     * this method returns the total amount of cards left in a player's hand
     * @return the amount of cards left in a player's hand
     */
    public int carCount(){
        return this.cardCount;
    }


    /**
     * returns the amount of points the player claimed during the game
     * @return the number of points the player claimed
     */
    public int claimPoints(){
        return this.claimPoints;
    }



    private int getClaimPoints(List<Route> routes){
        int TOTAL_CLAIMPOINTS = 0;
        for(Route route: routes){
            TOTAL_CLAIMPOINTS =+ route.claimPoints();
        }
        return TOTAL_CLAIMPOINTS;
    }

    private int getCardCount(List<Route> routes){

        int TOTAL_LENGTH = 0;

        for(Route route: routes){
            TOTAL_LENGTH  =+ route.length();
        }
        return Constants.INITIAL_CAR_COUNT - TOTAL_LENGTH;
    }
}
