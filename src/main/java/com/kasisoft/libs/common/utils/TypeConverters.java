package com.kasisoft.libs.common.utils;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.internal.*;

import com.kasisoft.libs.common.types.*;

import com.kasisoft.libs.common.pools.*;

import com.kasisoft.libs.common.*;

import jakarta.validation.constraints.*;

import java.lang.reflect.*;

import java.time.format.*;

import java.time.*;

import java.util.function.*;

import java.util.regex.Pattern;

import java.util.*;
import java.util.List;

import java.nio.file.*;

import java.awt.*;

import java.net.*;

import java.io.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class TypeConverters {

    private static final String NAN             = "NaN";

    private static final String NEG_INF         = "-INF";

    private static final String POS_INF1        = "+INF";

    private static final String POS_INF2        = "INF";

    private static final String MAX             = "MAX";

    private static final String MIN             = "MIN";

    private static final String DATE_PATTERN    = "yyyy-MM-dd";

    private static List<String> TRUE_VALUES     = Arrays.asList("yes", "ja", "j", "y", "on", "ein", "1", "-1", "an", "true", "enabled");

    private static List<String> FALSE_VALUES    = Arrays.asList("no", "nein", "n", "off", "aus", "0", "false", "disabled");

    private static String       DELIMITER       = ",";

    // string <-> color

    public static Color convertStringToColor(String value) {
        return encode(value, $val -> {
            if ($val.startsWith("#")) {
                return convertStringToColorNumerical($val);
            } else if ($val.startsWith("rgb(")) {
                return convertStringToColorRGB($val);
            }
            return convertStringToColorSymbolicName($val);
        });
    }

    private static Color convertStringToColorNumerical(@NotBlank String value) {
        if (value.length() == 7) {
            // rgb only
            var red   = value.substring(1, 3);
            var green = value.substring(3, 5);
            var blue  = value.substring(5, 7);
            return new Color(Integer.parseInt(red, 16), Integer.parseInt(green, 16), Integer.parseInt(blue, 16));
        } else if (value.length() == 9) {
            // argb
            var alpha = value.substring(1, 3);
            var red   = value.substring(3, 5);
            var green = value.substring(5, 7);
            var blue  = value.substring(7, 9);
            return new Color(Integer.parseInt(red, 16), Integer.parseInt(green, 16), Integer.parseInt(blue, 16), Integer.parseInt(alpha, 16));
        }
        throw new KclException(error_invalid_color.formatted(value));
    }

    private static Color convertStringToColorRGB(@NotBlank String value) {
        var idxOpen  = value.indexOf('(');
        var idxClose = value.indexOf(')', idxOpen + 1);
        if ((idxOpen != -1) && (idxClose != -1)) {
            var parts = convertStringToIntegerArray(value.substring(idxOpen + 1, idxClose));
            if (parts.length == 3) {
                // rgb
                return new Color(parts[0], parts[1], parts[2]);
            } else if (parts.length == 4) {
                // argb
                return new Color(parts[0], parts[1], parts[2], parts[3]);
            }
        }
        throw new KclException(error_invalid_color.formatted(value));
    }

    private static Color convertStringToColorSymbolicName(@NotBlank String value) {
        var fields = Color.class.getFields();
        for (var field : fields) {
            try {
                var color = (Color) field.get(null);
                if (field.getName().equalsIgnoreCase(value)) {
                    return color;
                }
            } catch (Exception ex) {
                throw new KclException(ex, error_invalid_color.formatted(value));
            }
        }
        throw new KclException(error_invalid_color.formatted(value));
    }

    public static String convertColorToString(Color value) {
        return encode(value, $val -> "#%02x%02x%02x%02x".formatted($val.getAlpha(), $val.getRed(), $val.getGreen(), $val.getBlue()));
    }

    // string <-> enum

    public static <T extends Enum<T>> T convertStringToEnum(String value, @NotNull Class<T> enumType) {
        return convertStringToEnum(value, enumType, false);
    }

    public static <T extends Enum<T>> T convertStringToEnum(String value, @NotNull Class<T> enumType, boolean ignorecase) {
        return encode(value, $val -> {
            T[] enums = enumType.getEnumConstants();
            for (int i = 0; i < enums.length; i++) {
                var text = String.valueOf(enums[i]);
                if (ignorecase) {
                    if (text.equalsIgnoreCase($val)) {
                        return enums[i];
                    }
                } else {
                    if (text.equals($val))  {
                        return enums[i];
                    }
                }
            }
            throw new KclException(error_invalid_enumeration_value.formatted(value));
        });
    }

    public static <T extends Enum<T>> String convertEnumToString(T value) {
        return encode(value, String::valueOf);
    }

    // string <-> version

    public static Version convertStringToVersion(String value) {
        return encode(value, $val -> {
            return Version.of($val).orElseThrow(() -> new KclException(Messages.error_version_cannot_parse_version.formatted($val)));
        });
    }

    public static String convertVersionToString(Version value) {
        return encode(value, String::valueOf);
    }

    // string <-> url

    public static URL convertStringToURL(String value) {
        return encode(value, $val -> {
            try {
                return URI.create($val.trim()).toURL();
            } catch (MalformedURLException ex) {
                throw new KclException(error_invalid_uri.formatted($val));
            }
        });
    }

    public static String convertURLToString(URL value) {
        return encode(value, $val -> $val.toExternalForm());
    }

    // string <-> uri

    public static URI convertStringToURI(String value) {
        return encode(value, $val -> URI.create($val.trim()));
    }

    public  static String convertURIToString(URI value) {
        return encode(value, $val -> {
            try {
                return $val.toURL().toExternalForm();
            } catch (MalformedURLException ex) {
                throw new KclException(error_invalid_uri.formatted($val));
            }
        });
    }

    // string <-> nio path

    public static Path convertStringToPath(String value) {
        return encode(value, $val -> Paths.get($val.replace('\\', '/')).normalize());
    }

    public  static String convertPathToString(Path value) {
        return encode(value, $val -> $val.normalize().toString().replace('\\', '/'));
    }

    // string <-> file

    public static File convertStringToFile(String value) {
        return convertStringToFile(value, false);
    }

    public static File convertStringToFile(String value, boolean canonical) {
        return encode(value, $val -> {
            var result = new File($val.replace('\\', '/').replace('/', File.separatorChar));
            if (canonical) {
                try {
                    result = result.getCanonicalFile();
                } catch (Exception ex) {
                    throw new KclException(ex, Messages.error_cannot_determine_canonical_file.formatted($val));
                }
            }
            return result;
        });
    }

    public  static String convertFileToString(File value) {
        return convertFileToString(value, false);
    }

    public  static String convertFileToString(File value, boolean canonical) {
        return encode(value, $val -> {
            if (canonical) {
                try {
                    $val = $val.getCanonicalFile();
                } catch (Exception ex) {
                    throw new KclException(ex, Messages.error_cannot_determine_canonical_file.formatted($val));
                }
            }
            return $val.getPath().replace('\\', '/');
        });
    }

    // string <-> date

    public static LocalDate convertStringToLocalDate(String value) {
        return convertStringToLocalDate(value, DATE_PATTERN);
    }

    public static LocalDate convertStringToLocalDate(String value, @NotBlank String pattern) {
        return encode(value, $val -> LocalDate.parse($val.trim(), DateTimeFormatter.ofPattern(pattern)));
    }

    public  static String convertLocalDateToString(LocalDate value) {
        return convertLocalDateToString(value, DATE_PATTERN);
    }

    public  static String convertLocalDateToString(LocalDate value, @NotBlank String pattern) {
        return encode(value, $val -> $val.format(DateTimeFormatter.ofPattern(pattern)));
    }

    // string <-> boolean

    public static Boolean convertStringToBoolean(String value) {
        return convertStringToBoolean(value, TRUE_VALUES, FALSE_VALUES);
    }

    public static Boolean convertStringToBoolean(String value, Collection<String> truevalues, Collection<String> falsevalues) {
        return encode(value, $val -> {
            var lower = $val.toLowerCase();
            if (truevalues.contains(lower)) {
                return true;
            }
            if (falsevalues.contains(lower)) {
                return false;
            }
            throw new KclException(Messages.error_invalid_boolean_value.formatted($val));
        });
    }

    public  static String convertBooleanToString(Boolean value) {
        return encode(value, String::valueOf);
    }

    // string <-> byte

    public static Byte convertStringToByte(String value) {
        return encode(value, $val -> Byte.parseByte($val.trim()));
    }

    public static String convertByteToString(Byte value) {
        return encode(value, $val -> Byte.toString($val));
    }

    // string <-> int

    public static Integer convertStringToInteger(String value) {
        return encode(value, $val -> Integer.parseInt($val.trim()));
    }

    public static String convertIntegerToString(Integer value) {
        return encode(value, $val -> Integer.toString($val));
    }

    // string <-> long

    public static Long convertStringToLong(String value) {
        return encode(value, $val -> Long.parseLong($val.trim()));
    }

    public static String convertLongToString(Long value) {
        return encode(value, $val -> Long.toString($val));
    }

    // string <-> short

    public static Short convertStringToShort(String value) {
        return encode(value, Short::parseShort);
    }

    public static String convertShortToString(Short value) {
        return encode(value, $val -> Short.toString($val));
    }

    // string <-> double

    public static Double convertStringToDouble(String value) {
        return encode(value, $val -> {
            $val = $val.trim();
            if (NAN.equalsIgnoreCase($val)) {
                return Double.NaN;
            }
            if (POS_INF1.equalsIgnoreCase($val) || POS_INF2.equalsIgnoreCase($val)) {
                return Double.POSITIVE_INFINITY;
            }
            if (NEG_INF.equalsIgnoreCase($val)) {
                return Double.NEGATIVE_INFINITY;
            }
            if (MAX.equalsIgnoreCase($val)) {
                return Double.MAX_VALUE;
            }
            if (MIN.equalsIgnoreCase($val)) {
                return Double.MIN_VALUE;
            }
            return Double.parseDouble($val);
        });
    }

    public static String convertDoubleToString(Double value) {
        return encode(value, $val -> {
            if (Double.isNaN($val)) {
                return NAN;
            }
            if (Double.isInfinite($val)) {
                if ($val < 0) {
                    return NEG_INF;
                } else {
                    return POS_INF1;
                }
            }
            if ($val == Double.MAX_VALUE) {
                return MAX;
            }
            if ($val == Double.MIN_VALUE) {
                return MIN;
            }
            return $val.toString();
        });
    }

    // string <-> float

    public static Float convertStringToFloat(String value) {
        return encode(value, $val -> {
            $val = $val.trim();
            if (NAN.equalsIgnoreCase($val)) {
                return Float.NaN;
            }
            if (POS_INF1.equalsIgnoreCase($val) || POS_INF2.equalsIgnoreCase($val)) {
                return Float.POSITIVE_INFINITY;
            }
            if (NEG_INF.equalsIgnoreCase($val)) {
                return Float.NEGATIVE_INFINITY;
            }
            if (MAX.equalsIgnoreCase($val)) {
                return Float.MAX_VALUE;
            }
            if (MIN.equalsIgnoreCase($val)) {
                return Float.MIN_VALUE;
            }
            return Float.parseFloat($val);
        });
    }

    public static String convertFloatToString(Float value) {
        return encode(value, $val -> {
            if (Float.isNaN($val)) {
                return NAN;
            }
            if (Float.isInfinite($val)) {
                if ($val < 0) {
                    return NEG_INF;
                } else {
                    return POS_INF1;
                }
            }
            if ($val == Float.MAX_VALUE) {
                return MAX;
            }
            if ($val == Float.MIN_VALUE) {
                return MIN;
            }
            return $val.toString();
        });
    }

    // string <-> Boolean[]

    public static Boolean[] convertStringToBooleanArray(String value) {
        return convertStringToBooleanArray(value, DELIMITER);
    }

    public static Boolean[] convertStringToBooleanArray(String value, String delimiter) {
        return convertStringToArray(value, delimiter, Boolean.class, TypeConverters::convertStringToBoolean);
    }

    public static String convertBooleanArrayToString(Boolean[] value) {
        return convertBooleanArrayToString(value, DELIMITER);
    }

    public static String convertBooleanArrayToString(Boolean[] value, String delimiter) {
        return convertArrayToString(value, delimiter, TypeConverters::convertBooleanToString);
    }

    // string <-> Byte[]

    public static Byte[] convertStringToByteArray(String value) {
        return convertStringToByteArray(value, DELIMITER);
    }

    public static Byte[] convertStringToByteArray(String value, String delimiter) {
        return convertStringToArray(value, delimiter, Byte.class, TypeConverters::convertStringToByte);
    }

    public static String convertByteArrayToString(Byte[] value, String delimiter) {
        return convertArrayToString(value, delimiter, TypeConverters::convertByteToString);
    }

    // string <-> Integer[]

    public static Integer[] convertStringToIntegerArray(String value) {
        return convertStringToIntegerArray(value, DELIMITER);
    }

    public static Integer[] convertStringToIntegerArray(String value, String delimiter) {
        return convertStringToArray(value, delimiter, Integer.class, TypeConverters::convertStringToInteger);
    }

    public static String convertIntegerArrayToString(Integer[] value) {
        return convertIntegerArrayToString(value, DELIMITER);
    }

    public static String convertIntegerArrayToString(Integer[] value, String delimiter) {
        return convertArrayToString(value, delimiter, TypeConverters::convertIntegerToString);
    }

    // string <-> Long[]

    public static Long[] convertStringToLongArray(String value) {
        return convertStringToLongArray(value, DELIMITER);
    }

    public static Long[] convertStringToLongArray(String value, String delimiter) {
        return convertStringToArray(value, delimiter, Long.class, TypeConverters::convertStringToLong);
    }

    public static String convertLongArrayToString(Long[] value) {
        return convertLongArrayToString(value, DELIMITER);
    }

    public static String convertLongArrayToString(Long[] value, String delimiter) {
        return convertArrayToString(value, delimiter, TypeConverters::convertLongToString);
    }

    // string <-> Short[]

    public static Short[] convertStringToShortArray(String value) {
        return convertStringToShortArray(value, DELIMITER);
    }

    public static Short[] convertStringToShortArray(String value, String delimiter) {
        return convertStringToArray(value, delimiter, Short.class, TypeConverters::convertStringToShort);
    }

    public static String convertShortArrayToString(Short[] value) {
        return convertShortArrayToString(value, DELIMITER);
    }

    public static String convertShortArrayToString(Short[] value, String delimiter) {
        return convertArrayToString(value, delimiter, TypeConverters::convertShortToString);
    }

    // string <-> Double[]

    public static Double[] convertStringToDoubleArray(String value) {
        return convertStringToDoubleArray(value, DELIMITER);
    }

    public static Double[] convertStringToDoubleArray(String value, String delimiter) {
        return convertStringToArray(value, delimiter, Double.class, TypeConverters::convertStringToDouble);
    }

    public static String convertDoubleArrayToString(Double[] value) {
        return convertDoubleArrayToString(value, DELIMITER);
    }

    public static String convertDoubleArrayToString(Double[] value, String delimiter) {
        return convertArrayToString(value, delimiter, TypeConverters::convertDoubleToString);
    }

    // string <-> Float[]

    public static Float[] convertStringToFloatArray(String value) {
        return convertStringToFloatArray(value, DELIMITER);
    }

    public static Float[] convertStringToFloatArray(String value, String delimiter) {
        return convertStringToArray(value, delimiter, Float.class, TypeConverters::convertStringToFloat);
    }

    public static String convertFloatArrayToString(Float[] value) {
        return convertFloatArrayToString(value, DELIMITER);
    }

    public static String convertFloatArrayToString(Float[] value, String delimiter) {
        return convertArrayToString(value, delimiter, TypeConverters::convertFloatToString);
    }

    private static <F, T> F encode(T decoded, Function<T, F> impl) {
        if (decoded != null) {
            return KclException.execute(() -> impl.apply(decoded), Messages.error_invalid_decoded_value.formatted(decoded));
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] convertStringToArray(String value, String delimiter, Class<T> type, Function<String, T> convert) {
        return encode(value, $val -> {
            var values = $val.split(Pattern.quote(delimiter));
            var result = (T[]) Array.newInstance(type, values.length);
            for (var i = 0; i < values.length; i++) {
                result[i] = convert.apply(values[i]);
            }
            return result;
        });
    }

    private static <T> String convertArrayToString(T[] value, String delimiter, Function<T, String> convert) {
        return encode(value, $val -> {
            return Buckets.bucketStringBuilder().forInstance($sb -> {
                if (value.length > 0) {
                    $sb.append(convert.apply(value[0]));
                    for (int i = 1; i < value.length; i++) {
                      $sb.append(delimiter);
                      $sb.append(convert.apply(value[i]));
                    }
                }
                return $sb.toString();
            });
        });
    }

} /* ENDCLASS */
