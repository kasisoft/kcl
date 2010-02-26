/**
 * Name........: EvaluatingProperties
 * Description.: Extension to the 'ExtProperties' type which provides additional evaluation
 *               functionalities.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util.properties;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.libs.common.util.*;

import java.util.regex.*;
import java.util.*;

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
    
    EvalType( String fmt, String indicator, String pattern, String head, String tail, String sep ) {
      formatter = fmt;
      trigger   = indicator;
      selector  = Pattern.compile( pattern );
      varhead   = head;
      vartail   = tail;
    }
    
    public boolean isVariableValue( String val ) {
      return (val != null) && (val.indexOf( trigger ) != -1);
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
    setPropertyValueFactory( new EvalPropertyValueFactory( evaluationtype ) );
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
  
  private Map<String,String> propertyvalues = null;
  
  String evaluate( String key, String value ) {
    value   = StringFunctions.replace( value, values );
    int idx = value.indexOf( evaltype.trigger );
    if( idx != -1 ) {
      boolean delete = propertyvalues == null;
      if( delete ) {
        propertyvalues = new HashMap<String,String>();
      }
      propertyvalues.put( String.format( evaltype.formatter, "", key ), String.format( evaltype.formatter, "", key ) );
      value = evaluate( value );
      propertyvalues.put( String.format( evaltype.formatter, "", key ), value );
      if( delete ) {
        propertyvalues = null;
      }
    }
    return value;
  }
  
  private String evaluate( String value ) {
    StringBuffer  buffer    = new StringBuffer();
    Matcher       matcher   = evaltype.selector.matcher( value );
    int           laststart = 0;
    while( matcher.find() ) {
      int start = matcher.start();
      int end   = matcher.end();
      if( start > laststart ) {
        buffer.append( value.substring( laststart, start ) );
      }
      String key = value.substring( start, end );
      if( propertyvalues.containsKey( key ) ) {
        buffer.append( propertyvalues.get( key ) );
      } else {
        key        = key.substring( evaltype.varhead.length(), key.length() - evaltype.vartail.length() );
        buffer.append( getProperty( key ) );
      }
      laststart  = end;
    }
    if( laststart < value.length() ) {
      buffer.append( value.substring( laststart ) );
    }
    return buffer.toString();
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
//    props.store( System.out, Encoding.getDefault() );
    System.err.println( "=> 'simple.3' - '" + props.getProperty( "simple.3" ) + "'" );
    System.err.println( "=> 'simple.4' - '" + props.getProperty( "simple.4" ) + "'" );
    System.err.println( "=> 'simple.5' - '" + props.getProperty( "simple.5" ) + "'" );
    System.err.println( "=> 'simple.6' - '" + props.getProperty( "simple.6" ) + "'" );
    System.err.println( "=> 'simple.7' - '" + props.getProperty( "simple.7" ) + "'" );
    System.err.println( "=> 'simple.8' - '" + props.getProperty( "simple.8" ) + "'" );
  }
  
} /* ENDCLASS */
