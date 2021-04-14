package ch.epfl.tchu.game;

import java.util.ArrayList;
import java.util.List;

/**
 * A class for Path(Trail) consisting of routes.
 *
 * @author Aidas Venckunas (325464)
 * @author Shin Urech (327245)
 */
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
     * A method that finds a longest path of the routes in player's possession.
     *
     * @param routes
     * routes in player's possession.
     *
     * returns a longest trail.
     * @return a longest trail
     */
    public static Trail longest(List<Route> routes){

        if(routes.isEmpty()){
            return new Trail(0, null, null, routes);
        }

        Trail longestTrail = new Trail(0, null, null, routes);
        int maxLength = 0;
        List<Trail> initialTrails = new ArrayList<Trail>();
        List<Trail> allTrails = new ArrayList<Trail>();

        for(Route a : routes){
            initialTrails.add(new Trail(a.length(), a.station1(), a.station2(), List.of(a)));
            initialTrails.add(new Trail(a.length(), a.station2(), a.station1(), List.of(a)));

            if(a.length() > maxLength){
                maxLength = a.length();
                longestTrail = new Trail(a.length(), a.station1(), a.station2(), List.of(a));
            }
        }

        while(!initialTrails.isEmpty()){

            allTrails.removeAll(allTrails);
            Station new_station1 = null;
            Station new_station2 = null;

            for(Trail a : initialTrails){

                //Finding which routes to add
                List<Route> routesToAdd = new ArrayList<Route>();
                for(Route b : routes){
                    if( !(a.routes.contains(b)) && (a.station2.equals(b.station1()) || a.station2.equals(b.station2()))){

                        routesToAdd.add(b);
                    }
                }

                //Adding these routes and defining new station1 and station2
                for(Route c : routesToAdd){

                    //adding
                    List<Route> addedRoutes = new ArrayList<Route>();
                    for(Route d : a.routes){
                        addedRoutes.add(d);
                    }
                    addedRoutes.add(c);

                    //defining
                    if(a.station2.equals(c.station1())){
                        new_station1 = a.station1;
                        new_station2 = c.station2();
                    }
                    else if(a.station2.equals(c.station2())){
                        new_station1 = a.station1;
                        new_station2 = c.station1();
                    }

                    allTrails.add(new Trail(a.length + c.length(), new_station1, new_station2, addedRoutes));

                    if (a.length + c.length() >= maxLength) {

                        maxLength = a.length + c.length();
                        longestTrail = new Trail(a.length + c.length(), new_station1, new_station2, addedRoutes);
                    }
                }
            }
            initialTrails.removeAll(initialTrails);
            initialTrails.addAll(allTrails);
        }
        return longestTrail;
    }

    /**
     * Returns a length of a trail.
     * @return a length of a trail
     */
    public int length(){
        return length;
    }

    /**
     * Returns the first station of a trail.
     * @return the first station of a trail
     */
    public Station station1(){
        return station1;
    }

    /**
     * Returns the last station of a trail.
     * @return the last station of a trail
     */
    public Station station2(){
        return station2;
    }

    /**
     * Overrides a method toString to make a textual representation of a trail.
     *
     * @return a textual representation of a trail
     */
    @Override
    public String toString(){

        String finalText = "";

        if(length == 0 && station1 == null && station2 == null){
            return "empty";
        }
        else{
            List <String> stationsOfJourney = new ArrayList<String>();

            if(routes.size() == 1){
                stationsOfJourney.add(routes.get(0).station1().toString());
                stationsOfJourney.add(routes.get(0).station2().toString());
            }

            else {
                if (routes.get(0).station1() == routes.get(1).station1()) {
                    stationsOfJourney.add(routes.get(0).station2().toString());
                    stationsOfJourney.add(routes.get(0).station1().toString());
                } else {
                    stationsOfJourney.add(routes.get(0).station1().toString());
                    stationsOfJourney.add(routes.get(0).station2().toString());
                }

                for (int a = 1; a < routes.size(); a++) {
                    if (routes.get(a).station1() == routes.get(a - 1).station1()) {
                        stationsOfJourney.add(routes.get(a).station2().toString());
                    } else if (routes.get(a).station1() == routes.get(a - 1).station2()) {
                        stationsOfJourney.add(routes.get(a).station2().toString());
                    } else if (routes.get(a).station2() == routes.get(a - 1).station1()) {
                        stationsOfJourney.add(routes.get(a).station1().toString());
                    } else if (routes.get(a).station2() == routes.get(a - 1).station2()) {
                        stationsOfJourney.add(routes.get(a).station1().toString());
                    }
                }
            }
            finalText = String.join(" - ", stationsOfJourney);
            finalText += " (" + length + ")";
            return finalText;
        }
    }
}
