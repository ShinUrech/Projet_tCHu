package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Color;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Trail;
import java.lang.Enum;

import java.util.List;

/**
 * @author Shin Urech (327245)
 * this class is used to creat all the textual content that will be implemented into the game later on
 */

public final class Info {

    private final String playername;

    /**
     * this public constructor is used to initialise the playername that is used to creat specific textual instances
     * @param playername is the name of the player in question
     */
    Info(String playername){
        this.playername = playername;
    }

    /**
     * this method returns a card's name (displays an s if count > 1)
     * @param card is the type of card we want to display
     * @param count is the multiplicity of the card in question
     * @return a string with the card's name (adds an s if there are more than one card)
     */
    public static String cardName(Card card, int count){
        String string = colorByType(card) + StringsFr.plural(count);
        return string;

    }

   private static String colorByType(Card card){
        String cardColor;

        switch(card){

            case BLACK: cardColor = StringsFr.BLACK_CARD;
                break;
            case VIOLET: cardColor = StringsFr.VIOLET_CARD;
                break;
            case BLUE: cardColor = StringsFr.BLUE_CARD;
                break;
            case GREEN: cardColor =  StringsFr.GREEN_CARD;
                break;
            case YELLOW: cardColor = StringsFr.YELLOW_CARD;
                break;
            case ORANGE: cardColor = StringsFr.ORANGE_CARD;
                break;
            case RED: cardColor = StringsFr.RED_CARD;
                break;
            case WHITE:cardColor = StringsFr.WHITE_CARD;
                break;
            case LOCOMOTIVE: cardColor = StringsFr.LOCOMOTIVE_CARD;
                break;
            default: cardColor = null;
        }
        return cardColor;
    }

    /**
     * This method gives back a String that consists of a list of all players that ccollected a given nb of points
     * (count points for instance)
     * @param playerNames is the list of all names of players that got count points
     * @param points is the number of points in question
     * @return a string that displays all player's names and the number of points they all got in common
     */
    public static String draw(List<String> playerNames, int points){
        return String.format(StringsFr.DRAW, playerNames, points);
    }

    /**
     * this method returns the name of the player that will play first
     * @return a string that tells players which one will start playing
     */
    public String willPlayFirst(){
       return String.format(StringsFr.WILL_PLAY_FIRST, this.playername);
    }

    /**
     * this method returns a sentence which indicates the number of tickets a player decides to keep
     * @param count the nb of tickets a player decides to keep
     * @return a sentence that tells which player kept how many cards
     */
    public String keptTickets(int count){
        return String.format(StringsFr.KEPT_N_TICKETS, this.playername, StringsFr.plural(count));
    }

    /**
     * this method tells which player is allowed to play
     * @return a string that tells which player is allowed to play
     */
    public String canPlay(){
        return String.format(StringsFr.CAN_PLAY, this.playername);
    }

    /**
     * this method announces the number of ticket's which have been drawn by a given player
     * @param count is the nb of ticket's that have been drawn by a certain player
     * @return a string that tells which player has drawn how many cards
     */
    public String drewTickets(int count) {
        return String.format(StringsFr.DREW_TICKETS, this.playername,count ,StringsFr.plural(count));
    }

    /**
     * this method will be used to announce that a player drew a card from the deck
     * @return a string that announces the statement above
     */
    public String drewBlindCard(){
        return String.format(StringsFr.DREW_BLIND_CARD, this.playername);
    }

    /**
     * this method creates a statement that says which player drew which type of card
     * @param card the type of card that has been drawn
     * @return a string that tells whiich player drew which type of card
     */
    public String drewVisibleCard(Card card){
        return String.format(StringsFr.DREW_VISIBLE_CARD, this.playername, card);
    }

    /**
     *this method tells whichever route have been claimed by whichever player with whichever cards
     * @param route is the claimed route
     * @param cards is the set of cards that have been used to claim the route
     * @return a sentence that says exactly what i said in the first line
     */
    public String claimedRoute(Route route, SortedBag<Card> cards){
        return String.format(StringsFr.CLAIMED_ROUTE, this.playername, route, drawSortedBag(cards));
    }

    /**
     * this method announces whenever a player is trying to claim a tunnel with a certain combination of cards
     * @param route is the tunnel that is being claimed
     * @param initialCards is the combination of cards used to attempt to claim it
     * @return a sentence that announces which player claims which tunnel with which combination of cards
     */
    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards){
        return String.format(StringsFr.ATTEMPTS_TUNNEL_CLAIM, this.playername, route, drawSortedBag(initialCards));
    }

    /**
     * this method gives back a statement that indicates which additionnal cards have been drawn and what the additionnal
     * cost is
     * @param drawnCards the list of drawn additional cards
     * @param additionalCost the cost of those cards
     * @return a statement that tells which cards have been drawn and what their cost is (says no additional cost if they
     * were free)
     */
    public String drewAdditionalCards(SortedBag<Card> drawnCards, int additionalCost){
        String statement;
        switch(additionalCost){
            case 0: statement = StringsFr.NO_ADDITIONAL_COST;
                break;
            default: statement = String.format(StringsFr.SOME_ADDITIONAL_COST, additionalCost, StringsFr.plural(additionalCost));
        }
        return String.format(StringsFr.ADDITIONAL_CARDS_ARE, drawSortedBag(drawnCards))+ statement;

    }

    /**
     * this method announces that a certain player did not claim a route
     * @param route is the route that was not claimed
     * @return gives back a statement that tells which player didnt claim which route
     */
    public String didNotClaimRoute(Route route){
        return String.format(StringsFr.DID_NOT_CLAIM_ROUTE, this.playername, route);
    }

    /**
     * this method announces that a given player only has two or less wagon cards left and that the last round begins
     * @param carCount number of cards left on a players hand
     * @return a statement that announces the amount of cards left in a players hand and tha the last round begins
     */
    public String lastTurnBegins(int carCount){
        Preconditions.checkArgument(carCount <= 2);
        return String.format(StringsFr.LAST_TURN_BEGINS, this.playername, carCount, StringsFr.plural(carCount));
    }

    /**
     * this method annonces that a certain player gets a longest trail bonus
     * @param longestTrail is the longest trail in question
     * @return a statement that announces which player gets a bonus for which trail
     */
    public String getsLongestTrailBonus(Trail longestTrail){
        return String.format(StringsFr.GETS_BONUS, this.playername, trailDraw(longestTrail));
    }

    /**
     * this method returns an endgame statement that anounces who wins with how many points
     * @param points amount of points of the winner
     * @param loserPoints amount of points of the looser
     * @return a statement with which player won with how many points and how many points the looser got
     */
    public String won(int points, int loserPoints){
        return String.format(StringsFr.WINS, this.playername, points, StringsFr.plural(points), loserPoints, StringsFr.plural(loserPoints));
    }

    private static String trailDraw(Trail trail){

       return trail.station1().toString() + StringsFr.EN_DASH_SEPARATOR + trail.station2().toString();

    }
    private static String drawSortedBag(SortedBag<Card> cards){
        String finalString = "";
        boolean firstCardAdded = false;

        for(int i = Card.COUNT-1; i >= 0; --i){
            if (!firstCardAdded && cards.contains(Card.ALL.get(i))){
                finalString = "et " + colorByType(Card.ALL.get(i))  + " x" + cards.countOf(Card.ALL.get(i));
                firstCardAdded = true;
            } else if (cards.contains(Card.ALL.get(i)))
                finalString = colorByType(Card.ALL.get(i)) +" x" + cards.countOf(Card.ALL.get(i)) + ", " + finalString;
        }

        return finalString;
    }
}


