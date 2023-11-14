package com.kasisoft.libs.common.i18n;

/**
 * Helper which provides formatting capabilities to a translation.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class I18NString {

    private String value;

    public I18NString(String value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @see String#format(String, Object...)
     */
    public String format(Object ... args) {
        if ((args == null) || (args.length == 0)) {
            return value;
        } else {
            try {
                return value.formatted(args);
            } catch (Exception ex) {
                return value;
            }
        }
    }

    @Override
    public String toString() {
        return value;
    }

} /* ENDCLASS */
