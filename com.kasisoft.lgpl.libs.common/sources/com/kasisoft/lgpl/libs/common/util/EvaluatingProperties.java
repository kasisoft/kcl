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

import java.util.*;
import java.util.regex.*;

/**
 * Extension to the 'ExtProperties' type which provides additional evaluation functionalities.
 */
public class EvaluatingProperties extends ExtProperties {
  
  public static enum EvalType {
    
    Standard  ( "${%s%s}"   , "${" , "\\$\\{[\\w\\.]+\\}" ),
    Windows   ( "%%%s%s%%"  , "%%" , "%[\\w\\.]+%"        );
    
    private String    formatter;
    private String    trigger;
    private Pattern   selector;
    
    EvalType( String fmt, String indicator, String pattern ) {
      formatter = fmt;
      trigger   = indicator;
      selector  = Pattern.compile( pattern );
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

  /**
   * 
   * @param value
   * @return
   */
  private String evaluate( String value ) {
    if( value != null ) {
      int idx = value.indexOf( evaltype.trigger );
      if( idx != -1 ) {
        value = StringFunctions.replace( value, values );
        idx   = value.indexOf( evaltype.trigger );
        if( idx != -1 ) {
          Map<String,String> current = new Hashtable<String,String>();
          value                      = replace( current, value );
        }
      }
    }
    return value;
  }
  
  private String replace( Map<String,String> map, String current ) {
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
      laststart = end;
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
        result.put( association, evaluate( result.get( association ) ) );
      }
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public synchronized String getAssociatedProperty( String key, String association, String defvalue ) {
    String result = super.getAssociatedProperty( key, association, defvalue ); 
    return evaluate( result );
  }

  /**
   * {@inheritDoc}
   */
  public synchronized String getAssociatedProperty( String key, String association ) {
    String result = super.getAssociatedProperty( key, association );
    return evaluate( result );
  }

  /**
   * {@inheritDoc}
   */
  public synchronized String getIndexedProperty( String key, int index, String defvalue ) {
    String result = super.getIndexedProperty( key, index, defvalue );
    return evaluate( result );
  }

  /**
   * {@inheritDoc}
   */
  public synchronized String getIndexedProperty( String key, int index ) {
    String result = super.getIndexedProperty( key, index );
    return evaluate( result );
  }

  /**
   * {@inheritDoc}
   */
  public synchronized List<String> getIndexedProperty( String key, List<String> defvalues ) {
    List<String> result = super.getIndexedProperty( key, defvalues );
    if( result != null ) {
      for( int i = 0; i < result.size(); i++ ) {
        result.set( i, evaluate( result.get(i) ) );
      }
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  public synchronized String getProperty( String key, String defvalue ) {
    String result = super.getProperty( key, defvalue );
    return evaluate( result );
  }

  /**
   * {@inheritDoc}
   */
  public synchronized String getProperty( String key ) {
    String result = super.getProperty( key );
    return evaluate( result );
  }

  /**
   * {@inheritDoc}
   */
  public synchronized String getSimpleProperty( String key, String defvalue ) {
    String result = super.getSimpleProperty( key, defvalue );
    return evaluate( result );
  }

  /**
   * {@inheritDoc}
   */
  public synchronized String getSimpleProperty( String key ) {
    String result = super.getSimpleProperty( key );
    return evaluate( result );
  }

} /* ENDCLASS */
