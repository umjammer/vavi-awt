/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.event;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;


/**
 * Utility for the EventPlug class.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020511 nsano initial version <br>
 */
public class EventPlugSupport {

    /** Map for managing EventPlug */
    protected final Map<String, EventPlug> eventPlugs = new HashMap<>();

    /**
     * Add EventPlug.
     *
     * @param eventPlug EventPlug
     */
    public void addEventPlug(EventPlug eventPlug) {
        eventPlugs.put(eventPlug.getName(), eventPlug);
    }

    /**
     * Delete the EventPlug.
     *
     * @param eventPlug EventPlug
     */
    public void removeEventPlug(EventPlug eventPlug) {
        eventPlugs.remove(eventPlug.getName());
    }

    /**
     * Deletes the EventPlug with the specified name.
     *
     * @param name EventPlug name
     */
    public void removeEventPlug(String name) {
        eventPlugs.remove(name);
    }

    /**
     * Gets the EventPlug with the specified name.
     *
     * @param name EventPlug name
     */
    private EventPlug getEventPlug(String name) {
        return eventPlugs.get(name);
    }

    /**
     * Sets the object that will emit events to the EventPlug with the specified name.
     *
     * @param name EventPlug name
     * @param invoker The object that emits the event
     */
    public void setInvoker(String name, Object invoker) {
        EventPlug eventPlug = getEventPlug(name);
        eventPlug.setInvoker(invoker);
    }

    /**
     * Gets the object that issues the EventPlug event with the specified name.
     *
     * @param name EventPlug name
     */
    public Object getInvoker(String name) {
        EventPlug eventPlug = getEventPlug(name);
        return eventPlug.getInvoker();
    }

    /**
     * Sets a listener to receive events on the EventPlug with the specified name.
     *
     * @param name EventPlug name
     * @param listener Listener that receives the event
     */
    public void setEventListener(String name, EventListener listener) {
        EventPlug eventPlug = getEventPlug(name);
        eventPlug.setEventListener(listener);
    }

    /**
     * Gets a listener that receives events from the EventPlug with the specified name.
     *
     * @param name EventPlug name
     */
    public EventListener getEventListener(String name) {
        EventPlug eventPlug = getEventPlug(name);
        return eventPlug.getEventListener();
    }

    /**
     * Duplicates the EventPlug with the specified name and sets the object that will issue the event.
     *
     * @param name EventPlug name
     * @param newName EventPlug name to duplicate
     * @param invoker The object that emits the event
     */
    public void addNewEventPlug(String name, String newName, Object invoker) {
        EventListener listener = getEventListener(name);
        addEventPlug(new EventPlug(newName, invoker, listener));
    }

    /**
     * Duplicates the EventPlug with the specified name and sets a listener to receive events.
     *
     * @param name EventPlug name
     * @param newName EventPlug name to duplicate
     * @param l Listener that receives the event
     */
    public void addNewEventPlug(String name, String newName, EventListener l) {
        Object invoker = getInvoker(name);
        addEventPlug(new EventPlug(newName, invoker, l));
    }

    /**
     * Attaches or detaches an EventPlug with the specified name.
     *
     * @param name EventPlug name
     * @param connected To combine or not
     */
    public void setConnected(String name, boolean connected) {
        EventPlug eventPlug = getEventPlug(name);
        eventPlug.setConnected(connected);
    }

    /**
     * Combines and separates all EventPlugs.
     *
     * @param connected To combine or not
     */
    public void setConnected(boolean connected) {
        for (EventPlug eventPlug : eventPlugs.values()) {
            eventPlug.setConnected(connected);
        }
    }
}
