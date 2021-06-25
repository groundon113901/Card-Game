package model.card;

public class CardImpl implements Card {

    private Suit suit;
    private Rank rank;

    public CardImpl(Suit suit, Rank rank){
        this.suit = suit;
        this.rank = rank;
    }

    @Override
    public Suit getSuit() { return suit; }

    @Override
    public Rank getRank() {
        return rank;
    }

    @Override
    public int getValue() {
        return rank.getRankValue();
    }

    @Override
    public boolean equals(Object obj){
        if (obj == null || obj.getClass() != this.getClass()){
            return false;
        }
        if (this == obj){
            return true;
        }
        CardImpl card = (CardImpl) obj;
        return (card.suit == this.suit && card.rank == this.rank);
    }

    @Override
    public int hashCode(){
        return suit.hashCode() + rank.hashCode();
    }

    @Override
    public int compareTo(Card card) {
        if (this.getSuit() == card.getSuit()){
            if (this.getRank() == card.getRank()){
                return 0;
            } else {
                if(this.getValue() == card.getValue()){
                    return this.getRank().compareTo(card.getRank());
                }else {
                    return this.getValue() - card.getValue();
                }
            }
        } else {
            return this.getSuit().compareTo(card.getSuit());
        }
    }

    @Override
    public String toString(){
        return String.format("%s of %s", getRank().toString(), getSuit().toString());
    }
}
