package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *this class represents a game state with publicly available information
 * @author Shin Urech (327245)
 *
 */

public class PublicGameState {

    private final int ticketsCount;
    private final PublicCardState cardState;
    private final PlayerId currentPlayerId;
    private final Map<PlayerId, PublicPlayerState> playerState;
    private final PlayerId lastPlayer;

    /**
     * this is the public constructor for a publicGameState
     * @param ticketsCount the # of thickets available in the ticket deck
     * @param cardState public state of wagon and locmotives cars
     * @param currentPlayerId current player
     * @param playerState each publicPlayerState is linked to his Id
     * @param lastPlayer last player to play
     * @throws IllegalArgumentException if ticketsCount is strictly negative or if playerState doesnt contain
     * exactly 2 elements and NullPointerException if cardState or currentPlayerId is null
     */
    public PublicGameState(int ticketsCount, PublicCardState cardState, PlayerId currentPlayerId, Map<PlayerId, PublicPlayerState> playerState, PlayerId lastPlayer){

        Preconditions.checkArgument(ticketsCount >= 0 && playerState.size() == 2);
        if(cardState == null || currentPlayerId == null){
            throw new NullPointerException();
        }

            this.ticketsCount = ticketsCount;
            this.cardState = cardState;
            this.currentPlayerId = currentPlayerId;
            this.playerState = Map.copyOf(playerState);
            this.lastPlayer = lastPlayer;
    }

    /**
     * getter for the size of the ticket deck
     * @return # of tickets still in the deck
     */
    public int ticketsCount(){
        return ticketsCount;
    }

    /**
     * this method indicates whether players can draw tickets from the deck
     * @return true if the deck is non empty and false otherwise
     */
    public boolean canDrawTickets(){
        if(ticketsCount == 0){
            return false;

        }else {

            return true;
        }
    }

    /**
     * method that returns the publicCardState of the current game
     * @return the game's current publicCardState
     */
    public PublicCardState cardState(){
        return cardState;
    }

    /**
     * this method indicates if a player can draw a card
     * @return false if there are less than 5 cards in the deck and the discard (added)
     */
    public boolean canDrawCards(){
        if(cardState.totalSize() < 5){
            return false;
        }else{
            return true;
        }
    }

    /**
     * this method returns the Id of the current player
     * @return the current player's Id
     */
    public PlayerId currentPlayerId(){
        return currentPlayerId;
    }

    /**
     * this method returns the PublicPlayerState of a given player
     * @param playerId the given player
     * @return the PublicPlayerState of playerId
     */
    public PublicPlayerState playerState(PlayerId playerId){
        return playerState.get(playerId);
    }

    /**
     * this method returns the PublicPlayerState of the current player
     * @return the PublicCardState of the current player
     */
    public PublicPlayerState currentPlayerState(){
        return playerState.get(currentPlayerId);
    }

    /**
     * this method returns a list of all claimed routes by either players
     * @return a list of all claimed routes
     */
    public List<Route> claimedRoutes(){

        List<Route> list = new ArrayList<>();

        list.addAll(playerState.get(currentPlayerId).routes());
        list.addAll(playerState.get(currentPlayerId).routes());

        return list;
    }

    /**
     * this method returns the last player's Id
     * @return null if the last player isnt known yet and lastPlayer if it is already defined
     */
    public PlayerId lastPlayer(){
        if(lastPlayer == null){
            return null;
        }else{
            return lastPlayer;
        }
    }


}
