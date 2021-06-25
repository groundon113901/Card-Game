package model.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeckImpl implements Deck{

    private static List<Card> cards = new ArrayList<>();
    private DeckImpl(List<Card> cards){
        DeckImpl.cards = cards;
    }

    @Override
    public Card removeNextCard() throws IllegalStateException {
        Card c = cards.get(0);
        if (c != null){
            cards.remove(0);
            return c;
        } else{
            throw new IllegalStateException("Deck is empty");
        }
    }

    @Override
    public int cardsInDeck() {
        return cards.size();
    }

    @Override
    public void shuffleDeck() {
        Collections.shuffle(cards);
    }

    private static void generateCards(){
        Suit[] suits = Suit.values();
        Rank[] ranks = Rank.values();
            for (Suit suit : suits) {
                for (Rank rank : ranks) {
                    Card c = new CardImpl(suit, rank);
                    cards.add(c);
                }
            }
    }

    public static Deck createSortedDeck(){
        generateCards();
        generateCards();
        return new DeckImpl(cards);
    }

    public static Deck createShuffledDeck(){
        generateCards();
        generateCards();
        generateCards();
        generateCards();
        Deck deck = new DeckImpl(cards);
        deck.shuffleDeck();
        return deck;
    }

    @Override
    public String toString(){
        return cards.toString();
    }
}
