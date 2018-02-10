/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import vavi.awt.dnd.BasicTransferable;


/**
 * The selection transferable object.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 020609 nsano initial version <br>
 */
public class SelectionTransferable extends BasicTransferable {

    /** XMLEn(De)coder は　UTF-8 固定らしい． */
    private static final String encoding = "UTF-8";

    /** this class's flavor */
    public static final DataFlavor selectionFlavor = new DataFlavor(SelectionTransferable.class, SelectionTransferable.class.getName());

    /** Flavors */
    {
        DataFlavor[] flavors = new DataFlavor[] {
            DataFlavor.stringFlavor,
            selectionFlavor,
        };
        flavorList = Arrays.asList(flavors);
    }

    /** */
    public SelectionTransferable(List<? extends Selectable> selection) throws IOException {
        super(selection);

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        XMLEncoder xe = new XMLEncoder(os);
        xe.writeObject(selection);
        xe.close();

        model = os.toString(encoding);
    }

    /** */
    public synchronized Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {

        if (flavor == DataFlavor.stringFlavor) {
            return model;
        } else if (flavor == selectionFlavor) {
            byte[] bytes = ((String) model).getBytes(encoding);
            ByteArrayInputStream is = new ByteArrayInputStream(bytes);

            XMLDecoder xd = new XMLDecoder(is);
            @SuppressWarnings("unchecked")
            List<? extends Component> selection = (List<? extends Component>) xd.readObject();
            xd.close();

            return selection;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}

/* */
