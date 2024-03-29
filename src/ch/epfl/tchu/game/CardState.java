package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Objects.checkIndex;

/**
 * This class represent the "private" card state which is all the cards that players cannot have
 * knowledge about (cards in the deck for example).
 *
 * @author Shin Urech (327245)
 * @author Aidas Venckunas (325464)
 */

public final class CardState extends PublicCardState{

    private final Deck<Card> deck;
    private final SortedBag<Card> discards;
    private final List<Card> faceUpCards;

    private CardState(Deck<Card> deck, SortedBag<Card> discards, List<Card> FaceUpCards){

        super(FaceUpCards, deck.size(), discards.size());

        this.faceUpCards = FaceUpCards;
        this.deck = deck;
        this.discards = discards;
    }

    /**
     * This method creates the initial card setup with 5 cards that are drawn from the top of the pile
     * to be exposed to the players.
     *
     * @param deck the game deck
     * @throws IllegalArgumentException if the deck size is less than 5
     *
     * @return a new card state which has 5 "face-up cards", the card deck without the 5 public cards
     * and an empty discard.
     */

    public static CardState of(Deck<Card> deck){
        Preconditions.checkArgument(deck.size() >= 5);
        return new CardState(deck.withoutTopCards(Constants.FACE_UP_CARDS_COUNT), SortedBag.of(),
                deck.topCards(Constants.FACE_UP_CARDS_COUNT).toList());

    }

    /**
     * This method draws a card from the 5 public cards on a given index and replaces it with the top
     * card from the deck.
     *
     * @param slot the slot of the card we choose from the 5 "public" cards
     * @throws NullPointerException if slot is not within the bounds
     * @throws IllegalArgumentException if the deck is empty
     *
     * @return a new CardState with a new card at the index number slot
     */

    public CardState withDrawnFaceUpCard(int slot){

        checkIndex(slot, Constants.FACE_UP_CARDS_COUNT);
        Preconditions.checkArgument(!deck.isEmpty());

        List<Card> newFaceUpCards = new ArrayList<>(faceUpCards);
        newFaceUpCards.set(slot, deck.topCard());
        Deck<Card> newDeck = deck.withoutTopCard();

        return new CardState(newDeck, this.discards, newFaceUpCards);
    }

    /**
     * This method returns the top card of the game deck.
     *
     * @throws IllegalArgumentException if the deck is empty
     *
     * @return returns the top card of the deck
     */
    public Card topDeckCard(){
        Preconditions.checkArgument(!deck.isEmpty());
        return deck.topCard();
    }

    /**
     * This method returns a new CardState without the top Card.
     *
     * @throws IllegalArgumentException if the deck is empty
     *
     * @return new CardState without the top card
     */
    public CardState withoutTopDeckCard(){
        Preconditions.checkArgument(!deck.isEmpty());
        return new CardState(deck.withoutTopCard(), this.discards, this.faceUpCards());
    }

    /**
     * This method returns a new CardState made out of all the discards.
     *
     * @param rng a random parameter for a fair shuffle
     * @throws IllegalArgumentException if the deck is not empty
     *
     * @return a new CardState made out of all previously disposed cards
     */
    public CardState withDeckRecreatedFromDiscards(Random rng){
        Preconditions.checkArgument(deck.isEmpty());
        return new CardState(deck.of(this.discards, rng), SortedBag.of(), this.faceUpCards);
    }

    /**
     * A method that adds given cards to a discards list.
     *
     * @param additionalDiscards a list of given cards to discard
     *
     * @return a new CardState with added discards
     */
    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards){

        SortedBag.Builder<Card> builder = new SortedBag.Builder<>();
        builder.add(additionalDiscards);
        builder.add(this.discards);

        return new CardState(this.deck, builder.build(), this.faceUpCards());
    }
}
