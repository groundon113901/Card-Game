package model;
import model.bet.Bet;
import model.bet.BetResult;
import model.card.Hand;
import model.card.HandImpl;

public class PlayerImpl  implements Player{

    private String id;
    private String name;
    private int points;
    private Hand hand = new HandImpl();
    private Bet playersBet = Bet.NO_BET;


    public PlayerImpl(String id, String name, int points) throws NullPointerException, IllegalArgumentException{
        this.id = id;
        this.name = name;
        this.points = points;
        if (id == null){
            throw new NullPointerException("ID supplied is null");
        }
        if (name == null){
            throw new NullPointerException("Name supplied is null");
        }
        if ( id.equals("")){
            throw new IllegalArgumentException("ID is empty");
        }
        if ( name.equals("")){
            throw new IllegalArgumentException("Name is empty");
        }
        if ( points <= 0){
            throw new IllegalArgumentException("Number of points must be greater 0");
        }

    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public int getTotalPoints() {
        if(playersBet != null){
            return this.getPoints() + playersBet.getAmount();
        }else{
            return this.getPoints();
        }
    }

    @Override
    public void assignBet(Bet bet) {
        playersBet = bet;
        points = points - bet.getAmount();
    }

    @Override
    public Bet getBet() {
        return playersBet;
    }

    @Override
    public Hand getHand() {
        return hand;
    }

    @Override
    public void applyBetResult(Hand houseHand) {
        if (houseHand != null){
            BetResult br = playersBet.finaliseBet(houseHand);
            switch(br){
                case DRAW:
                    points = getTotalPoints();
                    break;
                case PLAYER_WIN:
                    points += getBet().getAmount() + playersBet.getOutcome();
                case PLAYER_LOSS:
                    points += playersBet.getOutcome();
                    break;
                case UNDETERMINED:
                    break;
            }
        } else {
            throw new NullPointerException("HouseHand is null");
        }
    }

    @Override
    public void resetBet() {
        if (playersBet != null && playersBet.getResult() == BetResult.UNDETERMINED){
            points += playersBet.getAmount();
        }
        playersBet = Bet.NO_BET;
    }

    @Override
    public String toString(){
        return String.format("Player id=%s, name=%s, points=%d, %s, %s",
                this.id,
                this.name,
                this.points,
                playersBet.toString(),
                hand.toString());
    }

}
