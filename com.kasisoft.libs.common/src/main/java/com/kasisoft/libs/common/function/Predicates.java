package com.kasisoft.libs.common.function;

import java.util.function.*;

import java.util.regex.*;

/**
 * Collection of predicates.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Predicates {
  
  public static final Predicate<?> ACCEPT_ALL = $ -> true;
  
  public static final Predicate<?> ACCEPT_NONE = $ -> false;

  public static final Predicate<String> IS_JAVA_CLASS_FILE = new IsJavaClassFile();
  
  public static final Predicate<String> IS_RESOURCE = IS_JAVA_CLASS_FILE.negate();

  public static final Predicate<String> IS_RESOURCE_FILE = IS_RESOURCE.and( $ -> !$.endsWith("/"));

  public static final Predicate<String> IS_RESOURCE_DIR = IS_RESOURCE.and( $ -> $.endsWith("/"));

  public static final Predicate<String> IS_INNER_JAVA_CLASS_FILE = new IsInnerJavaClassFile();
  
  public static final Predicate<String> IS_ENCLOSING_JAVA_CLASS_FILE = IS_JAVA_CLASS_FILE.and( IS_INNER_JAVA_CLASS_FILE.negate() );
  
  public static <T> Predicate<T> acceptAll() {
    return $ -> true;
  }

  public static <T> Predicate<T> acceptNone() {
    return $ -> false;
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
  
} /* ENDCLASS */
