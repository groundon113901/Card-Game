package view;

import controller.EventListener;
import model.Player;
import model.bet.Bet;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.util.Map;

/**
 * The type Player panel.
 */
public class PlayerPanel {

    private JPanel playersPanel = new JPanel();
    /**
     * The Event listener.
     */
    public EventListener eventListener;

    /**
     * Instantiates a new Player panel.
     *
     * @param eventListener the event listener
     */
    public PlayerPanel(EventListener eventListener){
        this.eventListener = eventListener;
    }

    /**
     * Create player panel jpanel.
     *
     * @return the jpanel playersPanel
     */
    public JPanel createPlayerPanel(){
        playersPanel.setName("playersPanel");
        playersPanel.setLayout(new BoxLayout(playersPanel, BoxLayout.Y_AXIS));
        playersPanel.setMinimumSize(new Dimension(200, 400));
        playersPanel.add(new JLabel("Players: "));
        return playersPanel;
    }

    /**
     * Add to player panel.
     *
     * @param player      the player
     * @param playerIcons the players icon
     */
    public void addToPlayerPanel(Player player, Map<String, String> playerIcons){
        ImageIcon playersIcon = new ImageIcon("images/" +playerIcons.get(player.getName()) + ".png");
        JPanel playerPanel = new JPanel();
        playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
        playerPanel.setName(player.getName());
        playerPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        JLabel playerIcon = new JLabel();
        playerIcon.setIcon(playersIcon);
        playerIcon.setText("");
        JLabel playerDetails = new JLabel("ID: " + player.getId() + " Name: " + player.getName() + " Current Points: " + player.getPoints());
        playerDetails.setName("playerDetails");
        JLabel playerBet = new JLabel(player.getBet().toString());
        playerBet.setName("playerBet");
        JLabel playerHand = new JLabel(player.getHand().toString());
        playerHand.setName("playerHand");
        playerPanel.add(playerIcon);
        playerPanel.add(playerDetails);
        playerPanel.add(playerBet);
        playerPanel.add(playerHand);
        addBetButton(playerPanel, player);
        addDealButton(playerPanel, player);
        removePlayerButton(playerPanel);
        playersPanel.add(playerPanel, BorderLayout.SOUTH);
    }

    /**
     * Remove player from the playersPanel
     *
     * @param player the player
     */
    public void removePlayer(Player player){
        playersPanel.remove(returnPlayerPanel(player));
    }

    /**
     * Update playersPanel.
     *
     * @param player the player to be updated
     */
    public void updatePlayerPanel(Player player){
        JPanel playerPanel = (JPanel) returnPlayerPanel(player);
        for (Component component : playerPanel.getComponents()) {
            if (component.getName() != null) {
                switch (component.getName()) {
                    case "playerDetails":
                        JLabel playerDetails = (JLabel) component;
                        playerDetails.setText("ID: " + player.getId() +
                                " Name: " + player.getName() + " Current Points: " + player.getPoints());
                        break;
                    case "playerBet":
                        JLabel playerBet = (JLabel) component;
                        playerBet.setText(player.getBet().toString());
                        break;
                    case "playerHand":
                        JLabel playerHand = (JLabel) component;
                        playerHand.setText(player.getHand().toString());
                        break;
                    case "betButton":
                        playerPanel.remove(component);
                        break;
                }
            }
        }
        addBetButton(playerPanel, player);
        addDealButton(playerPanel, player);
        showHandButton(playerPanel, player);
        removePlayerButton(playerPanel);
    }

    /**
     * Gets the component from the supplied panel by name
     * @param panel to be searched
     * @param name of componet to be found
     * @return component founded or not
     */
    private Component getComponent(JPanel panel, String name){
        Component component = null;
        for (Component c: panel.getComponents()){
            if (c != null) {
                if (c.getName() != null) {
                    if (c.getName().equals(name)) {
                        component = c;
                    }
                }
            }
        }
        return component;
    }

    /**
     * Add bet button to playerPanel
     * @param panel panel to be added to
     * @param player to check if player already has bet
     */
    private void addBetButton(JPanel panel, Player player){
        if (player.getBet().getPlayer() == null){
            JButton betButton = new JButton("Add Bet");
            betButton.setName("betButton");
            betButton.addMouseListener(eventListener);
            panel.add(betButton);
        }
    }

    /**
     * Add remove player button
     * @param panel panel to add to
     */
    private void removePlayerButton(JPanel panel){
        if (getComponent(panel,"removePlayer" ) == null){
            JButton removeButton = new JButton("Remove Player");
            removeButton.setName("removePlayer");
            removeButton.addMouseListener(eventListener);
            panel.add(removeButton, BorderLayout.SOUTH);
        }
    }

    /**
     * Add dealHand button to playerPanel for player
     * @param panel for button to be added to
     * @param player for the button to be added for
     */
    private void addDealButton(JPanel panel, Player player){
        Component button = getComponent(panel, "dealButton");
        if (button == null && player.getHand().isEmpty() && player.getBet() != Bet.NO_BET){
            JButton dealButton = new JButton("Deal hand");
            dealButton.setName("dealButton");
            dealButton.addMouseListener(eventListener);
            panel.add(dealButton);
        }
        if (button != null && !player.getHand().isEmpty()){
            panel.remove(button);
        }
    }

    /**
     * Show handButton to playerPanel for player
     * @param panel for button to be added to
     * @param player for the button to be added for
     */
    private void showHandButton(JPanel panel, Player player){
        Component button = getComponent(panel, "showHandButton");;
        if(button == null && !player.getHand().isEmpty()) {
            JButton showHandButton = new JButton("Show hand");
            showHandButton.setName("showHandButton");
            showHandButton.addMouseListener(eventListener);
            panel.add(showHandButton);
        }
        if (button != null && player.getHand().isEmpty()){
            panel.remove(button);
        }
    }

    /**
     * Return playerPanel
     * @param player player whos panel to be returned
     * @return playerPanel component
     */
    private Component returnPlayerPanel(Player player){
        return getComponent(playersPanel, player.getName());
    }
}
