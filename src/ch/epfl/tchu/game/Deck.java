package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author Shin Urech(327245)
 * Deck class for the game that contains playing cards
 * @param <C> type of the elements in the deck
 */
public final class Deck<C extends Comparable<C>> {

    private final SortedBag<C> DECK;

    /**
     * This class creates and Shuffles the deck of cards
     * @param cards a sortedbag containing all cards needed to play the game
     * @param rng this is the key to randomise the deck shuffle
     * @param <C> generic type of the content of the deck
     * @return the return type is a shuffled deck
     */
   public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng){

       List<C> shuffleList = cards.toList();

       Collections.shuffle(shuffleList, rng);
       SortedBag<C> shuffleCards = SortedBag.of(shuffleList);

       Deck<C> deck = new Deck<>(shuffleCards);


            return deck;
   }

   private Deck(SortedBag<C> cards){
      DECK = cards;

   }

    /**
     * getter for the deck's size
     * @return the deck's size
     */
   public int size(){
      return DECK.size();
   }

    /**
     * this method tests whether or not the deck is empty
     * @return a boolean variable (false if the deck is nonempty)
     */
   public boolean isEmpty(){
       return DECK.isEmpty();
   }

    /**
     * this method gives back the card on the top of the deck
     * @return card at index 0 in the deck
     */
   public C topCard(){
       Preconditions.checkArgument(!DECK.isEmpty());
       return DECK.get(0);
   }


    /**
     * this method gives back a copy of the original deck without the top card
     * @return withoutTopCard, which is the copy of the inputted deck without the top card
     */
   public Deck<C> withoutTopCard(){

       SortedBag<C> topCard = SortedBag.of(DECK.get(0));
       Deck<C> withoutTopCard = new Deck<>(DECK.difference(topCard));
       return withoutTopCard;
   }

    /**
     * this method gives back a sortedBag that contains all the count th first elements in the DECK
     * @param count is the number of cards we want to get from the top of the DECK
     * @return a new sortedBag with only the count th first cards in the DECK
     */

    public SortedBag<C> topCards(int count){

       checkCount(count);
       SortedBag.Builder<C> builder = new SortedBag.Builder<>();

       for(int i = 0; i < count; ++i){
           builder.add(1,DECK.get(i));
        }

    return builder.build();

    }

    /**
     * this method returns a new Deck with the count th first cards of the original input Deck
     * @param count nb of cards we want to get from the top of the Deck
     * @return new Deck
     */

    public Deck<C> withoutTopCards(int count){
       Deck<C> newDeck = new Deck<>(DECK.difference(this.topCards(count)));
       return newDeck;

    }


    private void checkCount(int count){
        Preconditions.checkArgument(count >= 0 && count <= DECK.size());
    }
}
