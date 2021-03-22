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
        this.playerState = new EnumMap<PlayerId, PlayerState>(playerState);
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
        return new GameState(tickets.withoutTopCards(count), this.cardState, this.currentPlayerId(), this.playerState, this.lastPlayer());
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
        return new GameState(this.tickets, this.cardState.withMoreDiscardedCards(discardedCards), currentPlayerId(), this.playerState, lastPlayer());
    }

    /**
     * this method returns the exact same GameState if the card deck is non empty and returns a new GameState with a card
     * deck recreated with the discards deck if the card deck is empty
     * @param rng
     * @return
     */
    public GameState withCardsDeckRecreatedIfNeeded(Random rng){

        if(this.cardState.isDeckEmpty()){
            return new GameState(this.tickets, this.cardState.withDeckRecreatedFromDiscards(rng), currentPlayerId(), this.playerState, lastPlayer());
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
        Preconditions.checkArgument(playerState.get(playerId).tickets().size() == 0);

        playerState.get(playerId).withAddedTickets(chosenTickets);

        return new GameState(this.tickets, this.cardState, currentPlayerId(), this.playerState, lastPlayer());
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

    public GameState withDrawnFaceUpCard(int slot){
        Preconditions.checkArgument(canDrawCards());

        playerState.replace(currentPlayerId(), playerState.get(currentPlayerId()).withAddedCard(cardState.faceUpCard(slot)));
        return new GameState(tickets,cardState.withDrawnFaceUpCard(slot), currentPlayerId(), playerState, lastPlayer());
    }

    private void checkCount(int count){
        Preconditions.checkArgument(count >= 0 || count <= tickets.size());
    }

}
