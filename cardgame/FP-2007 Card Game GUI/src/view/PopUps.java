package view;

import model.Player;
import model.bet.BetResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * The type Pop ups.
 */
public class PopUps {
    private final int ICONCOUNT = 7;
    private Map<String, Component> componentMapPlayerForm = new HashMap<String,Component>();
    private Map<String, Component> componentMapBetForm = new HashMap<String,Component>();
    private Map<String, Component> componentMapSuitForm = new HashMap<String,Component>();
    private Map<String, Component> componentMapDealSpeedForm = new HashMap<String,Component>();


    /**
     * Add player form j panel.
     *
     * @return the j panel
     */
    public JPanel addPlayerForm(){
        JPanel playerInput = new JPanel(new BorderLayout(5,5));
        JPanel labels = new JPanel(new GridLayout(0,1,2,2));
        labels.add(new JLabel("Player Name: ", SwingConstants.RIGHT));
        labels.add(new JLabel("Number of Points: ", SwingConstants.RIGHT));
        labels.add(new JLabel("Icon:", SwingConstants.RIGHT));
        ButtonGroup buttonGroup = new ButtonGroup();
        playerInput.add(labels, BorderLayout.WEST);
        JPanel inputs = new JPanel(new GridLayout(0,1,2,2));
        JTextField name = new JTextField();
        componentMapPlayerForm.put("name", name);
        JTextField points = new JTextField();
        componentMapPlayerForm.put("points", points);
        JPanel iconPanel = new JPanel(new GridLayout(0,6,2,2));
        componentMapPlayerForm.put("iconPanel", iconPanel);
        inputs.add(name);
        inputs.add(points);
        for (var i = 1; i < ICONCOUNT; i++){
            ImageIcon icon = new ImageIcon("images/icon" + i + ".png");
            JToggleButton button = new JToggleButton(icon);
            button.setName("icon"+i);
            iconPanel.add(button);
            buttonGroup.add(button);
        }
        inputs.add(iconPanel);
        playerInput.add(inputs);
        return playerInput;
    }

    /**
     * Bet form j panel.
     *
     * @return the j panel
     */
    public JPanel betForm(){
        String[] type = {"Score", "Suit"};
        JComboBox<String> typeDropDown = new JComboBox<>(type);
        componentMapBetForm.put("typeDropDown", typeDropDown);
        JTextField amount = new JTextField();
        componentMapBetForm.put("amount", amount);
        JPanel placeBet = new JPanel(new BorderLayout(5,5));
        JPanel label = new JPanel(new GridLayout(0,1,2,2));
        label.add(new JLabel("Bet Amount: ", SwingConstants.RIGHT));
        label.add(new JLabel("Bet Type: ", SwingConstants.RIGHT));
        placeBet.add(label, BorderLayout.WEST);
        JPanel input = new JPanel(new GridLayout(0,1,2,2));
        input.add(amount);
        input.add(typeDropDown);
        placeBet.add(input, BorderLayout.CENTER);
        return placeBet;
    }

    /**
     * Suit bet form j panel.
     *
     * @return the j panel
     */
    public JPanel suitBetForm(){
        String[] suits = {"Clubs", "Diamonds", "Hearts", "Spades"};
        JComboBox<String> suitsDropDown = new JComboBox<>(suits);
        componentMapSuitForm.put("suitsDropDown", suitsDropDown);
        JPanel suitbet = new JPanel(new BorderLayout(5,5));
        JPanel suitLabel = new JPanel(new GridLayout(0,1,2,2));
        suitLabel.add(new JLabel("Bet Suit: ", SwingConstants.RIGHT));
        suitbet.add(suitLabel, BorderLayout.WEST);
        JPanel suitInput = new JPanel(new GridLayout(0,1,2,2));
        suitInput.add(suitsDropDown);
        suitbet.add(suitInput, BorderLayout.CENTER);
        return suitbet;
    }

    /**
     * Show deal speed j panel.
     *
     * @return the j panel
     */
    public JPanel showDealSpeed(){
        JPanel dealSpeed = new JPanel(new BorderLayout(5, 5));
        JPanel labels = new JPanel(new GridLayout(0,1,2,2));
        labels.add(new JLabel("Deal Speed: ", SwingConstants.RIGHT));
        dealSpeed.add(labels);
        JPanel inputs = new JPanel(new GridLayout(0,1,2,2));
        String[] options = {"Slow", "Default", "Fast"};
        JComboBox<String> speedDropDown = new JComboBox<>(options);
        inputs.add(speedDropDown);
        dealSpeed.add(inputs);
        componentMapDealSpeedForm.put("speedDropDown", speedDropDown);
        return dealSpeed;
    }


    /**
     * Show results j panel.
     *
     * @param players the players
     * @return the j panel
     */
    public JPanel showResults(Map<BetResult, Player> players){
        JPanel results = new JPanel(new BorderLayout(5,5));
        results.add(new JLabel("Bet Results: "));
        JPanel labels = new JPanel(new GridLayout(0,1,2,2));
        Map<BetResult, Player> orderPlayers = new TreeMap<>(
                Comparator.reverseOrder()
        );
        orderPlayers.putAll(players);
        for (Map.Entry<BetResult, Player> entry: orderPlayers.entrySet()){
            labels.add(new JLabel("Player: " + entry.getValue().getName() + " Bet Result: "+
                    entry.getValue().getBet().getResult() + " " +  entry.getValue().getBet().getOutcome()));
        }
        results.add(labels);
        return results;
    }

    /**
     * About dialog.
     *
     * @param aboutDialog the about dialog
     */
    public void aboutDialog(JDialog aboutDialog){
        aboutDialog.setLayout(new FlowLayout());
        JButton b = new JButton("Ok");
        b.setName("dialogOk");
        b.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                aboutDialog.setVisible(false);
            }
        });
        aboutDialog.add( new JLabel ("Student Name: Justin Gerussi"));
        aboutDialog.add( new JLabel ("Student Number: s3644927"));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        aboutDialog.add( new JLabel ("Current Time: "+ dtf.format(now)));
        aboutDialog.add(b);
        aboutDialog.setSize(300,300);
        aboutDialog.setVisible(true);
        aboutDialog.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }

    /**
     * Gets component map player form.
     *
     * @return the component map player form
     */
    public Map<String, Component> getComponentMapPlayerForm() {
        return componentMapPlayerForm;
    }

    /**
     * Gets component map bet form.
     *
     * @return the component map bet form
     */
    public Map<String, Component> getComponentMapBetForm() {
        return componentMapBetForm;
    }

    /**
     * Gets component map suit form.
     *
     * @return the component map suit form
     */
    public Map<String, Component> getComponentMapSuitForm() {
        return componentMapSuitForm;
    }

    /**
     * Gets component map deal speed form.
     *
     * @return the component map deal speed form
     */
    public Map<String, Component> getComponentMapDealSpeedForm() {return componentMapDealSpeedForm;}
}
