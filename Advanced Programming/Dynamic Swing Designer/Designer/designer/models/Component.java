package designer.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Component {
    private JComponent component;
    private int x;
    private int y;

    public void update(String property, Object value) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(component.getClass());
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                if (propertyDescriptor.getName().equals(property)) {
                    if (propertyDescriptor.getWriteMethod() != null) {
                        propertyDescriptor.getWriteMethod().invoke(component, value);
                        if (property.equals("x")) {
                            x = Integer.parseInt((String) value);
                        } else if (property.equals("y")) {
                            y = Integer.parseInt((String) value);
                        }
                        break;
                    }
                }
            }
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
