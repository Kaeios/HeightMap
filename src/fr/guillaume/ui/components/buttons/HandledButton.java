package fr.guillaume.ui.components.buttons;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class HandledButton extends JButton {

    private final Set<Object> handlers = new HashSet<>();

    private final String id;

    public HandledButton(String id, String label) {
        super(label);
        this.id = id;

        addActionListener(event -> {
            handlers.forEach(object -> {
                for (Method method : object.getClass().getMethods()) {
                    if(!method.canAccess(object)) continue;
                    if(!method.isAnnotationPresent(ButtonHandler.class)) continue;
                    ButtonHandler handler = method.getAnnotation(ButtonHandler.class);
                    if(!handler.targetId().equals(id)) continue;
                    try {
                        method.invoke(object);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
        });
    }

    public void registerHandler(Object handler) {
        this.handlers.add(handler);
    }

    public String getId() {
        return id;
    }

}
