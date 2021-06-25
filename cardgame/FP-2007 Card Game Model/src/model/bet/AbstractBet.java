package model.bet;

import model.Player;
import model.card.Hand;

public abstract class AbstractBet {

    private Player player;
    private int amount;
    protected boolean finalised = false;
    protected int playerScore, houseScore;
    private BetResult betResult = BetResult.UNDETERMINED;

    public AbstractBet(Player player, int amount) throws NullPointerException, IllegalArgumentException{
        this.amount = amount;
        this.player = player;
        if (player == null){
            throw new NullPointerException("The Player cannot be null");
        }

        if( amount < 0){
            throw new IllegalArgumentException("The amount has to be greater than 0");
        }
    }
    public abstract int getMultiplier();

    public Player getPlayer() {
        return player;
    }
    public int getAmount() {
        return amount;
    }
    public int getOutcome(BetResult result) {
        int score = 0;
        switch (result){
            case PLAYER_WIN:
                score =  getAmount() * getMultiplier();
                break;
            case PLAYER_LOSS:
                score = -(getAmount());
                break;
            default:
                break;
        }
        return score;
    }
    public BetResult getResult() {
        if (finalised) {
            if (playerScore > houseScore) {
                betResult = BetResult.PLAYER_WIN;
            }
            if (playerScore == houseScore) {
                betResult = BetResult.DRAW;
            }
            if (playerScore < houseScore){
                betResult = BetResult.PLAYER_LOSS;
            }
        }
        return betResult;
    }
    public BetResult finaliseBet(Hand houseHand) {
        finalised = true;
        houseScore = houseHand.getScore();
        playerScore = player.getHand().getScore();
        return getResult();
    }

    public int compareTo(Bet bet) {
        if(bet.getAmount() == this.getAmount()){
            return 0;
        } else{
            return bet.getAmount() - this.getAmount();
        }
    }
}
