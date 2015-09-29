package com.kasisoft.libs.common.function;

import java.util.function.*;

import java.util.regex.*;

/**
 * Collection of predicates.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Predicates {
  
  public static final Predicate         IS_NOT_NULL = $ -> $ != null;
  
  public static final Predicate         IS_NULL = IS_NOT_NULL.negate();
  
  public static final Predicate<String> IS_JAVA_CLASS_FILE = new IsJavaClassFile();
  
  public static final Predicate<String> IS_RESOURCE = IS_JAVA_CLASS_FILE.negate();

  public static final Predicate<String> IS_RESOURCE_FILE = IS_RESOURCE.and( $ -> !$.endsWith("/"));

  public static final Predicate<String> IS_RESOURCE_DIR = IS_RESOURCE.and( $ -> $.endsWith("/"));

  public static final Predicate<String> IS_INNER_JAVA_CLASS_FILE = new IsInnerJavaClassFile();
  
  public static final Predicate<String> IS_ENCLOSING_JAVA_CLASS_FILE = IS_JAVA_CLASS_FILE.and( IS_INNER_JAVA_CLASS_FILE.negate() );
  
  public static final Predicate<String> IS_SPI_FILE = new IsSPIFile();
  
  public static final Predicate<String> IS_MAGNOLIA_FILE = new IsMagnoliaFile();
  
  public static final Predicate<String> IS_MAVEN_FILE = new IsMavenFile();
  
  public static final Predicate<String> IS_JAVA_FQDN = new IsJavaFqdn();
  
  public static <T> Predicate<T> acceptAll() {
    return $ -> true;
  }

  public static <T> Predicate<T> acceptNone() {
    return $ -> false;
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
  
  private static class IsJavaClassFile implements Predicate<String> {

    Pattern pattern = Pattern.compile( "^(.+)(.class)$" );
    
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
