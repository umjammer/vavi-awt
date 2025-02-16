/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.event;

import java.lang.reflect.Method;
import java.util.EventListener;
import java.util.logging.Level;

import vavi.util.Debug;


/**
 * イベントを結合，分離するためのクラスです．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020512 nsano initial version <br>
 */
public class EventPlug {
    /** プラグの名前 */
    private final String name;
    /** イベントを発行するオブジェクト */
    private Object invoker;
    /** イベントを受け取るリスナ */
    private EventListener listener;
    /** 接続しているかどうか */
    private boolean connected = false;

    /**
     * イベントを結合，分離するプラグを構築します．
     *
     * @param name プラグの名前
     * @param invoker イベントを発行するオブジェクト
     * @param listener イベントを受け取るリスナ
     */
    public EventPlug(String name, Object invoker, EventListener listener) {
        this.name = name;
        this.invoker = invoker;
        this.listener = listener;
    }

    /** プラグの名前を取得します． */
    public String getName() {
        return name;
    }

    /** イベントを発行するオブジェクトを設定します． */
    public void setInvoker(Object invoker) {
        this.invoker = invoker;
    }

    /** イベントを発行するオブジェクトを取得します． */
    public Object getInvoker() {
        return invoker;
    }

    /** イベントを受け取るリスナを設定します． */
    public void setEventListener(EventListener listener) {
        this.listener = listener;
    }

    /** イベントを受け取るリスナを取得します． */
    public EventListener getEventListener() {
        return listener;
    }

    /** プラグが接続されているかどうかを返します． */
    public boolean isConnected() {
        return connected;
    }

    /** プラグを接続，分離します． */
    public void setConnected(boolean connected) {
        if (connected) {
            plugImpl("add");
        } else {
            plugImpl("remove");
        }
        this.connected = connected;
    }

    /**
     * リスナのクラスを取得します．
     * リスナは FooListener 形式の命名でなければいけません．
     * TODO interface が 2 つ以上の場合の処理．
     * TODO FooListener を探してループすべき？
     */
    private Class<?> getEventListenerClass() {
        Class<?> clazz = listener.getClass();
        String className = clazz.getName();
        if (!className.endsWith("Listener")) {
            Class<?>[] classes = clazz.getInterfaces();
Debug.println(Level.FINER, classes.length);
//for (int i = 0; i < classes.length; i++) {
// System.err.println(classes[i]);
//}
            clazz = classes[0];
        }
        return clazz;
    }

    /**
     * リスナの名前を取得します．
     */
    private String getEventListenerName() {
        return getClassName(getEventListenerClass().getName());
    }

    /** Gets class name w/o package name. */
    private static String getClassName(String className) {
        return className.substring(className.lastIndexOf(".") + 1);
    }

    /**
     * プラグを接続，分離する処理です．
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
Debug.printStackTrace(e);
            throw new IllegalStateException(getClassName(invoker.getClass().getName()) + "." + type + getEventListenerName() + "(" + getEventListenerClass() + ")");
        }
    }
}
