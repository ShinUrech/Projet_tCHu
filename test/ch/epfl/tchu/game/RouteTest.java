package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.ChMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    void ConstructorFailsForTheSameStations(){

        assertThrows(IllegalArgumentException.class,() -> new Route("LOZ_LOZ", LOZ, LOZ, 1,
                Route.Level.OVERGROUND, null));
         //Route TEST = new Route("LOZ_LOZ", LOZ, LOZ, 1, Route.Level.OVERGROUND, null);
    }


    @Test
    /**
     *Tests when the station argument is null
     */
    void ConstructorFails_Station_Level_ID_IsNull(){
        assertThrows(NullPointerException.class,() -> new Route("LOZ_LOZ", null, LOZ, 2,
                Route.Level.OVERGROUND, null));
        assertThrows(NullPointerException.class,() -> new Route("LOZ_LOZ", LOZ, null, 2,
                Route.Level.OVERGROUND, null));
        assertThrows(NullPointerException.class,() -> new Route("LOZ_LOZ", null, null, 2,
                Route.Level.OVERGROUND, null));
        assertThrows(NullPointerException.class,() -> new Route(null, LOZ, LOZANGELES, 2,
                Route.Level.OVERGROUND, null));
        assertThrows(NullPointerException.class,() -> new Route("LOZ_LOZ", LOZ, LOZANGELES, 2,
                null, null));
        //Route Test1 = new Route("NULL_LOZ", null, LOZ, 1,Route.Level.OVERGROUND, null);
        //Route Test2 = new Route("LOZ_NULL", LOZ, null,1,Route.Level.OVERGROUND,null);
    }

    @Test
    /**
     * Test for the route's length boundaries we set
     */
    void ConstructorFailsLengthIsOutOfBounds(){
        assertThrows(IllegalArgumentException.class, () -> new Route("LOZ_LOZANGELES", LOZ,LOZANGELES ,0,
                Route.Level.OVERGROUND, null));
        assertThrows(IllegalArgumentException.class, () -> new Route("LOZ_LOZANGELES", LOZ,LOZANGELES ,7,
                Route.Level.OVERGROUND, null));
        //Route Test1 = new Route("LOZ_LOZANGELES", LOZ,LOZANGELES ,0, Route.Level.OVERGROUND, null);
        //Route Test2 = new Route("LOZ_LOZANGELES_3", LOZ,LOZANGELES ,-2, Route.Level.OVERGROUND, null);
    }

    @Test
    /**
     * Test for the stationOpposite() method
     */
    void IsNotTheOppositeStation(){
        Route Test = new Route("LOZ_LOZANGELES", LOZ,LOZANGELES ,1, Route.Level.OVERGROUND, null);
        //Test.stationOpposite(CHAILLY);
        assertThrows(IllegalArgumentException.class, () -> Test.stationOpposite(CHAILLY));
    }

    @Test
    void IsTheOppositeStation(){
        Route Test = new Route("LOZ_LOZANGELES", LOZ,LOZANGELES ,1, Route.Level.OVERGROUND, null);
        //Test.stationOpposite(CHAILLY);
        assertEquals(LOZANGELES, Test.stationOpposite(LOZ));
    }

    @Test
    /**
     * BLACK(Color.BLACK),
     *     VIOLET(Color.VIOLET),
     *     BLUE(Color.BLUE),
     *     GREEN(Color.GREEN),
     *     YELLOW(Color.YELLOW),
     *     ORANGE(Color.ORANGE),
     *     RED(Color.RED),
     *     WHITE(Color.WHITE),
     *     LOCOMOTIVE(null);
     */
    void possibleClaimCardsWorksFor4cases(){
        //You can use the list of all routes provided by creating this list and extracting
        //needed routes from it
        //Go to ChMap and find a corresponding route for every of 4 cases (1. where route is OVERGROUND and colored
        //and so on..
        List<Route> AllRoutes = ChMap.routes();

        //Route1 should correspond for case 1, route2 - case 2...
        Route route1 = AllRoutes.get(3);//overground/not null/ violet length 2
        Route route2 = AllRoutes.get(16);//overground/null/ length 4
        Route route3 = AllRoutes.get(2);//underground/not null/ length 3 red
        Route route4 = AllRoutes.get(0);//underground/null/ length 4

        //test 1
        SortedBag.Builder <Card> route1_ = new SortedBag.Builder<Card>();
        route1_.add(SortedBag.of(2, Card.VIOLET));
        List <SortedBag <Card>> route1_answer = List.of(route1_.build());

        for (SortedBag <Card> a : route1_answer){
            System.out.println(a);
        }

        for (SortedBag <Card> a : route1.possibleClaimCards()){
            System.out.println(a);
        }

        assertEquals(route1_answer, route1.possibleClaimCards());

        //test 2
        SortedBag.Builder <Card> route2_ = new SortedBag.Builder<Card>();
        route2_.add(SortedBag.of(4, Card.BLACK));
        route2_.add(SortedBag.of(4, Card.VIOLET));
        route2_.add(SortedBag.of(4, Card.BLUE));
        route2_.add(SortedBag.of(4, Card.GREEN));
        route2_.add(SortedBag.of(4, Card.YELLOW));
        route2_.add(SortedBag.of(4, Card.ORANGE));
        route2_.add(SortedBag.of(4, Card.RED));
        route2_.add(SortedBag.of(4, Card.WHITE));
        List <SortedBag <Card>> route2_answer = List.of(route2_.build());
        assertEquals(route2_answer, route2.possibleClaimCards());

        for (SortedBag <Card> a : route2_answer){
            System.out.println(a);
        }

        for (SortedBag <Card> a : route2.possibleClaimCards()){
            System.out.println(a);
        }


        //test 3
        SortedBag.Builder <Card> route3_ = new SortedBag.Builder<Card>();
        route3_.add(SortedBag.of(3, Card.RED));
        route3_.add(SortedBag.of(2, Card.RED, 1, Card.LOCOMOTIVE));
        route3_.add(SortedBag.of(1, Card.RED, 2, Card.LOCOMOTIVE));
        route3_.add(SortedBag.of(3, Card.LOCOMOTIVE));

        List <SortedBag <Card>> route3_answer = List.of(route3_.build());
        assertEquals(route3_answer, route3.possibleClaimCards());





    }





}
