package com.kasisoft.libs.common.sys;

import com.kasisoft.libs.common.data.*;
import com.kasisoft.libs.common.function.*;

import java.util.*;

/**
 * Functions allowing to create classpath related partitioners.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ClasspathPartitioners {

  public static <R extends Collection<String>> Partitioner<String,R> newToEnclosingClass( R data ) {
    return new DefaultPartitioner<String,String,R>( Predicates.IS_ENCLOSING_JAVA_CLASS_FILE, ClasspathPartitioners::file2Class, data );
  }

  public static <R extends Collection<String>> Partitioner<String,R> newToInnerClass( R data ) {
    return new DefaultPartitioner<String,String,R>( Predicates.IS_INNER_JAVA_CLASS_FILE, ClasspathPartitioners::file2Class, data );
  }

  public static <R extends Collection<String>> Partitioner<String,R> newToClass( R data ) {
    return new DefaultPartitioner<String,String,R>( Predicates.IS_JAVA_CLASS_FILE, ClasspathPartitioners::file2Class, data );
  }

  public static <R extends Collection<String>> Partitioner<String,R> newToResource( R data ) {
    return new DefaultPartitioner<String,String,R>( Predicates.IS_RESOURCE, data );
  }

  public static <R extends Collection<String>> Partitioner<String,R> newToResourceFile( R data ) {
    return new DefaultPartitioner<String,String,R>( Predicates.IS_RESOURCE_FILE, data );
  }

  public static <R extends Collection<String>> Partitioner<String,R> newToResourceDir( R data ) {
    return new DefaultPartitioner<String,String,R>( Predicates.IS_RESOURCE_DIR, data );
  }

  private static String file2Class( String file ) {
    return file.replace('/', '.').substring( 0, file.length() - ".class".length() );
  }
  
} /* ENDCLASS */
