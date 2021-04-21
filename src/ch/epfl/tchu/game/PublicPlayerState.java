package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;

/**
 * A class that represents elements of Player state that are visible publicly.
 *
 * @author Aidas Venckunas (325464)
 * @author Shin Urech (327245)
 */
public class PublicPlayerState {

    private final int ticketCount;
    private final int cardCount;
    private final List<Route> routes;
    private final int claimPoints;
    private final int carCount;

    /**
     * The public constructor for the public player state.
     *
     * @param ticketCount the amount of tickets a player owns
     * @param cardCount the amount of cards a player owns
     * @throws IllegalArgumentException if the ticket or card count is negative
     * @param routes the list of all claimed routes by the player
     */
    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes){

        Preconditions.checkArgument(cardCount >= 0 && ticketCount >= 0);

        this.ticketCount = ticketCount;
        this.cardCount = cardCount;
        this.routes = List.copyOf(routes);
        this.carCount = getCarCount(routes);
        this.claimPoints = getClaimPoints(routes);

    }

    /**
     * This method returns the amount of tickets the player has.
     * @return the amount of tickets left on a players hand
     */
    public int ticketCount(){
        return ticketCount;
    }

    /**
     * This method returns the amount of cards the player has.
     * @return the amount of tickets left on a player's hand
     */
    public int cardCount(){
        return cardCount;
    }

    /**
     * This method returns a list with all routes claimed by the player.
     * @return the list with all claimed routes
     */
    public List<Route> routes(){
        return routes;
    }

    /**
     * This method returns the total amount of cards left in a player's hand.
     * @return the amount of cards left in a player's hand
     */
    public int carCount(){
        return carCount;
    }


    /**
     * Returns the amount of points the player claimed during the game.
     * @return the number of points the player claimed
     */
    public int claimPoints(){
        return claimPoints;
    }



    private int getClaimPoints(List<Route> routes){
        int totalClaimPoints = 0;
        for(Route route: routes){
            totalClaimPoints += route.claimPoints();
        }
        return totalClaimPoints;
    }

    private int getCarCount(List<Route> routes){

        int totalLength = 0;

        for(Route route: routes){
            totalLength += route.length();
        }
        return Constants.INITIAL_CAR_COUNT - totalLength;
    }
}
