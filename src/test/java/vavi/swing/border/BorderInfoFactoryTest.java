/*
 * Copyright (c) 2026 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.util.List;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * BorderInfoFactoryTest.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2026-09-25 nsano initial version <br>
 */
class BorderInfoFactoryTest {

    @ParameterizedTest
    @ValueSource(classes = {SoftBevelBorder.class, BevelBorder.class, EtchedBorder.class, LineBorder.class,
            MatteBorder.class, TitledBorder.class, javax.swing.border.CompoundBorder.class, javax.swing.border.EmptyBorder.class})
    void testGetBorderInfo(Class<?> borderClass) {
        BorderInfo bi = BorderInfoFactory.getBorderInfo(borderClass);
        assertNotNull(bi);
        assertEquals(borderClass, bi.getBeanDescriptor().getBeanClass());
    }

    @Test
    void testGetBorderInfoUnknown() {
        assertNull(BorderInfoFactory.getBorderInfo(String.class));
    }

    @Test
    void testGetSampleBorderInfos() {
        List<SampleBorderInfo> bis = BorderInfoFactory.getSampleBorderInfos();
        assertEquals(6, bis.size());

        assertNull(bis.get(0).border);
        assertEquals("None", bis.get(0).desc);

        assertInstanceOf(EtchedBorder.class, bis.get(1).border);
        assertEquals(BevelBorder.LOWERED, ((BevelBorder) bis.get(2).border).getBevelType());
        assertEquals(BevelBorder.RAISED, ((BevelBorder) bis.get(3).border).getBevelType());
        assertEquals(java.awt.Color.black, ((LineBorder) bis.get(4).border).getLineColor());

        assertNull(bis.get(5).border);
        assertEquals("UserDefined", bis.get(5).desc);

        bis.forEach(bi -> assertNotNull(bi.icon, bi.desc));
    }
}
