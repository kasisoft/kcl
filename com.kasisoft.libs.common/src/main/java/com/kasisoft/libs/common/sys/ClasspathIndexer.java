package com.kasisoft.libs.common.sys;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.io.datatypes.*;

import com.kasisoft.libs.common.io.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.zip.*;

import java.util.*;

import java.net.*;

import java.nio.file.*;

import java.io.*;

/**
 * A simple helper allowing to index the content of the classpath.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClasspathIndexer {

  Map<Path,Set<String>>   resources;
  List<Path>              pathesAsList;
  List<String>            asList;
  
  @Getter @Setter
  Predicate<Path>         pathTester;
  
  @Getter @Setter
  Predicate<String>       resourceTester;
  
  FileType                zipTest;
  
  public ClasspathIndexer() {
    zipTest         = new ZipFileType();
    resources       = new Hashtable<>();
    asList          = new ArrayList<>();
    pathesAsList    = new ArrayList<>();
    pathTester      = null;
    resourceTester  = null;
  }
  
  /**
   * Sets up the supplied {@link Predicate} to be used in order to accept/reject classpath elements.
   * 
   * @param predicate   The predicate used to test the classpath element. If <code>null</code> everything get's accepted.
   * 
   * @return this
   */
  public <R extends ClasspathIndexer> R withPathTester( Predicate<Path> predicate ) {
    setPathTester( predicate );
    return (R) this;
  }

  /**
   * Sets up the supplied {@link Predicate} to be used in order to accept/reject resources.
   * 
   * @param tester   The predicate used to test a single resource. If <code>null</code> everything get's accepted.
   * 
   * @return this
   */
  public <R extends ClasspathIndexer> R withResourceTester( Predicate<String> tester ) {
    setResourceTester( tester );
    return (R) this;
  }

  /**
   * Returns a list of all classpath elements.
   * 
   * @return   A list of all classpath elements. Immutable. Not <code>null</code>.
   */
  public List<Path> getPathes() {
    return Collections.unmodifiableList( pathesAsList );
  }
  
  /**
   * Returns the resources associated with the supplied classpath element.
   * 
   * @param path   The classpath element used to access the resources. Not <code>null</code>.
   * 
   * @return   The resources associated with the supplied classpath element. Immutable. Not <code>null</code>. 
   */
  public Set<String> getResourcesForPath( @NonNull Path path ) {
    Set<String> set = resources.get( path );
    if( set != null ) {
      return Collections.unmodifiableSet( set );
    } else {
      return Collections.emptySet();
    }
  }

  /**
   * Returns a list of all resources.
   * 
   * @return   A list of all resources. Immutable. Not <code>null</code>.
   */
  public List<String> getAllResources() {
    return Collections.unmodifiableList( asList );
  }
  
  /**
   * Resets this indexer meaning to clear all collected values.
   */
  public void reset() {
    resources    . clear();
    asList       . clear();
    pathesAsList . clear();
  }

  /**
   * Indexes the current system classpath {@link SysProperty#ClassPath}.
   */
  public void indexSystemClasspath() {
    List<File> files = SysProperty.ClassPath.getValue( System.getProperties() );
    files.forEach( f -> processClasspathEntry( Paths.get( f.toURI() ) ) );
  }

  /**
   * Indexes the supplied classpath elements. Files that aren't zip archives are being ignored.
   * 
   * @param path   The classpath elements that shall be collected. Not <code>null</code>.
   */
  public void indexClasspath( @NonNull Path ... path ) {
    indexClasspath( Arrays.asList( path ) );
  }

  /**
   * Indexes the supplied classpath elements. Files that aren't zip archives are being ignored.
   * 
   * @param pathes   The classpath elements that shall be collected. Not <code>null</code>.
   */
  public void indexClasspath( @NonNull List<Path> pathes ) {
    pathes.forEach( this::processClasspathEntry );
  }

  /**
   * Indexes the classpath from the current context class loader. If it's not a descendant of an {@link URLClassLoader} 
   * it will be ignored. Files that aren't zip archives are being ignored.
   */
  public void indexClassLoader() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    if( classLoader instanceof URLClassLoader ) {
      indexClassLoader( (URLClassLoader) classLoader );
    }
  }

  /**
   * Indexes the classpath of the supplied classloader. Files that aren't zip archives are being ignored.
   *  
   * @param classloader   The classloader which had been established through a specific classpath. Not <code>null</code>.
   */
  public void indexClassLoader( @NonNull URLClassLoader classloader ) {
    List<URL> urls = Arrays.asList( classloader.getURLs() );
    urls.forEach( u -> processClasspathEntry( IoFunctions.toPath(u) ) );
  }

  /**
   * Processes the supplied classpath element while collecting it's content.
   * 
   * @param path   The classpath element that is supposed to be collected. Not <code>null</code>.
   */
  private void processClasspathEntry( Path path ) {
    if( isValid( path ) ) {
      Set<String> entries = new HashSet<>();
      if( Files.isDirectory( path ) ) {
        entries.addAll( IoFunctions.listPathes( path, getResourceFilter() ) );
      } else {
        byte[] fragment = IoFunctions.forInputStream( path, zipTest.getMinSize(), IoFunctions::loadFragment );
        if( zipTest.test( fragment ) ) {
          entries.addAll( IoFunctions.listPathesInZip( path, getZipResourceFilter() ) );
        }
      }
      resources.put( path, entries );
      asList.addAll( entries );
      pathesAsList.add( path );
    }
  }
  
  private BiPredicate<Path,String> getResourceFilter() {
    if( resourceTester == null ) {
      return (p,s) -> true;
    } else {
      return (p,s) -> resourceTester.test(s);
    }
  }

  private BiPredicate<Path,ZipEntry> getZipResourceFilter() {
    if( resourceTester == null ) {
      return (p,z) -> true;
    } else {
      return (p,z) -> resourceTester.test(z.getName());
    }
  }

  private boolean isValid( Path path ) {
    if( pathTester != null ) {
      return pathTester.test( path );
    }
    return true;
  }
  
  public static void main( String[] args ) throws Exception {
    ClasspathIndexer indexer = new ClasspathIndexer();
    indexer.indexSystemClasspath();
    indexer.resources.values().forEach( System.err::println );
  }
  
}
