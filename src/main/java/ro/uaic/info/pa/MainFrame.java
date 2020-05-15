package ro.uaic.info.pa;

import javax.swing.*;
import java.awt.*;

/**
 * Main driver class serving as the app window with the 2 main components:
 * the Control Panel and the Design Panel
 */
public class MainFrame extends JFrame {
    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }

    public MainFrame() {
        super("Swing Simulator");
        setSize(1600, 1000);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new JPanel(), BorderLayout.CENTER);

        DesignPanel designPanel = new DesignPanel();
        add(designPanel, BorderLayout.CENTER);
        add(new ControlPanel(designPanel), BorderLayout.NORTH);
    }
}
