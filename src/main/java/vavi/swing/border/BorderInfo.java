/*
 * Copyright (c) 2002 by Naohide Sano, All rights reserved.
 *
 * Programmed by Naohide Sano
 */

package vavi.swing.border;

import java.beans.BeanInfo;


/**
 * Border クラスの BeanInfo です．
 * <p>
 * Border は getter メソッドが無いクラスがほとんどなので，getter メソッド のみを扱う
 * PropertyDescriptor として BorderPropertyDescriptor を用意して 使用します．
 * </p>
 * <p>
 * Beans の仕様とは違うので，Introspector ではなく BorderInfoFactory を 用いて BorderInfo
 * クラスを取得してください．
 * </p>
 *
 * @author <a href="mailto:vavivavi@yahoo.co.jp">Naohide Sano</a> (nsano)
 * @version 0.00 020518 nsano initial version <br>
 */
public interface BorderInfo extends BeanInfo {

    /** */
    BorderPropertyDescriptor[] getBorderPropertyDescriptors();
}

/* */
