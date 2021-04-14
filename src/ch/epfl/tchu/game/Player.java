package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.Map;

/**
 * A class that represents a Player and its' behavior.
 *
 * @author Aidas Venckunas (325464)
 * @author Shin Urech (327245)
 */
public interface Player {

    /**
     * Enumerator for 3 different types of turns.
     */
    enum TurnKind {

        DRAW_TICKETS,
        DRAW_CARDS,
        CLAIM_ROUTE;

        /**
         * A list of all types of turns.
         */
        public static List<TurnKind> ALL = List.of(TurnKind.values());
    }

    /**
     * This method is used to communicate to the players their own Ids as well as the other players names.
     *
     * @param ownId the player's Id
     * @param playerNames the players' names
     */
    public abstract void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    /**
     * This method communicates whatever information to a player.
     *
     * @param info the information that needs to be communicated
     */
    public abstract void receiveInfo(String info);

    /**
     * This method gives game update information to the player.
     *
     * @param newState the new PublicGameState
     * @param ownState the new PlayerState of a given player
     */
    public abstract void updateState(PublicGameState newState, PlayerState ownState);

    /**
     * This method tells the player which tickets were drawn from the ticket deck at the start of a game.
     *
     * @param tickets are the drawn tickets
     */
    public abstract void setInitialTicketChoice(SortedBag<Ticket> tickets);

    /**
     * This method tells the player which tickets he chose to keep from the initial set of 5 tickets he got.
     *
     * @return the tickets he chose
     */
    public abstract SortedBag<Ticket> chooseInitialTickets();

    /**
     * This method is called when a player is about to play and he needs to decide which action he will take.
     */
    public abstract TurnKind nextTurn();

    /**
     * This method is called whenever a player decides to pick a ticket and it gives the player all options he has
     * to pick a ticket.
     *
     * @param options the tickets that can be picked by the player
     *
     * @return choice
     */
    public abstract SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);

    /**
     * This method is called whenever a player wants to pick a card and needs to decide from which slot
     * he wants to pick the card.
     *
     * @return a value from 0 to 4 (included) if the card is picked from the face-up Cards and Constants.DECK_SLOT
     * if the player chose to pick from the deck
     */
    public abstract int drawSlot();

    /**
     * This method is used to give the Id of a road whenever a player is trying to claim one.
     *
     * @return the route that is being claimed
     */
    public abstract Route claimedRoute();

    /**
     * This method is used to see what cards a player is going to use to claim a route.
     *
     * @return the cards he is going to use to try to claim a road
     */
    public abstract SortedBag<Card> initialClaimCards();

    /**
     * This method is used whenever a player is trying to claim a tunnel and needs to choose which
     * additional card he is going to use.
     *
     * @param options the options available to claim the tunnel
     *
     * @return the cards picked by the player to claim the tunnel
     */
    public abstract SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);
}
