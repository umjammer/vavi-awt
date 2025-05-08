/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.event;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.reflect.Method;
import java.util.EventListener;

import static java.lang.System.getLogger;


/**
 * A class for combining and separating events.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020512 nsano initial version <br>
 */
public class EventPlug {

    private static final Logger logger = getLogger(EventPlug.class.getName());

    /** Plug Name */
    private final String name;
    /** The object that fires the event */
    private Object invoker;
    /** Listener that receives the event */
    private EventListener listener;
    /** Connected or not */
    private boolean connected = false;

    /**
     * Constructs plugs to join and split events.
     *
     * @param name Plug Name
     * @param invoker The object that emits the event
     * @param listener Listener that receives the event
     */
    public EventPlug(String name, Object invoker, EventListener listener) {
        this.name = name;
        this.invoker = invoker;
        this.listener = listener;
    }

    /** Gets the name of the plug. */
    public String getName() {
        return name;
    }

    /** Sets the object that will issue the event. */
    public void setInvoker(Object invoker) {
        this.invoker = invoker;
    }

    /** Gets the object that issues the event. */
    public Object getInvoker() {
        return invoker;
    }

    /** Sets a listener to receive events. */
    public void setEventListener(EventListener listener) {
        this.listener = listener;
    }

    /** Gets the listener that will receive the event. */
    public EventListener getEventListener() {
        return listener;
    }

    /** Returns whether the plug is connected. */
    public boolean isConnected() {
        return connected;
    }

    /** Connect and disconnect the plug. */
    public void setConnected(boolean connected) {
        if (connected) {
            plugImpl("add");
        } else {
            plugImpl("remove");
        }
        this.connected = connected;
    }

    /**
     * Gets the listener class.
     * Listeners must be named in the format FooListener.
     * TODO Handling cases where there are two or more interfaces.
     * TODO Should I loop looking for FooListener s?
     */
    private Class<?> getEventListenerClass() {
        Class<?> clazz = listener.getClass();
        String className = clazz.getName();
        if (!className.endsWith("Listener")) {
            Class<?>[] classes = clazz.getInterfaces();
logger.log(Level.TRACE, classes.length);
//for (int i = 0; i < classes.length; i++) {
// System.err.println(classes[i]);
//}
            clazz = classes[0];
        }
        return clazz;
    }

    /**
     * Gets the name of the listener.
     */
    private String getEventListenerName() {
        return getClassName(getEventListenerClass().getName());
    }

    /** Gets class name w/o package name. */
    private static String getClassName(String className) {
        return className.substring(className.lastIndexOf(".") + 1);
    }

    /**
     * This is the process of connecting and disconnecting plugs.
     *
     * @param type "add" or "remove"
     */
    private void plugImpl(String type) {
        try {
            Class<?> clazz = invoker.getClass();
            String methodName = type + getEventListenerName();
            Method method = clazz.getMethod(methodName, getEventListenerClass());
            method.invoke(invoker, listener);
        } catch (Exception e) {
logger.log(Level.INFO, e.getMessage(), e);
            throw new IllegalStateException(getClassName(invoker.getClass().getName()) + "." + type + getEventListenerName() + "(" + getEventListenerClass() + ")");
        }
    }
}
