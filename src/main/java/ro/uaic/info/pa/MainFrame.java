package ro.uaic.info.pa;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public static void main(String[] args) {
        new MainFrame().setVisible(true);
    }

    public MainFrame() {
        super("Swing Simulator");
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new JPanel(), BorderLayout.CENTER);

        DesignPanel designPanel = new DesignPanel();
        add(designPanel, BorderLayout.CENTER);
        add(new ControlPanel(designPanel), BorderLayout.NORTH);
    }
}
