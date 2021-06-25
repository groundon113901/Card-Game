package controller;

import model.GameEngine;
import model.Player;
import model.PlayerImpl;
import model.bet.Bet;
import model.bet.BetResult;
import model.card.Card;
import model.card.Hand;
import model.card.Suit;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.lang.Integer.parseInt;

/**
 * The type Gui engine.
 */
public class GuiEngine {

    private JFrame frame = new JFrame();
    /**
     * The Game engine.
     */
    public GameEngine gameEngine;
    /**
     * Creates the Event listener object.
     */
    public EventListener eventListener = new EventListener(this);
    /**
     * Create the Menubar object
     */
    private MenuBarView menuBar = new MenuBarView(eventListener);
    /**
     * Create the ToolBarView object
     */
    private ToolBarView toolBar = new ToolBarView(eventListener);
    /**
     * Create the StatusBarView object
     */
    private StatusBarView statusBar = new StatusBarView();
    /**
     * Create the PopUps object
     */
    private PopUps popUps = new PopUps();
    /**
     * Create the PlayerPanel object
     */
    private PlayerPanel playerPanel = new PlayerPanel(eventListener);
    /**
     * Create the PlayersHandPanelView object
     */
    private PlayersHandPanelView playerHandPanel = new PlayersHandPanelView();
    /**
     * Sets the playerID count to 0 to allow for automatic ID for each player
     */
    private int playerID = 0;
    /**
     * Prevents magic numbers for the slow settings for deal speed
     */
    private final int SLOW = 1500;
    /**
     * Prevents magic numbers for the default settings for deal speed
     */
    private final int DEFAULT = 500;
    /**
     * Prevents magic numbers for the fast settings for deal speed
     */
    private final int FAST = 150;
    /**
     * Used to store the player name and icon name they selected to use for the playerPanel
     */
    private Map<String, String> playerIconMap = new HashMap<>();
    /**
     * Used to store which card each player had busted on to display in the playerHandPanel
     */
    private Map<Player, Card> playerBustCardMap = new HashMap<>();
    /**
     * Used to store which bet the player had selected for a suitBet
     */
    private Map<Player, Suit> playerBetSuit = new HashMap<>();
    /**
     * Presets the dealSpeed to default which then can be changed later
     */
    private int dealSpeed = DEFAULT;

    /**
     * Instantiates a new Gui engine.
     *
     * @param gameEngine the game engine
     * @author Justin Gerussi
     */
    public GuiEngine(GameEngine gameEngine){
        this.gameEngine = gameEngine;
    }

    /**
     * Create main window.
     * Adds all elements of the main window, including toolbar, menubar, playerPanel and PlayerHandPanel
     * @author Justin Gerussi
     */
    public void createMainWindow(){
        frame.setSize(1500,1000);
        frame.setMinimumSize(new Dimension(800, 800));
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setTitle("Card Game");
        frame.setJMenuBar(menuBar.createMenuBar());
        frame.add(toolBar.createToolBar(), BorderLayout.NORTH);
        frame.add(statusBar.createStatusBar(frame.getWidth()), BorderLayout.SOUTH);
        frame.add(playerPanel.createPlayerPanel(), BorderLayout.LINE_START);
        frame.add(playerHandPanel.createPlayersHandPanel(), BorderLayout.CENTER);
        updateFrame();
    }

    /**
     * Update frame.
     * Updates the frame by invalidating, re-validating and repaints to reflect changes made in the gui
     */
    public void updateFrame(){
        frame.invalidate();
        frame.validate();
        frame.repaint();
    }

    /**
     * Finds the player via the player name supplied
     * @param playerName the player name to be searched
     * @return the player or if not found returns null
     */
    private Player findPlayer (String playerName){
        Player player = null;
        for (Player p: gameEngine.getAllPlayers()){
            if (p.getName().equals(playerName)){
                player = p;
            }
        }
        return player;
    }

    /**
     * Gets the togglebutton from the Addplayer form
     * @param iconPanel from the addPlayer function
     * @return the JtoggleButton selected in the panel
     */
    private JToggleButton getIcon(JPanel iconPanel){
        JToggleButton icon = null;
        for (Component c: iconPanel.getComponents()){
            JToggleButton b = (JToggleButton)c;
            if (b.isSelected()) {
                icon = b;
            }
        }
        return icon;
    }

    /**
     * Converts string to int, helps seperate this function to perform the try catch block
     * @param number to be converted to an int
     * @return an int of the converted string or -1 if could not be parsed
     */
    private int convertToInt(String number){
        int convertedString;
        try{
            convertedString = parseInt(number);
        }catch(Exception e){
            convertedString = -1;
        }
        return convertedString;
    }

    /**
     * Checks to see if supplied thread name is active
     * @param threadName the thread to be checked if active
     * @return the thread but if not active will return null
     */
    private Thread checkThread(String threadName){
        Thread thread = null;
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread t: threadSet){
            if(t.getName().equals(threadName)){
                thread = t;
            }
        }
        return thread;
    }

    /**
     * Add player.
     * This is trigged when the add player button is clicked, this creates the player form to get player info,
     * validates the player form to make sure the input is valid and then triggers the gameEngine to add the player
     */
    public void addPlayer() {
        boolean iconSelected = false;
        String playerIcon = null;
        int convertPointsToInt;
        Thread dealingPlayerHand = checkThread("dealPlayer");
        Thread dealingHouseHand = checkThread("dealHouse");
        if(dealingPlayerHand == null && dealingHouseHand == null) {
            JPanel playerInput = popUps.addPlayerForm();
            JTextField name = (JTextField) popUps.getComponentMapPlayerForm().get("name");
            JTextField points = (JTextField) popUps.getComponentMapPlayerForm().get("points");
            JPanel iconPanel = (JPanel) popUps.getComponentMapPlayerForm().get("iconPanel");
            int option = JOptionPane.showConfirmDialog(frame, playerInput, "Player Details", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                Player player = findPlayer(name.getText());
                JToggleButton iconSelectedComponent = getIcon(iconPanel);
                if (iconSelectedComponent != null) {
                    iconSelected = iconSelectedComponent.isSelected();
                    playerIcon = iconSelectedComponent.getName();
                }
                convertPointsToInt = convertToInt(points.getText());
                String finalPlayerIcon = playerIcon;
                if (!name.getText().isEmpty() && !points.getText().isEmpty() && player == null && iconSelected && playerIcon != null && convertPointsToInt > 0) {
                    new Thread(() -> {
                        playerIconMap.put(name.getText(), finalPlayerIcon);
                        gameEngine.addPlayer(new PlayerImpl(Integer.toString(playerID + 1), name.getText(), convertPointsToInt));
                    }).start();
                    SwingUtilities.invokeLater(() -> {
                        statusBar.updateStatusBar("Adding player", name.getText());
                        playerID++;
                    });
                } else {
                    if (name.getText().isEmpty()) {
                        statusBar.updateStatusBar("Adding player Failed", "Name was not entered");
                        JOptionPane.showMessageDialog(frame, "Name was not entered", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (points.getText().isEmpty()) {
                        statusBar.updateStatusBar("Adding player Failed", "Number of points was not entered");
                        JOptionPane.showMessageDialog(frame, "Number of points was not entered", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (player != null) {
                        statusBar.updateStatusBar("Adding player Failed", "Player already exists");
                        JOptionPane.showMessageDialog(frame, "Player already exists", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (!iconSelected) {
                        statusBar.updateStatusBar("Adding player Failed", "No Icon Selected");
                        JOptionPane.showMessageDialog(frame, "No Icon Selected", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (convertPointsToInt == -1) {
                        statusBar.updateStatusBar("Adding player Failed", "Points entered was not an Integer");
                        JOptionPane.showMessageDialog(frame, "Points entered was not an Integer", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (convertPointsToInt <= 0 && convertPointsToInt != -1) {
                        statusBar.updateStatusBar("Adding player Failed", "Points must be greater than 0");
                        JOptionPane.showMessageDialog(frame, "Points must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                statusBar.updateStatusBar("Adding player Cancelled", "Successful");
            }
            statusBar.updateStatusBar("", "");
        }else{
            if (dealingHouseHand != null){
                JOptionPane.showMessageDialog(frame, "Can't add player while the house is being dealt to", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (dealingPlayerHand != null){
                JOptionPane.showMessageDialog(frame, "Can't add player while a player is being dealt to", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Remove player.
     * This is triggered when the remove player button is clicked, it checks if the gameEngine is dealing to the house
     * or player, if not allows the gameEngine to remove that player and then is removed from the GUI as well
     * @param playerName the player name
     */
    public void removePlayer(String playerName){
        Thread dealingPlayerHand = checkThread("dealPlayer");
        Thread dealingHouseHand = checkThread("dealHouse");
        if (dealingHouseHand == null && dealingPlayerHand == null) {
            Player player = findPlayer(playerName);
            if (player != null) {
                new Thread(() -> gameEngine.removePlayer(player.getId())).start();
                SwingUtilities.invokeLater(() -> statusBar.updateStatusBar("Removing player", player.getId()));
            }
        }else{
            if (dealingHouseHand != null){
                JOptionPane.showMessageDialog(frame, "Can't remove player while the house is being dealt to", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (dealingPlayerHand != null){
                JOptionPane.showMessageDialog(frame, "Can't remove player while a player is being dealt to", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Place bet.
     * This is trigger when the place bet button is clicked, creates the form to get the bet info,
     * validates if the input is correct and then calls the gameEngine to add the bet,
     * which then updates the GUI with this information
     * @param playerName the player name for the bet to be placed for.
     */
    public void placeBet(String playerName){
        Thread dealingPlayerHand = checkThread("dealPlayer");
        Thread dealingHouseHand = checkThread("dealHouse");
        if(dealingHouseHand == null && dealingPlayerHand == null) {
            Player player = findPlayer(playerName);
            JPanel placeBet = popUps.betForm();
            JPanel suitbet = popUps.suitBetForm();
            JTextField amount = (JTextField) popUps.getComponentMapBetForm().get("amount");
            JComboBox typeDropDown = null;
            if (popUps.getComponentMapBetForm().get("typeDropDown") instanceof JComboBox) {
                typeDropDown = (JComboBox) popUps.getComponentMapBetForm().get("typeDropDown");
            }
            int result = JOptionPane.showConfirmDialog(frame, placeBet, "Place Bet", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                int convertPointsToInt = convertToInt(amount.getText());
                if (!amount.getText().isEmpty() && player != null && convertPointsToInt > 0 && convertPointsToInt <= player.getPoints()) {
                    if (typeDropDown != null && typeDropDown.getSelectedItem() != null && typeDropDown.getSelectedItem().toString().equals("Suit")) {
                        int suitRsult = JOptionPane.showConfirmDialog(frame, suitbet, "Select Suit", JOptionPane.OK_CANCEL_OPTION);
                        JComboBox suitsDropDown = null;
                        if (popUps.getComponentMapSuitForm().get("suitsDropDown") instanceof JComboBox) {
                            suitsDropDown = (JComboBox) popUps.getComponentMapSuitForm().get("suitsDropDown");
                        }
                        if (suitRsult == JOptionPane.OK_OPTION) {
                            if (suitsDropDown != null && suitsDropDown.getSelectedItem() != null) {
                                String suitSelected = (String) suitsDropDown.getSelectedItem();
                                Suit suit = null;
                                switch (suitSelected) {
                                    case "Clubs":
                                        suit = Suit.CLUBS;
                                        break;
                                    case "Hearts":
                                        suit = Suit.HEARTS;
                                        break;
                                    case "Diamonds":
                                        suit = Suit.DIAMONDS;
                                        break;
                                    case "Spades":
                                        suit = Suit.SPADES;
                                        break;
                                    default:
                                        break;
                                }
                                Suit finalSuit = suit;
                                Thread placeBetThread = new Thread(() -> gameEngine.placeBet(player.getId(), convertPointsToInt, finalSuit));
                                placeBetThread.setName("placeBetThread");
                                placeBetThread.start();
                                JComboBox finalSuitsDropDown = suitsDropDown;
                                SwingUtilities.invokeLater(() -> {
                                    statusBar.updateStatusBar("Adding bet", "Player: " + player.getName() + "for " + convertPointsToInt + "on " + finalSuitsDropDown.getSelectedItem().toString());
                                    playerBetSuit.put(player, finalSuit);
                                });
                            }
                        }
                    }
                    if (typeDropDown != null && typeDropDown.getSelectedItem() != null && typeDropDown.getSelectedItem().toString().equals("Score") && convertPointsToInt > 0) {
                        Thread placeBetThread = new Thread(() -> gameEngine.placeBet(player.getId(), convertPointsToInt));
                        placeBetThread.setName("placeBetThread");
                        placeBetThread.start();
                        SwingUtilities.invokeLater(() -> statusBar.updateStatusBar("Adding bet", "Player: " + player.getName() + "for " + convertPointsToInt + "on Score"));
                    }
                } else {
                    if (convertPointsToInt == -1) {
                        statusBar.updateStatusBar("Adding bet", "Error: Points entered was not an Integer");
                        JOptionPane.showMessageDialog(frame, "Points entered was not an Integer", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (convertPointsToInt <= 0 && convertPointsToInt != -1) {
                        statusBar.updateStatusBar("Adding bet", "Error: Points must be greater than 0");
                        JOptionPane.showMessageDialog(frame, "Points must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (amount.getText().isEmpty()) {
                        statusBar.updateStatusBar("Adding bet", "Error: Number of points was not entered");
                        JOptionPane.showMessageDialog(frame, "Number of points was not entered", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (player == null){
                        statusBar.updateStatusBar("Adding bet", "Error: Player does not exist");
                        JOptionPane.showMessageDialog(frame, "Error: Player does not exist", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (player != null && convertPointsToInt > player.getPoints()) {
                        statusBar.updateStatusBar("Adding bet", "Error: Player does not have enough points");
                        JOptionPane.showMessageDialog(frame, "Player does not have enough points", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                statusBar.updateStatusBar("Cancelled Bet", "Successful");
                JOptionPane.showMessageDialog(frame, "Cancelled Bet");
            }
            statusBar.updateStatusBar("", "");
        }
        else{
            if (dealingHouseHand != null){
                JOptionPane.showMessageDialog(frame, "Can't place bet while the house is being dealt to", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (dealingPlayerHand != null){
                JOptionPane.showMessageDialog(frame, "Can't place bet while a player is being dealt to", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Reset bets.
     * Triggered when the reset bet button is clicked,
     * calls the gameEgine to reset all the bets and hands, this then calls the GUI to reset it as well
     */
    public void resetBets(){
        new Thread(() -> gameEngine.resetAllBetsAndHands()).start();
        SwingUtilities.invokeLater(() -> {
            for (Player p: gameEngine.getAllPlayers()){
                playerPanel.updatePlayerPanel(p);
            }
            playerBustCardMap.clear();
            playerHandPanel.clearPanel();
            statusBar.updateStatusBar("Bets and Hands Reset", "Successful");
            JOptionPane.showMessageDialog(frame, "Bets and Hands Reset");
            updateFrame();
            statusBar.updateStatusBar("", "");
        });
    }

    /**
     * Show player hand.
     * When called it shows the selected players hand in the main panel
     * @param playerName the player name to show their hand
     */
    public void showPlayerHand(String playerName){
        Player player = findPlayer(playerName);
        playerHandPanel.showPlayerCards(player, playerBustCardMap, playerBetSuit);
        updateFrame();
        updateHandPanel(playerName);
    }

    /**
     * Manually deal.
     * Triggered if deal hand clicked for the player, this calls the gameEngine to deal to the player directly.
     * Is also used for the auto dealing function but this just handles dealing to a singlar player
     * @param playerName the player name to deal a hand to
     */
    public void manuallyDeal(String playerName){
        Player player = findPlayer(playerName);
        if(player.getBet() == Bet.NO_BET){
            statusBar.updateStatusBar("Dealing", "Player " + player.getName() + " has not placed a bet ");
            JOptionPane.showMessageDialog(frame, "Player " + player.getName() + " has not placed a bet ");
        } else {
            if (player.getHand().isEmpty()) {
                Thread dealingPlayer = new Thread(() -> gameEngine.dealPlayer(player.getId(), dealSpeed));
                dealingPlayer.setName("dealPlayer");
                dealingPlayer.start();
                SwingUtilities.invokeLater(() -> statusBar.updateStatusBar("Dealing", "Player " + player.getName() + " will be dealt to"));
            }else{
                statusBar.updateStatusBar("Dealing", "Player " + player.getName() + " already has a hand ");
                JOptionPane.showMessageDialog(frame, "Player " + player.getName() + " already has a hand ");
            }
        }
    }

    /**
     * Show deal speed.
     * Shows the option to adjust the dealspped which is then stored in the global variable for dealspeed
     */
    public void showDealSpeed(){
        JPanel dealSpeed = popUps.showDealSpeed();
        Map<String, Component> componentMapDealSpeedForm = popUps.getComponentMapDealSpeedForm();
        int result =  JOptionPane.showConfirmDialog(frame, dealSpeed, "Select Deal Speed", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION){
        	 JComboBox speedDropDown = (JComboBox) componentMapDealSpeedForm.get("speedDropDown");
            if (speedDropDown.getSelectedItem() != null) {
                switch (speedDropDown.getSelectedItem().toString()) {
                    case "Slow":
                        this.dealSpeed = SLOW;
                        break;
                    case "Fast":
                        this.dealSpeed = FAST;
                        break;
                    default:
                        this.dealSpeed = DEFAULT;
                        break;
                }
            }
        }
        JOptionPane.showMessageDialog(frame, "Deal speed set to:" + this.dealSpeed);
    }

    /**
     * Deal to house.
     * Called to deal to the house, which then finalises the bets currently placed to then show the results.
     */
    public void dealToHouse(){
        boolean canDeal = false;
        for (Player p: gameEngine.getAllPlayers()){
            if (p.getBet() != Bet.NO_BET && !p.getHand().isEmpty()){
                    canDeal = true;
            }
        }
        if(canDeal) {
            Thread dealingHouse = new Thread(() -> gameEngine.dealHouse(dealSpeed));
            dealingHouse.setName("dealHouse");
            dealingHouse.start();
        }else{
            JOptionPane.showMessageDialog(frame, "No player has a bet", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Auto deal.
     * Is called every time a bet is added to see if all players have
     * placed a bet to then automatically deal to them all then the house once completed.
     */
    public void autoDeal() {
        int count = 0;
        for (Player p: gameEngine.getAllPlayers()){
            if (p.getBet() != Bet.NO_BET){
                count++;
            }
        }
        if (count == gameEngine.getAllPlayers().size()){
            for (Player p: gameEngine.getAllPlayers()){
                if (p.getHand().isEmpty()){
                    manuallyDeal(p.getName());
                }
                try {
                    checkThread("dealPlayer").join();
                } catch (Exception e){
                    System.out.println("Exception has been" +
                            " caught" + e);
                }
            }
            dealToHouse();
        }
    }

    /**
     * Update status bar.
     * Used to show the status of the engines or what the game is doing without using a message pop up.
     * @param function the function string to display
     * @param message  the message string to display
     */
    public void updateStatusBar(String function, String message){
        statusBar.updateStatusBar(function, message);
        updateFrame();
    }

    /**
     * Add to player panel.
     * Used to keep playerPanel private and call the addToPlayerPanel in the playerPanel object
     * @param player the player
     */
    public void addToPlayerPanel(Player player) {
        playerPanel.addToPlayerPanel(player, playerIconMap);
    }

    /**
     * Remove player.
     * Used to keep playerPanel private and call the removePlayer in the playerPanel object
     * @param player the player
     */
    public void removePlayer(Player player){
        playerPanel.removePlayer(player);
    }

    /**
     * Update player panel.
     * Used to keep playerPanel private and call the updatePlayerPanel in the playerPanel object
     * @param player the player
     */
    public void updatePlayerPanel(Player player){
        playerPanel.updatePlayerPanel(player);
    }

    /**
     * Show house hand.
     *Used to keep playerHandPanel private and call the showHouseHand in the playerHandPanel object
     * @param houseHand the house hand
     * @param card      the card
     */
    public void showHouseHand(Hand houseHand, Card card){
        playerHandPanel.showHouseHand(houseHand, card);
        updateFrame();
    }

    /**
     * Update hand panel.
     * Used to keep playerHandPanel private and call the updateHandPanel in the playerHandPanel object
     * @param playerName the player name
     */
    public void updateHandPanel(String playerName){
        playerHandPanel.updatePanel(playerName);
    }

    /**
     * Show results.
     * Used to keep gameEngine exposed to GuiEngine and
     * gets the players and their bet result into a map to then show the results in a message dialog
     */
    public void showResults(){
        Map<BetResult, Player> players = new HashMap<>();
        for (Player p: gameEngine.getAllPlayers()){
            if (p.getBet() != Bet.NO_BET && !p.getHand().isEmpty()) {
                players.put(p.getBet().getResult(), p);
            }
        }
        JOptionPane.showMessageDialog(frame, popUps.showResults(players));
    }

    /**
     * Add to player bust card map.
     * Used to keep playerBustCardMap private and add the supplied card to the supplied player in the hashMap
     * @param player the player
     * @param card   the card
     */
    public void addToPlayerBustCardMap(Player player, Card card){
        playerBustCardMap.put(player, card);
    }

    /**
     * About dialog.
     * Used to keep frame private, creating the JDialog to parse to create the rest of the conent for the about dialog
     */
    public void aboutDialog(){
        JDialog aboutDialog = new JDialog(frame, "About", true);
        popUps.aboutDialog(aboutDialog);
    }

    /**
     * Show instructions.
     * Used to keep frame private and show the instructions for the game.
     */
    public void showInstructions(){
        JOptionPane.showMessageDialog(frame, "Card Game: \n 1. Add player/s \n " +
                "2. After all players added, each player must place a bet on either the score of hand or a suit bet \n"
                +
                " 3. Each player after they have bet can deal a hand or wait until all players have bet \n" +
                " 4. House will be dealt hand and results will be show.");
    }
}
