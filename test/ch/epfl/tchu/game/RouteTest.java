package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.ChMap;

/**
 * Test class for the Route class
 * @author Shin Urech (327245)
 */

public class RouteTest {


    //fictive station made for the Tests
    Station LOZ = new Station(0, "LOZ");
    Station LOZANGELES = new Station(1,"LOZ2");
    Station CHAILLY = new Station(1012, "CHA");


    @Test
    /**
     *Test if the constructor rejects two same stations as parameters for s1 and s2.
     */
    void IsTheSameStation(){
         Route TEST = new Route("LOZ_LOZ", LOZ, LOZ, 1, Route.Level.OVERGROUND, null);
    }


    @Test
    /**
     *Tests when the station argument is null
     */
    void StationIsNull(){
        Route Test1 = new Route("NULL_LOZ", null, LOZ, 1,Route.Level.OVERGROUND, null);
        Route Test2 = new Route("LOZ_NULL", LOZ, null,1,Route.Level.OVERGROUND,null);
    }

    @Test
    /**
     * Test for the route's length boundaries we set
     */
    void LengthIsOutOfBounds(){
        Route Test1 = new Route("LOZ_LOZANGELES", LOZ,LOZANGELES ,0, Route.Level.OVERGROUND, null);
        Route Test2 = new Route("LOZ_LOZANGELES_3", LOZ,LOZANGELES ,-2, Route.Level.OVERGROUND, null);
    }


    /*
    @Test

    void HasTheSameId(){
        Route Test1 = new Route("LOZ_LOZANGELES", LOZ,LOZANGELES ,0, Route.Level.OVERGROUND, null);
        Route Test2 = new Route("LOZ_LOZANGELES", CHAILLY,LOZANGELES ,0, Route.Level.OVERGROUND, null);
    }
     */
    @Test
    /**
     * Test for the stationOpposite() method
     */
    void IsNotTheOppositeStation(){
        Route Test = new Route("LOZ_LOZANGELES", LOZ,LOZANGELES ,0, Route.Level.OVERGROUND, null);
        Test.stationOpposite(CHAILLY);
    }






}
