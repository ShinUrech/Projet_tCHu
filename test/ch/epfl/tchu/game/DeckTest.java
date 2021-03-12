package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public final class DeckTest {

    /**
     * Attributes used multiple times
     */
    public Random random = new Random(0L);
    public SortedBag.Builder<Card> emptyCardsBuilder = new SortedBag.Builder<>();
    //Create non empty SortedBag.Builder
    public SortedBag.Builder<Card> nonEmptyCardsBuilder = new SortedBag.Builder<>();
    {
        //nonEmptyCardsBuilder.add(Card.BLACK);
        nonEmptyCardsBuilder.add(Card.BLUE);
        nonEmptyCardsBuilder.add(Card.ORANGE);
        nonEmptyCardsBuilder.add(3 ,Card.YELLOW);
    }
    Deck<Card> deck = Deck.of(nonEmptyCardsBuilder.build(), random);
    Deck<Card> emptyDeck = Deck.of(emptyCardsBuilder.build(), random);

    //No need to test the constructor on error
    //No need to test the methode "of" because will be tested through the other methods


//isEmpty()
    @Test
    public void isEmptyWorksOnEmptyDeck(){
        assertTrue(Deck.of(emptyCardsBuilder.build(), random).isEmpty());
    }

    @Test
    public void isEmptyWorksOnNonEmptyDeck(){
        assertFalse(Deck.of(nonEmptyCardsBuilder.build(), random).isEmpty());
    }


//topCard()
    @Test
    public  void topCardThrowsIllegalArgumentOnEmptyDeck(){
        assertThrows(IllegalArgumentException.class, () -> {
            emptyDeck.topCard();
        });
    }

    @Test
    public void topCardWorkOnNonEmptyDeck(){
        assertEquals(Card.ORANGE, deck.topCard());
    }

//withoutTopCard()
    @Test
    public void withoutTopCardWorksOnIllegalArgument(){
        assertThrows(IllegalArgumentException.class, () -> {
            emptyDeck.withoutTopCard();
        });
    }

    @Test
    public void withoutTopCardWorksNonEmptyDeck(){
        var cardsBuilder = new SortedBag.Builder<Card>();
        cardsBuilder.add(Card.BLUE);
        cardsBuilder.add(Card.ORANGE);
        cardsBuilder.add(3 ,Card.YELLOW);

        //assertEquals(List.of(Card.YELLOW, Card.YELLOW, Card.YELLOW, Card.BLUE), deck.withoutTopCard().DECK);
    }

//topCards(int count)
    @Test
    public  void topCardsThrowsIllegalArgumentOnEmptyDeck(){
        assertThrows(IllegalArgumentException.class, () -> {
            emptyDeck.topCards(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            emptyDeck.topCards(emptyDeck.size()+1);
        });
    }

    @Test
    public void topCardsWorkOnNonEmptyDeck(){
        assertEquals(SortedBag.of(1,Card.ORANGE,1,Card.YELLOW).toList(), deck.topCards(2).toList());
    }

//withoutTopCards(int count)
    @Test
    public void withoutTopCardsThrowsIllegalArgumentOnEmptyDeck(){
        //Be careful to test first IllegalArgument before IndexOUtOfBound (with Objects.checkIndex(...)) !!!!
        assertThrows(IllegalArgumentException.class, () -> {
            emptyDeck.withoutTopCards(-1);
        });
    }

    @Test
    public void withoutTopCardsThrowsIllegalArgumentOnCountOutOfRange(){
        assertThrows(IllegalArgumentException.class, () -> {
            deck.withoutTopCards(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            deck.withoutTopCards(nonEmptyCardsBuilder.build().size()+1);
        });
    }

    @Test
    public void withoutTopCardsWorksOnNonEmptyDeck(){

        //assertEquals(List.of(Card.YELLOW, Card.YELLOW, Card.BLUE), deck.withoutTopCards(2).DECK);
    }

//size()
    @Test
    public void sizeWorks(){
        var emptyDeck = Deck.of(emptyCardsBuilder.build(), random);
        var nonEmptyDeck = Deck.of(nonEmptyCardsBuilder.build(), random);
        assertEquals(0, emptyDeck.size());
        assertEquals(5, nonEmptyDeck.size());
        assertEquals(4, nonEmptyDeck.withoutTopCard().size());
    }
}