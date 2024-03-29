package com.kasisoft.libs.common.utils;

import com.kasisoft.libs.common.pools.*;

import com.kasisoft.libs.common.text.*;

import jakarta.validation.constraints.*;

import java.util.regex.Pattern;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class RegExpressions {

    public static String  YOUTUBE_ID_PATTERN_VALUE = ".*(?:youtu.be\\/|v\\/|u\\/\\w\\/|embed\\/|watch\\?v=)([^#\\&\\?]*).*";

    public static Pattern YOUTUBE_ID_PATTERN       = Pattern.compile(YOUTUBE_ID_PATTERN_VALUE);

    public static String  EMAIL_PATTERN_VALUE      = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public static Pattern EMAIL_PATTERN            = Pattern.compile(EMAIL_PATTERN_VALUE);

    public static String extractYoutubeId(@NotNull String url) {
        return extract(url, YOUTUBE_ID_PATTERN, 1);
    }

    public static String isEmail(@NotNull String url) {
        return extract(url, EMAIL_PATTERN, 0);
    }

    private static String extract(String value, @NotNull Pattern pattern, @Min(0) int number) {
        value = StringFunctions.cleanup(value);
        if (value != null) {
            var matcher = pattern.matcher(value);
            if (matcher.matches()) {
                if (number <= matcher.groupCount()) {
                    return matcher.group(number);
                }
            }
        }
        return null;
    }

    public static String oredKeywords(Collection<String> keywords) {
        // build a big OR of all keys
        return Buckets.stringBuilder().forInstance($sb -> {
            keywords.forEach($key -> $sb.append('|').append(Pattern.quote($key)));
            $sb.setCharAt(0, '(');
            $sb.append(')');
            return $sb.toString();
        });
    }

} /* ENDCLASS */
