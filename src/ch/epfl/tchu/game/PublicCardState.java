package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import java.util.List;

import static java.util.Objects.checkIndex;

/**
 * @author Shin Urech (327245)
 * this class contains all the public information about playing cards (for example:
 * the size of the deck).
 */

public class PublicCardState<Cards> {

    public final List<Card> faceUpCards;
    private final int deckSize;
    private final int discardsSize;

    /**
     * public constructor for the public cards
     * @param faceUpCards is the list of cards that we want to make publicly available
     * @param deckSize size of the deck
     * @param discardsSize size of the discard deck
     */
    public PublicCardState(List<Card> faceUpCards, int deckSize, int discardsSize){

        Preconditions.checkArgument(faceUpCards.size() == 5);
        Preconditions.checkArgument(deckSize >= 0 && discardsSize >= 0);

        this.faceUpCards = faceUpCards;
        this.deckSize = deckSize;
        this.discardsSize = discardsSize;

    }

    /**
     * this method gives back the count of all cards that arent being held by a player
     * @return the number of cards that arent in a player's hand
     */

    public int totalSize(){
        return faceUpCards.size() + deckSize + discardsSize;
    }

    /**
     * this method gives back a list of all 5 publicly available cards
     * @return
     */
    public List<Card> faceUpCards(){
        return faceUpCards;
    }

    /**
     * this method gives back a face up card at a given index
     * @param slot index of the card we want to get (from 0 to 4)
     * @return the card we asked for at the given index
     */
    public Card faceUpCard(int slot){
        checkIndex(slot, 5);
        return faceUpCards.get(slot);
    }

    /**
     * this method gives back the deck's size
     * @return the deck size
     */
    public int deckSize(){
        return deckSize;
    }

    /**
     * this method returns whether the deck is empty or not
     * @return true if the deck is empty and false otherwise
     */
    public boolean isDeckEmpty(){
        if(deckSize == 0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * this method returns the discard's size
     * @return discard's size
     */
    public int discardSize(){
        return discardsSize;
    }

}
