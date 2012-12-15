/**
 * Name........: DateAdapter
 * Description.: An adapter used to convert a Date into a String and vice versa.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import java.text.*;

import java.util.*;

/**
 * An adapter used to convert a Date into a String and vice versa. Also note that this adapter is not only used for jaxb.
 */
public class DateAdapter extends NullSafeAdapter<String,Date> {

  private static final String FORMAT_DEFAULT = "yyyy-MM-dd";
  
  private SimpleDateFormat   formatter;
  
  /**
   * Creates this adapter using the default format which is {@link #FORMAT_DEFAULT}. The default locale is {@link Locale#ENGLISH}.
   */
  public DateAdapter() {
    this( null, null );
  }

  /**
   * Creates this adapter with a specified format.
   * 
   * @param format   The format which has to be used. If not <code>null</code> it's not allowed to be empty. 
   *                 If <code>null</code> the default format {@link #FORMAT_DEFAULT} is used.
   * @param locale   The Locale instance to be used for the formatting/parsing. If <code>null</code> 
   *                 {@link Locale#ENGLISH} is being used.
   */
  public DateAdapter( String format, Locale locale ) {
    if( locale == null ) {
      locale = Locale.ENGLISH;
    }
    if( format == null ) {
      format  = FORMAT_DEFAULT;
    }
    formatter = new SimpleDateFormat( format );
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String marshalImpl( Date v ) {
    return formatter.format(v);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date unmarshalImpl( String v ) throws Exception {
    return formatter.parse( v );
  }

} /* ENDCLASS */
