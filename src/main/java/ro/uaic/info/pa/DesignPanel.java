package ro.uaic.info.pa;

import javax.swing.*;

/**
 * Class that represents a JPanel component added to the app window,
 * serving as a sort of canvas for the components added by the user
 */
public class DesignPanel extends JPanel {

    public DesignPanel() {
        this.setBorder(BorderFactory.createTitledBorder("Design Panel"));
        this.setLayout(null);
    }

}
