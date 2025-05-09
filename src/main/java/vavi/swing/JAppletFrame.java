/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing;

import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AppletStub;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import vavi.util.Debug;

import static java.lang.System.getLogger;


/**
 * Run an Applet as an application.
 * <p>
 * Using this class you can add a trivial main program to any Applet and run it
 * directly, as well as from a browser or the appletviewer. And unlike some
 * versions of this concept, MainFrame implements both images and sound.
 * <p>
 * Sample main program:
 *
 * <pre>
 * public static void main(String[] args) {
 *     new vavi.swing.JAppletFrame(new ThisApplet(), args, 400, 400);
 * }
 * </pre>
 *
 * The only methods you need to know about are the constructors.
 * <p>
 * You can specify Applet parameters on the command line, as name=value. For
 * instance, the equivalent of:
 *
 * <pre>
 *
 *  &lt;PARAM NAME=&quot;vavi.swing.JAppletFrame.parameter.pause&quot; VALUE=&quot;200&quot;&gt;
 *
 * </pre>
 *
 * would just be:
 *
 * <pre>
 * pause = 200
 * </pre>
 *
 * You can also specify three special parameters:
 *
 * <pre>
 *
 *  vavi.swing.JAppletFrame.parameter.width=N          Width of the Applet.
 *  vavi.swing.JAppletFrame.parameter.height=N         Height of the Applet.
 *  vavi.swing.JAppletFrame.parameter.barebones=true   Leave off the menu bar and status area.
 *
 * </pre>
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010901 nsano port from Acme.MainFrame <br>
 *          0.01 020415 nsano change class name <br>
 *          0.02 020419 nsano change package <br>
 */
public class JAppletFrame extends JFrame implements Runnable, AppletStub, AppletContext {

    private static final Logger logger = getLogger(JAppletFrame.class.getName());

    // Context parameters
    private static final String VERSION = "19-Apr-02";

    private static final String VENDOR = "Vavisoft";

    private static final String VENDOR_URL = "http://www.vavisoft.com/";

    /** Whether to hide the menu bar and status bar */
    private boolean barebones = false;

    /** Status Bar */
    private JLabel label = null;

    /** Applet parameters */
    private String[] args = null;

    private static int instances = 0;

    /** The applet class name */
    private String name;

    private Applet applet;

    private Dimension appletSize;

    private JPanel appletPanel;

    private static final String PARAM_PROP_PREFIX = "vavi.swing.JAppletFrame.parameter.";

    /** Constructor with everything specified. */
    public JAppletFrame(Applet applet, String[] args, int width, int height) {
        build(applet, args, width, height);
    }

    /** Constructor with no default width/height. */
    public JAppletFrame(Applet applet, String[] args) {
        build(applet, args, -1, -1);
    }

    /** Constructor with no arg parsing. */
    public JAppletFrame(Applet applet, int width, int height) {
        build(applet, null, width, height);
    }

    /** Internal constructor routine. */
    private void build(Applet applet, String[] args, int width, int height) {

        ++instances;
        this.applet = applet;
        this.args = args;
        applet.setStub(this);
        name = applet.getClass().getName();
        setTitle(name);

        // Set up properties.
        Properties props = System.getProperties();
        props.put("browser", getClass().getName());
        props.put("browser.version", VERSION);
        props.put("browser.vendor", VENDOR);
        props.put("browser.vendor.url", VENDOR_URL);

        // Turn args into parameters by way of the properties list.
        if (args != null) {
            parseArgs(args, props);
        }

        // If width and height are specified in the parameters, override
        // the compiled-in values.
        String widthStr = getParameter(PARAM_PROP_PREFIX + "width");
        if (widthStr != null) {
            width = Integer.parseInt(widthStr);
        }
        String heightStr = getParameter(PARAM_PROP_PREFIX + "height");
        if (heightStr != null) {
            height = Integer.parseInt(heightStr);
        }

        // Were width and height specified somewhere?
        if (width == -1 || height == -1) {
            System.err.println("Width and height must be specified.");
            return;
        }

        // Do we want to run bare-bones?
        String bonesStr = getParameter(PARAM_PROP_PREFIX + "barebones");
        if (bonesStr != null && bonesStr.equals("true")) {
            barebones = true;
        }

        if (!barebones) {
            // Make menu bar.
            JMenuBar mb = new JMenuBar();
            JMenu m = new JMenu("Applet");
            // How to display a light menu (JMenu) on a heavy one (Applet).
            m.getPopupMenu().setLightWeightPopupEnabled(false);
            m.add(new AbstractAction("Restart") {
                @Override
                public void actionPerformed(ActionEvent ev) {
                    JAppletFrame.this.applet.stop();
                    JAppletFrame.this.applet.destroy();
                    Thread thread = new Thread(JAppletFrame.this);
                    thread.start();
                }
            });
            m.add(new AbstractAction("Clone") {
                @Override
                public void actionPerformed(ActionEvent ev) {
                    try {
                        build(JAppletFrame.this.applet.getClass().getDeclaredConstructor().newInstance(), JAppletFrame.this.args, appletSize.width, appletSize.height);
                    } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | RuntimeException | InvocationTargetException e) {
                        showStatus(e.getMessage());
                    }
                }
            });
            m.add(new AbstractAction("Close") {
                @Override
                public void actionPerformed(ActionEvent ev) {
                    setVisible(false);
                    remove(JAppletFrame.this.applet);
                    JAppletFrame.this.applet.stop();
                    JAppletFrame.this.applet.destroy();
                    if (label != null) {
                        remove(label);
                    }
                    dispose();
                    --instances;
                    if (instances == 0) {
                        System.exit(0);
                    }
                }
            });
            m.add(new AbstractAction("Quit") {
                @Override
                public void actionPerformed(ActionEvent ev) {
                    JAppletFrame.this.dispatchEvent(new WindowEvent(JAppletFrame.this, WindowEvent.WINDOW_CLOSING));
                }
            });
            mb.add(m);
            setJMenuBar(mb);
        }

//        addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent ev) {
//                System.exit(0);
//            }
//        });
logger.log(Level.TRACE, width + ", " + height);
        appletSize = new Dimension(width, height);
logger.log(Level.TRACE, "applet: " + appletSize.width + ", " + appletSize.height);

        // Layout components.

        appletPanel = new JPanel(new BorderLayout());
        appletPanel.setPreferredSize(appletSize);
        getContentPane().add(BorderLayout.CENTER, appletPanel);

        if (!barebones) {
            JPanel borderPanel = new JPanel();
            borderPanel.setLayout(new BorderLayout());
            label = new JLabel(name);
            borderPanel.add(BorderLayout.CENTER, label);
            getContentPane().add(BorderLayout.SOUTH, borderPanel);
        }

        // Set up size.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        validate();

        setVisible(true);

        // Start a separate thread to call the applet's init() and start()
        // methods, in case they take a long time.
        new Thread(this).start();
    }

    /**
     * Turn command-line arguments into Applet parameters, by way of the properties list.
     */
    private static void parseArgs(String[] args, Properties props) {
        for (String arg : args) {
            int ind = arg.indexOf('=');
            if (ind == -1) {
                props.put(PARAM_PROP_PREFIX + arg.toLowerCase(), "");
            } else {
                props.put(PARAM_PROP_PREFIX + arg.substring(0, ind).toLowerCase(), arg.substring(ind + 1));
            }
        }
    }

    @Override
    public void showStatus(String status) {
        if (label != null)
            label.setText(status);
    }

    // Methods from Runnable.

    /** Separate thread to call the applet's init() and start() methods. */
    @Override
    public void run() {
        showStatus(name + " initializing...");
        applet.init();
        appletPanel.add(BorderLayout.CENTER, applet);
        validate();
        showStatus(name + " starting...");
        applet.start();
        validate();
        showStatus(name + " running...");
    }

    // Methods from AppletStub.

    @Override
    public boolean isActive() {
        return true;
    }

    /** Returns the current directory. */
    @Override
    public URL getDocumentBase() {
        String dir = System.getProperty("user.dir");
        String urlDir = dir.replace(File.separatorChar, '/');
        try {
            return new URL("file:" + urlDir + "/");
        } catch (MalformedURLException e) {
            return null;
        }
    }

    /**
     * Hack: loop through each item in CLASSPATH, checking if the appropriately named .class file exists there. But this doesn't
     * account for .zip files.
     */
    @Override
    public URL getCodeBase() {
        String path = System.getProperty("java.class.path");
        Enumeration<?> st = new StringTokenizer(path, ":");
        while (st.hasMoreElements()) {
            String dir = (String) st.nextElement();
            String filename = dir + File.separatorChar + name + ".class";
            File file = new File(filename);
            if (file.exists()) {
                String urlDir = dir.replace(File.separatorChar, '/');
                try {
                    return new URL("file:" + urlDir + "/");
                } catch (MalformedURLException e) {
                    return null;
                }
            }
        }
        return null;
    }

    /** Return a parameter via the munged names in the properties list. */
    @Override
    public String getParameter(String name) {
        return System.getProperty(PARAM_PROP_PREFIX + name.toLowerCase());
    }

    /**
     * Change the frame's size by the same amount that the applet's size is changing.
     */
    @Override
    public void appletResize(int width, int height) {
logger.log(Level.TRACE, Debug.getTopCallerMethod("vavi"));

        appletPanel.setPreferredSize(new Dimension(width, height));
logger.log(Level.TRACE, "resize: " + width + ", " + height);
        Dimension frameSize = getSize();
logger.log(Level.TRACE, "frame: " + frameSize.width + ", " + frameSize.height);
        Insets insets = getInsets();

//        if (!barebones) {
//            insets.bottom += label.getHeight();
//        }

logger.log(Level.TRACE, "insets: " + insets.top + ", " + insets.bottom);
logger.log(Level.TRACE, "insets: " + insets.left + ", " + insets.right);

//        frameSize.width = width + insets.left + insets.right; frameSize.height = height + insets.top + insets.bottom;

logger.log(Level.TRACE, "frame: " + frameSize.width + ", " + frameSize.height);
//        setSize(frameSize);

        appletSize = applet.getSize();
logger.log(Level.TRACE, "applet: " + appletSize.width + ", " + appletSize.height);
    }

    @Override
    public AppletContext getAppletContext() {
        return this;
    }

    // Methods from AppletContext.

    /**
     * TODO can optimize, see AppletFrame_0.java
     */
    @Override
    public AudioClip getAudioClip(URL url) {
        return Applet.newAudioClip(url);
    }

    private static final Toolkit toolkit = Toolkit.getDefaultToolkit();

    @Override
    public Image getImage(URL url) {
        try {
            Object content = url.getContent();
            if (content instanceof ImageProducer ip) {
                return toolkit.createImage(ip);
            } else if (content instanceof InputStream is) { // for ikvm
                //int i = 0;
//while (is.available() > 0) {
// int c = is.read();
// System.err.printf("%02x ", c);
// i++;
// if (i % 16 == 0) {
//  System.err.println();
// }
//}
//System.err.println();
                return ImageIO.read(is);
            } else {
System.err.println("unhandled content type: " + content);
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    /** Returns this Applet or nothing. */
    @Override
    public Applet getApplet(String name) {
        if (name.equals(this.name)) {
            return applet;
        }
        return null;
    }

    /** Just yields this applet. */
    @Override
    public Enumeration<Applet> getApplets() {
        Vector<Applet> v = new Vector<>();
        v.addElement(applet);
        return v.elements();
    }

    /** Ignore. */
    @Override
    public void showDocument(URL url) {
    }

    /** Ignore. */
    @Override
    public void showDocument(URL url, String target) {
    }

    /** Ignore. */
    @Override
    public void setStream(String key, InputStream stream) throws IOException {
    }

    /** Ignore. */
    @Override
    public InputStream getStream(String key) {
        return null;
    }

    /** Ignore. */
    @Override
    public Iterator<String> getStreamKeys() {
        return null;
    }
}
