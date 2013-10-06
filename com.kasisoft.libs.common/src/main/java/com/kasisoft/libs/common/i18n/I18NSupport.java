/**
 * Name........: I18NSupport
 * Description.: A helper which initializes the translations managed by a class.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.i18n;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.util.*;

import java.util.*;

import java.net.*;

import java.io.*;

import java.lang.reflect.*;

/**
 * A helper which initializes the translations managed by a class. Each translation class is supposed to provide fields
 * with the modifiers <code>public static String</code> and a corresponding <code>I18N</code> annotation.
 * The initialization of such a class could be performed like this:
 * 
 * <pre>
 * public class MyTexts {
 * 
 *    @I18N("You made a fault.")
 *    public static String   message;
 * 
 *    static {
 *      I18NSupport.initialize( MyTexts.class );
 *    }
 * 
 * }
 * </pre>
 * 
 */
public class I18NSupport {

  private static final int MODIFIERS = Modifier.STATIC | Modifier.PUBLIC;
  
  /**
   * Returns <code>true</code> if the supplied field can be considered to be a translation field.
   * 
   * @param field   The field that is supposed to be tested. Not <code>null</code>.
   * 
   * @return   <code>true</code> <=> The supplied field is a translation field.
   */
  private static final boolean isTranslationField( Field field ) {
    int modifier = field.getModifiers();
    if( modifier != MODIFIERS ) {
      return false;
    }
    return (field.getType() == String.class) || (field.getType() == I18NFormatter.class);
  }
  
  /**
   * Creates a map with all translatable fields.
   * 
   * @param clazz   The class which is supposed to be translated. Not <code>null</code>.
   * 
   * @return   A mapping between field names and the corresponding {@link Field} instances. Not <code>null</code>.
   */
  private static final Map<String,Field> collectFields( Class<?> clazz ) {
    Map<String,Field> result = new Hashtable<String,Field>();
    Field[]           fields = clazz.getDeclaredFields();
    for( Field field : fields ) {
      if( isTranslationField( field ) ) {
        result.put( field.getName(), field );
      }
    }
    return result;
  }
  
  /**
   * Loads all properties matching the current locale (we support different formats).
   * 
   * @param candidates   A list of resource pathes pointing to possible translations. Not <code>null</code>.
   * 
   * @return   The {@link Properties} instance providing all current translations. Not <code>null</code>.
   */
  private static final Properties loadTranslations( String[] candidates ) {
    Properties  result      = new Properties();
    ClassLoader classloader = Thread.currentThread().getContextClassLoader();
    for( String variant : candidates ) {
      URL url = classloader.getResource( variant );
      if( url == null ) {
        continue;
      }
      Reader reader  = null;
      try {
        reader = Encoding.UTF8.openReader( url.openStream() );
        result.load( reader );
      } catch( IOException ex ) {
        throw new FailureException( FailureCode.IO, ex );
      } finally {
        MiscFunctions.close( reader );
      }
    }
    return result;
  }
  
  /**
   * Applies all translations to the supplied class while changing the field values.
   * 
   * @param prefix         A prefix which has to be used for the property names. Not <code>null</code>.
   * @param translations   The {@link Properties} instance which contains all translations. 
   *                       Not <code>null</code>.
   * @param fields         The map with all {@link Field} instances that have to be changed. Not <code>null</code>.
   */
  private static final void applyTranslations( String prefix, Properties translations, Map<String, Field> fields ) {
    for( Map.Entry<String, Field> entry : fields.entrySet() ) {
      String key      = entry.getKey();
      String property = String.format( "%s%s", prefix, key );
      Field  field    = entry.getValue();
      String value    = null;
      if( translations.containsKey( property ) ) {
        value = translations.getProperty( property );
      } else {
        I18N i18ndefault = field.getAnnotation( I18N.class );
        if( i18ndefault != null ) {
          value = i18ndefault.value();
        }
      }
      if( value != null ) {
        try {
          if( field.getType() == String.class ) {
            field.set( null, value );
          } else {
            field.set( null, new I18NFormatter( value ) );
          }
        } catch( Exception ex ) {
          throw new FailureException( FailureCode.Reflections, ex );
        }
      }
    }
  }

  /**
   * Applies all translations to the supplied class using the default locale.
   * 
   * @param clazz   The class that is supposed to be translated. Not <code>null</code>.
   */
  public static final void initialize( Class<?> clazz ) {
    initialize( null, clazz );
  }
  
  /**
   * Applies all translations to the supplied class.
   * 
   * @param locale   The {@link Locale} instance which has to be used. If <code>null</code> {@link Locale#getDefault()}
   *                 will be used.
   * @param clazz    The class that is supposed to be translated. Not <code>null</code>.
   */
  public static final void initialize( Locale locale, Class<?> clazz ) {
    
    if( locale == null ) {
      locale = Locale.getDefault();
    }
    
    I18NBasename basename = clazz.getAnnotation( I18NBasename.class );
    String       base     = null;
    String       prefix   = null;
    if( basename != null ) {
      base    = basename.resource();
      prefix  = basename.prefix();
    } else {
      base    = clazz.getName().toLowerCase().replace('.','/');
      prefix  = "";
    }
    String[] candidates = new String[] {
      String.format( "%s_%s_%s.properties"  , base, locale.getLanguage(), locale.getCountry() ),  // f.e. de_DE
      String.format( "%s_%s.properties"     , base, locale.getLanguage() ),                       // f.e. de
      String.format( "%s.properties"        , base ),
    };

    applyTranslations( prefix, loadTranslations( candidates ), collectFields( clazz ) );
    
  }

} /* ENDCLASS */
