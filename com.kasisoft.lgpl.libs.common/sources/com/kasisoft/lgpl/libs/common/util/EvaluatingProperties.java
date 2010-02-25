/**
 * Name........: EvaluatingProperties
 * Description.: Extension to the 'ExtProperties' type which provides additional evaluation
 *               functionalities.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util;

import com.kasisoft.lgpl.libs.common.constants.*;

import java.util.*;
import java.util.regex.*;

import java.io.*;

/**
 * Extension to the 'ExtProperties' type which provides additional evaluation functionalities.
 */
public class EvaluatingProperties extends ExtProperties {
  
  public static enum EvalType {
    
    Standard  ( "${%s%s}"   , "${" , "\\$\\{[\\w\\.]+\\}" , "${" , "}", "\\$\\{\\}" ),
    Windows   ( "%%%s%s%%"  , "%%" , "%[\\w\\.]+%"        , "%"  , "%", "%" );
    
    private String    formatter;
    private String    trigger;
    private Pattern   selector;
    private String    varhead;
    private String    vartail;
    private String    split;
    
    EvalType( String fmt, String indicator, String pattern, String head, String tail, String sep ) {
      formatter = fmt;
      trigger   = indicator;
      selector  = Pattern.compile( pattern );
      varhead   = head;
      vartail   = tail;
      split     = sep;
    }
    
  } /* ENDENUM */

  private Map<String,String>   values;
  private EvalType             evaltype;
  
  /**
   * Initialises this Properties implementation.
   */
  public EvaluatingProperties( EvalType evaltype ) {
    super();
    setupValues( evaltype );
  }
  
  /**
   * Initialises this Properties implementation.
   * 
   * @param del       The delimiter to be used for the key value pairs. Maybe <code>null</code>.
   * @param comment   The literal which introduces a comment. Maybe <code>null</code>.
   * @param array     The way indexed/associated keys are represented. Maybe <code>null</code>.
   */
  public EvaluatingProperties( String del, String comment, ArrayStyle array,  EvalType evaltype ) {
    super( del, comment, array );
    setupValues( evaltype );
  }
  
  private void setupValues( EvalType evaluationtype ) {
    evaltype                        = evaluationtype;
    values                          = new Hashtable<String,String>();
    Map<String,String> environment  = System.getenv();
    for( Map.Entry<String,String> env : environment.entrySet() ) {
      System.out.printf( "'%s' - '%s'\n", String.format( evaluationtype.formatter, "env:", env.getKey() ), env.getValue() );
      values.put( String.format( evaluationtype.formatter, "env:", env.getKey() ), env.getValue() );
    }
    Properties          sysprops    = System.getProperties();
    Enumeration<String> names       = (Enumeration<String>) sysprops.propertyNames();
    while( names.hasMoreElements() ) {
      String name   = names.nextElement();
      String value  = sysprops.getProperty( name );
      values.put( String.format( evaluationtype.formatter, "", name ), value );
    }
  }
  
  private Map<String,String>   propertyvalues = new Hashtable<String,String>();

  /**
   * 
   * @param value
   * @return
   */
  private String evaluate( String key, String value ) {
    propertyvalues.put( String.format( evaltype.formatter, "", key ), String.format( evaltype.formatter, "", key ) ); 
    if( value != null ) {
      int idx = value.indexOf( evaltype.trigger );
      if( idx != -1 ) {
        value = StringFunctions.replace( value, values );
        idx   = value.indexOf( evaltype.trigger );
        if( idx != -1 ) {
          value = replace( value );
        }
      }
    }
    propertyvalues.put( String.format( evaltype.formatter, "", key ), value ); 
    return value;
  }
  
  private String replace( String current ) {
    StringBuffer  buffer    = new StringBuffer();
    Matcher       matcher   = evaltype.selector.matcher( current );
    int           laststart = 0;
    while( matcher.find() ) {
      int start = matcher.start();
      int end   = matcher.end();
      if( start > laststart ) {
        buffer.append( current.substring( laststart, start ) );
      }
      String key = current.substring( start, end );
      if( propertyvalues.containsKey( key ) ) {
        buffer.append( propertyvalues.get( key ) );
      } else {
        key        = key.substring( evaltype.varhead.length(), key.length() - evaltype.vartail.length() );
        buffer.append( getProperty( key ) );
      }
      laststart  = end;
    }
    if( laststart < current.length() ) {
      buffer.append( current.substring( laststart ) );
    }
    return buffer.toString();
  }
  
  /**
   * {@inheritDoc}
   */
  public synchronized Map<String, String> getAssociatedProperty( String key, Map<String, String> defvalues ) {
    Map<String,String> result = super.getAssociatedProperty( key, defvalues );
    if( result != null ) {
      for( String association : result.keySet() ) {
        result.put( association, evaluate( getAssociatedPropertyKey( key, association ), result.get( association ) ) );
      }
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public synchronized String getAssociatedProperty( String key, String association, String defvalue ) {
    String result = super.getAssociatedProperty( key, association, defvalue ); 
    return evaluate( getAssociatedPropertyKey( key, association ), result );
  }

  /**
   * {@inheritDoc}
   */
  public synchronized String getIndexedProperty( String key, int index, String defvalue ) {
    String result = super.getIndexedProperty( key, index, defvalue );
    return evaluate( getIndexedPropertyKey( key, index ), result );
  }

  /**
   * {@inheritDoc}
   */
  public synchronized Map<Integer,String> getIndexedProperty( String key, Map<Integer,String> defvalues ) {
    Map<Integer,String> map = super.getIndexedPropertyMap( key, defvalues );
    if( map != null ) {
      Integer[] keys = map.keySet().toArray( new Integer[ map.size() ] );
      for( Integer index : keys ) {
        String indexedkey = getIndexedPropertyKey( key, index.intValue() );
        map.put( index, evaluate( indexedkey, map.get( key ) ) );
      }
    }
    return map;
  }

  /**
   * {@inheritDoc}
   */
  public synchronized List<String> getIndexedProperty( String key, List<String> defvalues ) {
    Map<Integer,String> map = super.getIndexedPropertyMap( key, (Map<Integer,String>) null );
    if( map != null ) {
      Integer[] keys = map.keySet().toArray( new Integer[ map.size() ] );
      for( Integer index : keys ) {
        String indexedkey = getIndexedPropertyKey( key, index.intValue() );
        map.put( index, evaluate( indexedkey, map.get( key ) ) );
      }
      Arrays.sort( keys );
      List<String> resultlist = new ArrayList<String>();
      for( Integer index : keys ) {
        resultlist.add( map.get( index ) );
      }
      return resultlist;
    } else {
      return defvalues;
    }
  }

  /**
   * {@inheritDoc}
   */
  public synchronized String getSimpleProperty( String key, String defvalue ) {
    String result = super.getSimpleProperty( key, defvalue );
    return evaluate( key, result );
  }

  public static final void main( String[] args ) {
    EvaluatingProperties props = new EvaluatingProperties( EvalType.Standard );
    props.load( new File( "simple.properties" ), Encoding.UTF8 );
    props.store( System.out, Encoding.getDefault() );
    System.err.println( "=> 'simple.3' - '" + props.getProperty( "simple.3" ) + "'" );
    System.err.println( "=> 'simple.4' - '" + props.getProperty( "simple.4" ) + "'" );
    System.err.println( "=> 'simple.5' - '" + props.getProperty( "simple.5" ) + "'" );
    System.err.println( "=> 'simple.6' - '" + props.getProperty( "simple.6" ) + "'" );
  }
  
} /* ENDCLASS */
