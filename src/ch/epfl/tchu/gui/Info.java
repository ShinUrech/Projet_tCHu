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
 * This class is used to create all the textual content that will be implemented into the game later on.
 *
 * @author Shin Urech (327245)
 * @author Aidas Venckunas (325464)
 */

public final class Info {

    private final String playerName;

    /**
     * this public constructor is used to initialise the player name that is used to
     * create specific textual instances.
     *
     * @param playerName is the name of the player in question.
     */
    public Info(String playerName){
        this.playerName = playerName;
    }

    /**
     * This method returns a card's name (adds an s if count > 1).
     *
     * @param card is the type of card we want to display
     * @param count is the multiplicity of the card in question
     *
     * @return a string with the card's name (adds an s if there are more than one card).
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
     * This method gives back a String that consists of a list of all players that collected a given nb of points
     * (count points for instance).
     *
     * @param playerNames is the list of all names of players that got count points
     * @param points is the number of points in question
     *
     * @return a string that displays all player's names and the number of points they all got in common
     */
    public static String draw(List<String> playerNames, int points){
        return String.format(StringsFr.DRAW, String.join(StringsFr.AND_SEPARATOR, playerNames), points);
    }

    /**
     * This method returns the name of the player that will play first.
     * @return a string that tells players which one will start playing
     */
    public String willPlayFirst(){
       return String.format(StringsFr.WILL_PLAY_FIRST, this.playerName);
    }

    /**
     * This method returns a sentence which indicates the number of tickets a player decides to keep.
     *
     * @param count the nb of tickets a player decides to keep
     *
     * @return a sentence that tells which player kept how many cards
     */
    public String keptTickets(int count){
        return String.format(StringsFr.KEPT_N_TICKETS, this.playerName, count, StringsFr.plural(count));
    }

    /**
     * This method tells which player is allowed to play.
     * @return a string that tells which player is allowed to play
     */
    public String canPlay(){
        return String.format(StringsFr.CAN_PLAY, this.playerName);
    }

    /**
     * This method announces the number of tickets which have been drawn by a given player.
     *
     * @param count is the nb of tickets that have been drawn by a certain player
     *
     * @return a string that tells which player has drawn how many cards
     */
    public String drewTickets(int count) {
        return String.format(StringsFr.DREW_TICKETS, this.playerName, count, StringsFr.plural(count));
    }

    /**
     * This method announces that a player drew a card from the deck.
     * @return a string that announces the statement above
     */
    public String drewBlindCard(){
        return String.format(StringsFr.DREW_BLIND_CARD, this.playerName);
    }

    /**
     * This method creates a statement that says which player drew which type of card.
     *
     * @param card the type of card that has been drawn
     *
     * @return a string that tells whiich player drew which type of card
     */
    public String drewVisibleCard(Card card){
        return String.format(StringsFr.DREW_VISIBLE_CARD, this.playerName, colorByType(card));
    }

    /**
     * This method tells whichever route have been claimed by whichever player with whichever cards.
     *
     * @param route is the claimed route
     * @param cards is the set of cards that have been used to claim the route
     *
     * @return a sentence that says exactly what i said in the first line
     */
    public String claimedRoute(Route route, SortedBag<Card> cards){
        return String.format(StringsFr.CLAIMED_ROUTE, this.playerName, route.station1().toString() +
                StringsFr.EN_DASH_SEPARATOR + route.station2().toString(), drawSortedBag(cards));
    }

    /**
     * This method announces whenever a player is trying to claim a tunnel with a certain combination of cards.
     *
     * @param route is the tunnel that is being claimed
     * @param initialCards is the combination of cards used to attempt to claim it
     *
     * @return a sentence that announces which player claims which tunnel with which combination of cards
     */
    public String attemptsTunnelClaim(Route route, SortedBag<Card> initialCards){
        return String.format(StringsFr.ATTEMPTS_TUNNEL_CLAIM, this.playerName, route.station1().toString() +
                StringsFr.EN_DASH_SEPARATOR + route.station2().toString(), drawSortedBag(initialCards));
    }

    /**
     * This method gives back a statement that indicates which additional cards have been drawn and what the
     * additional cost is.
     *
     * @param drawnCards the list of drawn additional cards
     * @param additionalCost the cost implied by these cards
     *
     * @return a statement that tells which cards have been drawn and what their cost is (says no additional
     * cost if they were free).
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
     * This method announces that a certain player did not claim a route.
     *
     * @param route is the route that was not claimed
     *
     * @return gives back a statement that tells which player didn't claim which route.
     */
    public String didNotClaimRoute(Route route){
        return String.format(StringsFr.DID_NOT_CLAIM_ROUTE, this.playerName, route.station1().toString() +
                StringsFr.EN_DASH_SEPARATOR + route.station2().toString());
    }

    /**
     * This method announces that a player only has two or less wagon cards left and that the last round begins.
     *
     * @param carCount number of cards left on a players hand
     * @throws IllegalArgumentException if cars count is bigger than 2
     *
     * @return a statement that announces the amount of cards left in a players hand and that the last round begins
     */
    public String lastTurnBegins(int carCount){
        Preconditions.checkArgument(carCount <= 2);
        return String.format(StringsFr.LAST_TURN_BEGINS, this.playerName, carCount, StringsFr.plural(carCount));
    }

    /**
     * This method announces that a certain player gets a longest trail bonus.
     *
     * @param longestTrail is the longest trail in question
     *
     * @return a statement that announces which player gets a bonus for which trail
     */
    public String getsLongestTrailBonus(Trail longestTrail){
        return String.format(StringsFr.GETS_BONUS, this.playerName, trailDraw(longestTrail));
    }

    /**
     * This method returns an endgame statement that announces who wins with how many points.
     *
     * @param points amount of points of the winner
     * @param loserPoints amount of points of the looser
     *
     * @return a statement with which player won with how many points and how many points the looser got
     */
    public String won(int points, int loserPoints){
        return String.format(StringsFr.WINS, this.playerName, points, StringsFr.plural(points),
                loserPoints, StringsFr.plural(loserPoints));
    }

    private static String trailDraw(Trail trail){

       return trail.station1().toString() + StringsFr.EN_DASH_SEPARATOR + trail.station2().toString();

    }

    private static String drawSortedBag(SortedBag<Card> cards) {
        String finalString = null;
        boolean firstCardAdded = false;
        int alreadyAdded = 0;
        int countTypesOfcards = 0;

        for(int i = 0; i < Card.COUNT; ++i){
            if(cards.contains(Card.ALL.get(i))){
                ++countTypesOfcards;
            }
        }

        for (int i = 0; i < Card.COUNT; ++i) {

            if (cards.size() == 1 && cards.contains(Card.ALL.get(i))) {
                finalString = cards.countOf(Card.ALL.get(i)) + " " + colorByType(Card.ALL.get(i))
                        + StringsFr.plural(cards.countOf(Card.ALL.get(i)));
                return finalString;
            } else if (cards.contains(Card.ALL.get(i)) && finalString == null) {

                finalString = cards.countOf(Card.ALL.get(i)) + " " + colorByType(Card.ALL.get(i))
                        + StringsFr.plural(cards.countOf(Card.ALL.get(i)));
                ++alreadyAdded;
            } else if (cards.contains(Card.ALL.get(i)) && alreadyAdded < countTypesOfcards - 1) {

                finalString += ", " + cards.countOf(Card.ALL.get(i)) + " " + colorByType(Card.ALL.get(i))
                        + StringsFr.plural(cards.countOf(Card.ALL.get(i)));
                ++alreadyAdded;
            } else if (cards.contains(Card.ALL.get(i)) && alreadyAdded == countTypesOfcards - 1) {

                finalString += " et " + cards.countOf(Card.ALL.get(i)) + " " + colorByType(Card.ALL.get(i))
                        + StringsFr.plural(cards.countOf(Card.ALL.get(i)));
                ++alreadyAdded;

                return finalString;
            }
        }
        return finalString;
    }
}


