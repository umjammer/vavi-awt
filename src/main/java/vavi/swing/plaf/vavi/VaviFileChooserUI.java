package vavi.swing.plaf.vavi;

import java.io.File;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalFileChooserUI;


/**
 * Original look&feel (filechooser).
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 */
public class VaviFileChooserUI extends MetalFileChooserUI {

    /** */
    public VaviFileChooserUI(JFileChooser filechooser) {
        super(filechooser);
    }

    /** */
    public static ComponentUI createUI(JComponent c) {
        return new VaviFileChooserUI((JFileChooser) c);
    }

    /** */
    public void setFileName(String filename) {
        String oldName;
        File file;

        oldName = getFileName();
        file = getFileChooser().getSelectedFile();

        if (file == null) {
            super.setFileName(oldName);

            return;
        }

        if (file.isDirectory()) {
            if (getFileChooser().isDirectorySelectionEnabled()) {
                super.setFileName(filename);
            } else {
                super.setFileName(oldName);
            }
        } else {
            if (getFileChooser().isFileSelectionEnabled()) {
                super.setFileName(filename);
            } else {
                super.setFileName(oldName);
            }
        }
    }
}
