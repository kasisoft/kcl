package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.util.*;

import lombok.experimental.*;

import lombok.*;

import java.util.regex.*;

import java.util.*;

/**
 * Adapter used to convert a String into a data structure which consists of a delimited list.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListAdapter<T> extends TypeAdapter<String,List<T>> {

  String                  delimiter;
  String                  quoted;
  TypeAdapter<String,T>   elementadapter;
  
  /**
   * Initialises this adapter with the default delimiter ','.
   * 
   * @param adapter   The adapter that will be used for the element types. Not <code>null</code>.
   */
  public ListAdapter( @NonNull TypeAdapter<String,T> adapter ) {
    this( null, null, adapter );
  }

  /**
   * Initialises this adapter with the supplied delimiter.
   * 
   * @param delim     The delimiter to be used for the textual representation. If <code>null</code> or empty the default 
   *                  ',' is used.
   * @param adapter   The adapter that will be used for the element types. Not <code>null</code>.
   */
  public ListAdapter( String delim, @NonNull TypeAdapter<String,T> adapter ) {
    this( null, delim, adapter );
  }

  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param adapter   The adapter that will be used for the element types. Not <code>null</code>.
   */
  public ListAdapter( SimpleErrorHandler handler, @NonNull TypeAdapter<String,T> adapter ) {
    this( handler, null, adapter );
  }

  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param delim     The delimiter to be used for the textual representation. If <code>null</code> or empty the default 
   *                  ',' is used.
   * @param adapter   The adapter that will be used for the element types. Not <code>null</code>.
   */
  public ListAdapter( SimpleErrorHandler handler, String delim, @NonNull TypeAdapter<String,T> adapter ) {
    super( handler, "", Collections.<T>emptyList() );
    delimiter = StringFunctions.cleanup( delim );
    if( delimiter == null ) {
      delimiter = ",";
    }
    quoted          = Pattern.quote( delimiter );
    elementadapter  = adapter;
  }

  @Override
  protected List<T> unmarshalImpl( @NonNull String v ) throws Exception {
    String[] parts = v.split( quoted );
    if( parts == null ) {
      return Collections.<T>emptyList();
    }
    return unmarshalListImpl( parts );
  }

  protected List<T> unmarshalListImpl( @NonNull String[] v ) throws Exception {
    List<T> result = new ArrayList<>();
    for( int i = 0; i < v.length; i++ ) {
      result.add( elementadapter.unmarshal( v[i] ) );
    }
    return result;
  }

  @Override
  protected String marshalImpl( List<T> elements ) throws Exception {
    if( elements.size() > 0 ) {
      StringBuilder buffer = new StringBuilder();
      for( int i = 0; i < elements.size(); i++ ) {
        buffer.append( delimiter );
        buffer.append( elementadapter.marshal( elements.get(i) ) );
      }
      buffer.delete( 0, delimiter.length() );
      return buffer.toString();
    } else {
      return "";
    }
  }

} /* ENDCLASS */
