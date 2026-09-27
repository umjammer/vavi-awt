/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.layout;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.beans.BeanInfo;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.OverlayLayout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * LayoutManagerInfoFactoryTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2026-09-25 nsano initial version <br>
 */
class LayoutManagerInfoFactoryTest {

    @ParameterizedTest
    @ValueSource(classes = {GridLayout.class, FlowLayout.class, CardLayout.class, BorderLayout.class,
            GridBagLayout.class, BoxLayout.class, OverlayLayout.class})
    void testGetBeanInfo(Class<?> lmClass) {
        BeanInfo bi = LayoutManagerInfoFactory.getBeanInfo(lmClass);
        assertNotNull(bi);
        assertEquals(lmClass, bi.getBeanDescriptor().getBeanClass());
    }

    @Test
    void testGetBeanInfoUnknown() {
        assertNull(LayoutManagerInfoFactory.getBeanInfo(String.class));
    }

    @Test
    void testGetSampleLayoutManagerInfos() {
        List<SampleLayoutManagerInfo> lmis = LayoutManagerInfoFactory.getSampleLayoutManagerInfos();
        assertEquals(7, lmis.size());

        assertNull(lmis.get(0).layout);
        assertEquals("None", lmis.get(0).desc);

        assertInstanceOf(GridLayout.class, lmis.get(1).layout);
        assertInstanceOf(FlowLayout.class, lmis.get(2).layout);
        assertInstanceOf(CardLayout.class, lmis.get(3).layout);
        assertInstanceOf(BorderLayout.class, lmis.get(4).layout);
        assertInstanceOf(GridBagLayout.class, lmis.get(5).layout);

        assertNull(lmis.get(6).layout);
        assertEquals("UserDefined", lmis.get(6).desc);

        lmis.forEach(lmi -> assertNotNull(lmi.icon, lmi.desc));
    }
}
