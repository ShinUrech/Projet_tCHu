package ch.epl.tchu.game;

import ch.epl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Trip {

    private final Station from;
    private final Station to;
    private final int points;

    public Trip(Station from, Station to, int points){

        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);

        Preconditions.checkArgument(points > 0);
        this.points = points;

    }

    public static List<Trip> all(List<Station> from, List<Station> to, int points){

        List<Trip> allTrips = new ArrayList<Trip>();
        Preconditions.checkArgument(from != null && to != null && points > 0);

        for(Station a : from){
            for(Station b : to){
                allTrips.add(new Trip(a, b, points));
            }
        }

        return allTrips;

    }

    public Station from(){
        return from;
    }

    public Station to(){
        return to;
    }

    public int points(){
        return points;
    }

    public int points(StationConnectivity connectivity){

        if(connectivity.connected(from, to)){
            return points;
        }
        else return points * -1;

    }

}
