package com.kasisoft.libs.common.sys;

import com.kasisoft.libs.common.constants.*;

import lombok.experimental.*;

import lombok.*;

import java.util.*;

import java.io.*;

import java.nio.file.*;

/**
 * A simple helper allowing to index the content of the classpath.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClasspathIndexer {

  public <R extends ClasspathIndexer> R withSystemClasspath() {
    List<File> files = SysProperty.ClassPath.getValue( System.getProperties() );
    files.forEach( f -> processClasspathEntry( Paths.get( f.toURI() ) ) );
    return (R) this;
  }
  
  protected void processClasspathEntry( Path path ) {
    if( isValid( path ) ) {
      if( Files.isDirectory( path ) ) {
        processClasspathDirectory( path );
      } else {
        processClasspathArchive( path );
      }
    }
  }
  
  protected void processClasspathDirectory( Path path ) {
  }

  protected void processClasspathArchive( Path path ) {
  }
  
  protected boolean isValid( Path path ) {
    return true;
  }

  public static void main( String[] args ) throws Exception {
    ClasspathIndexer indexer = new ClasspathIndexer();
    indexer.withSystemClasspath();
  }
  
}
