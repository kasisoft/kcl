package com.kasisoft.libs.common.utils;

import com.kasisoft.libs.common.functional.*;
import com.kasisoft.libs.common.pools.*;
import com.kasisoft.libs.common.text.*;

import javax.validation.constraints.*;

import java.util.function.*;

import java.util.*;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PropertyResolver implements SimpleFunction<String> {
  
  public static final String DEFAULT_VARFORMAT          = "${%s}";
  public static final String DEFAULT_SYSPROP_PREFIX     = "sys";
  public static final String DEFAULT_ENV_PREFIX         = "env";

  String                varFormat;
  Map<String, String>   replacementMap;
  List<String>          keys;
  
  public PropertyResolver() {
    varFormat           = DEFAULT_VARFORMAT;
    replacementMap      = new HashMap<>(20);
    keys                = new LinkedList<>();
  }
  
  public @NotNull PropertyResolver withVarFormat(String varFormat) {
    this.varFormat = varFormat != null ? varFormat : DEFAULT_VARFORMAT;
    return this;
  }
  
  private void addReplacement(String key, String value) {
    replacementMap.put(key, value);
    int idx = Collections.binarySearch(keys, key, ($a, $b) -> Integer.compare($b.length(), $a.length()));
    if (idx < 0) {
      idx = -idx - 1;
    }
    keys.add(idx, key);
  }
  
  /**
   * Creates a map allowing to variable based replacements.
   * 
   * @param settings      The object providing the variables.
   * @param prefix        An optional prefix. If set each variablename will use this prefix. F.e. prefix = sys and varname = basedir -> sys:basedir
   * @param getKeys       A function to get all keys of the settings object.
   * @param getValue      The function to retrieve the value.
   * 
   * @return   A map with the text replacements for expressions.
   */
  private <T> @NotNull PropertyResolver withReplacementMap(
    @NotNull T                                                              settings,
             String                                                         prefix,
    @NotNull Function<@NotNull T, ? extends Collection<@NotNull String>>    getKeys,
    @NotNull BiFunction<@NotNull T, String, @NotNull String>                getValue
  ) {
    var keys = getKeys.apply(settings);
    if ((keys != null) && (!keys.isEmpty())) {
      var                      cleanedPrefix = StringFunctions.cleanup(prefix);
      Function<String, String> prefixer      = cleanedPrefix != null ? $ -> cleanedPrefix + ":" + $ : Function.identity();
      for (var key : keys) {
        var textvalue   = getValue.apply(settings, key);
        var prefixedKey = prefixer.apply(key);
        addReplacement(String.format(varFormat, prefixedKey), textvalue);
      }
    }
    return this;
  }

  public @NotNull PropertyResolver withMap(@NotNull Map<String, String> map) {
    return withMap(null, map);
  }
  
  public @NotNull PropertyResolver withMap(String prefix, @NotNull Map<String, String> map) {
    return withReplacementMap(map, prefix, Map::keySet, Map::get);
  }

  public @NotNull PropertyResolver withProperties(@NotNull Properties properties) {
    return withProperties(null, properties);
  }

  public @NotNull PropertyResolver withProperties(String prefix, @NotNull Properties properties) {
    return withReplacementMap(properties, prefix, $ -> Collections.list((Enumeration<String>) $.propertyNames()), Properties::getProperty);
  }
  
  public @NotNull PropertyResolver withEnvironment() {
    return withEnvironment((String) null, System.getenv());
  }
  
  public @NotNull PropertyResolver withEnvironment(String prefix) {
    return withEnvironment(prefix, System.getenv());
  }

  public @NotNull PropertyResolver withEnvironment(@NotNull Map<String, String> map) {
    return withEnvironment(null, map);
  }

  public @NotNull PropertyResolver withEnvironment(String prefix, @NotNull Map<String, String> map) {
    prefix = StringFunctions.cleanup(prefix);
    return withMap(prefix != null ? prefix : DEFAULT_ENV_PREFIX, map);
  }
  
  public @NotNull PropertyResolver withSysProperties() {
    return withSysProperties((String) null, System.getProperties());
  }
  
  public @NotNull PropertyResolver withSysProperties(String prefix) {
    return withSysProperties(prefix, System.getProperties());
  }

  public @NotNull PropertyResolver withSysProperties(@NotNull Properties properties) {
    return withSysProperties(null, properties);
  }

  public @NotNull PropertyResolver withSysProperties(String prefix, @NotNull Properties properties) {
    prefix = StringFunctions.cleanup(prefix);
    return withProperties(prefix != null ? prefix : DEFAULT_SYSPROP_PREFIX, properties);
  }

  @Override
  public String apply(String input) {
    return Buckets.bucketStringFBuilder().forInstance($ -> {
      $.append(input);
      $.replaceAll(replacementMap);
      return $.toString();
    });
  }
  
} /* ENDCLASS */
