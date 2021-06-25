package view;

import controller.EventListener;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * The type Menu bar view.
 */
public class MenuBarView {

    /**
     * The Event listener.
     */
    public EventListener eventListener;

    /**
     * Instantiates a new Menu bar view.
     *
     * @param eventListener the event listener
     */
    public MenuBarView(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * Create menu bar j menu bar.
     *
     * @return the j menu bar
     */
    public JMenuBar createMenuBar(){
        JMenuBar  menu = new JMenuBar();
        menu.setName("menuBar");
        JMenu game = new JMenu("Game");
        game.setName("GameMenu");
        game.setMnemonic(KeyEvent.VK_G);
        JMenu help = new JMenu("Help");
        help.setName("HelpMenu");
        help.setMnemonic(KeyEvent.VK_H);
        JMenu players = new JMenu("Players");
        players.setName("PlayersMenu");
        players.setMnemonic(KeyEvent.VK_P);
        JMenu about = new JMenu("About");
        about.setName("AboutMenu");
        about.setMnemonic(KeyEvent.VK_A);
        String[] gameMenuOptions = {"Deal Speed", "Deal to House", "Reset Bets", "Exit"};
        String[] helpMenuOptions = {"Instructions"};
        String[] playersMenuOptions = {"Add Player"};
        about.addMouseListener(eventListener);
        for (String s: gameMenuOptions){
            JMenuItem menuItem = new JMenuItem(s);
            menuItem.setName(s);
            menuItem.addMouseListener(eventListener);
            game.add(menuItem);
        }
        for (String s: helpMenuOptions){
            JMenuItem menuItem = new JMenuItem(s);
            menuItem.setName(s);
            menuItem.addMouseListener(eventListener);
            help.add(menuItem);
        }
        for (String s: playersMenuOptions){
            JMenuItem menuItem = new JMenuItem(s);
            menuItem.setName(s);
            menuItem.addMouseListener(eventListener);
            players.add(menuItem);
        }
        menu.add(game);
        menu.add(help);
        menu.add(players);
        menu.add(about);
        return menu;
    }
}
