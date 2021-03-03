package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.List;

/**
 * A class to express the different routes a player can take
 * @author Aidas Venckunas (325464)
 *
 */

public final class Route {

    private final String id;
    private final Station station1;
    private final Station station2;
    private final int length;
    private final Level level;
    private final Color color;

    /**
     * enum for the different ground levels a route can have (eiter overground or underground)
     */
    public enum Level{

        OVERGROUND,
        UNDERGROUND;
    }

    /**
     * Route constructor
     * @param id
     * @param station1
     * @param station2
     * @param length
     * @param level
     * @param color
     * @throws IllegalArgumentException if both station1 and station2 are the same or if the route length doesnt
     * correspond to the bounds we set
     * @throws NullPointerException if either one of the two station's ID is null or the groud level's value is null
     */
    public Route(String id, Station station1, Station station2, int length, Level level, Color color){

        Preconditions.checkArgument(!station1.equals(station2));
        Preconditions.checkArgument(length > Constants.MAX_ROUTE_LENGTH);
        Preconditions.checkArgument(length < Constants.MIN_ROUTE_LENGTH);
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

    //getter for the route's ID
    public String id(){
        return id;
    }

    //getter for the first station
    public Station station1(){
        return station1;
    }

    //getter for the second station
    public Station station2(){
        return station2;
    }

    //getter for the route length
    public int length(){
        return length;
    }

    //getter for the route's ground level
    public Level level(){
        return level;
    }

    //getter for the route's color
    public Color color(){
        return color;
    }

    //getter for the pair of stations that makes up the route
    public List<Station> stations(){
        return List.of(station1, station2);
    }

    /**
     * this methods gives us the "opposite" station on a certain route . Ex: if a route A goes from Lausanne to
     * Bern and you call the method for A with Bern as a parameter, the method will give Lausanne back.
     * @param station
     * @return the opposite station on a give route
     * @throws IllegalArgumentException if the imput does not correspond to either station of the route
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
     * Methods that calculates all possible combinations of cards that can be used to claim a route
     * @return List of all possible combinations of cards to claim a route
     */
    public List<SortedBag<Card>> possibleClaimCards(){

        SortedBag.Builder <Card> possibilities = new SortedBag.Builder<Card>();

        switch (level) {

            case OVERGROUND:

                if(color != null){

                    possibilities.add(SortedBag.of(length, Card.of(color)));
                    SortedBag <Card> possibilities_built = possibilities.build();
                    return List.of(possibilities_built);
                }
                else{

                    for(Card b : Card.CARS){
                        possibilities.add(SortedBag.of(length, b));
                    }
                    SortedBag <Card> possibilities_built = possibilities.build();
                    return List.of(possibilities_built);
                }

            case UNDERGROUND:

                if(color != null){

                    for(int a = length; a > 0; a--){
                        possibilities.add(SortedBag.of(a, Card.of(color), length - a, Card.LOCOMOTIVE));
                    }
                    SortedBag <Card> possibilities_built = possibilities.build();
                    return List.of(possibilities_built);
                }
                else{

                    for(int a = 0; a < length; a++){

                        for(Card b : Card.CARS){
                            possibilities.add(SortedBag.of(length - a, b, a, Card.LOCOMOTIVE));
                        }
                    }
                    SortedBag <Card> possibilities_built = possibilities.build();
                    return List.of(possibilities_built);
                }


        }
        return null;
    }

    /**
     * Method that calculates the number of additionnal cards needed to claim a tunnel
     * @param claimCards
     * @param drawnCards
     * @return the number of additional claim cards needed to claim a tunnel
     * @throws
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
     * Method that returns the amount of points a player can get by claiming a route
     * @return nb of points after the claim
     */
    public int claimPoints(){
        return Constants.ROUTE_CLAIM_POINTS.get(length);
    }
}
