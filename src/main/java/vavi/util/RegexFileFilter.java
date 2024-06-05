/*
 * Copyright (c) 2001 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.util;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Regex でフィルタをかけるファイルフィルタです．
 *
 * @author <a href="mailto:umjammer@gmail.com">Naohide Sano</a> (nsano)
 * @version 0.00 010910 nsano initial version <br>
 *          0.01 010912 nsano ignores InterruptedException <br>
 */
public class RegexFileFilter extends javax.swing.filechooser.FileFilter implements FileFilter, Predicate<Path> {

    /** パターンの配列 */
    private final List<String> regexs = new ArrayList<>(1);

    /** フィルタの説明 */
    private String description;

    /**
     * ファイルフィルタを作成します．
     */
    public RegexFileFilter() {
    }

    /**
     * 指定したパターンのファイルフィルタを作成します．
     *
     * @param regex パターン
     */
    public RegexFileFilter(String regex) {
        regexs.add(regex);
    }

    /**
     * 指定したパターンのファイルフィルタを作成します．
     *
     * @param regex パターン
     */
    public RegexFileFilter(String regex, String description) {
        regexs.add(regex);
        this.description = description;
    }

    /**
     * フィルタの説明を設定します．
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * パターンを追加します．
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
Debug.println(Level.FINE, "no pattern");
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
Debug.println(Level.FINE, e);
        }

        return false;
    }

    @Override
    public boolean test(Path file) {
        if (Files.isDirectory(file)) {
            return true;
        }

        if (regexs.isEmpty()) {
Debug.println(Level.FINE, "no pattern");
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
Debug.println(Level.FINE, e);
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
