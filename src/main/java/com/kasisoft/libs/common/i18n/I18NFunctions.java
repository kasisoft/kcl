package com.kasisoft.libs.common.i18n;

import jakarta.validation.constraints.*;

import java.util.*;

/**
 * Collection of I18n related functions.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class I18NFunctions {

    /**
     * Returns the current locale. Uses the default if none had been set yet.
     *
     * @param locale
     *            The locale to be used.
     * @return The current locale.
     */
    @NotNull
    public static Locale getLocale(Locale locale) {
        if (locale == null) {
            return Locale.getDefault();
        } else {
            return locale;
        }
    }

} /* ENDCLASS */
