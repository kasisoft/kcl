package com.kasisoft.libs.common.types;

import com.kasisoft.libs.common.comparator.Comparators;

import jakarta.validation.constraints.*;

import java.util.*;

/**
 * A simple descriptional datastructure for a version.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public record Version(int major, int minor, Integer micro, String qualifier) implements Comparable<Version> {

    private static final java.util.regex.Pattern PATTERN = java.util.regex.Pattern.compile("([\\d]+)([\\.]([\\d]+))?([\\.]([\\d]+))?(([\\.\\-_])(\\w+))?");

    public static Version of(int major, int minor) {
        return new Version(major, minor, null, null);
    }

    public static Version of(int major, int minor, int micro) {
        return new Version(major, minor, micro, null);
    }

    public static Version of(int major, int minor, String qualifier) {
        return new Version(major, minor, null, qualifier);
    }

    public static Version of(int major, int minor, int micro, String qualifier) {
        return new Version(major, minor, micro, qualifier);
    }

    public static Optional<Version> of(@NotBlank String version) {
        try {
            var matcher = PATTERN.matcher(version);
            if (matcher.matches()) {
                int     major     = toInteger(matcher.group(1));
                int     minor     = toInteger(matcher.group(3));
                Integer micro     = toInteger(matcher.group(5));
                // String  qdelim    = matcher.group(7);
                String  qualifier = matcher.group(8);
                return Optional.of(new Version(major, minor, micro, qualifier));
            }
        } catch (Exception ex) {
            // invalid version
        }
        return  Optional.empty();
    }

    private static Integer toInteger(String val) {
        if (val != null) {
            return Integer.parseInt(val);
        }
        return null;
    }

    /**
     * Creates a textual presentation of this version.
     *
     * @return A textual presentation of this version.
     */
    @NotBlank
    public String toText() {
        return toText('.');
    }

    /**
     * Creates a textual presentation of this version.
     *
     * @param qualifierdelim
     *            The delimiter which has to be used for the qualifier (sometimes you might wann use
     *            '_').
     * @return A textual presentation of this version.
     */
    @NotBlank
    public String toText(char qualifierdelim) {
        var microstr = micro     != null ? ".%s".formatted(micro) : "";
        var qstr     = qualifier != null ? "%s%s".formatted(qualifierdelim, qualifier) : "";
        return "%d.%d%s%s".formatted(major, minor, microstr, qstr);
    }

    @Override
    public String toString() {
        return toText();
    }

    @Override
    public int compareTo(Version other) {
        if (other == null) {
            return -1;
        }
        var result = major - other.major;
        if (result == 0) {
            result = minor - other.minor;
        }
        if (result == 0) {
            result = Comparators.nullSafeCompareTo(micro, other.micro);
        }
        if (result == 0) {
            result = Comparators.nullSafeCompareTo(qualifier, other.qualifier);
        }
        return result;
    }

} /* ENDRECORD */
