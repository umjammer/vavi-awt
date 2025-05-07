/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.List;

import static java.lang.System.getLogger;


/**
 * The basic transferable object.
 *
 * @done is ClipboardOwner needed?
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010820 nsano initial version <br>
 *          0.01 020507 nsano delete lostOwnership <br>
 *          0.02 020609 nsano refine <br>
 */
public abstract class BasicTransferable implements Transferable {

    private static final Logger logger = getLogger(BasicTransferable.class.getName());

    /** Flavor list */
    protected List<DataFlavor> flavorList;

    /** The model to transfer */
    protected Object model;

    /**
     * Simply initializes instance variable.
     */
    public BasicTransferable(Object model) {
        this.model = model;
logger.log(Level.TRACE, model);
    }

    /** Dumps flavors. */
    protected final void dumpFlavor(DataFlavor flavor) {
        System.err.println("getMimeType " + flavor.getMimeType());
        System.err.println("getHumanPresentableName " + flavor.getHumanPresentableName());
        System.err.println("getRepresentationClass " + flavor.getRepresentationClass().getName());
        System.err.println("isMimeTypeSerializedObject " + flavor.isMimeTypeSerializedObject());
        System.err.println("isRepresentationClassInputStream " + flavor.isRepresentationClassInputStream());
        System.err.println("isRepresentationClassSerializable " + flavor.isRepresentationClassSerializable());
        System.err.println("isRepresentationClassRemote " + flavor.isRepresentationClassRemote());
        System.err.println("isFlavorSerializedObjectType " + flavor.isFlavorSerializedObjectType());
        System.err.println("isFlavorRemoteObjectType " + flavor.isFlavorRemoteObjectType());
        System.err.println("isFlavorJavaFileListType " + flavor.isFlavorJavaFileListType());
    }

    /** Gets transferable flavors. */
    @Override
    public synchronized DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = new DataFlavor[flavorList.size()];
        flavorList.toArray(flavors);
        return flavors;
    }

    /** Tells if the specified flavor is supported or not. */
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
logger.log(Level.TRACE, flavorList.contains(flavor));
        return flavorList.contains(flavor);
    }

    /** Gets the transferable data. */
    @Override
    public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
logger.log(Level.TRACE, toString());
//        dumpFlavor(flavor);

        if (isDataFlavorSupported(flavor)) {
            return model;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}
