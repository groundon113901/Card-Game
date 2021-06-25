package view;

import controller.EventListener;

import javax.swing.*;

/**
 * The type Tool bar view.
 */
public class ToolBarView{
    /**
     * The Event listener.
     */
    public EventListener eventListener;

    /**
     * Instantiates a new Tool bar view.
     *
     * @param eventListener the event listener
     */
    public ToolBarView(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    /**
     * Create tool bar j tool bar.
     *
     * @return the j tool bar
     */
    public JToolBar createToolBar(){
        JToolBar toolbar = new JToolBar();
        toolbar.setName("toolBar");
        JPanel p = new JPanel();
        p.setName("toolBarPanel");
        JButton addPlayer = new JButton("Add player");
        addPlayer.setName("addPlayer");
        addPlayer.addMouseListener(eventListener);
        JButton resetBets = new JButton("Reset Bets");
        resetBets.setName("resetBets");
        resetBets.addMouseListener(eventListener);
        JButton dealToHouse = new JButton("Deal to House");
        dealToHouse.setName("dealToHouse");
        dealToHouse.addMouseListener(eventListener);
        p.add(addPlayer);
        p.add(resetBets);
        p.add(dealToHouse);
        toolbar.add(p);
        return toolbar;
    }
}
