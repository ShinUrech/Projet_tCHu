package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.Random;

import static java.util.Objects.checkIndex;

/**
 * @author Shin Urech (327245)
 * this class represent the "private" card state which is bassically all the cards that players cannot have knowledges
 * about (cards in the deck for example).
 */

public final class CardState extends PublicCardState{

    private final Deck<Card> gameDeck;
    private final SortedBag<Card> discards;

    private CardState(Deck<Card> deck, SortedBag<Card> discards, SortedBag<Card> upFacedCards){
        super(upFacedCards.toList(), Constants.TOTAL_CARDS_COUNT-Constants.FACE_UP_CARDS_COUNT, 0);

        this.gameDeck = deck;
        this.discards = discards;
    }

    /**
     * this method creates the initial card setup with 5 cards that are drawn from the top of the pile to be exposed to
     * the players
     * @param deck is the game deck
     * @return a new card state which has 5 "public cards", the card deck without the 5 public cards and an empty discard
     */

    public CardState of(Deck<Card> deck){
        Preconditions.checkArgument(deck.size() >= 5);
        return new CardState(deck, SortedBag.of(), deck.topCards(4));

    }

    /**
     * this method draws a card from the 5 public cards on a give index and replaces it with the top card from the deck
     * @param slot is the slot of the card we choose from the 5 "public" cards
     * @return a new CardState with a new card at the index nb slot th
     */

    public CardState withDrawnFaceUpCard(int slot){
        checkIndex(slot,5);
        publicCards.remove(slot);
        publicCards.add(slot, gameDeck.topCard());
        return this;
    }

    /**
     * this method returns the top card of the game deck
     * @return returns the top card of the deck
     */
    public Card topDeckCard(){
        Preconditions.checkArgument(!gameDeck.isEmpty());
        return gameDeck.topCard();
    }

    /**
     * this method returns a new CardState without the top Card
     * @return new CardState without the top card
     */
    public CardState withOutTopDeckCard(){
        Preconditions.checkArgument(!gameDeck.isEmpty());
        return new CardState(gameDeck.withoutTopCard(), this.discards, SortedBag.of(this.faceUpCards()));
    }

    /**
     * this method returns a new Cardstate made out of all the discards
     * @param rng is a random parameter for a fair shuffle
     * @return a new CardState made out of all previously disposed cards
     */
    public CardState withDeckRecreatedFromDiscards(Random rng){
        Preconditions.checkArgument(gameDeck.isEmpty());
        return this.of(Deck.of(discards, rng));
    }

    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards){

        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
        builder.add(additionalDiscards);
        builder.add(this.discards);

        return new CardState(this.gameDeck, builder.build(), SortedBag.of(this.faceUpCards()));



    }



}
