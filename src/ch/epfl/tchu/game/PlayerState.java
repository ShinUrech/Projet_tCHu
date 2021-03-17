package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;

public final class PlayerState extends PublicPlayerState{

    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;
    private final List<Route> routes;

    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes){
        super(tickets.size(), cards.size(), routes);

        this.tickets = tickets;
        this.cards = cards;
        this.routes = routes;
    }

    public static PlayerState initial(SortedBag<Card> initialCards){
        return new PlayerState(SortedBag.of(), initialCards, List.of());
    }

    public SortedBag<Ticket> tickets(){
        return tickets;
    }

    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets){

        SortedBag.Builder<Ticket> builder = new SortedBag.Builder<>();
        builder.add(tickets);
        builder.add(newTickets);

        return new PlayerState(builder.build(), cards, routes);
    }

    public SortedBag<Card> cards(){
        return cards;
    }

    public PlayerState withAddedCard(Card card){

        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
        builder.add(cards);
        builder.add(card);

        return new PlayerState(tickets, builder.build(), routes);
    }

    public PlayerState withAddedCards(SortedBag<Card> additionalCards){

        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
        builder.add(cards);
        builder.add(additionalCards);

        return new PlayerState(tickets, builder.build(), routes);
    }

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

}
