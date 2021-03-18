package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A class that represents a PlayerState that is not visible publicly.
 *
 * @author Aidas Venckunas(325464)
 */
public final class PlayerState extends PublicPlayerState{

    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;
    private final List<Route> routes;

    /**
     * Constructor that initializes a Player State with the following parameters:
     *
     * @param tickets the tickets that a player has
     * @param cards the cards that a player has
     * @param routes routes that player possesses
     */
    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes){
        super(tickets.size(), cards.size(), routes);

        this.tickets = tickets;
        this.cards = cards;
        this.routes = routes;
    }

    /**
     * A static method that makes an initial Player State at the very start of the game.
     *
     * @param initialCards initial cards that a player has
     *
     * @return a Player State with no tickets or routes, only initial cards
     */
    public static PlayerState initial(SortedBag<Card> initialCards){
        return new PlayerState(SortedBag.of(), initialCards, List.of());
    }

    /**
     * A method that returns all tickets that a player has.
     * @return all tickets that a player has
     */
    public SortedBag<Ticket> tickets(){
        return tickets;
    }

    /**
     * A method that returns a new Player State with more tickets.
     *
     * @param newTickets tickets that need to be added
     *
     * @return a new Player State that is the same as a previous one, except with more tickets
     */
    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets){

        SortedBag.Builder<Ticket> builder = new SortedBag.Builder<>();
        builder.add(tickets);
        builder.add(newTickets);

        return new PlayerState(builder.build(), cards, routes);
    }

    /**
     * Returns all the cards that a player has.
     * @return all the cards that a player has
     */
    public SortedBag<Card> cards(){
        return cards;
    }

    /**
     * A method that returns a new Player State with one more card.
     *
     * @param card a card that needs to be added
     *
     * @return a new Player State that is the same as a previous one, except with one more card
     */
    public PlayerState withAddedCard(Card card){

        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
        builder.add(cards);
        builder.add(card);

        return new PlayerState(tickets, builder.build(), routes);
    }

    /**
     * A method that returns a new Player State with more cards.
     *
     * @param additionalCards cards that need to be added
     *
     * @return a new Player State that is the same as a previous one, except with more cards
     */
    public PlayerState withAddedCards(SortedBag<Card> additionalCards){

        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
        builder.add(cards);
        builder.add(additionalCards);

        return new PlayerState(tickets, builder.build(), routes);
    }

    /**
     * A method that identifies whether a player can claim a given route with cards in his possession.
     *
     * @param route a given route
     *
     * @return true if and only if a player has needed cards to claim a route
     */
    public boolean canClaimRoute(Route route){

        boolean answer = false;

        if(carCount() < route.length()){
            return false;
        }
        else{
            List<SortedBag<Card>> availablePossibilities = possibleClaimCards(route);

            for (SortedBag<Card> a : route.possibleClaimCards()){
                if(availablePossibilities.contains(a)){
                    answer = true;
                }
            }
        }
        return answer;
    }

    /**
     * A method that makes a list of all possible combinations of cards in possession that a player can use
     * to claim a given route.
     *
     * @param route a given route
     * @throws IllegalArgumentException a player has less cars left than a route length requires to
     *
     * @return a list of all possible combinations of cards.
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route){

        Preconditions.checkArgument(carCount() >= route.length());

        List<SortedBag<Card>> possibilities = new ArrayList<SortedBag<Card>>();

        switch (route.level()) {

            case OVERGROUND:

                if(route.color() != null){

                    if(cards.contains(SortedBag.of(route.length(), Card.of(route.color()))))
                        possibilities.add(SortedBag.of(route.length(), Card.of(route.color())));
                    return possibilities;
                }
                else{

                    for(Card b : Card.CARS){
                        if(cards.contains(SortedBag.of(route.length(), b)))
                            possibilities.add(SortedBag.of(route.length(), b));
                    }

                    return possibilities;
                }

            case UNDERGROUND:

                if(route.color() != null){

                    for(int a = route.length(); a >= 0; a--){
                        if(cards.contains(SortedBag.of(a, Card.of(route.color()),
                                route.length() - a, Card.LOCOMOTIVE)))
                            possibilities.add(SortedBag.of(a, Card.of(route.color()),
                                    route.length() - a, Card.LOCOMOTIVE));
                    }
                    return possibilities;
                }
                else{

                    for(int a = 0; a < route.length(); a++){

                        for(Card b : Card.CARS){
                            if(cards.contains(SortedBag.of(route.length() - a, b, a, Card.LOCOMOTIVE)))
                                possibilities.add(SortedBag.of(route.length() - a, b, a, Card.LOCOMOTIVE));
                        }
                    }

                    if(cards.contains(SortedBag.of(route.length(), Card.LOCOMOTIVE)))
                        possibilities.add(SortedBag.of(route.length(), Card.LOCOMOTIVE));

                    return possibilities;
                }
        }
        return null;
    }

    /**
     * A method that makes a list of all possible combinations of additional cards that a player can use to claim
     * an underground route, given:
     *
     * @param additionalCardsCount a number of additional cards required
     * @throws IllegalArgumentException if the number is less than one or bigger than three
     * @param initialCards initially used player's cards
     * @throws IllegalArgumentException if there are no initially used cards or there are more than two suits
     * of cards used initially
     * @param drawnCards three cards that have been drawn
     * @throws IllegalArgumentException if the number of drawn cards is not equal to three
     *
     * @return a list of all possible combinations of additional cards to use for a player
     */
    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards, SortedBag<Card> drawnCards){

        Preconditions.checkArgument(additionalCardsCount >=1 && additionalCardsCount <=3);
        Preconditions.checkArgument(!initialCards.isEmpty() && initialCards.toSet().size() <= 2);
        Preconditions.checkArgument(drawnCards.size() == Constants.ADDITIONAL_TUNNEL_CARDS);

        SortedBag<Card> availableCards = cards.difference(initialCards);
        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
        builder.add(availableCards.countOf(Card.LOCOMOTIVE), Card.LOCOMOTIVE);

        if(initialCards.toSet().size() == 2){
            builder.add(availableCards.countOf(initialCards.get(0)), initialCards.get(0));
        }
        availableCards = builder.build();

        List <SortedBag <Card>> options = new ArrayList<>(availableCards.subsetsOfSize(additionalCardsCount));
        options.sort (
                Comparator.comparingInt (cs -> cs.countOf (Card.LOCOMOTIVE)));

        return options;
    }

    /**
     * A method that adds a given route to the list of players' routes.
     *
     * @param route a given route
     * @param claimCards cards to discard when a route was claimed
     *
     * @return the same Player State, except that there is one more route in possession and all used cards
     * are removed from players' list of cards
     */
    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards){

        List<Route> newRoutes = routes;
        newRoutes.add(route);

        SortedBag<Card> newCards = cards.difference(claimCards);

        return new PlayerState(tickets, newCards, newRoutes);
    }

    /**
     * A method that counts a number of points received by tickets of a player.
     *
     * @return a number of gained or lost points
     */
    public int ticketPoints(){

        int points = 0;

        int maxID = 0;

        for (Route a : routes){
            if(a.station1().id() > maxID){
                maxID = a.station1().id();
            }
            if(a.station2().id() > maxID){
                maxID = a.station2().id();
            }
        }
        ++maxID;

        StationPartition.Builder builder = new StationPartition.Builder(maxID);

        for(int a = 0; a < routes.size(); a++){
            for (int b = a; b < routes.size(); b++){
                if(routes.get(a).station1().equals(routes.get(b).station1())){
                    builder.connect(routes.get(a).station1(), routes.get(b).station1());
                }
                else if(routes.get(a).station1() == routes.get(b).station2()){
                    builder.connect(routes.get(a).station1(), routes.get(b).station2());
                }
                else if(routes.get(a).station2() == routes.get(b).station1()){
                    builder.connect(routes.get(a).station2(), routes.get(b).station1());
                }
                else if(routes.get(a).station2() == routes.get(b).station2()){
                    builder.connect(routes.get(a).station2(), routes.get(b).station2());
                }
            }
        }

        StationPartition partition = builder.build();

        for (Ticket a : tickets){
            points += a.points(partition);
        }

        return points;
    }

    /**
     * A method that returns players' final points.
     * @return points given by claimed routes added with points given by tickets.
     */
    public int finalPoints(){
        return claimPoints() + ticketPoints();
    }
}
