/**
 * Name........: DateAdapter
 * Description.: An adapter used to convert a Date into a String and vice versa.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.util.*;

import java.text.*;

import java.util.*;

/**
 * An adapter used to convert a Date into a String and vice versa.
 */
public class DateAdapter extends TypeAdapter<String,Date> {

  private static final String FORMAT_DEFAULT = "yyyy-MM-dd";
  
  private SimpleDateFormat   formatter;
  
  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   */
  public DateAdapter() {
    this( null, null, null, null, null );
  }

  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public DateAdapter( SimpleErrorHandler handler, String defval1, Date defval2 ) {
    this( handler, defval1, defval2, null, null );
  }
  
  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param format   The format which has to be used. If not <code>null</code> it's not allowed to be empty. 
   *                 If <code>null</code> the default format {@link #FORMAT_DEFAULT} is used.
   * @param locale   The Locale instance to be used for the formatting/parsing. If <code>null</code> 
   *                 {@link Locale#ENGLISH} is being used.
   */
  public DateAdapter( String format, Locale locale ) {
    this( null, null, null, format, locale );
  }
  
  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   * @param format    The format which has to be used. If not <code>null</code> it's not allowed to be empty. 
   *                  If <code>null</code> the default format {@link #FORMAT_DEFAULT} is used.
   * @param locale    The Locale instance to be used for the formatting/parsing. If <code>null</code> 
   *                  {@link Locale#ENGLISH} is being used.
   */
  public DateAdapter( SimpleErrorHandler handler, String defval1, Date defval2, String format, Locale locale ) {
    super( handler, defval1, defval2 );
    if( locale == null ) {
      locale = Locale.ENGLISH;
    }
    if( format == null ) {
      format  = FORMAT_DEFAULT;
    }
    formatter = new SimpleDateFormat( format );
  }
  
  @Override
  public String marshalImpl( Date v ) {
    return formatter.format(v);
  }

  @Override
  public Date unmarshalImpl( String v ) throws Exception {
    return formatter.parse( v );
  }

} /* ENDCLASS */
