package com.kasisoft.libs.common.text;

import com.kasisoft.libs.common.internal.text.*;

import com.kasisoft.libs.common.model.*;

import java.util.function.*;

import java.util.regex.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface TextProcessingFactory<T extends CharSequence> {

  TextProcessingFactory<String>         STRING          = new TextProcessingFactoryImpl<>( String.class );

  TextProcessingFactory<StringBuffer>   STRINGBUFFER    = new TextProcessingFactoryImpl<>( StringBuffer.class );

  TextProcessingFactory<StringBuilder>  STRINGBUILDER   = new TextProcessingFactoryImpl<>( StringBuilder.class );

  TextProcessingFactory<StringFBuffer>  STRINGFBUFFER   = new TextProcessingFactoryImpl<>( StringFBuffer.class );

  TextProcessingFactory<StringFBuilder> STRINGFBUILDER  = new TextProcessingFactoryImpl<>( StringFBuilder.class );

  /**
   * Returns an operation that allows to replace the supplied key by a corresponding value. This operation
   * is case sensitive.
   * 
   * @param key     The key to be replaced. Not <code>null</code>.
   * @param value   The value to be inserted. Not <code>null</code>.
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> replace( String key, String value );

  /**
   * Returns an operation that allows to replace the supplied key by a corresponding value.
   * 
   * @param key             The key to be replaced. Not <code>null</code>.
   * @param value           The value to be inserted. Not <code>null</code>.
   * @param caseSensitive   <code>true</code> <=> The task shall be case sensitive.
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> replace( String key, String value, boolean caseSensitive );

  /**
   * Returns an operation that allows to replace the supplied keys by their corresponding values. The execution
   * order is undefined. This operation is case sensitive.
   * 
   * @param replacements   The key-value pairs to be used for the replacement. A <code>null</code> value 
   *                       within a key-value pair is treated as an empty string. Not <code>null</code>.
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> replace( Map<String, String> replacements );

  /**
   * Returns an operation that allows to replace the supplied keys by their corresponding values. The execution
   * order is undefined.
   * 
   * @param replacements    The key-value pairs to be used for the replacement. A <code>null</code> value 
   *                        within a key-value pair is treated as an empty string. Not <code>null</code>.
   * @param caseSensitive   <code>true</code> <=> The task shall be case sensitive.
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> replace( Map<String, String> replacements, boolean caseSensitive );

  /**
   * Returns an operation that allows to replace the supplied keys by their corresponding values. The order 
   * is given by the supplied list.
   * 
   * @param replacements   The key-value pairs to be used for the replacement. A <code>null</code> value 
   *                       within a key-value pair is treated as an empty string. Not <code>null</code>.
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> replace( List<Pair<String, String>> replacements );

  /**
   * Returns an operation that allows to replace the supplied keys by their corresponding values. The order 
   * is given by the supplied list. This operation is case sensitive.
   * 
   * @param replacements    The key-value pairs to be used for the replacement. A <code>null</code> value 
   *                        within a key-value pair is treated as an empty string. Not <code>null</code>.
   * @param caseSensitive   <code>true</code> <=> The task shall be case sensitive.
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> replace( List<Pair<String, String>> replacements, boolean caseSensitive );

  /**
   * Returns an operation that trims the input at both ends (standard whitespace characters).
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> trim();
  
  /**
   * Returns an operation that trims the input at both ends.
   * 
   * @param wschars   The characters used for the trimming. If <code>null</code> the standard whitepsace 
   *                  characters are being used.
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> trim( String wschars );

  /**
   * Returns an operation that trims the input at the left side (standard whitespace characters).
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> trimLeft();
  
  /**
   * Returns an operation that trims the input at the left side.
   * 
   * @param wschars   The characters used for the trimming. If <code>null</code> the standard whitepsace 
   *                  characters are being used.
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> trimLeft( String wschars );

  /**
   * Returns an operation that trims the input at the right side (standard whitespace characters).
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> trimRight();
  
  /**
   * Returns an operation that trims the input at the right side.
   * 
   * @param wschars   The characters used for the trimming. If <code>null</code> the standard whitepsace 
   *                  characters are being used.
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> trimRight( String wschars );

  /**
   * Returns an operation that makes a lower case version of the input.
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> toLowerCase();
  
  /**
   * Returns an operation that makes a upper case version of the input.
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> toUpperCase();
  
  /**
   * Returns an operation that replaces each regex occurrence with a value.
   * 
   * @param pattern       The regex pattern. Not <code>null</code>.
   * @param replacement   The replacement value. Maybe <code>null</code>.
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> replaceAll( Pattern pattern, String replacement );
  
  /**
   * Returns an operation that replaces each regex occurrence with a value.
   * 
   * @param pattern       The regex pattern. Not <code>null</code>.
   * @param replacement   A function providing the replacement value. Maybe <code>null</code>.
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> replaceAll( Pattern pattern, Function<String, String> replacement );

  /**
   * Returns an operation that replaces the first regex occurrence with a value.
   * 
   * @param pattern       The regex pattern. Not <code>null</code>.
   * @param replacement   The replacement value. Maybe <code>null</code>.
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> replaceFirst( Pattern pattern, String replacement );
  
  /**
   * Returns an operation that replaces the first regex occurrence with a value.
   * 
   * @param pattern       The regex pattern. Not <code>null</code>.
   * @param replacement   A function providing the replacement value. Maybe <code>null</code>.
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> replaceFirst( Pattern pattern, Function<String, String> replacement );

  /**
   * Returns an operation that replaces the last regex occurrence with a value.
   * 
   * @param pattern       The regex pattern. Not <code>null</code>.
   * @param replacement   The replacement value. Maybe <code>null</code>.
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> replaceLast( Pattern pattern, String replacement );
  
  /**
   * Returns an operation that replaces the last regex occurrence with a value.
   * 
   * @param pattern       The regex pattern. Not <code>null</code>.
   * @param replacement   A function providing the replacement value. Maybe <code>null</code>.
   * 
   * @return   The operation. Not <code>null</code>.
   */
  Function<T, T> replaceLast( Pattern pattern, Function<String, String> replacement );

} /* ENDINTERFACE */
