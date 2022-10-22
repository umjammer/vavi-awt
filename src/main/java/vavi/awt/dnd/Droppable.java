/*
 * Copyright (c) 2022 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.awt.dnd;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;


/**
 * Droppable.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 2022-09-15 nsano initial version <br>
 */
public interface Droppable {

    /**
     * make a component droppable, files multiple dropped after index 1 (0 origin) are ignored.
     * @param impl user process after dropped a file
     */
    static void makeComponentSinglePathDroppable(Component component, Function<Path, Boolean> impl) {
        new DropTarget(
                component,
                DnDConstants.ACTION_COPY_OR_MOVE,
                new BasicDTListener() {

                    @Override
                    protected boolean isDragFlavorSupported(DropTargetDragEvent ev) {
                        return ev.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
                    }

                    @Override
                    protected DataFlavor chooseDropFlavor(DropTargetDropEvent ev) {
                        if (ev.isLocalTransfer() && ev.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                            return DataFlavor.javaFileListFlavor;
                        }
                        DataFlavor chosen = null;
                        if (ev.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                            chosen = DataFlavor.javaFileListFlavor;
                        }
                        return chosen;
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    protected boolean dropImpl(DropTargetDropEvent ev, Object data) {
                        Path path = Paths.get(((List<File>) data).get(0).getPath());
                        return impl.apply(path);
                    }
                },
                true);
    }
}
