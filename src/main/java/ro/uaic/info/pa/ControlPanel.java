package ro.uaic.info.pa;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.List;

public class ControlPanel extends JPanel {

    private DesignPanel designPanel;

    private final JComboBox<String> componentOptions;
    private final JLabel textLabel;
    private final JTextField text;
    private final JButton addButton;

    public ControlPanel(DesignPanel designPanel) {
        this.designPanel = designPanel;
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
        addingAddButtonActionListener();
        add(addButton);

        textLabel.setVisible(false);
        text.setVisible(false);
    }

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

    private void addingComponentOptionsActionListener() {
        componentOptions.addActionListener(e -> {
            List<String> bothNames = Arrays.asList(componentOptions.getSelectedItem().toString().split(" "));
            String longName = bothNames.get(1);
            try {
                Class swingClass = Class.forName(longName.substring(1, longName.length() - 1));
                modifyTextArea(swingClass);
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }
        });
    }

    private void addingAddButtonActionListener() {
        addButton.addActionListener(e -> {
            List<String> bothNames = Arrays.asList(componentOptions.getSelectedItem().toString().split(" "));
            String longName = bothNames.get(1);
            try {
                Class swingClass = Class.forName(longName.substring(1, longName.length() - 1));
                designPanel.add((JComponent) swingClass.newInstance());
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            } catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            } catch (InstantiationException instantiationException) {
                instantiationException.printStackTrace();
            }
        });
    }

}
