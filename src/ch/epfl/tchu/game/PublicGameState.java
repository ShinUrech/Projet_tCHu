package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class represents a Game State with publicly available information.
 *
 * @author Shin Urech (327245)
 * @author Aidas Venckunas (325464)
 */

public class PublicGameState {

    private final int ticketsCount;
    private final PublicCardState cardState;
    private final PlayerId currentPlayerId;
    private final Map<PlayerId, PublicPlayerState> playerState;
    private final PlayerId lastPlayer;

    /**
     * A public constructor for a public Game State.
     *
     * @param ticketsCount the number of tickets available in the ticket deck
     * @param cardState public state of wagon and locomotives cars
     * @param currentPlayerId current player
     * @param playerState each publicPlayerState is linked to his Id
     * @param lastPlayer last player to play
     * @throws IllegalArgumentException if ticketsCount is strictly negative or if playerState does not contain
     * exactly 2 elements
     * @throws NullPointerException if cardState or currentPlayerId is null
     */
    public PublicGameState(int ticketsCount, PublicCardState cardState, PlayerId currentPlayerId, Map<PlayerId,
            PublicPlayerState> playerState, PlayerId lastPlayer){

        Preconditions.checkArgument(ticketsCount >= 0 && playerState.size() == 2);
        if(cardState == null || currentPlayerId == null){
            throw new NullPointerException();
        }

            this.ticketsCount = ticketsCount;
            this.cardState = Objects.requireNonNull(cardState);
            this.currentPlayerId = currentPlayerId;
            this.playerState = Map.copyOf(playerState);
            this.lastPlayer = lastPlayer;
    }

    /**
     * Returns the size of the ticket deck
     * @return number of tickets still in the deck
     */
    public int ticketsCount(){
        return ticketsCount;
    }

    /**
     * This method indicates whether players can draw tickets from the deck.
     * @return true if the deck of tickets is non empty and false otherwise
     */
    public boolean canDrawTickets(){
        if(ticketsCount == 0){
            return false;

        }
        return true;
    }

    /**
     * A method that returns the publicCardState of the current game.
     * @return the game's current publicCardState
     */
    public PublicCardState cardState(){
        return cardState;
    }

    /**
     * This method indicates if a player can draw a card.
     * @return false if there are less than 5 cards in the deck + the discard pile
     */
    public boolean canDrawCards(){
        if(cardState.deckSize() + cardState.discardsSize() < 5){
            return false;
        }
        return true;
    }

    /**
     * This method returns the Id of the current player.
     * @return the current player's Id
     */
    public PlayerId currentPlayerId(){
        return currentPlayerId;
    }

    /**
     * This method returns the PublicPlayerState of a given player.
     * @param playerId the given player
     * @return the PublicPlayerState of playerId
     */
    public PublicPlayerState playerState(PlayerId playerId){
        return playerState.get(playerId);
    }

    /**
     * This method returns the PublicPlayerState of the current player.
     * @return the PublicCardState of the current player
     */
    public PublicPlayerState currentPlayerState(){
        return playerState.get(currentPlayerId);
    }

    /**
     * This method returns a list of all claimed routes by either players.
     * @return a list of all claimed routes
     */
    public List<Route> claimedRoutes(){

        List<Route> list = new ArrayList<>();

        for(Map.Entry<PlayerId, PublicPlayerState> entry : playerState.entrySet()){
            list.addAll(entry.getValue().routes());
        }

        return list;
    }

    /**
     * This method returns the last player's Id.
     * @return null if the last player is not known yet and lastPlayer if it is already defined
     */
    public PlayerId lastPlayer(){
        return lastPlayer;
    }
}
