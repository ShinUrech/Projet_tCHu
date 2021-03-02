package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

public final class Trail {

    private final int length;
    private final Station station1;
    private final Station station2;
    private final List<Route> routes;

    private Trail(int length, Station station1, Station station2, List<Route> routes){
        this.length = length;
        this.station1 = station1;
        this.station2 = station2;
        this.routes = routes;
    }

    /**
     * cs = list of paths made up of a single route
     * as long as cs is not empty:
     *   cs' = empty list
     *   for any path c of cs:
     *     rs = (routes belonging to the player,
     *           not belonging to c, and which
     *           can extend c)
     *     for any route r of rs:
     *       add c extended from r to cs '
     *   cs = cs'
     */
    public static Trail longest(List<Route> routes){

        if(routes.isEmpty()){
            return new Trail(0, null, null, null);
        }

        Trail longestTrail = null;
        int maxLength = 0;
        List<Trail> initialTrails = new ArrayList<Trail>();

        for(Route a : routes){
            initialTrails.add(new Trail(a.length(), a.station1(), a.station2(), List.of(a)));
            initialTrails.add(new Trail(a.length(), a.station2(), a.station1(), List.of(a)));
        }

        while(!initialTrails.isEmpty()) {

            List<Trail> allTrails = new ArrayList<Trail>();

            for (Trail a : initialTrails) {

                for (Route b : routes) {

                    if (a.station2.equals(b.station1()) && !a.routes.contains(b)) {

                        List<Route> addedRoutesInTrail = new ArrayList<Route>();

                        for (Route c : a.routes){
                            addedRoutesInTrail.add(c);
                        }
                        addedRoutesInTrail.add(b);

                        allTrails.add(new Trail(a.length + b.length(), a.station1,
                                b.station2(), addedRoutesInTrail));

                        if(a.length + b.length() > maxLength){

                            maxLength = a.length + b.length();
                            longestTrail = new Trail(maxLength, a.station1,
                                    b.station2(), addedRoutesInTrail);
                        }
                    }
                }
            }
            initialTrails = allTrails;
            allTrails.clear();
        }
        return longestTrail;
    }

    public int length(){
        return length;
    }

    public Station station1(){
        return station1;
    }

    public Station station2(){
        return station2;
    }

    @Override
    public String toString(){

        String finalText = "";

        if(length == 0 && station1.equals(null) && station2.equals(null)){
            return "";
        }
        else{
            List <String> stationsOfJourney = new ArrayList<String>();
            for (Route a : routes){
                stationsOfJourney.add(a.station1().toString());
            }
            stationsOfJourney.add(routes.get(routes.size()-1).station2().toString());

            finalText = String.join(" - ", stationsOfJourney);
            finalText += " (" + length + ")";
            return finalText;
        }
    }
}
