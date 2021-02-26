package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;
import java.util.TreeSet;

/**
 * A class to represent a ticket.
 *
 * @author Aidas Venckunas (325464)
 */
public final class Ticket implements Comparable<Ticket> {

    private final String text;
    private final List<Trip> trips;

    /**
     * Constructs a ticket according to a list of given trips.
     *
     * @param trips
     * List of trips.
     * @throws IllegalArgumentException if the list of trips is empty
     * or if the departure station is not the same over the list
     */
    public Ticket(List<Trip> trips){

        Preconditions.checkArgument(!trips.isEmpty());
        for(int a = 0; a < trips.size(); a++){
            if(trips.get(0).from() != trips.get(a).from()){
                throw new IllegalArgumentException();
            }
        }

        this.trips = trips;
        this.text = computeText(trips);
    }

    /**
     * Constructs a list of trips if there is only one trip in a list.
     *
     * @param from
     * The departure station.
     * @param to
     * The arrival station.
     * @param points
     * A given number of points for a trip.
     */
    public Ticket(Station from, Station to, int points){
        this(List.of(new Trip(from, to, points)));
    }

    /**
     * Returns a textual representation of a ticket.
     * @return Returns a textual representation of a ticket.
     */
    public String text(){
        return text;
    }

    //Bern - {Germany (6), Austria (11), France (5), Italy (8)}
    private static String computeText(List<Trip> trips){

        TreeSet<String> allStations = new TreeSet<String>();
        String finalText = "";

        for (Trip a :trips){
            allStations.add(a.to().toString() + String.format(" (%s)", a.points()));
        }

        finalText += String.join(", ", allStations);

        if(allStations.size() != 1){
            finalText = "{" + finalText + "}";
        }
        finalText = trips.get(0).from().toString() + " - " + finalText;

        return finalText;
    }

    /**
     * A method that evaluates the number of points for a ticket given the connectivity
     * of a player.
     *
     * @param connectivity
     * connectivity of a player.
     * @return the number of calculated points.
     */
    public int points(StationConnectivity connectivity){

        int points = 0;
        int incompleteTrips = 0;
        int minLostPoints = -10000;

        for (Trip a : trips){
            if(a.points(connectivity) < 0){
                incompleteTrips++;
                if(minLostPoints < a.points(connectivity)){
                    minLostPoints = a.points(connectivity);
                }
            }
            else if(a.points(connectivity) > points){
                points = a.points(connectivity);
            }
        }

        if(incompleteTrips == trips.size()){
            points = minLostPoints;
        }

        return points;
    }

    /**
     * a method that compares the textual length of two tickets.
     *
     * @param that
     * a ticket that we want our ticket to be compared to.
     *
     * @return a strictly negative integer if (this) is strictly less than (that), a strictly
     * positive integer if (this) is strictly greater than (that), and zero if the two are equal.
     */
    public int compareTo(Ticket that){
        return this.text.compareTo(that.text);
    }

    /**
     *  Overrides the toString method so that it
     *  returns the textual representation of a ticket.
     * @return the textual representation of a ticket
     */
    @Override
    public String toString(){
        return text;
    }


}
