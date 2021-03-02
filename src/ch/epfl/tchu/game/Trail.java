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


        List<Trail> initialTrails = new ArrayList<Trail>();

        for(Route a : routes){
            initialTrails.add(new Trail(a.length(), a.station1(), a.station2(), List.of(a)));
            initialTrails.add(new Trail(a.length(), a.station2(), a.station1(), List.of(a)));
        }
        while(!initialTrails.isEmpty()) {

            List<Trail> allTrails = new ArrayList<Trail>();

            for (Trail a : initialTrails) {

                for (Route b : routes) {

                    if ((a.station1.equals(b.station1()) || a.station2.equals(b.station1())) && !a.routes.contains(b)) {
                        //allTrails.add(new Trail(a.length + b.length(), a.station))
                    }
                }
            }
        }
    }
}
