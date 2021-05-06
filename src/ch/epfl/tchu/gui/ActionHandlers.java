package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;

/**
 * @author Shin Urech(327245)
 *
 * this interface contains 5 functionnal interfaces that contain the game's gui's handlers methods
 */

public interface ActionHandlers {

    /**
     * this func. interface containes a method that is called whenever a player wants to draw tickets
     */
    interface DrawTicketsHandler{ abstract void onDrawTickets();}

    /**
     * this func. interface contains a method that is called whenever a player wants to draw a card and needs to indicate
     * his choice to the game
     */
    //note to myself: preconditions are int from 0 to 4 or -1
    interface DrawCardHandler{ abstract void onDrawCard(int index);}

    /**
     * this func. interface contains a method that is called whenever a player wants to claim a route with a certain set
     * of cards.
     */
    interface ClaimRouteHandler{ abstract void onClaimRoute(Route toBeClaimed, SortedBag<Card> claimCards);}

    /**
     * this func. interface contains a method that is called whenever a player draws tickets and needs to indicate his
     * tickets choice to the game
     */
    interface ChooseTicketsHandler{ abstract void onChooseTickets(SortedBag<Ticket> keptTickets);}

    /**
     * this func. interface contains a method that is called whenever a player is trying to claim a tunnel and needs to
     * indicate his choice to the game.
     */
    interface ChooseCardsHandler{ abstract void onChooseCards(SortedBag<Card> tunnelClaimCards);}

}
