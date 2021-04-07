package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * This class represents all private attributes of a game of tCHu.
 *
 * @author Shin Urech (327245)
 * @author Aidas Venckunas (325464)
 */
public final class GameState extends PublicGameState{

    private final Deck<Ticket> tickets;
    private final CardState cardState;
    private final Map<PlayerId, PlayerState> playerState;

    //private constructor for the class
    private GameState(Deck<Ticket> tickets, CardState cardState, PlayerId currentPlayerId, Map<PlayerId,
            PlayerState> playerState, PlayerId lastPlayer){

        super(tickets.size(), cardState, currentPlayerId, Map.copyOf(playerState), lastPlayer);

        this.tickets = tickets;
        this.cardState = cardState;
        this.playerState = playerState;
    }

    /**
     * This class is a GameState initializer. It creates a the initial GameState at the very start of a game of tCHu.
     *
     * @param tickets is the set of tickets that are going to be used during the game
     * @param rng is a random parameter
     *
     * @return a new GameState that is the initial one
     */
    public static GameState initial(SortedBag<Ticket> tickets, Random rng){

        Deck<Ticket> ticketDeck = Deck.of(tickets, rng);
        Deck<Card> cardDeck = Deck.of(Constants.ALL_CARDS, rng);
        Map<PlayerId, PlayerState> playerState = new EnumMap<>(PlayerId.class);
        CardState cardState = CardState.of(cardDeck.withoutTopCards(PlayerId.COUNT*Constants.INITIAL_CARDS_COUNT));

        PlayerId firstPlayer = PlayerId.ALL.get(rng.nextInt(2));
        PlayerId otherPlayer = firstPlayer.next();

        PlayerState firstPlayerState = PlayerState.initial(cardDeck.topCards(Constants.INITIAL_CARDS_COUNT));
        PlayerState otherPlayerState = PlayerState.initial(cardDeck.withoutTopCards(Constants.INITIAL_CARDS_COUNT)
                .topCards(Constants.INITIAL_CARDS_COUNT));

        playerState.put(firstPlayer, firstPlayerState);
        playerState.put(otherPlayer, otherPlayerState);

       return new GameState(ticketDeck, cardState, firstPlayer, playerState, null);

    }

    /**
     * This method is a getter for the playerState of any given player.
     *
     * @param playerId the given player
     *
     * @return the playerState of playerId
     */
    @Override
    public PlayerState playerState(PlayerId playerId){
        return playerState.get(playerId);
    }

    /**
     * This method is a getter for the current player's playerState.
     *
     * @return the current player's playerState
     */
    @Override
    public PlayerState currentPlayerState(){
        return playerState.get(currentPlayerId());
    }

    /**
     * This method returns a given number of tickets on the top of the tickets deck.
     *
     * @param count is the number of tickets we want to take
     * @throws IllegalArgumentException if count is < 0, or > size of the deck
     *
     * @return a SortedBag of top tickets
     */
    public SortedBag<Ticket> topTickets(int count){
        checkCount(count);
        return tickets.topCards(count);
    }

    /**
     * This method returns a new Game State with a new ticket deck that has a given number less tickets on the top.
     *
     * @param count is the number of tickets that will be drawn from the deck
     * @throws IllegalArgumentException if count is < 0, or > size of the deck
     *
     * @return a new GameState with a modified ticket deck (similar to the input otherwise)
     */
    public GameState withoutTopTickets(int count){
        checkCount(count);
        return new GameState(tickets.withoutTopCards(count), cardState, currentPlayerId(), playerState, lastPlayer());
    }

    /**
     * This method returns the top card of the deck.
     *
     * @throws IllegalArgumentException if the deck is empty
     *
     * @return the top card of the deck
     */
    public Card topCard(){
        Preconditions.checkArgument(!cardState.isDeckEmpty());
        return cardState.topDeckCard();
    }

    /**
     * This method returns a new Game State without a top card.
     *
     * @throws IllegalArgumentException if the deck is empty
     *
     * @return a new Game State without a top card
     */
    public GameState withoutTopCard(){
        Preconditions.checkArgument(!cardState.isDeckEmpty());
        return new GameState(tickets, cardState.withoutTopDeckCard(), currentPlayerId(), playerState, lastPlayer());
    }

    /**
     * This method returns a new GameState with more discarded cards.
     *
     * @param discardedCards a SortedBag of cards to put in the discard
     *
     * @return a new GameState that is almost identical to the input except that
     * the discard contains more discarded cards
     */
    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards){
        return new GameState(tickets, cardState.withMoreDiscardedCards(discardedCards), currentPlayerId(),
                playerState, lastPlayer());
    }

    /**
     * This method returns the exact same GameState if the card deck is non empty and returns
     * a new GameState with a card deck recreated with the discards deck if the card deck is empty.
     *
     * @param rng a randomizer
     *
     * @return a new GameState with a card deck recreated with the discards deck if the card deck is empty
     */
    public GameState withCardsDeckRecreatedIfNeeded(Random rng){

        if(cardState.isDeckEmpty()){
            return new GameState(tickets, cardState.withDeckRecreatedFromDiscards(rng), currentPlayerId(),
                    playerState, lastPlayer());
        }
        return this;
    }

    /**
     * This method returns a new GameState with some tickets added to a certain player's CardState.
     *
     * @param playerId is the player which will receive the tickets
     * @param chosenTickets are the tickets the chosen player will get
     * @throws IllegalArgumentException if the given player already has a ticket
     *
     * @return a new GameState with tickets added to the player's CardState
     */
    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets){

        Preconditions.checkArgument(playerState(playerId).tickets().size() == 0);

        return new GameState(tickets, cardState, currentPlayerId(), changeTickets(playerId, chosenTickets),
                lastPlayer());
    }

    /**
     * This method returns a new GameState that is identical to the input except that the current player has chosen
     * additional tickets.
     *
     * @param drawnTickets are the tickets that are being drawn from the ticket deck
     * @param chosenTickets are the tickets chosen by the current player to be kept
     * @throws IllegalArgumentException if the chosen tickets arent contained in the drawn ones
     *
     * @return a new GameState with new tickets added to the current player's hand
     */
    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets){

        Preconditions.checkArgument(drawnTickets.contains(chosenTickets));

       return new GameState(tickets.withoutTopCards(drawnTickets.size()), this.cardState, currentPlayerId(),
               changeTickets(currentPlayerId(), chosenTickets), lastPlayer());

    }

    /**
     * This method returns an an identical state to the receiver except that the face-up
     * card at the given location has been placed in the current player's hand
     * and replaced by the one at the top of the draw pile.
     *
     * @param slot is the slot of the card that has been picked
     * @throws IllegalArgumentException if it is not possible to draw cards
     *
     * @return new game state with a new face-up card and a new card on the player's hand
     */
    public GameState withDrawnFaceUpCard(int slot){

        Preconditions.checkArgument(canDrawCards());

        return new GameState(tickets, cardState.withDrawnFaceUpCard(slot), currentPlayerId(),
                changeCardState(currentPlayerId(), cardState.faceUpCard(slot)), lastPlayer());
    }

    /**
     * This method picks the top deck card from the deck and gives it to the current player.
     *
     * @throws IllegalArgumentException if it is not possible to draw cards
     *
     * @return a new GameState with a new deck and a new cardState for the current player
     */
    public GameState withBlindlyDrawnCard(){
        Preconditions.checkArgument(canDrawCards());

        return new GameState(tickets, cardState.withoutTopDeckCard(), currentPlayerId(),
                changeCardState(currentPlayerId(), cardState.topDeckCard()), lastPlayer());
    }

    /**
     * This method returns an identical state to the receiver but in which the current player has
     * seized the given route by means of the given cards.
     *
     * @param route route that has been claimed
     * @param cards cards used to claim the route
     *
     * @return a new game state with attributes modified
     */
    public GameState withClaimedRoute(Route route, SortedBag<Card> cards){
        return new GameState(tickets, cardState.withMoreDiscardedCards(cards), currentPlayerId(),
                changeRoute(currentPlayerId(), route, cards), lastPlayer());
    }

    /**
     * This method determines whether the last turn can begin or not.
     *
     * @return true if and only if the current player has two cards or less
     */
    public boolean lastTurnBegins(){
        if(playerState(currentPlayerId()).carCount() <= 2){
            return true;
        }
        return false;
    }

    /**
     * This method returns a new game state that is used for next turn.
     *
     * @return the new current player is the next of the current player and if it is the final turn the
     * current player becomes the final one
     */
    public GameState forNextTurn(){
        if(!lastTurnBegins()) {
            return new GameState(tickets, cardState, currentPlayerId().next(), playerState, lastPlayer());
        }
        return new GameState(tickets, cardState, currentPlayerId().next(), playerState, currentPlayerId());
    }


    private void checkCount(int count){
        Preconditions.checkArgument(count >= 0 && count <= tickets.size());
    }

    private Map<PlayerId, PlayerState> changeCardState(PlayerId player, Card card){
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(PlayerId.class);
        newPlayerState.putAll(playerState);

        PlayerState toBeModified = playerState(player);
        toBeModified = toBeModified.withAddedCard(card);
        newPlayerState.replace(player, toBeModified);

        return newPlayerState;
    }

    private Map<PlayerId, PlayerState> changeTickets(PlayerId player, SortedBag<Ticket> tickets){
        Map<PlayerId, PlayerState> withNewTickets = new EnumMap<>(PlayerId.class);
        withNewTickets.putAll(playerState);

        PlayerState toBeModified = playerState(player);
        toBeModified = toBeModified.withAddedTickets(tickets);

        withNewTickets.replace(player, toBeModified);
        return  withNewTickets;
    }

    private Map<PlayerId, PlayerState> changeRoute(PlayerId player, Route route, SortedBag<Card> cards){
        Map<PlayerId, PlayerState> withNewRoute = new EnumMap<>(PlayerId.class);
        withNewRoute.putAll(playerState);

        PlayerState toBeModified = playerState(player);
        toBeModified = toBeModified.withClaimedRoute(route, cards);

        withNewRoute.replace(player, toBeModified);
        return withNewRoute;
    }
}
