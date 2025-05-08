/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.io.File;
import java.io.FileFilter;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.getLogger;


/**
 * Regex file filter.
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010910 nsano initial version <br>
 *          0.01 010912 nsano ignores InterruptedException <br>
 */
public class RegexFileFilter extends javax.swing.filechooser.FileFilter implements FileFilter, Predicate<Path> {

    private static final Logger logger = getLogger(RegexFileFilter.class.getName());

    /** Pattern Array */
    private final List<String> regexs = new ArrayList<>(1);

    /** Filter Description */
    private String description;

    /**
     * Create a file filter.
     */
    public RegexFileFilter() {
    }

    /**
     * Creates a file filter for the specified pattern.
     *
     * @param regex pattern
     */
    public RegexFileFilter(String regex) {
        regexs.add(regex);
    }

    /**
     * Creates a file filter for the specified pattern.
     *
     * @param regex pattern
     */
    public RegexFileFilter(String regex, String description) {
        regexs.add(regex);
        this.description = description;
    }

    /**
     * Set the description of the filter.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Add a pattern.
     */
    public void addPattern(String regex) {
        regexs.add(regex);
    }

    @Override
    public boolean accept(File file) {

        if (file.isDirectory()) {
            return true;
        }

        if (regexs.isEmpty()) {
logger.log(Level.DEBUG, "no pattern");
            return true;
        }

        try {
            for (String regex : regexs) {
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(file.getName());
                if (m.matches()) {
                    return true;
                }
            }
        } catch (Exception e) {
logger.log(Level.DEBUG, e);
        }

        return false;
    }

    @Override
    public boolean test(Path file) {
        if (Files.isDirectory(file)) {
            return true;
        }

        if (regexs.isEmpty()) {
logger.log(Level.DEBUG, "no pattern");
            return true;
        }

        try {
            for (String regex : regexs) {
                Pattern p = Pattern.compile(regex);
                Matcher m = p.matcher(file.getFileName().toString());
                if (m.matches()) {
                    return true;
                }
            }
        } catch (Exception e) {
logger.log(Level.DEBUG, e);
        }

        return false;
    }

    @Override
    public String getDescription() {
        if (description == null) {
            if (regexs.isEmpty()) {
                return "all";
            }
            StringBuilder sb = new StringBuilder("Regex: ");
            for (String regex : regexs) {
                sb.append("'").append(regex).append("', ");
            }
            sb.setLength(sb.length() - 2);
            description = sb.toString();
        }
        return description;
    }
}
