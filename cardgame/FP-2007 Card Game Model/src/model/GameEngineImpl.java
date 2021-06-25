package model;

import model.bet.*;
import model.card.*;
import view.GameCallback;
import view.GameCallbackCollection;

import java.util.*;

public class GameEngineImpl implements GameEngine, GameCallbackCollection {

    private Map<String, Player> playersMap = new HashMap<>();
    private List<GameCallback> callBackCollection = new ArrayList<>();
    private Deck deck = null;

    @Override
    public void addPlayer(Player player) throws NullPointerException, IllegalArgumentException {
        if (player == null) throw new NullPointerException("Player can't be null");
        if (playersMap.get(player.getId()) != null) throw new IllegalArgumentException("Player ID can't be the"
                + "same as an existing player");
        playersMap.put(player.getId(), player);
        for (GameCallback callback: callBackCollection){
            callback.addPlayer(player);
        }
    }

    @Override
    public void removePlayer(String playerId) throws NullPointerException, IllegalArgumentException {
        if (playerId == null) throw new NullPointerException("Player ID can't be null");
        if(playersMap.get(playerId) != null){
            for (GameCallback callback: callBackCollection){
                callback.removePlayer(playersMap.get(playerId));
            }
            playersMap.remove(playerId);
        } else throw new IllegalArgumentException("Player does not exist");
    }

    @Override
    public Collection<Player> getAllPlayers() {
        List<Player> allPlayers = new ArrayList<>(playersMap.values());
        return Collections.unmodifiableList(allPlayers);
    }

    private void validateBet(String playerId, int betAmount, Suit suit) throws NullPointerException, IllegalArgumentException{
        if (playerId == null) throw new NullPointerException("No player ID entered");
        Player player = playersMap.get(playerId);
        if (player == null) throw new IllegalArgumentException("No player with that ID found");
        if (betAmount < 0) throw new IllegalArgumentException("Amount is not a positive number");
        if (player.getPoints() < betAmount) throw new IllegalArgumentException("Player does not have enough points");
        if (player.getBet().getAmount() > betAmount) throw new IllegalArgumentException("New bet is less than current bet");

        if (suit == null){
            player.assignBet(new ScoreBetImpl(player, betAmount));
        }else{
            player.assignBet(new SuitBetImpl(player, betAmount, suit));
        }
        for (GameCallback callback: callBackCollection){
            callback.betUpdated(player);
        }
    }
    @Override
    public void placeBet(String playerId, int betAmount) throws NullPointerException, IllegalArgumentException {
        validateBet(playerId, betAmount, null);
    }

    @Override
    public void placeBet(String playerId, int betAmount, Suit suit) throws NullPointerException, IllegalArgumentException {
        validateBet(playerId, betAmount, suit);
    }

    @Override
    public void dealPlayer(String playerId, int delay) throws NullPointerException, IllegalArgumentException, IllegalStateException {
        if (playerId == null) throw new NullPointerException("No player ID entered");
        Player player = playersMap.get(playerId);
        if (player == null) throw new IllegalArgumentException("No player with that ID found");
        if (delay < 0) throw new IllegalArgumentException("Delay is less than 0");
        if (player.getBet() == Bet.NO_BET) throw new IllegalStateException("Player supplied has not placed a bet");
        if (player.getHand().getNumberOfCards() != 0) throw new IllegalStateException("Player has already been dealt to");
        if (deck == null) {
            deck= DeckImpl.createShuffledDeck();
            for (GameCallback callback: callBackCollection){
                callback.newDeck(deck);
            }
        }
        boolean dealtCard = true;
        Hand hand = player.getHand();
        Card card = null;
        while(dealtCard){
            card = deck.removeNextCard();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dealtCard = hand.dealCard(card);
            if (dealtCard){
                for (GameCallback callback: callBackCollection){
                    callback.playerCard(player, card);
                }
            }
        }
        for (GameCallback callback : callBackCollection) {
            callback.playerBust(player, card);
        }
    }

    @Override
    public void dealHouse(int delay) throws IllegalArgumentException {
        if (delay < 0) throw new IllegalArgumentException("Delay is less than 0");
        boolean dealtCard = true;
        Hand hand = new HandImpl();
        Card card = null;
        while(dealtCard){
            card = deck.removeNextCard();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dealtCard = hand.dealCard(card);
            if (dealtCard){
                for (GameCallback callback: callBackCollection){
                    callback.houseCard(hand, card);
                }
            }
        }
        for (Map.Entry<String, Player> entry: playersMap.entrySet()){
            Player p = entry.getValue();
            if (p.getBet() != Bet.NO_BET){
                p.applyBetResult(hand);
            }
        }
        for (GameCallback callback : callBackCollection) {
            callback.houseBust(hand, card);
        }
    }

    @Override
    public void resetAllBetsAndHands() {
        for (Map.Entry<String, Player> entry: playersMap.entrySet()){
            Player p = entry.getValue();
            p.resetBet();
            p.getHand().reset();
        }
        deck = null;
    }

    @Override
    public void registerCallback(GameCallback callback) {
        callBackCollection.add(callback);
    }

    @Override
    public void removeCallback(GameCallback callback) {
        callBackCollection.remove(callback);
    }
}
