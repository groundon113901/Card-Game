package view;

import controller.GuiEngine;
import model.Player;
import model.card.Card;
import model.card.Deck;
import model.card.Hand;

/**
 * The type Gui call back.
 */
public class GuiCallBack implements GameCallback{

    /**
     * The Gui engine.
     */
    public GuiEngine guiEngine;

    /**
     * Instantiates a new Gui call back.
     *
     * @param guiEngine the gui engine
     */
    public GuiCallBack(GuiEngine guiEngine) {
        this.guiEngine = guiEngine;
    }

    /**
     * When gameEngine calls addPlayer
     * @param player the {@link model.Player} that was added
     */
    @Override
    public void addPlayer(Player player) {
        guiEngine.updateStatusBar("Added player", player.toString());
        guiEngine.addToPlayerPanel(player);
        guiEngine.updateFrame();
    }

    /**
     * When gameEngine calls removePlayer
     * @param player the {@link model.Player} that was removed
     */
    @Override
    public void removePlayer(Player player) {
        guiEngine.updateStatusBar("Removed player", player.toString());
        guiEngine.removePlayer(player);
        guiEngine.updateHandPanel(player.getName());
        guiEngine.updateFrame();
    }

    /**
     * When gameEngine calls betUpdated
     * @param player the {@link model.Player} who's bet and points have been updated
     */
    @Override
    public void betUpdated(Player player){
        guiEngine.updateStatusBar("Bet added", player.getBet().toString());
        guiEngine.updatePlayerPanel(player);
        guiEngine.updateFrame();
        guiEngine.autoDeal();
    }

    /**
     * When gameEngine calls newDeck
     * @param deck the {@link model.card.Deck} that has been created
     */
    @Override
    public void newDeck(Deck deck) {
        guiEngine.updateStatusBar("New Deck has been created", deck.cardsInDeck() + " cards");
    }

    /**
     * When gameEngine calls playerCard
     * @param player the {@link model.Player}
     * @param card the {@link model.card.Card}
     */
    @Override
    public void playerCard(Player player, Card card) {
        guiEngine.updateStatusBar("Dealing Hand", "Player: " + player.getName() + " was dealt " + card.toString());
        guiEngine.showPlayerHand(player.getName());
    }

    /**
     * When gameEngine calls playerBust
     * @param player the {@link model.Player}
     * @param card the {@link model.card.Card}
     */
    @Override
    public void playerBust(Player player, Card card) {
        guiEngine.addToPlayerBustCardMap(player, card);
        guiEngine.showPlayerHand(player.getName());
        guiEngine.updateStatusBar("Dealing to Player", "Player " + player.getName() + " has bust on " + card.toString());
    }


    /**
     * When gameEngine calls houseCard
     * @param houseHand the {@link model.card.Hand}
     * @param card the {@link model.card.Card}
     */
    @Override
    public void houseCard(Hand houseHand, Card card) {
        guiEngine.updateStatusBar("Dealing Hand", "House was dealt " + card.toString());
        guiEngine.showHouseHand(houseHand, card);
    }

    /**
     * When gameEngine calls house bust
     * @param houseHand the {@link model.card.Hand}
     * @param card the {@link model.card.Card}
     */
    @Override
    public void houseBust(Hand houseHand, Card card) {
        guiEngine.updateStatusBar("Dealing to house", "House has bust on " + card.toString());
        guiEngine.showHouseHand(houseHand, card);
        guiEngine.showResults();
        guiEngine.resetBets();
    }
}
