package com.kasisoft.libs.common.old.sys;

import lombok.experimental.*;

import lombok.*;

import com.kasisoft.libs.common.old.constants.*;
import com.kasisoft.libs.common.old.data.*;
import com.kasisoft.libs.common.old.io.*;
import com.kasisoft.libs.common.old.io.datatypes.*;

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

  Map<Path, Set<String>>                      resources;
  List<Path>                                  pathesAsList;
  List<String>                                asList;
  
  @Getter @Setter
  Predicate<Path>                             pathTester;
  
  @Getter @Setter
  Predicate<String>                           resourceTester;
  
  FileType                                    zipTest;
  
  Map<String, Partitioner<String, Path, ?>>   partitioners;
  
  public ClasspathIndexer() {
    zipTest         = new ZipFileType();
    resources       = new Hashtable<>();
    asList          = new ArrayList<>();
    pathesAsList    = new ArrayList<>();
    partitioners    = new Hashtable<>();
    pathTester      = null;
    resourceTester  = null;
  }
  
  
  /**
   * Registers a partitioner for java classes instance with a corresponding name. The partition data is of type
   * Set<String>.
   * 
   * @param name   The name for the partition. Neither <code>null</code> nor empty.
   * @param data   The data used to collect the records. Not <code>null</code>.
   * 
   * @return   this
   */
  public <R extends ClasspathIndexer, C extends Collection<String>> R withJavaClassPartitioner( @NonNull String name, @NonNull C data ) {
    return withPartitioner( name, ClasspathPartitioners.newToEnclosingClass( data ) ); 
  }

  /**
   * Registers a partitioner for resource files instance with a corresponding name. The partition data is of type
   * Set<String>.
   * 
   * @param name   The name for the partition. Neither <code>null</code> nor empty.
   * @param data   The data used to collect the records. Not <code>null</code>.
   * 
   * @return   this
   */
  public <R extends ClasspathIndexer, C extends Collection<String>> R withResourceFilesPartitioner( @NonNull String name, @NonNull C data ) {
    return withPartitioner( name, ClasspathPartitioners.newToResourceFile( data ) ); 
  }

  /**
   * Registers a partitioner for resource dirs instance with a corresponding name. The partition data is of type
   * Set<String>.
   * 
   * @param name   The name for the partition. Neither <code>null</code> nor empty.
   * @param data   The data used to collect the records. Not <code>null</code>.
   * 
   * @return   this
   */
  public <R extends ClasspathIndexer, C extends Collection<String>> R withResourceDirsPartitioner( @NonNull String name, @NonNull C data ) {
    return withPartitioner( name, ClasspathPartitioners.newToResourceDir( data ) ); 
  }

  /**
   * Registers a certain partitioner instance with a corresponding name.
   * 
   * @param name          The name for the partition. Neither <code>null</code> nor empty.
   * @param partitioner   The partitioner used to collect the data. Not <code>null</code>.
   * 
   * @return   this
   */
  public <R extends ClasspathIndexer> R withPartitioner( @NonNull String name, @NonNull Partitioner<String, Path,?> partitioner ) {
    partitioners.put( name, partitioner );
    return (R) this; 
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
   * Returns a list of all resources in the order they have been indexed (essentially in order of the classpath).
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
    partitioners.values().forEach( $ -> $.clear() );
  }
  
  /**
   * Reindexes the current classpath records.
   */
  public void reindex() {
    List<Path> list = new ArrayList<>( pathesAsList );
    reset();
    indexClasspath( list );
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
  private void indexClassLoader( @NonNull URLClassLoader classloader ) {
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
      } else if( Files.isRegularFile( path ) ) {
        Optional<byte[]> fragment = DefaultIO.PATH_INPUTSTREAM_EX.forInputStream( path, zipTest.getMinSize(), IoFunctions::loadFragment );
        if( fragment.filter( zipTest ).isPresent() ) {
          entries.addAll( IoFunctions.listPathesInZip( path, getZipResourceFilter() ) );
        }
      }
      resources.put( path, entries );
      asList.addAll( entries );
      pathesAsList.add( path );
      
      // collect the data per partitioner
      partitioners.values().forEach( x -> entries.stream().filter(x).forEach( $ -> x.collect( path, $ ) ) );
      
    }
  }
  
  private BiPredicate<Path, String> getResourceFilter() {
    if( resourceTester == null ) {
      return (p,s) -> true;
    } else {
      return (p,s) -> resourceTester.test(s);
    }
  }

  private BiPredicate<Path, ZipEntry> getZipResourceFilter() {
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
  
  /**
   * Returns the partition data of a certain partition.
   * 
   * @param name   The name of the desired partition. Neither <code>null</code> nor empty.
   * 
   * @return   The partition data. Maybe <code>null</code>.
   */
  public <R> R getPartition( @NonNull String name ) {
    Partitioner<String, Path, ?> partitioner = partitioners.get( name );
    if( partitioner != null ) {
      return (R) partitioner.getPartition();
    } else {
      return null;
    }
  }
  
  public static void main( String[] args ) throws Exception {
    ClasspathIndexer indexer = new ClasspathIndexer()
      .withResourceDirsPartitioner( "resourcedirs", new HashSet<>() )
      .withPartitioner( "maven", ClasspathPartitioners.newMaven( new ArrayList<>() ) )
      .withJavaClassPartitioner( "java", new ArrayList<>() );
    indexer.indexSystemClasspath();
    List<String> spi = indexer.getPartition( "maven" );
    spi.forEach( x -> System.err.println( ">> " + x ) );
  }

} /* ENDCLASS */
