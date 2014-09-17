/**
 * Name........: IoFunctionsTest
 * Description.: Tests for the class 'IoFunctions'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.test.framework.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

import java.io.*;

/**
 * Tests for the class 'IoFunctions'.
 * 
 * @ks.todo [06-Feb-2010:KASI]   Tests for the close methods are still missing.
 */
@Test
public class IoFunctionsTest {

  private File   testdata;
  private File   directory;
  private File   unpackeddir;
  private File   destfile;
  
  @BeforeTest
  public void setup() {
    testdata      = Utilities.getTestdataDir();
    destfile      = IoFunctions.newTempFile( "file-", ".zip" );
    directory     = IoFunctions.newTempFile( "temp-" );
    directory.mkdirs();
    unpackeddir   = IoFunctions.newTempFile( "temp-" );
    unpackeddir.mkdirs();
    Utilities.createFileSystemStructure( directory );
  }

  @Test(groups="all")
  public void newTempFile() {
    
    File file1 = IoFunctions.newTempFile();
    Assert.assertNotNull( file1 );
    
    File file2 = IoFunctions.newTempFile( "frog" );
    Assert.assertNotNull( file2 );
    Assert.assertTrue( file2.getName().startsWith( "frog" ) );
  
    File file3 = IoFunctions.newTempFile( "frog", ".txt" );
    Assert.assertNotNull( file3 );
    Assert.assertTrue( file3.getName().startsWith( "frog" ) );
    Assert.assertTrue( file3.getName().endsWith( ".txt" ) );
    
  }
  
  @Test(groups="all")
  public void allocateAndRelease() {
    
    Integer defaultsize = CommonProperty.BufferCount.getValue( System.getProperties() );
    byte[]  data1       = IoFunctions.allocateBytes( null );
    Assert.assertNotNull( data1 );
    Assert.assertTrue( data1.length >= defaultsize.intValue() );

    IoFunctions.releaseBytes( data1 );
    IoFunctions.releaseBytes( data1 ); // just to be sure that double release won't do any bad
    
    byte[]  data2       = IoFunctions.allocateBytes( null );
    Assert.assertNotNull( data2 );
    Assert.assertEquals( data2, data1 );

    byte[]  data3       = IoFunctions.allocateBytes( Integer.valueOf( 8192 ) );
    Assert.assertNotNull( data3 );
    Assert.assertTrue( data3.length >= 8192 );

    IoFunctions.releaseBytes( data3 );
    IoFunctions.releaseBytes( data3 ); // just to be sure that double release won't do any bad

    byte[]  data4       = IoFunctions.allocateBytes( Integer.valueOf( 8192 ) );
    Assert.assertNotNull( data4 );
    Assert.assertEquals( data4, data3 );
    
  }

  @Test(groups="all")
  public void copyStreams() {
    
    byte[]                data    = "MY DATA".getBytes();
    ByteArrayInputStream  bytein  = new ByteArrayInputStream( data );
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    IoFunctions.copy( bytein, byteout );
    Assert.assertEquals( byteout.toByteArray(), data );
    
    bytein.reset();
    byteout.reset();
    
    byte[]                buffer  = new byte[1024];
    IoFunctions.copy( bytein, byteout, buffer );
    Assert.assertEquals( byteout.toByteArray(), data );

    bytein.reset();
    byteout.reset();

    IoFunctions.copy( bytein, byteout, 1024 );
    Assert.assertEquals( byteout.toByteArray(), data );

  }

  @Test(groups="all")
  public void copyFiles() {
    
    File    tempfile1 = Utilities.createRandomBytesFile();
    File    tempfile2 = IoFunctions.newTempFile();
    IoFunctions.copy( tempfile1, tempfile2 );
    AssertExtension.assertEquals( tempfile2, tempfile1 );
    
  }
  
  @Test(groups="all")
  public void copyDir() {
    
    File tempdir1 = Utilities.createRandomDirectory();
    File tempdir2 = IoFunctions.newTempFile();
    IoFunctions.copyDir( tempdir1, tempdir2, true );
    AssertExtension.assertEquals( tempdir2, tempdir1 );
    
  }
  
  @Test(groups="all")
  public void copyReaderToWriter() throws IOException {
    
    char[]           data    = "MY DATA".toCharArray();
    CharArrayReader  charin  = new CharArrayReader( data );
    CharArrayWriter  charout = new CharArrayWriter();
    IoFunctions.copy( charin, charout );
    Assert.assertEquals( charout.toCharArray(), data );
    
    charin.reset();
    charout.reset();
    
    char[]           buffer  = new char[1024];
    IoFunctions.copy( charin, charout, buffer );
    Assert.assertEquals( charout.toCharArray(), data );

    charin.reset();
    charout.reset();

    IoFunctions.copy( charin, charout, 1024 );
    Assert.assertEquals( charout.toCharArray(), data );

  }
  
  @Test(groups="all")
  public void loadAndWriteBytes() {
    
    byte[] data     = Utilities.createByteBlock();
    File   tempfile = IoFunctions.newTempFile();
    IoFunctions.writeBytes( tempfile, data );
    
    byte[] loaded1  = IoFunctions.loadBytes( tempfile, null );
    Assert.assertEquals( loaded1, data );

    byte[] loaded2  = IoFunctions.loadBytes( tempfile, Integer.valueOf( 1024 ) );
    Assert.assertEquals( loaded2, data );

    byte[] loaded3  = IoFunctions.loadBytes( new ByteArrayInputStream( data ), null );
    Assert.assertEquals( loaded3, data );

    byte[] loaded4  = IoFunctions.loadBytes(  new ByteArrayInputStream( data ), Integer.valueOf( 1024 ) );
    Assert.assertEquals( loaded4, data );

  }

  @Test(groups="all")
  public void loadChars() {
    
    char[] data     = Utilities.createCharacterBlock();
    File   tempfile = IoFunctions.newTempFile();
    IoFunctions.writeCharacters( tempfile, data, Encoding.getDefault() );
    
    char[] loaded1  = IoFunctions.loadChars( tempfile, null, Encoding.getDefault() );
    Assert.assertEquals( loaded1, data );

    char[] loaded2  = IoFunctions.loadChars( tempfile, Integer.valueOf( 1024 ), Encoding.getDefault() );
    Assert.assertEquals( loaded2, data );

    char[] loaded3  = IoFunctions.loadChars( new CharArrayReader( data ), null );
    Assert.assertEquals( loaded3, data );

    char[] loaded4  = IoFunctions.loadChars(  new CharArrayReader( data ), Integer.valueOf( 1024 ) );
    Assert.assertEquals( loaded4, data );

  }

  @Test(groups="all")
  public void loadTest() {
    
    File          testfile  = new File( testdata, "testfile.txt" );
  
    List<String>  text1     = IoFunctions.readText( testfile, false, true, Encoding.UTF8 );
    Assert.assertNotNull( text1 );
    Assert.assertEquals( 7, text1.size() );
    Assert.assertEquals( text1.toArray(), new String[] { "BEGIN BLÖD", "", "LINE 1", "", "   LINE 2   ", "", "BLABLUB" } );

    List<String>  text2     = IoFunctions.readText( testfile, false, false, Encoding.UTF8 );
    Assert.assertNotNull( text2 );
    Assert.assertEquals( 4, text2.size() );
    Assert.assertEquals( text2.toArray(), new String[] { "BEGIN BLÖD", "LINE 1", "   LINE 2   ", "BLABLUB" } );

    List<String>  text3     = IoFunctions.readText( testfile, true, false, Encoding.UTF8 );
    Assert.assertNotNull( text3 );
    Assert.assertEquals( 4, text3.size() );
    Assert.assertEquals( text3.toArray(), new String[] { "BEGIN BLÖD", "LINE 1", "LINE 2", "BLABLUB" } );

    byte[]        textdata  = IoFunctions.loadBytes( testfile, null );

    Reader        reader1   = Encoding.UTF8.openReader( new ByteArrayInputStream( textdata ) );
    List<String>  text4     = IoFunctions.readText( reader1, false, true );
    Assert.assertNotNull( text4 );
    Assert.assertEquals( 7, text4.size() );
    Assert.assertEquals( text4.toArray(), new String[] { "BEGIN BLÖD", "", "LINE 1", "", "   LINE 2   ", "", "BLABLUB" } );

    Reader        reader2   = Encoding.UTF8.openReader( new ByteArrayInputStream( textdata ) );
    List<String>  text5     = IoFunctions.readText( reader2, false, false );
    Assert.assertNotNull( text5 );
    Assert.assertEquals( 4, text5.size() );
    Assert.assertEquals( text5.toArray(), new String[] { "BEGIN BLÖD", "LINE 1", "   LINE 2   ", "BLABLUB" } );

    Reader        reader3   = Encoding.UTF8.openReader( new ByteArrayInputStream( textdata ) );
    List<String>  text6     = IoFunctions.readText( reader3, true, false );
    Assert.assertNotNull( text6 );
    Assert.assertEquals( 4, text6.size() );
    Assert.assertEquals( text6.toArray(), new String[] { "BEGIN BLÖD", "LINE 1", "LINE 2", "BLABLUB" } );

  }
  
  @Test(groups="all")
  public void skip() {
    
    String                str     = "BLA BLUB WAS HERE";
    
    ByteArrayInputStream  bytein  = new ByteArrayInputStream( Encoding.UTF8.encode( str ) );
    IoFunctions.skip( bytein, 4 );
    String                result1 = Encoding.UTF8.decode( IoFunctions.loadBytes( bytein, null ) );
    Assert.assertEquals( result1, "BLUB WAS HERE" );
    
    CharArrayReader       charin  = new CharArrayReader( str.toCharArray() );
    IoFunctions.skip( charin, 4 );
    String                result2 = new String( IoFunctions.loadChars( charin, null ) );
    Assert.assertEquals( result2, "BLUB WAS HERE" );
    
  }

  @Test(groups="all")
  public void loadFragment() {
    
    String                str       = "BLA BLUB WAS HERE";
    
    ByteArrayInputStream  bytein    = new ByteArrayInputStream( Encoding.UTF8.encode( str ) );
    String                result1   = Encoding.UTF8.decode( IoFunctions.loadFragment( bytein, 4, 4 ) );
    Assert.assertEquals( result1, "BLUB" );
    
    File                  testfile  = new File( testdata, "testfile.txt" );
    String                result2   = Encoding.UTF8.decode( IoFunctions.loadFragment( testfile, 15, 6 ) );
    Assert.assertEquals( result2, "LINE 1" );
    
  }

  @Test(groups="all")
  public void crc32() {
   
    File    testfile  = new File( testdata, "testfile.gz" );
    
    Assert.assertEquals( IoFunctions.crc32( testfile ), 1699530864 );
    
    byte[]  data      = IoFunctions.loadBytes( testfile, null );
    Assert.assertEquals( IoFunctions.crc32( new ByteArrayInputStream( data ) ), 1699530864 );
    
  }

  @Test(groups="all")
  public void delete() {
    File        tempdir = IoFunctions.newTempFile();
    tempdir.mkdirs();
    List<File>  files   = Utilities.createFileSystemStructure( tempdir );
    IoFunctions.delete( tempdir );
    for( File file : files ) {
      Assert.assertFalse( file.exists() );
    }
  }
  
  @Test(groups="all")
  public void writeText() {
    
    List<String> lines = new ArrayList<String>();
    lines.add( "FRED" );
    lines.add( "FLINTSTONES" );
    lines.add( "ANIMAL" );
    lines.add( "IS" );
    lines.add( "NAMED" );
    lines.add( "DINO" );
    
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    IoFunctions.writeText( byteout, lines, Encoding.UTF8 );
    
    byte[]       data1      = byteout.toByteArray();
    Reader       reader1    = Encoding.UTF8.openReader( new ByteArrayInputStream( data1 ) );
    List<String> loaded1    = IoFunctions.readText( reader1, false, true );
    Assert.assertEquals( loaded1, lines );
    
    File         tempfile1  = IoFunctions.newTempFile();
    IoFunctions.writeText( tempfile1, lines, Encoding.UTF8 );
    List<String> loaded2    = IoFunctions.readText( tempfile1, false, true, Encoding.UTF8 );
    Assert.assertEquals( loaded2, lines );
    
    StringBuilder buffer    = new StringBuilder();
    for( int i = 0; i < lines.size(); i++ ) {
      buffer.append( lines.get(i) );
      buffer.append( "\n" );
    }
    
    File         tempfile2  = IoFunctions.newTempFile();
    IoFunctions.writeText( tempfile2, buffer.toString(), Encoding.UTF8 );
    
    List<String> loaded3    = IoFunctions.readText( tempfile2, false, true, Encoding.UTF8 );
    Assert.assertEquals( loaded3, lines );
    
  }

  @Test(groups="all")
  public void readTextAsIs() {
    
    StringBuilder builder = new StringBuilder();
    builder.append( "FRED\n" );
    builder.append( "FLINTSTONES\r\n\n" );
    builder.append( "ANIMAL" );
    builder.append( "IS\n\n" );
    builder.append( "NAMED\\n" );
    builder.append( "DINO" );
    
    String               text    = builder.toString();
    ByteArrayInputStream bytein  = new ByteArrayInputStream( Encoding.UTF8.encode( text ) ); 
    String               current = IoFunctions.readTextAsIs( bytein, Encoding.UTF8 );
    Assert.assertEquals( current, text );
    
  }

  
  @Test(groups="all")
  public void listRecursive() {
    FileFilter filter = new FileFilter() {
      @Override
      public boolean accept( File pathname ) {
        if( pathname.isDirectory() ) {
          return ! "_svn".equalsIgnoreCase( pathname.getName() );
        }
        return true;
      }
    };
    
    List<File> list1  = IoFunctions.listRecursive( testdata, filter );
    Assert.assertEquals( list1.size(), 26 );

    List<File> list2  = IoFunctions.listRecursive( testdata, filter, true, false );
    Assert.assertEquals( list2.size(), 19 );

    List<File> list3  = IoFunctions.listRecursive( testdata, filter, false, true );
    Assert.assertEquals( list3.size(), 7 );

  }
  
  @Test(groups="all")
  public void zip() {
    Assert.assertTrue( IoFunctions.zip( destfile, directory, null ) );
  }
  
  @Test(dependsOnMethods="zip",groups="all")
  public void unzip() {
    Assert.assertTrue( IoFunctions.unzip( destfile, unpackeddir, null ) );
  }

  @Test(groups="all")
  public void locateDirectory() throws IOException {
    File dir       = IoFunctions.locateDirectory( Iso3166Test.class );
    Assert.assertNotNull( dir );
    File current1  = new File( "target/test-classes" ).getCanonicalFile(); // using maven
    File current2  = new File( "build/classes/test"  ).getCanonicalFile(); // using gradle
    Assert.assertTrue( dir.equals( current1 ) || dir.equals( current2 ) );
  }
  
} /* ENDCLASS */
