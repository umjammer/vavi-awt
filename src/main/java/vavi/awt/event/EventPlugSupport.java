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
 * EventPlug クラスのユーティリティです．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020511 nsano initial version <br>
 */
public class EventPlugSupport {

    /** EventPlug 管理用 Hashtable */
    protected volatile Map<String, EventPlug> eventPlugs = new HashMap<>();

    /**
     * EventPlug を追加します．
     *
     * @param eventPlug EventPlug
     */
    public void addEventPlug(EventPlug eventPlug) {
        eventPlugs.put(eventPlug.getName(), eventPlug);
    }

    /**
     * EventPlug を削除します．
     *
     * @param eventPlug EventPlug
     */
    public void removeEventPlug(EventPlug eventPlug) {
        eventPlugs.remove(eventPlug.getName());
    }

    /**
     * 指定した名前の EventPlug を削除します．
     *
     * @param name EventPlug の名前
     */
    public void removeEventPlug(String name) {
        eventPlugs.remove(name);
    }

    /**
     * 指定した名前の EventPlug を取得します．
     *
     * @param name EventPlug の名前
     */
    private EventPlug getEventPlug(String name) {
        return eventPlugs.get(name);
    }

    /**
     * 指定した名前の EventPlug にイベントを発行するオブジェクトを設定します．
     *
     * @param name EventPlug の名前
     * @param invoker イベントを発行するオブジェクト
     */
    public void setInvoker(String name, Object invoker) {
        EventPlug eventPlug = getEventPlug(name);
        eventPlug.setInvoker(invoker);
    }

    /**
     * 指定した名前の EventPlug のイベントを発行するオブジェクトを取得します．
     *
     * @param name EventPlug の名前
     */
    public Object getInvoker(String name) {
        EventPlug eventPlug = getEventPlug(name);
        return eventPlug.getInvoker();
    }

    /**
     * 指定した名前の EventPlug にイベントを受け取るリスナを設定します．
     *
     * @param name EventPlug の名前
     * @param listener イベントを受け取るリスナ
     */
    public void setEventListener(String name, EventListener listener) {
        EventPlug eventPlug = getEventPlug(name);
        eventPlug.setEventListener(listener);
    }

    /**
     * 指定した名前の EventPlug のイベントを受け取るリスナを取得します．
     *
     * @param name EventPlug の名前
     */
    public EventListener getEventListener(String name) {
        EventPlug eventPlug = getEventPlug(name);
        return eventPlug.getEventListener();
    }

    /**
     * 指定した名前の EventPlug を複製し イベントを発行するオブジェクトを設定します．
     *
     * @param name EventPlug の名前
     * @param newName 複製する EventPlug の名前
     * @param invoker イベントを発行するオブジェクト
     */
    public void addNewEventPlug(String name, String newName, Object invoker) {
        EventListener listener = getEventListener(name);
        addEventPlug(new EventPlug(newName, invoker, listener));
    }

    /**
     * 指定した名前の EventPlug を複製し イベントを受け取るリスナを設定します．
     *
     * @param name EventPlug の名前
     * @param newName 複製する EventPlug の名前
     * @param l イベントを受け取るリスナ
     */
    public void addNewEventPlug(String name, String newName, EventListener l) {
        Object invoker = getInvoker(name);
        addEventPlug(new EventPlug(newName, invoker, l));
    }

    /**
     * 指定した名前の EventPlug を結合，分離します．
     *
     * @param name EventPlug の名前
     * @param connected 結合するかどうか
     */
    public void setConnected(String name, boolean connected) {
        EventPlug eventPlug = getEventPlug(name);
        eventPlug.setConnected(connected);
    }

    /**
     * すべての EventPlug を結合，分離します．
     *
     * @param connected 結合するかどうか
     */
    public void setConnected(boolean connected) {
        for (EventPlug eventPlug : eventPlugs.values()) {
            eventPlug.setConnected(connected);
        }
    }
}

/* */
