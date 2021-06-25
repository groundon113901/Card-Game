package model.bet;

import model.Player;

public class ScoreBetImpl extends AbstractBet implements ScoreBet{

    private final int BET_MULTIPLIER = 2;

    public ScoreBetImpl(Player player, int amount) throws NullPointerException, IllegalArgumentException{
        super(player, amount);
    }

    @Override
    public int getMultiplier() {
        return BET_MULTIPLIER;
    }

    @Override
    public int getOutcome() {
        return getOutcome(getResult());
    }

    @Override
    public String toString(){
        if (getPlayer() == null){
            return "No Bet";
        }else {
            return String.format("Score Bet for %d", getAmount());
        }
    }
}
