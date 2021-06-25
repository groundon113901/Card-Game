package controller;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * The type Event listener.
 */
public class EventListener implements MouseListener {

    /**
     * The Gui engine.
     */
    public GuiEngine guiEngine;

    /**
     * This implements mouselistener so that all the mouse based events for the buttons can be handled.
     *
     * @param guiEngine Used perform the GUI functions when the event has been triggered
     */
    public EventListener(GuiEngine guiEngine) {
        this.guiEngine = guiEngine;
    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {}

    /**
     * Mouse Pressed event has been overridden so when the buttons/options are
     * clicked in the GUI the correct event gets triggered.
     * @param mouseEvent mouseEvent comes from the Mouselistener and
     *                   is used to get the component that triggered the event
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        JComponent component = (JComponent) mouseEvent.getComponent();
        switch (mouseEvent.getComponent().getName()){
            case "AboutMenu":
                guiEngine.aboutDialog();
                break;
            case "Add Player":
            case "addPlayer":
                guiEngine.updateStatusBar("Adding player", "");
                guiEngine.addPlayer();
                break;
            case "removePlayer":
                guiEngine.updateStatusBar("Removing player", "");
                guiEngine.removePlayer(component.getParent().getName());
                break;
            case "betButton":
                guiEngine.updateStatusBar("Placing Bet", "");
                guiEngine.placeBet(component.getParent().getName());
                break;
            case "resetBets":
            case "Reset Bets":
                guiEngine.updateStatusBar("Resetting bets and hands", "");
                guiEngine.resetBets();
                break;
            case "dealButton":
                guiEngine.updateStatusBar("Dealing hand", "");
                guiEngine.manuallyDeal(component.getParent().getName());
                break;
            case "showHandButton":
                guiEngine.updateStatusBar("Showing Players hand", "");
                guiEngine.showPlayerHand(component.getParent().getName());
                break;
            case "Instructions":
                guiEngine.updateStatusBar("Showing instructions to game", "");
                guiEngine.showInstructions();
                guiEngine.updateStatusBar("", "");
                break;
            case "dealToHouse":
            case "Deal to House":
                guiEngine.dealToHouse();
                break;
            case "Exit":
                System.exit(0);
                break;
            case "Deal Speed":
                guiEngine.showDealSpeed();
                break;
            default:
                JOptionPane.showMessageDialog(mouseEvent.getComponent(), component.getName() + " was clicked");
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}
}
