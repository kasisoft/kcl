package com.kasisoft.libs.common.old.sys;

import com.kasisoft.libs.common.old.data.*;
import com.kasisoft.libs.common.old.function.*;

import java.util.*;

import java.nio.file.*;

/**
 * Functions allowing to create classpath related partitioners.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ClasspathPartitioners {

  public static <R extends Collection<String>> Partitioner<String, Path, R> newSPI( R model ) {
    return new DefaultPartitioner<>( Predicates.IS_SPI_FILE, ClasspathPartitioners::service2Class, model );
  }

  public static <R extends Collection<String>> Partitioner<String, Path, R> newMagnolia( R model ) {
    return new DefaultPartitioner<>( Predicates.IS_MAGNOLIA_FILE, model );
  }

  public static <R extends Collection<String>> Partitioner<String, Path, R> newMaven( R model ) {
    return new DefaultPartitioner<>( Predicates.IS_MAVEN_FILE, model );
  }

  public static <R extends Collection<String>> Partitioner<String, Path, R> newToEnclosingClass( R model ) {
    return new DefaultPartitioner<>( Predicates.IS_ENCLOSING_JAVA_CLASS_FILE, ClasspathPartitioners::file2Class, model );
  }

  public static <R extends Collection<String>> Partitioner<String, Path, R> newToInnerClass( R model ) {
    return new DefaultPartitioner<>( Predicates.IS_INNER_JAVA_CLASS_FILE, ClasspathPartitioners::file2Class, model );
  }

  public static <R extends Collection<String>> Partitioner<String, Path, R> newToClass( R model ) {
    return new DefaultPartitioner<>( Predicates.IS_JAVA_CLASS_FILE, ClasspathPartitioners::file2Class, model );
  }

  public static <R extends Collection<String>> Partitioner<String, Path, R> newToResource( R model ) {
    return new DefaultPartitioner<>( Predicates.IS_RESOURCE, model );
  }

  public static <R extends Collection<String>> Partitioner<String, Path, R> newToResourceFile( R model ) {
    return new DefaultPartitioner<>( Predicates.IS_RESOURCE_FILE, model );
  }

  public static <R extends Collection<String>> Partitioner<String, Path, R> newToResourceDir( R model ) {
    return new DefaultPartitioner<>( Predicates.IS_RESOURCE_DIR, model );
  }

  private static String file2Class( String file ) {
    return file.replace('/', '.').substring( 0, file.length() - ".class".length() );
  }

  private static String service2Class( String file ) {
    return file.substring( "META-INF/services/".length() );
  }

} /* ENDCLASS */
