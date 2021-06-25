package model.card;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HandImpl implements Hand {

    private List<Card> hand = new ArrayList<>();
    private int handScore = 0;

    public HandImpl(){}

    @Override
    public boolean dealCard(Card card) {
        if(card != null) {
            int currentScore = getScore();
            int newScore = currentScore + card.getValue();
            if (newScore > BUST_SCORE) {
                return false;
            } else {
                hand.add(card);
                handScore += card.getValue();
                return true;
            }
        }else{
            throw new NullPointerException("Card supplied is null");
        }
    }

    @Override
    public int getScore() {
        return handScore;
    }

    @Override
    public int getSuitCount(Suit suit) {
        int count = 0;
        if (suit != null){
            for (Card c : hand) {
                if (c.getSuit().equals(suit)) {
                    ++count;
                }
            }
            return count;
        }else{
            throw new NullPointerException("Card supplied is null");
        }
    }

    @Override
    public Collection<Card> getCards() {
        return hand;
    }

    @Override
    public boolean isEmpty() {
        return hand.isEmpty();
    }

    @Override
    public int getNumberOfCards() {
        return hand.size();
    }

    @Override
    public void reset() {
        if (hand != null) {
            hand.clear();
            handScore = 0;
        }
    }

    @Override
    public String toString(){
        ArrayList<String> cards = new ArrayList<>();
        if(isEmpty()){
            return "Empty Hand";
        } else {
            for (Card c: hand){
                cards.add(c.getRank() + " of " + c.getSuit());
            }
            return String.format("Hand of %d cards " + cards + " Score: %d", getNumberOfCards(), getScore());
        }

    }
}

