package com.kasisoft.libs.common.test.i18n;

import com.kasisoft.libs.common.i18n.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Messages4 {

    @I18N("Default.0")
    public static String     m0;

    @I18N("Default.1")
    public static String     m1;

    @I18N("Default.%s.2")
    public static String     m2;

    @I18N("Default.3")
    public static String     m3;

    @I18N("Default.4")
    public static String     m4;

    static {
        I18NSupport.initialize(Messages4.class);
    }

} /* ENDCLASS */
