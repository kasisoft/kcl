package com.kasisoft.libs.common.i18n;

import com.kasisoft.libs.common.constants.Encoding;

import com.kasisoft.libs.common.KclException;
import com.kasisoft.libs.common.text.StringFunctions;
import com.kasisoft.libs.common.types.Pair;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import java.io.Writer;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * A helper which initializes the translations managed by a class. Each translation class is supposed to provide fields
 * with the modifiers <code>public static String</code> and a corresponding <code>I18N</code> annotation.
 * The initialization of such a class could be performed like this:
 *
 * <pre>
 * public class MyTexts {
 *
 *    I18N("You made a fault.")
 *    public static String   message;
 *
 *    static {
 *      I18NSupport.initialize( MyTexts.class );
 *    }
 *
 * }
 * </pre>
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class I18NSupport {

  static final int MODIFIERS = Modifier.STATIC | Modifier.PUBLIC;

  static Set<Class<?>> CLASSES = new HashSet<>();

  /**
   * Returns <code>true</code> if the supplied field can be considered to be a translation field.
   *
   * @param field   The field that is supposed to be tested.
   *
   * @return   <code>true</code> <=> The supplied field is a translation field.
   */
  @SuppressWarnings("deprecation")
  private static boolean isTranslationField(@NotNull Field field) {
    var modifier = field.getModifiers();
    if (modifier != MODIFIERS) {
      return false;
    }
    return (field.getType() == String.class) || (field.getType() == I18NString.class);
  }

  /**
   * Creates a map with all translatable fields.
   *
   * @param clazz   The class which is supposed to be translated.
   *
   * @return   A mapping between field names and the corresponding {@link Field} instances.
   */
  private static @NotNull Map<String, Field> collectFields(@NotNull Class<?> clazz) {
    var result = new HashMap<String, Field>();
    var fields = clazz.getDeclaredFields();
    for (var field : fields) {
      if (isTranslationField(field)) {
        result.put(field.getName(), field);
      }
    }
    return result;
  }

  /**
   * Loads all properties matching the current locale (we support different formats).
   *
   * @param candidates   A list of resource pathes pointing to possible translations.
   *
   * @return   The {@link Properties} instance providing all current translations.
   */
  private static @NotNull Properties loadTranslations(@NotNull Class<?> clazz, @NotNull String[] candidates) {
    var result = new Properties();
    for (var variant : candidates) {

      if (variant == null) {
        continue;
      }

      var url = clazz.getClassLoader().getResource(variant);
      if (url == null) {
        continue;
      }

      try (
        var instream = url.openStream();
        var reader   = Encoding.UTF8.openReader(instream) 
       ) {
        var props = new Properties();
        props.load(reader);
        apply(result, props);
      } catch(Exception ex) {
        throw KclException.wrap(ex);
      }

    }
    return result;
  }

  /**
   * Applies all new properties to the supplied receiver.
   *
   * @param receiver   The result properties.
   * @param source     The source properties.
   */
  private static void apply(@NotNull Properties receiver, @NotNull Properties source) {
    var names = source.stringPropertyNames();
    for (var name : names) {
      if (!receiver.containsKey(name)) {
        receiver.setProperty(name, source.getProperty(name));
      }
    }
  }

  /**
   * Applies all translations to the supplied class while changing the field values.
   *
   * @param prefix         A prefix which has to be used for the property names.
   * @param translations   The {@link Properties} instance which contains all translations.
   * @param fields         The map with all {@link Field} instances that have to be changed.
   */
  private static void applyTranslations(@NotNull String prefix, @NotNull Properties translations, @NotNull Map<String, Field> fields) {
    
    for(var entry : fields.entrySet() ) {

      var    field    = entry.getValue();
      var    i18n     = field.getAnnotation(I18N.class);
      String property = null;

      // check whether the key is being overridden
      if (i18n != null) {
        var key = StringFunctions.cleanup(i18n.key());
        if (key != null) {
          property  = key;
        }
      }

      // use the default key mechanism
      if (property == null) {
        property = String.format("%s%s", prefix, entry.getKey());
      }

      applyFieldValue(field, extractValue(translations, i18n, property));

    }

  }

  /**
   * Returns the value from the available translations or the default.
   *
   * @param translations   The currently available translations.
   * @param i18n           The annotation which might provide a default value.
   * @param property       The property which identifies the field.
   *
   * @return   The value associated with the property.
   */
  private static @Null String extractValue(@NotNull Properties translations, @Null I18N i18n, @NotBlank String property) {
    String result = null;
    if (translations.containsKey(property)) {
      result = translations.getProperty(property);
    } else {
      if (i18n != null) {
        result = i18n.value();
      }
    }
    return result;
  }

  /**
   * Applies the supplied value to the corresponding field.
   *
   * @param field   The field which is supposed to be edited.
   * @param value   The value which has to be set.
   */
  @SuppressWarnings("deprecation")
  private static void applyFieldValue(@NotNull Field field, @Null String value) {
    if (value != null) {
      try {
        if (field.getType() == String.class) {
          field.set( null, value );
        } else {
          var i18nstring = (I18NString) field.get(null);
          if (i18nstring == null) {
            i18nstring = new I18NString(value);
            field.set(null, i18nstring);
          }
          i18nstring.setValue(value);
        }
      } catch (IllegalAccessException ex ) {
        // won't happen as the supplied fields are definitely accessible
      }
    }
  }

  /**
   * Applies all translations to the supplied class using the default locale.
   *
   * @param clazz   The class that is supposed to be translated.
   */
  public static void initialize(@NotNull Class<?> clazz) {
    initialize(null, clazz);
  }

  /**
   * Applies all translations to the supplied class.
   *
   * @param locale      The {@link Locale} instance which has to be used. 
   * @param clazz       The class that is supposed to be translated.
   */
  public static void initialize(@Null Locale locale, @NotNull Class<?> clazz) {

    locale = I18NFunctions.getLocale(locale);

    var baseAndPrefix = extractBaseAndPrefix(clazz);
    var base          = baseAndPrefix.getValue1();
    var prefix        = baseAndPrefix.getValue2();
    var candidates    = getTranslationCandidates(locale, base);

    applyTranslations(prefix, loadTranslations(clazz, candidates), collectFields(clazz));

    CLASSES.add(clazz);

  }

  private static @NotNull String[] getTranslationCandidates(@NotNull Locale locale, @NotNull String base) {
    var result = new String[3];
    var country    = StringFunctions.cleanup(locale.getCountry());
    if (country != null) {
      result[0] = String.format("%s_%s_%s.properties", base, locale.getLanguage(), country); // f.e. de_DE
    }
    result[1] = String.format("%s_%s.properties", base, locale.getLanguage()); // f.e. de
    result[2] = String.format("%s.properties", base);
    return result;
  }

  private static Pair<String, String> extractBaseAndPrefix(@NotNull Class<?> clazz) {
    var    basename = clazz.getAnnotation(I18NBasename.class);
    String base     = null;
    String prefix   = null;
    if (basename != null) {
      base    = basename.resource();
      prefix  = basename.prefix();
    } else {
      base    = clazz.getName().toLowerCase().replace('.','/');
      prefix  = "";
    }
    return new Pair<>(base, prefix);
  }

  @SuppressWarnings("null")
  private static @NotNull Map<String, String> collectTranslations(@NotNull String prefix, @NotNull Map<String, Field> fields) {
    var result = new HashMap<String, String>(fields.size());
    for (var entry : fields.entrySet()) {

      var    field    = entry.getValue();
      var    i18n     = field.getAnnotation(I18N.class);
      String property = null;

      // check whether the key is being overridden
      if (i18n != null) {
        var key = StringFunctions.cleanup(i18n.key());
        if (key != null) {
          property  = key;
        }
      }

      // use the default key mechanism
      if (property == null) {
        property = String.format("%s%s", prefix, entry.getKey());
      }

      result.put(property, i18n.value());

    }
    return result;
  }

  public static void writeProperties(@Null Locale locale, @NotNull Class<?> clazz, @NotNull Writer writer) {

    locale = I18NFunctions.getLocale(locale);

    var baseAndPrefix = extractBaseAndPrefix(clazz);
    var prefix        = baseAndPrefix.getValue2();

    var translations  = collectTranslations(prefix, collectFields(clazz));
    var keys          = new ArrayList<>(translations.keySet());
    Collections.sort(keys);

    for (var key : keys) {
      try {
        writer.write(String.format("%s=%s\n", key, translations.get(key)));
      } catch (Exception ex) {
        throw KclException.wrap(ex);
      }
    }

  }

  public static void setLocale(Locale newLocale) {
    if (newLocale != null) {
      CLASSES.forEach($ -> initialize(newLocale, $));
      Locale.setDefault(newLocale);
    }
  }

} /* ENDCLASS */
