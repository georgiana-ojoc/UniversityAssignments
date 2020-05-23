package designer.controllers;

import designer.models.Component;
import designer.models.Properties;
import designer.models.Property;
import designer.views.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    private Map<JComponent, Component> components;
    private MainFrame mainFrame;
    private ConfigurationPanel configurationPanel;
    private DesignPanel designPanel;
    private PropertiesPanel propertiesPanel;

    public Controller() {
        components = new HashMap<>();
        configurationPanel = new ConfigurationPanel();
        designPanel = new DesignPanel();
        designPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent firstMouseEvent) {
                @SuppressWarnings("rawtypes")
                Class loadedClass = null;
                @SuppressWarnings("rawtypes")
                Class annotationClass = null;
                try {
                    loadedClass = Class.forName(configurationPanel.getSwingComponent().getText());
                } catch (ClassNotFoundException ignored) {
                }
                if (loadedClass == null) {
                    TypeLoader typeLoader = new TypeLoader();
                    File path = new File(configurationPanel.getJarPath().getText());
                    if (!path.exists()) {
                        return;
                    }
                    URL url;
                    try {
                        url = path.toURI().toURL();
                    } catch (MalformedURLException exception) {
                        System.out.println(exception.getMessage());
                        return;
                    }
                    typeLoader.addURL(url);
                    try {
                        loadedClass = typeLoader.loadClass(configurationPanel.getSwingComponent().getText());
                        annotationClass = typeLoader.loadClass("components.Text");
                    } catch (ClassNotFoundException exception) {
                        System.out.println(exception.getMessage());
                        return;
                    }
                }
                try {
                    String text;
                    if (annotationClass != null) {
                        String annotationString = loadedClass.getAnnotation(annotationClass).toString();
                        text = annotationString.substring(annotationString.lastIndexOf('=') + 2, annotationString.lastIndexOf(')') - 1);
                    } else {
                        text = configurationPanel.getDefaultText().getText();
                    }
                    @SuppressWarnings("unchecked")
                    JComponent jComponent = (JComponent) loadedClass.getConstructor(String.class)
                            .newInstance(text);
                    jComponent.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mousePressed(MouseEvent secondMouseEvent) {
                            createTable(jComponent);
                            propertiesPanel.repaint();
                            propertiesPanel.revalidate();
                        }
                    });
                    Component component = new Component(jComponent, firstMouseEvent.getX(), firstMouseEvent.getY());
                    components.put(jComponent, component);
                    designPanel.getComponentList().add(component);
                    designPanel.repaint();
                    designPanel.revalidate();
                } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
                    System.out.println(exception.getMessage());
                }
            }
        });
        propertiesPanel = new PropertiesPanel();
        mainFrame = new MainFrame(configurationPanel, designPanel, propertiesPanel, new ControlPanel(this));
    }

    private void createTable(JComponent component) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(component.getClass());
            List<Property> properties = new ArrayList<>();
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                String name = propertyDescriptor.getName();
                if (propertyDescriptor.getPropertyType() == null) {
                    continue;
                }
                String type = propertyDescriptor.getPropertyType().getTypeName();
                switch (type) {
                    case "int":
                        properties.add(new Property(name, type, (Integer) propertyDescriptor.getReadMethod().invoke(component)));
                        break;
                    case "java.lang.String":
                        properties.add(new Property(name, type, (String) propertyDescriptor.getReadMethod().invoke(component)));
                        break;
                    default:
                        properties.add(new Property(name, type));
                }
            }
            propertiesPanel.update(new Properties(components.get(component), designPanel, properties));
            propertiesPanel.repaint();
            propertiesPanel.revalidate();
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public void load() {
        JFileChooser fileChooser = new JFileChooser(Paths.get("").toAbsolutePath().toString());
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            DesignPanel loadedDesignPanel = deserializeFromXml(fileChooser.getSelectedFile().getAbsolutePath());
            if (loadedDesignPanel != null) {
                designPanel = loadedDesignPanel;
                mainFrame.changeDesignPanel(designPanel);
                designPanel.repaint();
                designPanel.revalidate();
            }
        }
    }

    public void save() {
        JFileChooser fileChooser = new JFileChooser(Paths.get("").toAbsolutePath().toString());
        int returnValue = fileChooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (designPanel != null) {
                serializeToXml(designPanel, fileChooser.getSelectedFile().getAbsolutePath());
            }
        }
    }

    public void reset() {
        designPanel.getComponentList().clear();
        designPanel.removeAll();
        designPanel.repaint();
        designPanel.revalidate();
    }

    private void serializeToXml(DesignPanel designPanel, String path) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            XMLEncoder xmlEncoder = new XMLEncoder(fileOutputStream);
            xmlEncoder.writeObject(designPanel);
            xmlEncoder.close();
            fileOutputStream.close();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private DesignPanel deserializeFromXml(String path) {
        DesignPanel designPanel = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            XMLDecoder xmlDecoder = new XMLDecoder(fileInputStream);
            designPanel = (DesignPanel) xmlDecoder.readObject();
            xmlDecoder.close();
            fileInputStream.close();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        return designPanel;
    }
}
