/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.binder;

import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import javax.swing.JCheckBox;
import javax.swing.JSlider;
import javax.swing.JTextField;

import org.junit.jupiter.api.Test;
import vavi.swing.binding.Component;
import vavi.swing.binding.Components;
import vavi.swing.binding.Updater;
import vavi.util.Debug;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * BinderTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-15 nsano initial version <br>
 */
class BinderTest {

    // must be public
    @Components(updater = Bean.class)
    public static class Bean implements Updater<Bean> {
        @Component(name = "slider")
        int sliderValue = 50;
        @Component(name = "textField")
        String textValue;
        @Component(name = "checkBox")
        boolean checkBoxValue;

        @Override
        public String toString() {
            return "Bean{" +
                    "sliderValue=" + sliderValue +
                    ", textValue='" + textValue + '\'' +
                    ", checkBoxValue=" + checkBoxValue +
                    ", cdl=" + cdl +
                    ", hash=" + hashCode() +
                    '}';
        }

        CountDownLatch cdl;
        CountDownLatch newCountDownLatch() {
            cdl = new CountDownLatch(1);
            return cdl;
        }
        @Override
        public void update(Bean bean) {
            // warning: this updater uses the same bean class,
            //          so the caller instance of this method is always not the same.
            //          the argument bean is the original bean instance.
Debug.println(Level.FINE, "here: " + bean);
            bean.cdl.countDown();
            bean.cdl = null;
        }

        void reset() {
            sliderValue = 50;
            textValue = null;
            checkBoxValue = false;
        }
    }

    static class SwingBean {
        JSlider slider = new JSlider();
        JCheckBox checkBox = new JCheckBox();
        JTextField textField = new JTextField();
        SwingBean() {
            slider.setMaximum(100);
            slider.setMinimum(0);
        }
    }

    @Test
    void test1() throws Exception {
        Bean bean = new Bean();
        SwingBean swings = new SwingBean();
        Components.Util.bind(bean, swings);

        // swing -> bean

        CountDownLatch cdl = bean.newCountDownLatch();
        swings.slider.setValue(70);
        cdl.await();
        assertEquals(70, bean.sliderValue);
Debug.println(Level.FINE, "here1");

        cdl = bean.newCountDownLatch();
        swings.checkBox.setSelected(true);
        cdl.await();
        assertTrue(bean.checkBoxValue);
Debug.println(Level.FINE, "here2");

        cdl = bean.newCountDownLatch();
        swings.textField.setText("vavi");
        cdl.await();
        assertEquals("vavi", bean.textValue);
Debug.println(Level.FINE, "here3");

        // bean -> swing

        bean.reset();
        Components.Util.rebind(bean, swings);
        assertEquals(50, swings.slider.getValue());
        assertFalse(swings.checkBox.isSelected());
        assertTrue(swings.textField.getText().isEmpty());
    }
}
