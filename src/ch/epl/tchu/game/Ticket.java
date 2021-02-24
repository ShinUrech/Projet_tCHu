package ch.epl.tchu.game;

import ch.epl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public final class Ticket implements Comparable<Ticket> {

    private final String text;
    private final List<Trip> trips;

    public Ticket(List<Trip> trips){

        Preconditions.checkArgument(trips != null);
        for(Trip a : trips){
            if(a.from() != trips.get(0).from()){
                throw new IllegalArgumentException();
            }
        }

        this.trips = trips;
        this.text = computeText(trips);
    }

    public Ticket(Station from, Station to, int points){
        this(List.of(new Trip(from, to, points)));
    }

    public String text(){
        return text;
    }

    //Bern - {Germany (6), Austria (11), France (5), Italy (8)}
    private static String computeText(List<Trip> trips){

        TreeSet<String> allStations = new TreeSet<String>();
        String finalText = trips.get(0).from().toString();

        for (Trip a :trips){
            allStations.add(a.to().toString() + String.format(" (% s)", a.points()));
        }

        finalText += String.join(",", allStations);

        if(allStations.size() != 1){
            finalText = "{" + finalText + "}";
        }

        return finalText;
    }

    //https://piazza.com/class/kl7qpcvl86d619?cid=85
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

    public int compareTo(Ticket that){
        return this.text.compareTo(that.text);
    }

    @Override
    public String toString(){
        return text;
    }


}
