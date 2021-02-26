package ch.epl.tchu.game;

import ch.epl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A class to represent a trip between the departure and
 * arrival station.
 *
 * @author Aidas Venckunas (325464)
 * @author Shin Urech (327245)
 *
 */
public final class Trip {

    private final Station from;
    private final Station to;
    private final int points;

    /**
     * Constructs a Trip (journey) with its' following characterization.
     *
     * @param from
     * A station of departure.
     * @param to
     * A station of arrival.
     * @throws NullPointerException if one of the two stations is empty(null)
     *
     * @param points
     * Points given for a trip between these two Stations.
     * @throws IllegalArgumentException if the number of points is not strictly positive
     */
    public Trip(Station from, Station to, int points){

        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);

        Preconditions.checkArgument(points > 0);
        this.points = points;

    }

    /**
     * A method list of all the possible journeys going from one of the
     * stations of the first list (from) to one of the stations of the second list (to)
     * each worth a given number of points.
     *
     * @param from
     * A list of departure stations.
     * @param to
     * a list of arrival stations.
     * @param points
     *
     * @throws IllegalArgumentException if one of the lists is empty, or if the number of points
     * is not strictly positive.
     *
     * Returns a list of all possible trips.
     * @return a List of all trips.
     */
    public static List<Trip> all(List<Station> from, List<Station> to, int points){

        List<Trip> allTrips = new ArrayList<Trip>();
        Preconditions.checkArgument(!from.isEmpty() && !to.isEmpty() && points > 0);

        for(Station a : from){
            for(Station b : to){
                allTrips.add(new Trip(a, b, points));
            }
        }

        return allTrips;

    }

    /**
     * Returns a departure station.
     * @return a departure station
     */
    public Station from(){
        return from;
    }

    /**
     * Returns the arrival station.
     * @return the arrival station
     */
    public Station to(){
        return to;
    }

    /**
     * Returns a number of points given for a trip.
     * @return a number of points given for a trip
     */
    public int points(){
        return points;
    }

    /**
     * a method to evaluate the connectivity of a Trip and
     * give a certain number of points for it.
     *
     * @param connectivity
     * connectivity between two stations.
     *
     * Returns the number of points in the path for the given connectivity.
     * @return the number of points in the path for the given connectivity
     */
    public int points(StationConnectivity connectivity){

        if(connectivity.connected(from, to)){
            return points;
        }
        else return points * -1;

    }

}
