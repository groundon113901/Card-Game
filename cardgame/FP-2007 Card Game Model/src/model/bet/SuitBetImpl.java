package model.bet;

import model.Player;
import model.card.Hand;
import model.card.Suit;

public class SuitBetImpl extends AbstractBet implements SuitBet {

    private Suit suit;
    private final int BET_MULTIPLIER = 4;

    public SuitBetImpl(Player player, int amount, Suit suit) throws NullPointerException, IllegalArgumentException{
        super(player, amount);
        this.suit = suit;
        if(suit == null){
            throw new NullPointerException("The Suit cannot be null");
        }

    }
    @Override
    public Suit getSuit() {
        return suit;
    }

    @Override
    public BetResult finaliseBet(Hand houseHand) {
        finalised = true;
        houseScore = houseHand.getSuitCount(suit);
        playerScore = getPlayer().getHand().getSuitCount(suit);
        return getResult();
    }

    @Override
    public int getMultiplier() {
        return BET_MULTIPLIER;
    }

    @Override
    public String toString(){
        if (getPlayer() == null){
            return "No Bet";
        }else {
            return String.format("Suit bet for %d on %s", getAmount(), getSuit().toString());
        }
    }


    @Override
    public int getOutcome() {
        return getOutcome(getResult());
    }

}
