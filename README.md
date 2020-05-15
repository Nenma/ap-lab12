# ap-lab12

## Overview
At the moment, the app contains:
- The MainFrame main class of type JFrame
- The ControlPanel class serving as a menu bar as a component of the MainFrame
- The DesignPanel class serbing as a canvas as a component of the MainFrame
- The ControlPanel contains:
  - a JComboBox drop-down list containing all Swing components options
  - a JTextField that appears only for an applicable JComponent
  - a button whose usability has been replaced with a MouseListener event
- The DesignPanel that just extends JPanel and has its layout set to `null` for the absolute positioning

## Compulsory
The tasks are:
- Create the class *MainFrame* of type *JFrame*, that will also represent the main class of the application. The frame will contain a *ControlPanel* in the north and a *DesignPanel* in the center.
- The *ControlPanel* will allow the user to specify **any class name of a Swing component** (such as `javax.swing.JButton`, `javax.swing.JLabel`, etc.), a default text for that component (if applicable) and a button for *creating and adding an instance* of the specified component to the *DesignPanel*.
- The *DesignPanel* represents a simple JPanel, using *absolute positioning* of its components and containing Swing components added by our application. 