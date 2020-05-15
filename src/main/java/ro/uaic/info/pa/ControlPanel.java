package ro.uaic.info.pa;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.List;

/**
 * Class that represents a JPanel component added to the app window,
 * serving as a menu bar and that handles user interaction with
 * action listeners
 */
public class ControlPanel extends JPanel {

    private DesignPanel designPanel;

    private final JComboBox<String> componentOptions;
    private final JLabel textLabel;
    private final JTextField text;
    private final JButton addButton;

    public ControlPanel(DesignPanel designPanel) {
        this.setBorder(BorderFactory.createTitledBorder("Control Panel"));
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 25, 10));

        add(new JLabel("Choose component:"));
        componentOptions = new JComboBox<>(getSwingClassNames());
        addingComponentOptionsActionListener();
        add(componentOptions);

        textLabel = new JLabel("Text:");
        add(textLabel);
        text = new JTextField(10);
        add(text);

        addButton = new JButton("ADD");
        //addingAddButtonActionListener();
        add(addButton);

        textLabel.setVisible(false);
        text.setVisible(false);

        this.designPanel = designPanel;
        addingDesignPanelMouseListener();
    }

    /**
     * Using the Reflections library and the native ClassLoader, the 'javax.swing' package
     * is being searched for classes that are subtypes of JComponent
     * (source: https://www.baeldung.com/reflections-library)
     * @return a Vector of strings containing the simple and full names of the found classes
     */
    private Vector<String> getSwingClassNames() {
        List<ClassLoader> classLoadersList = new LinkedList<>();
        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("javax.swing"))));

        Set<Class<? extends JComponent>> classes = reflections.getSubTypesOf(JComponent.class);

        Vector<String> classNames = new Vector<>();
        for (Class<?> cls : classes) {
            if (cls.getSimpleName().length() > 0 && cls.getSimpleName().charAt(0) == 'J') {
                classNames.add(cls.getSimpleName() + " (" + cls.getName() + ")");
            }
        }

        return classNames;
    }

    /**
     * Makes the 'text area' hidden or shown depending on whether the specified class has a constructor
     * with a single String parameter, and return the result
     * @param swingClass the class whose constructors are being checked
     * @return whether the class has a constructor with a single String parameter or not
     */
    private boolean modifyTextArea(Class swingClass) {
        boolean hasStringConstructor = false;
        for (Constructor con : swingClass.getConstructors()) {
            if (con.toString().contains("(java.lang.String)")) {
                hasStringConstructor = true;
                break;
            }
        }

        if (hasStringConstructor) {
            textLabel.setVisible(true);
            text.setVisible(true);
        } else {
            textLabel.setVisible(false);
            text.setVisible(false);
        }

        return hasStringConstructor;
    }

    /**
     * The currently selected component in the ComboBox is being loaded as a class and then the
     * 'text area' is being hidden or shown depending on whether the specified class has a constructor
     * with a single String parameter
     */
    private void addingComponentOptionsActionListener() {
        componentOptions.addActionListener(e -> {
            List<String> bothNames = Arrays.asList(componentOptions.getSelectedItem().toString().split(" "));
            String longName = bothNames.get(1);
            try {
                Class swingClass = Class.forName(longName.substring(1, longName.length() - 1));
                modifyTextArea(swingClass);
            } catch (ClassNotFoundException classNotFoundException) {
                System.err.println("Class not found due to error!");
                classNotFoundException.printStackTrace();
            }
        });
    }

    /**
     * Adds the component specified in the ComboBox at the location where the mouse event specifies
     * (via the getX() and getY() methods) and then, according to whether it has a single String parameter
     * constructor, it uses it with the JTextField text or uses the default constructor
     * @param e the mouse event
     */
    private void handleComponentOption(MouseEvent e) {
        System.out.println("pressed button!");
        List<String> bothNames = Arrays.asList(componentOptions.getSelectedItem().toString().split(" "));
        String longName = bothNames.get(1);
        try {

            Class swingClass = Class.forName(longName.substring(1, longName.length() - 1));
            JComponent component;
            if (!modifyTextArea(swingClass)) {
                component = (JComponent) swingClass.newInstance();
                component.setBounds(0, 0, 400, 400);
                designPanel.add(component);
            } else {
                Constructor ctor = swingClass.getConstructor(String.class);
                component = (JComponent) ctor.newInstance(text.getText());
                component.setBounds(e.getX(), e.getY(), 200, 200);
                designPanel.add(component);
            }

        } catch (Exception ex) {
            System.err.println("Something went wrong!");
            ex.printStackTrace();
        }
    }

    /**
     * Adds the component specified in the ComboBox at the location where the mouse pointer
     * is currently located via the mouseClicked method (the rest are not used)
     */
    private void addingDesignPanelMouseListener() {
        designPanel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleComponentOption(e);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // not used...
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // not used...
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // not used...
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // not used...
            }
        });
    }

}
