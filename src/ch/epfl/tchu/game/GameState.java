package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * this class represents all private attributes of a game of tCHu
 * @author Shin Urech (327245)
 */
public final class GameState extends PublicGameState{

    private final Deck<Ticket> tickets;
    private final CardState cardState;
    private final Map<PlayerId, PlayerState> playerState;

    //private constructor for the class
    private GameState(Deck<Ticket> tickets, CardState cardState, PlayerId currentPlayerId, Map<PlayerId, PlayerState> playerState, PlayerId lastPlayer){
        super(tickets.size(), cardState, currentPlayerId, Map.copyOf(playerState), lastPlayer);

        this.tickets = tickets;
        this.cardState = cardState;
        this.playerState = new EnumMap<>(PlayerId.class);
    }

    /**
     * this class is a GameState initializer. It creates a the initial GameState at the very start of a game of tCHu
     * @param tickets is the set of tickets that are going to be used during the game
     * @param rng is a random parameter
     * @return a new GameState that is the initial one
     */
    public GameState initial(SortedBag<Ticket> tickets, Random rng){

        Deck<Ticket> ticketDeck = Deck.of(tickets, rng);
        Deck<Card> cardDeck = Deck.of(Constants.ALL_CARDS, rng);
        Map<PlayerId, PlayerState> playerState = new TreeMap<>();
        CardState cardState = CardState.of(cardDeck.withoutTopCards(PlayerId.COUNT*Constants.INITIAL_CARDS_COUNT));

        PlayerId firstPlayer = PlayerId.ALL.get(rng.nextInt(2));
        PlayerId otherPlayer = firstPlayer.next();


        PlayerState firstPlayerState = PlayerState.initial(cardDeck.topCards(Constants.INITIAL_CARDS_COUNT));
        PlayerState otherPlayerState = PlayerState.initial(cardDeck.withoutTopCards(Constants.INITIAL_CARDS_COUNT).topCards(Constants.INITIAL_CARDS_COUNT));

        playerState.put(firstPlayer, firstPlayerState);
        playerState.put(otherPlayer, otherPlayerState);

       return new GameState(ticketDeck, cardState, firstPlayer, playerState, otherPlayer);

    }

    /**
     * this method is a getter for the playerState of any given player
     * @param playerId the given player
     * @return the playerState of playerId
     */
    @Override
    public PlayerState playerState(PlayerId playerId){
        return playerState.get(playerId);
    }

    /**
     * this method is a getter for the current player's playerState
     * @return the current player's playerState
     */
    @Override
    public PlayerState currentPlayerState(){
        return playerState.get(currentPlayerId());
    }

    /**
     * this method returns count tickets on the top of the tickets deck
     * @param count is the # of tickets we want to take
     * @throws IllegalArgumentException if count is (>0) or (< size of the deck)
     * @return a sortedbag of # tickets
     */
    public SortedBag<Ticket> topTickets(int count){
        checkCount(count);
        return tickets.topCards(count);
    }

    /**
     * this method returns a new Game State with a new ticket deck that has count less tickets on the top
     * @param count is the # of tickets that will be drawn from the deck
     * @return a new GameState with a new ticket deck (similar to the input otherwise)
     */
    public GameState withoutTopTickets(int count){
        checkCount(count);
        return new GameState(tickets.withoutTopCards(count), cardState, currentPlayerId(), playerState, lastPlayer());
    }

    /**
     * this method returns the top card of the deck
     * @return the top card of the deck
     */
    public Card topCard(){
        return cardState.topDeckCard();
    }

    /**
     * this method returns a new GameState with more dicarded cards
     * @param discardedCards a SortedBag of cards to put in the discard
     * @return a new GameState that is almost identical to the input except that the discard contains more discarded cards
     */
    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards){
        return new GameState(tickets, cardState.withMoreDiscardedCards(discardedCards), currentPlayerId(), playerState, lastPlayer());
    }

    /**
     * this method returns the exact same GameState if the card deck is non empty and returns a new GameState with a card
     * deck recreated with the discards deck if the card deck is empty
     * @param rng
     * @return
     */
    public GameState withCardsDeckRecreatedIfNeeded(Random rng){

        if(this.cardState.isDeckEmpty()){
            return new GameState(tickets, cardState.withDeckRecreatedFromDiscards(rng), currentPlayerId(), playerState, lastPlayer());
        } else {
            return this;
        }
    }

    /**
     * this method returns a new GameState with some tickets added to a certain player's CardState
     * @param playerId is the player which will recieve the tickets
     * @param chosenTickets are the tickets the chosen player will get
     * @throws IllegalArgumentException if the given player already has a ticket
     * @return a new GameState with tickets added to the player's CardState
     */
    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets){
        Preconditions.checkArgument(playerState(playerId).tickets().size() == 0);

        return new GameState(tickets, cardState, currentPlayerId(), changeTickets(playerId, chosenTickets), lastPlayer());
    }

    /**
     * this method returns a new GameState that is identical to the input except that the current player has some
     * additional tickets
     * @param drawnTickets are the tickets that are being drawn from the ticket deck
     * @param chosenTickets are the tickets chosen by the current player to be kept
     * @throws IllegalArgumentException if the chosen tickets arent contained in the drawn ones
     * @return a new GameState with new tickets added to the current player's hand
     */
    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets){
        Preconditions.checkArgument(drawnTickets.contains(chosenTickets));

       return this.withInitiallyChosenTickets(currentPlayerId(), chosenTickets);

    }

    /**
     *this method returns an identical gameState except that the faceup card at the index slot is now on the current player's
     * hand and a card picked up from the top of the deck has replaced the card at the index slot
     * @param slot is the slot of the card that has been picked
     * @return new game state with a new faceup card and a new card on the player's hand
     */
    public GameState withDrawnFaceUpCard(int slot){
        Preconditions.checkArgument(canDrawCards());

        return new GameState(tickets,cardState.withDrawnFaceUpCard(slot), currentPlayerId(), changeCardState(currentPlayerId(), cardState.faceUpCard(slot)), lastPlayer());
    }

    /**
     * this method picks the top deck card from the deck and gives it to the current player
     * @return a new gameState with a new deck and a new cardState for the current player
     */
    public GameState withBlindlyDrawnCard(){
        Preconditions.checkArgument(canDrawCards());

        return new GameState(tickets, cardState.withoutTopDeckCard(), currentPlayerId(), changeCardState(currentPlayerId(), cardState.topDeckCard()), lastPlayer());
    }

    /**
     * this method adds a new route to the player state with some claim cards
     * @param route route that is been claimed
     * @param cards cards used to claim the route
     * @return a new game state with attributes modified
     */
    public GameState withClaimedRoute(Route route, SortedBag<Card> cards){
        return new GameState(tickets, cardState, currentPlayerId(), changeRoute(currentPlayerId(), route, cards), lastPlayer());
    }

    /**
     * this method determines whether the last turn can begin or not
     * @return true iff the current player has two cards or less
     */
    public boolean lastTurnBegins(){
        if(playerState(currentPlayerId()).cards().size() <= 2){
            return true;
        }else{
            return false;
        }
    }

    /**
     * this method returns a new game state that represents the next turn of the game
     * @return the new current player is the next of the current player and if it is the final turn the current player becomes the final one
     */
    public GameState forNextTurn(){
        if(!this.lastTurnBegins()) {
            return new GameState(tickets, cardState, currentPlayerId(), playerState, currentPlayerId());

        }else{
            return new GameState(tickets, cardState, currentPlayerId().next(), playerState, lastPlayer().next());
        }
    }


    private void checkCount(int count){
        Preconditions.checkArgument(count >= 0 || count <= tickets.size());
    }

    private Map<PlayerId, PlayerState> changeCardState(PlayerId player, Card card){
        Map<PlayerId, PlayerState> newPlayerState = new EnumMap<>(PlayerId.class);
        newPlayerState.putAll(playerState);

        PlayerState toBeModified = playerState(player);
        toBeModified.withAddedCard(card);
        newPlayerState.replace(player, toBeModified);

        return newPlayerState;
    }

    private Map<PlayerId, PlayerState> changeTickets(PlayerId player, SortedBag<Ticket> tickets){
        Map<PlayerId, PlayerState> withNewTickets = new EnumMap<>(PlayerId.class);
        withNewTickets.putAll(playerState);

        PlayerState toBeModified = playerState(player);
        toBeModified.withAddedTickets(tickets);

        withNewTickets.replace(player, toBeModified);
        return  withNewTickets;
    }

    private Map<PlayerId, PlayerState> changeRoute(PlayerId player, Route route, SortedBag<Card> cards){
        Map<PlayerId, PlayerState> withNewRoute = new EnumMap<>(PlayerId.class);
        withNewRoute.putAll(playerState);

        PlayerState toBeModified = playerState(player);
        toBeModified.withClaimedRoute(route, cards);

        withNewRoute.replace(player, toBeModified);
        return withNewRoute;
    }
}
