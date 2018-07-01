package com.kasisoft.libs.common.function;

import com.kasisoft.libs.common.util.*;

import java.util.function.*;

import java.util.regex.*;

import java.nio.file.*;

/**
 * Collection of predicates.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Predicates {
  
  public static final Predicate         IS_NOT_NULL = $ -> $ != null;
  
  public static final Predicate         IS_NULL = IS_NOT_NULL.negate();
  
  public static final Predicate<String> IS_JAVA_CLASS_FILE = new SuffixPredicate( ".class" );

  /** @deprecated   Use IS_YAML_FILE instrad. Will be removed with version 3.1 */
  @Deprecated
  public static final Predicate<String> IS_JAVA_YAML_FILE = new SuffixPredicate( ".yaml", ".yml" );
  
  public static final Predicate<String> IS_YAML_FILE = new SuffixPredicate( ".yaml", ".yml" );

  public static final Predicate<String> IS_JSON_FILE = new SuffixPredicate( ".json" );

  public static final Predicate<String> IS_RESOURCE = IS_JAVA_CLASS_FILE.negate();

  public static final Predicate<String> IS_RESOURCE_FILE = IS_RESOURCE.and( $ -> !$.endsWith("/") );

  public static final Predicate<String> IS_RESOURCE_DIR = IS_RESOURCE.and( $ -> $.endsWith("/") );

  public static final Predicate<String> IS_INNER_JAVA_CLASS_FILE = new IsInnerJavaClassFile();
  
  public static final Predicate<String> IS_ENCLOSING_JAVA_CLASS_FILE = IS_JAVA_CLASS_FILE.and( IS_INNER_JAVA_CLASS_FILE.negate() );
  
  public static final Predicate<String> IS_SPI_FILE = new IsSPIFile();
  
  public static final Predicate<String> IS_MAGNOLIA_FILE = new IsMagnoliaFile();
  
  public static final Predicate<String> IS_MAVEN_FILE = new IsMavenFile();
  
  public static final Predicate<String> IS_JAVA_FQDN = new IsJavaFqdn();
  
  public static final Predicate<String> IS_BOOLEAN = MiscFunctions::isBoolean;
  
  public static final Predicate<String> IS_LONG = $ -> isValid( $, Long::parseLong );
  
  public static final Predicate<String> IS_INTEGER = $ -> isValid( $, Integer::parseInt );

  public static final Predicate<String> IS_SHORT = $ -> isValid( $, Short::parseShort );

  public static final Predicate<String> IS_BYTE = $ -> isValid( $, Byte::parseByte );

  public static final Predicate<String> IS_FLOAT = $ -> isValid( $, Float::parseFloat );

  public static final Predicate<String> IS_DOUBLE = $ -> isValid( $, Double::parseDouble );

  private static boolean isValid( String value, Function<String, ?> parse ) {
    if( value != null ) {
      try {
        parse.apply( value );
        return true;
      } catch( NumberFormatException ex ) {
        // valid case
      }
    }
    return false;
  }

  public static <T> Predicate<T> acceptAll() {
    return $ -> true;
  }

  public static <T> Predicate<T> acceptNone() {
    return $ -> false;
  }
  
  public static Predicate<Path> path( Predicate<String> predicate ) {
    return $ -> predicate.test( $.toString() );
  }

  private static class IsMavenFile implements Predicate<String> {
    
    @Override
    public boolean test( String resource ) {
      return resource.endsWith( "/pom.xml" ) || resource.endsWith( "/pom.properties" ) ||
             resource.equals( "pom.xml" ) || resource.equals( "pom.properties" ) ;
    }
      
  }

  private static class IsMagnoliaFile implements Predicate<String> {
    
    private static final String PREFIX = "META-INF/magnolia/";
    
    @Override
    public boolean test( String resource ) {
      boolean equals = PREFIX.equals( resource );
      return resource.startsWith( PREFIX ) && (!equals);
    }
      
  }

  private static class IsSPIFile implements Predicate<String> {
      
    private static final String PREFIX = "META-INF/services/";
    
    @Override
    public boolean test( String resource ) {
      boolean equals = PREFIX.equals( resource );
      if( resource.startsWith( PREFIX ) && (!equals) && (resource.indexOf( '$' ) == -1) ) {
        return IS_JAVA_FQDN.test( resource.substring( PREFIX.length() ) );
      }
      return false;
    }
      
  }

  private static class SuffixPredicate implements Predicate<String> {
    
    Pattern   pattern;
    
    public SuffixPredicate( String ... suffices ) {
      StringBuilder builder = new StringBuilder( "^(.+)(" );
      builder.append( suffices[0] );
      for( int i = 1; i < suffices.length; i++ ) {
        builder.append( "|" );
        builder.append( suffices[i] );
      }
      builder.append( ")$" );
      pattern = Pattern.compile( builder.toString() );
    }
    
    @Override
    public boolean test( String resource ) {
      return pattern.matcher( resource ).matches();
    }
    
  } /* ENDCLASS */

  private static class IsInnerJavaClassFile implements Predicate<String> {

    Pattern pattern = Pattern.compile( "^([^$]+)\\$(.*)(.class)$" );
    
    @Override
    public boolean test( String resource ) {
      return pattern.matcher( resource ).matches();
    }
    
  } /* ENDCLASS */
  
  private static class IsJavaFqdn implements Predicate<String> {
     
    private static final Pattern PATTERN = Pattern.compile(
      "^(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*\\.)*(\\p{javaJavaIdentifierStart}\\p{javaJavaIdentifierPart}*)$"
    );

    @Override
    public boolean test( String classname ) {
      if( !classname.endsWith( ".class" ) ) {
        // to be more accurate: split segments and make sure that the segments aren't keywords
        return PATTERN.matcher( classname ).matches();
      }
      return false;
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
