package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.List;

public final class Route {

    private final String id;
    private final Station station1;
    private final Station station2;
    private final int length;
    private final Level level;
    private final Color color;

    public enum Level{

        OVERGROUND,
        UNDERGROUND;
    }

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

    public String id(){
        return id;
    }

    public Station station1(){
        return station1;
    }

    public Station station2(){
        return station2;
    }

    public int length(){
        return length;
    }

    public Level level(){
        return level;
    }

    public Color color(){
        return color;
    }

    public List<Station> stations(){
        return List.of(station1, station2);
    }

    public Station stationOpposite(Station station) {

        if (!station.equals(station1) && !station.equals(station2)) {
            throw new IllegalArgumentException();
        }
        else if (!station.equals(station1)) {
            return station1;
        }
        else return station2;

    }

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

    public int claimPoints(){
        return Constants.ROUTE_CLAIM_POINTS.get(length);
    }
}
