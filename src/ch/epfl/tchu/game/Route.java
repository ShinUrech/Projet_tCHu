package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to express the different routes a player can take
 *
 * @author Aidas Venckunas (325464)
 */

public final class Route {

    private final String id;
    private final Station station1;
    private final Station station2;
    private final int length;
    private final Level level;
    private final Color color;

    /**
     * enum for the different ground levels a route can have (either overground or underground).
     */
    public enum Level{

        OVERGROUND,
        UNDERGROUND;
    }

    /**
     * Route constructor with following parameters.
     *
     * @param id
     * the id of a route.
     * @param station1
     * Station1 of the route.
     * @param station2
     * Station2 of the route.
     * @param length
     * Length of a route.
     * @param level
     * Level of the Route(Tunnel of Road).
     * @param color
     * Color of a route(can be null).
     *
     * @throws IllegalArgumentException if both station1 and station2 are the same or if the route length doesn't
     * correspond to the bounds MIN_ROUTE_LENGTH or MAX_ROUTE_LENGTH.
     * @throws NullPointerException if either one of the two station's, ID or level value is null.
     */
    public Route(String id, Station station1, Station station2, int length, Level level, Color color){

        Preconditions.checkArgument(!station1.equals(station2));
        Preconditions.checkArgument(length <= Constants.MAX_ROUTE_LENGTH);
        Preconditions.checkArgument(length >= Constants.MIN_ROUTE_LENGTH);
        if(id == null || station1 == null || station2 == null || level == null){
            throw new NullPointerException();
        }

        this.id = id;
        this.station1 = station1;
        this.station2 = station2;
        this.length = length;
        this.level = level;
        this.color = color;

    }

    /**
     * Returns id of a route.
     * @return id of a route.
     */
    public String id(){
        return id;
    }

    /**
     * Returns station1.
     * @return station1.
     */
    public Station station1(){
        return station1;
    }

    /**
     * Returns station2.
     * @return station2.
     */
    public Station station2(){
        return station2;
    }

    /**
     * Returns the length of a route.
     * @return the length of a route.
     */
    public int length(){
        return length;
    }

    /**
     * Returns the level of a route.
     * @return the level of a route.
     */
    public Level level(){
        return level;
    }

    /**
     * Returns the route's color.
     * @return the route's color.
     */
    public Color color(){
        return color;
    }

    /**
     * Returns the pair of stations that make up a route.
     * @return the pair of stations that make up a route.
     */
    public List<Station> stations(){
        return List.of(station1, station2);
    }

    /**
     * this methods gives us the "opposite" station on a certain route. Ex: if a route A goes from Lausanne to
     * Bern and you call the method for A with Bern as a parameter, the method will give Lausanne back.
     *
     * @param station
     * the given station.
     *
     * @return the opposite station on a give route.
     *
     * @throws IllegalArgumentException if the input does not correspond to any station of the route.
     */
    public Station stationOpposite(Station station) {

        if (!station.equals(station1) && !station.equals(station2)) {
            throw new IllegalArgumentException();
        }
        else if (!station.equals(station1)) {
            return station1;
        }
        else return station2;

    }

    /**
     * Method that calculates all possible combinations of cards that can be used to claim a route.
     *
     * @return List of all possible combinations of cards to claim a route.
     */
    public List<SortedBag<Card>> possibleClaimCards(){

        List<SortedBag<Card>> possibilities = new ArrayList<SortedBag<Card>>();

        switch (level) {

            case OVERGROUND:

                if(color != null){
                    possibilities.add(SortedBag.of(length, Card.of(color)));
                    return possibilities;
                }
                else{

                    for(Card b : Card.CARS){
                        possibilities.add(SortedBag.of(length, b));
                    }

                    return possibilities;
                }

            case UNDERGROUND:

                if(color != null){

                    for(int a = length; a >= 0; a--){
                        possibilities.add(SortedBag.of(a, Card.of(color), length - a, Card.LOCOMOTIVE));
                    }
                    return possibilities;
                }
                else{

                    for(int a = 0; a < length; a++){

                        for(Card b : Card.CARS){
                            possibilities.add(SortedBag.of(length - a, b, a, Card.LOCOMOTIVE));
                        }
                    }
                    possibilities.add(SortedBag.of(length, Card.LOCOMOTIVE));

                    return possibilities;
                }


        }
        return null;
    }

    /**
     * Method that calculates the number of additional cards needed to claim a tunnel.
     *
     * @param claimCards
     * Cards used to claim a tunnel.
     * @param drawnCards
     * 3 Additionally drawn cards from top of a deck.
     * @throws IllegalArgumentException if level of a route is OVERGROUND or if a number of additionally
     * drawn cards is not equal to 3.
     *
     * @return the number of additional claim cards needed to claim a tunnel.
     */
    public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards){

        int count = 0;

        Preconditions.checkArgument(level == Level.UNDERGROUND);
        Preconditions.checkArgument(drawnCards.size() == Constants.ADDITIONAL_TUNNEL_CARDS);

        for(Card a : drawnCards){

            for (Card b : claimCards){

                if(a.equals(b) || a == Card.LOCOMOTIVE) {

                    ++count;
                    break;
                }
            }
        }

        return count;
    }

    /**
     * Method that returns the amount of points a player gets for claiming a route according
     * to the route's length.
     *
     * @return number of points given for a claim.
     */
    public int claimPoints(){
        return Constants.ROUTE_CLAIM_POINTS.get(length);
    }
}
