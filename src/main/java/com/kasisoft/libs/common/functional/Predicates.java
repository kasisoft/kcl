package com.kasisoft.libs.common.functional;

import org.w3c.dom.*;

import com.kasisoft.libs.common.pools.*;

import com.kasisoft.libs.common.utils.*;

import jakarta.validation.constraints.*;

import java.util.function.*;

import java.util.regex.Pattern;

import java.nio.file.*;

/**
 * Collection of predicates.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public class Predicates {

    public static final KPredicate<String> IS_JAVA_CLASS_FILE           = new SuffixPredicate(".class");

    public static final KPredicate<Path>   IS_JAVA_CLASS_PATH           = path(IS_JAVA_CLASS_FILE);

    public static final KPredicate<String> IS_YAML_FILE                 = new SuffixPredicate(".yaml", ".yml");

    public static final KPredicate<Path>   IS_YAML_PATH                 = path(IS_YAML_FILE);

    public static final KPredicate<String> IS_JSON_FILE                 = new SuffixPredicate(".json");

    public static final KPredicate<Path>   IS_JSON_PATH                 = path(IS_JSON_FILE);

    public static final KPredicate<String> IS_XML_FILE                  = new SuffixPredicate(".xml");

    public static final KPredicate<Path>   IS_XML_PATH                  = path(IS_XML_FILE);

    public static final KPredicate<String> IS_RESOURCE                  = IS_JAVA_CLASS_FILE.negate();

    public static final KPredicate<String> IS_RESOURCE_FILE             = IS_RESOURCE.and($ -> !$.endsWith("/"));

    public static final KPredicate<String> IS_RESOURCE_DIR              = IS_RESOURCE.and($ -> $.endsWith("/"));

    public static final KPredicate<String> IS_INNER_JAVA_CLASS_FILE     = new IsInnerJavaClassFile();

    public static final KPredicate<String> IS_ENCLOSING_JAVA_CLASS_FILE = IS_JAVA_CLASS_FILE.and(IS_INNER_JAVA_CLASS_FILE.negate());

    public static final KPredicate<String> IS_SPI_FILE                  = new IsSPIFile();

    public static final KPredicate<String> IS_MAGNOLIA_FILE             = new IsMagnoliaFile();

    public static final KPredicate<String> IS_MAVEN_FILE                = new IsMavenFile();

    public static final KPredicate<String> IS_JAVA_FQDN                 = new IsJavaFqdn();

    public static final KPredicate<String> IS_BOOLEAN                   = $ -> isValid($, TypeConverters::convertStringToBoolean);

    public static final KPredicate<String> IS_LONG                      = $ -> isValid($, Long::parseLong);

    public static final KPredicate<String> IS_INTEGER                   = $ -> isValid($, Integer::parseInt);

    public static final KPredicate<String> IS_SHORT                     = $ -> isValid($, Short::parseShort);

    public static final KPredicate<String> IS_BYTE                      = $ -> isValid($, Byte::parseByte);

    public static final KPredicate<String> IS_FLOAT                     = $ -> isValid($, Float::parseFloat);

    public static final KPredicate<String> IS_DOUBLE                    = $ -> isValid($, Double::parseDouble);

    private static boolean isValid(String value, @NotNull Function<String, ?> parse) {
        if (value != null) {
            try {
                parse.apply(value);
                return true;
            } catch (Exception ex) {
                // valid case
            }
        }
        return false;
    }

    @NotNull
    public static KPredicate<Node> isXmlElement(@NotNull String tag) {
        return new IsXmlElement(tag);
    }

    @NotNull
    public static KPredicate<Path> path(@NotNull KPredicate<String> predicate) {
        return $path -> predicate.test($path.toString());
    }

    @NotNull
    public static <T> Predicate<T> notNull() {
        return $ -> $ != null;
    }

    @NotNull
    public static <T> Predicate<T> acceptAll() {
        return $ -> true;
    }

    @NotNull
    public static <T> KPredicate<T> acceptAllIfUnset(KPredicate<T> test) {
        if (test != null) {
            return test;
        }
        return $ -> true;
    }

    @NotNull
    public static <T> Predicate<T> acceptNone() {
        return $ -> false;
    }

    @NotNull
    public static <T> KPredicate<T> acceptNoneIfUnset(KPredicate<T> test) {
        if (test != null) {
            return test;
        }
        return $ -> false;
    }

    private static record IsXmlElement(String tag) implements KPredicate<Node> {

        @Override
        public boolean test(@NotNull Node node) {
            if (node instanceof Element element) {
                return tag.equals(element.getTagName());
            }
            return false;
        }

    } /* ENDCLASS */

    private static class IsMavenFile implements KPredicate<String> {

        @Override
        public boolean test(@NotNull String resource) {
            return resource.endsWith("/pom.xml") ||
                   resource.endsWith("/pom.properties") ||
                   resource.equals("pom.xml") ||
                   resource.equals("pom.properties");
        }

    } /* ENDCLASS */

    private static class IsMagnoliaFile implements KPredicate<String> {

        private static final String PREFIX = "META-INF/magnolia/";

        @Override
        public boolean test(@NotNull String resource) {
            boolean equals = PREFIX.equals(resource);
            return resource.startsWith(PREFIX) && (!equals);
        }

    } /* ENDCLASS */

    private static class IsSPIFile implements KPredicate<String> {

        private static final String PREFIX = "META-INF/services/";

        @Override
        public boolean test(@NotNull String resource) throws Exception {
            boolean equals = PREFIX.equals(resource);
            if ((!equals) && resource.startsWith(PREFIX) && (resource.indexOf('$') == -1)) {
                return IS_JAVA_FQDN.test(resource.substring(PREFIX.length()));
            }
            return false;
        }

    } /* ENDCLASS */

    private static class SuffixPredicate implements KPredicate<String> {

        Pattern pattern;

        public SuffixPredicate(@NotNull String ... suffices) {
            pattern = Buckets.stringBuilder().forInstance($sb -> {
                $sb.append("^(.+)(");
                $sb.append(suffices[0]);
                for (var i = 1; i < suffices.length; i++) {
                    $sb.append("|");
                    $sb.append(suffices[i]);
                }
                $sb.append(")$");
                return Pattern.compile($sb.toString());
            });
        }

        @Override
        public boolean test(@NotNull String resource) {
            return pattern.matcher(resource).matches();
        }

    } /* ENDCLASS */

    private static class IsInnerJavaClassFile implements KPredicate<String> {

        private static Pattern PATTERN = Pattern.compile("^([^$]+)\\$(.*)(.class)$");

        @Override
        public boolean test(@NotNull String resource) {
            return PATTERN.matcher(resource).matches();
        }

    } /* ENDCLASS */

    private static class IsJavaFqdn implements KPredicate<String> {

        private static Pattern PATTERN = Pattern.compile("^(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)$");

        @Override
        public boolean test(@NotNull String classname) {
            if (!classname.endsWith(".class")) {
                // to be more accurate: split segments and make sure that the segments aren't keywords
                return PATTERN.matcher(classname).matches();
            }
            return false;
        }

    } /* ENDCLASS */

} /* ENDCLASS */
