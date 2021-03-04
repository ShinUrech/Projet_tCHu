package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.ChMap;

import java.util.ArrayList;
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
    private static final Station BAD = new Station(0, "Baden");
    private static final Station BAL = new Station(1, "Bâle");
    private static final Station BEL = new Station(2, "Bellinzone");
    private static final Station BER = new Station(3, "Berne");
    private static final Station BRI = new Station(4, "Brigue");
    private static final Station BRU = new Station(5, "Brusio");
    private static final Station COI = new Station(6, "Coire");
    private static final Station DAV = new Station(7, "Davos");
    private static final Station DEL = new Station(8, "Delémont");
    private static final Station FRI = new Station(9, "Fribourg");
    private static final Station GEN = new Station(10, "Genève");
    private static final Station INT = new Station(11, "Interlaken");
    private static final Station KRE = new Station(12, "Kreuzlingen");
    private static final Station LAU = new Station(13, "Lausanne");
    private static final Station LCF = new Station(14, "La Chaux-de-Fonds");
    private static final Station LOC = new Station(15, "Locarno");
    private static final Station LUC = new Station(16, "Lucerne");
    private static final Station LUG = new Station(17, "Lugano");
    private static final Station MAR = new Station(18, "Martigny");
    private static final Station NEU = new Station(19, "Neuchâtel");
    private static final Station OLT = new Station(20, "Olten");
    private static final Station PFA = new Station(21, "Pfäffikon");
    private static final Station SAR = new Station(22, "Sargans");
    private static final Station SCE = new Station(23, "Schaffhouse");
    private static final Station SCZ = new Station(24, "Schwyz");
    private static final Station SIO = new Station(25, "Sion");
    private static final Station SOL = new Station(26, "Soleure");
    private static final Station STG = new Station(27, "Saint-Gall");
    private static final Station VAD = new Station(28, "Vaduz");
    private static final Station WAS = new Station(29, "Wassen");
    private static final Station WIN = new Station(30, "Winterthour");
    private static final Station YVE = new Station(31, "Yverdon");
    private static final Station ZOU = new Station(32, "Zoug");
    private static final Station ZUR = new Station(33, "Zürich");

    // Stations - countries
    private static final Station DE1 = new Station(34, "Allemagne");
    private static final Station DE2 = new Station(35, "Allemagne");
    private static final Station DE3 = new Station(36, "Allemagne");
    private static final Station DE4 = new Station(37, "Allemagne");
    private static final Station DE5 = new Station(38, "Allemagne");
    private static final Station AT1 = new Station(39, "Autriche");
    private static final Station AT2 = new Station(40, "Autriche");
    private static final Station AT3 = new Station(41, "Autriche");
    private static final Station IT1 = new Station(42, "Italie");
    private static final Station IT2 = new Station(43, "Italie");
    private static final Station IT3 = new Station(44, "Italie");
    private static final Station IT4 = new Station(45, "Italie");
    private static final Station IT5 = new Station(46, "Italie");
    private static final Station FR1 = new Station(47, "France");
    private static final Station FR2 = new Station(48, "France");
    private static final Station FR3 = new Station(49, "France");
    private static final Station FR4 = new Station(50, "France");


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
        Route route4 = new Route("AT1_STG_1", AT1, STG, 2, Route.Level.UNDERGROUND, null);//underground/null/ length 4

        //test 1
        List<SortedBag <Card>> route1_ = new ArrayList<SortedBag<Card>>();
        route1_.add(SortedBag.of(2, Card.VIOLET));

        for (SortedBag <Card> a : route1_){
            System.out.println(a);
        }

        System.out.println();

        for (SortedBag <Card> a : route1.possibleClaimCards()){
            System.out.println(a);
        }

        System.out.println();

        assertEquals(route1_, route1.possibleClaimCards());

        //test 2
        List<SortedBag <Card>> route2_ = new ArrayList<SortedBag<Card>>();
        route2_.add(SortedBag.of(4, Card.BLACK));
        route2_.add(SortedBag.of(4, Card.VIOLET));
        route2_.add(SortedBag.of(4, Card.BLUE));
        route2_.add(SortedBag.of(4, Card.GREEN));
        route2_.add(SortedBag.of(4, Card.YELLOW));
        route2_.add(SortedBag.of(4, Card.ORANGE));
        route2_.add(SortedBag.of(4, Card.RED));
        route2_.add(SortedBag.of(4, Card.WHITE));
        assertEquals(route2_, route2.possibleClaimCards());

        for (SortedBag <Card> a : route2_){
            System.out.println(a);
        }

        System.out.println();

        for (SortedBag <Card> a : route2.possibleClaimCards()){
            System.out.println(a);
        }
        System.out.println();


        //test 3
        List<SortedBag <Card>> route3_ = new ArrayList<SortedBag<Card>>();
        route3_.add(SortedBag.of(3, Card.RED));
        route3_.add(SortedBag.of(2, Card.RED, 1, Card.LOCOMOTIVE));
        route3_.add(SortedBag.of(1, Card.RED, 2, Card.LOCOMOTIVE));
        route3_.add(SortedBag.of(3, Card.LOCOMOTIVE));

        assertEquals(route3_, route3.possibleClaimCards());

        for (SortedBag <Card> a : route3_){
            System.out.println(a);
        }

        System.out.println();

        for (SortedBag <Card> a : route3.possibleClaimCards()){
            System.out.println(a);
        }

        System.out.println();

        //test 4

        List<SortedBag <Card>> route4_ = new ArrayList<SortedBag<Card>>();
        route4_.add(SortedBag.of(2, Card.BLACK));
        route4_.add(SortedBag.of(2, Card.VIOLET));
        route4_.add(SortedBag.of(2, Card.BLUE));
        route4_.add(SortedBag.of(2, Card.GREEN));
        route4_.add(SortedBag.of(2, Card.YELLOW));
        route4_.add(SortedBag.of(2, Card.ORANGE));
        route4_.add(SortedBag.of(2, Card.RED));
        route4_.add(SortedBag.of(2, Card.WHITE));
        route4_.add(SortedBag.of(1, Card.BLACK, 1, Card.LOCOMOTIVE));
        route4_.add(SortedBag.of(1, Card.VIOLET, 1, Card.LOCOMOTIVE));
        route4_.add(SortedBag.of(1, Card.BLUE, 1, Card.LOCOMOTIVE));
        route4_.add(SortedBag.of(1, Card.GREEN, 1, Card.LOCOMOTIVE));
        route4_.add(SortedBag.of(1, Card.YELLOW, 1, Card.LOCOMOTIVE));
        route4_.add(SortedBag.of(1, Card.ORANGE, 1, Card.LOCOMOTIVE));
        route4_.add(SortedBag.of(1, Card.RED, 1, Card.LOCOMOTIVE));
        route4_.add(SortedBag.of(1, Card.WHITE, 1, Card.LOCOMOTIVE));
        route4_.add(SortedBag.of(2, Card.LOCOMOTIVE));

        assertEquals(route4_, route4.possibleClaimCards());

        for (SortedBag <Card> a : route4_){
            System.out.println(a);
        }

        System.out.println();

        for (SortedBag <Card> a : route4.possibleClaimCards()){
            System.out.println(a);
        }

        System.out.println();
    }

    @Test
    void AdditionalClaimCardsErrorsOnInvalidInputs(){

        List<Route> AllRoutes = ChMap.routes();

        Route route1 = AllRoutes.get(3);//overground/not null/ violet length 2
        Route route3 = AllRoutes.get(2);//underground/not null/ length 3 red

        assertThrows(IllegalArgumentException.class, ()-> route1.additionalClaimCardsCount(SortedBag.of(3, Card.BLUE),
                SortedBag.of(3, Card.BLUE)));

        assertThrows(IllegalArgumentException.class, ()-> route3.additionalClaimCardsCount(SortedBag.of(3, Card.BLUE),
                SortedBag.of(2, Card.BLUE)));

    }

    @Test
    void AdditionalClaimCardsDifferentTypes(){

        List<Route> AllRoutes = ChMap.routes();

        Route route3 = AllRoutes.get(2);//underground/not null/ length 3 red
        Route route4 = new Route("AT1_STG_1", AT1, STG, 2, Route.Level.UNDERGROUND, null);

        assertEquals(3, route3.additionalClaimCardsCount(SortedBag.of(3, Card.BLUE),
                SortedBag.of(3, Card.BLUE)));

        assertEquals(0, route3.additionalClaimCardsCount(SortedBag.of(3, Card.BLUE),
                SortedBag.of(3, Card.RED)));

        assertEquals(3, route3.additionalClaimCardsCount(SortedBag.of(4, Card.BLUE),
                SortedBag.of(3, Card.BLUE)));

        assertEquals(3, route3.additionalClaimCardsCount(SortedBag.of(2, Card.BLUE, 1, Card.LOCOMOTIVE),
                SortedBag.of(3, Card.LOCOMOTIVE)));

        SortedBag.Builder <Card> drawnCards = new SortedBag.Builder<Card>();
        drawnCards.add(SortedBag.of(1, Card.BLUE, 1, Card.BLACK));
        drawnCards.add(SortedBag.of(1, Card.LOCOMOTIVE));
        SortedBag<Card> drawnCards_built = drawnCards.build();

        assertEquals(2, route3.additionalClaimCardsCount(SortedBag.of(1, Card.BLACK), drawnCards_built));

        SortedBag.Builder <Card> drawnCards2 = new SortedBag.Builder<Card>();
        drawnCards2.add(SortedBag.of(1, Card.BLUE, 1, Card.BLACK));
        drawnCards2.add(SortedBag.of(1, Card.LOCOMOTIVE));
        SortedBag<Card> drawnCards_built2 = drawnCards.build();

        assertEquals(1, route3.additionalClaimCardsCount(SortedBag.of(1, Card.LOCOMOTIVE), drawnCards_built2));

        assertEquals(2, route3.additionalClaimCardsCount(SortedBag.of(1, Card.BLUE),
                SortedBag.of(2, Card.BLUE, 1, Card.BLACK)));
    }

    @Test
    void ClaimPointsWorks(){

        List<Route> AllRoutes = ChMap.routes();
        Route route3 = AllRoutes.get(2);

        assertEquals(4, route3.claimPoints());
    }





}
