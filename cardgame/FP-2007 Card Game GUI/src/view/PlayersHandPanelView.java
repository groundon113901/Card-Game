package view;


import model.Player;
import model.card.Card;
import model.card.Hand;
import model.card.Suit;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.Collection;
import java.util.Map;

/**
 * The type Players hand panel view.
 */
public class PlayersHandPanelView {

    /**
     * The Players hand panel.
     */
    JPanel playersHandPanel = new JPanel();

    /**
     * Create players hand panel j panel.
     *
     * @return the j panel
     */
    public JPanel createPlayersHandPanel() {
        playersHandPanel.setName("playersHandPanel");
        playersHandPanel.setOpaque(true);
        playersHandPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        playersHandPanel.setBackground(Color.green);
        playersHandPanel.setLayout(new BoxLayout(playersHandPanel, BoxLayout.Y_AXIS));
        return playersHandPanel;
    }

    /**
     * Gets the Icon for the card supplied
     * @param card to get the icon for
     * @return a JLabel which has the card Icon
     */
    private JLabel getCardIcon(Card card){
        ImageIcon cardIcon = new ImageIcon("images/cards/" + card.toString() + ".png");
        JLabel cardLabel = new JLabel();
        cardLabel.setIcon(cardIcon);
        cardLabel.setText("");
        return cardLabel;
    }

    /**
     * Show player cards.
     * Shows the given players cards
     * @param player    the player
     * @param bustCards the bust cards
     * @param suitMap   the suit map
     */
    public void showPlayerCards(Player player, Map<Player, Card> bustCards, Map<Player, Suit> suitMap){
        JPanel playerHandPanel = new JPanel();
        playerHandPanel.setOpaque(false);
        playerHandPanel.setName(player.getName());
        Card bustCard = bustCards.get(player);
        Hand hand = player.getHand();
        Collection<Card> c = hand.getCards();
        JPanel dealtCardPanel = new JPanel();
        dealtCardPanel.setOpaque(false);
        JPanel bustedCardPanel = new JPanel();
        bustedCardPanel.setOpaque(false);
        dealtCardPanel.setName("dealtCardPanel");
        for (Card card: c){
            dealtCardPanel.add(getCardIcon(card));
        }
        if (bustCard != null) {
            bustedCardPanel.add(new JLabel("Bust on Card"));
            bustedCardPanel.add(getCardIcon(bustCard));
        }
        JPanel handDetails = new JPanel();
        handDetails.setOpaque(false);
        handDetails.add(new JLabel("Hand Score: " + player.getHand().getScore()));
        if (suitMap.get(player) != null) {
            handDetails.add(new JLabel("Count of " + suitMap.get(player).toString() + " in hand: " + player.getHand().getSuitCount(suitMap.get(player))));
        }
        if (playersHandPanel.getComponents() != null) {
            for (Component component : playersHandPanel.getComponents()) {
                    playersHandPanel.remove(component);
            }
        }
        playerHandPanel.add(new JLabel(player.getName() + "'s Hand:"));
        playerHandPanel.add(dealtCardPanel);
        playerHandPanel.add(handDetails);
        playerHandPanel.add(bustedCardPanel);
        playersHandPanel.add(playerHandPanel);
    }

    /**
     * Show house hand.
     *
     * @param houseHand the house hand
     * @param card      the card
     */
    public void showHouseHand(Hand houseHand, Card card){
        if (playersHandPanel.getComponents() != null) {
            for (Component component : playersHandPanel.getComponents()) {
                playersHandPanel.remove(component);
            }
        }
        Collection<Card> cards = houseHand.getCards();
        JPanel dealtCardPanel = new JPanel();
        dealtCardPanel.setOpaque(false);
        dealtCardPanel.setName("dealtCardPanel");
        for (Card c: cards){
            dealtCardPanel.add(getCardIcon(c));
        }
        JPanel handDetails = new JPanel();
        handDetails.setOpaque(false);
        handDetails.add(new JLabel("Hand Score: " + houseHand.getScore()));
        playersHandPanel.add(new JLabel("Houses Hand:"));
        playersHandPanel.add(dealtCardPanel);
        playersHandPanel.add(handDetails);
    }

    /**
     * Clears playerHandPanel.
     */
    public void clearPanel(){
        if (playersHandPanel.getComponents() != null) {
            for (Component component : playersHandPanel.getComponents()) {
                playersHandPanel.remove(component);
            }
        }
    }

    /**
     * Update panel.
     * Used when the playerRemoved from game had hand in panel.
     * @param playername the playername
     */
    public void updatePanel(String playername){
        if (playersHandPanel.getComponents() != null){
            for (Component component : playersHandPanel.getComponents()) {
                if (component.getName().equals(playername)){
                    playersHandPanel.remove(component);
                }
            }
        }
    }
}
