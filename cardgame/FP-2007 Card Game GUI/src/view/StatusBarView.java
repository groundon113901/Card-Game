package view;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;

/**
 * The type Status bar view.
 */
public class StatusBarView {

    private JPanel statusPanel = new JPanel();
    private JPanel statusLeftPanel = new JPanel();
    private JPanel statusRightPanel = new JPanel();
    private JLabel statusLeftLabel = new JLabel("status");
    private JLabel statusRightLabel = new JLabel("mcstatus");

    /**
     * Create status bar j panel.
     *
     * @param frameWidth the frame width
     * @return the j panel
     */
    public JPanel createStatusBar(int frameWidth){
        statusPanel.setName("statusPanel");
        statusLeftPanel.setName("statusPanelLeft");
        statusRightPanel.setName("statusPanelRight");
        statusLeftPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusRightPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusPanel.add(statusLeftPanel, BorderLayout.WEST);
        statusPanel.add(statusRightPanel, BorderLayout.EAST);
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        statusLeftPanel.setPreferredSize(new Dimension(frameWidth, 30));
        statusRightPanel.setPreferredSize(new Dimension(frameWidth, 30));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLeftPanel.add(statusLeftLabel);
        statusRightPanel.add(statusRightLabel);
        return statusPanel;
    }

    /**
     * Update status bar.
     *
     * @param functionName the function name
     * @param details      the details
     */
    public void updateStatusBar(String functionName, String details){
        statusLeftLabel.setText(functionName);
        statusRightLabel.setText(details);
    }
}
