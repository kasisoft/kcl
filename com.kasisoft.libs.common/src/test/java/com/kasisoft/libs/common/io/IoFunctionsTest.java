package com.kasisoft.libs.common.io;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

import org.testng.*;

import com.kasisoft.libs.common.constants.*;

import com.kasisoft.libs.common.test.framework.*;

import lombok.experimental.*;

import lombok.*;

import java.util.*;

import java.nio.file.*;

import java.io.*;

/**
 * Tests for the class 'IoFunctions'.
 * 
 * @ks.todo [06-Feb-2010:KASI]   Tests for the close methods are still missing.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Test
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IoFunctionsTest {

  File   testdata;
  File   directory;
  File   unpackeddir;
  File   destfile;
  Path   testdataAsPath;
  
  @BeforeTest
  public void setup() {
    testdata        = Utilities.getTestdataDir();
    destfile        = IoFunctions.newTempFile( "file-", ".zip" );
    directory       = IoFunctions.newTempFile( "temp-" );
    directory.mkdirs();
    unpackeddir     = IoFunctions.newTempFile( "temp-" );
    unpackeddir.mkdirs();
    Utilities.createFileSystemStructure( directory );
    testdataAsPath  = Paths.get( testdata.toURI() );
  }

  @Test(groups="all")
  public void newTempFile() {
    
    File file1 = IoFunctions.newTempFile();
    assertThat( file1, is( notNullValue() ) );
    
    File file2 = IoFunctions.newTempFile( "frog" );
    assertThat( file2, is( notNullValue() ) );
    assertTrue( file2.getName().startsWith( "frog" ) );
  
    File file3 = IoFunctions.newTempFile( "frog", ".txt" );
    assertThat( file3, is( notNullValue() ) );
    assertTrue( file3.getName().startsWith( "frog" ) );
    assertTrue( file3.getName().endsWith( ".txt" ) );
    
  }
  
  @Test(groups="all")
  public void allocateAndRelease() {
    
    Integer defaultsize = CommonProperty.BufferCount.getValue( System.getProperties() );
    byte[]  data1       = Primitive.PByte.allocate( null );
    assertThat( data1, is( notNullValue() ) );
    assertTrue( data1.length >= defaultsize.intValue() );

    Primitive.PByte.release( data1 );
    Primitive.PByte.release( data1 ); // just to be sure that double release won't do any bad
    
    byte[]  data2       = Primitive.PByte.allocate( null );
    assertThat( data2, is( notNullValue() ) );
    assertThat( data2, is( data1 ) );

    byte[]  data3       = Primitive.PByte.allocate( Integer.valueOf( 8192 ) );
    assertThat( data3, is( notNullValue() ) );
    assertTrue( data3.length >= 8192 );

    Primitive.PByte.release( data3 );
    Primitive.PByte.release( data3 ); // just to be sure that double release won't do any bad

    byte[]  data4       = Primitive.PByte.allocate( Integer.valueOf( 8192 ) );
    assertThat( data4, is( notNullValue() ) );
    assertThat( data4, is( data3 ) );
    
  }

  @Test(groups="all")
  public void copyStreams() {
    
    byte[]                data    = "MY DATA".getBytes();
    ByteArrayInputStream  bytein  = new ByteArrayInputStream( data );
    ByteArrayOutputStream byteout = new ByteArrayOutputStream();
    IoFunctions.copy( bytein, byteout );
    assertThat( byteout.toByteArray(), is( data ) );
    
    bytein.reset();
    byteout.reset();
    
    byte[]                buffer  = new byte[1024];
    IoFunctions.copy( bytein, byteout, buffer );
    assertThat( byteout.toByteArray(), is( data ) );

    bytein.reset();
    byteout.reset();

    IoFunctions.copy( bytein, byteout, 1024 );
    assertThat( byteout.toByteArray(), is( data ) );

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
    assertThat( charout.toCharArray(), is( data ) );
    
    charin.reset();
    charout.reset();
    
    char[]           buffer  = new char[1024];
    IoFunctions.copy( charin, charout, buffer );
    assertThat( charout.toCharArray(), is( data ) );

    charin.reset();
    charout.reset();

    IoFunctions.copy( charin, charout, 1024 );
    assertThat( charout.toCharArray(), is( data ) );

  }
  
  @Test(groups="all")
  public void loadAndWriteBytes() {
    
    byte[] data     = Utilities.createByteBlock();
    File   tempfile = IoFunctions.newTempFile();
    IoFunctions.forOutputStreamDo( tempfile, $ -> IoFunctions.writeBytes( $, data ) );
    
    byte[] loaded1  = IoFunctions.loadBytes( tempfile, null );
    assertThat( loaded1, is( data ) );

    byte[] loaded2  = IoFunctions.loadBytes( tempfile, Integer.valueOf( 1024 ) );
    assertThat( loaded2, is( data ) );

    byte[] loaded3  = IoFunctions.loadBytes( new ByteArrayInputStream( data ), null );
    assertThat( loaded3, is( data ) );

    byte[] loaded4  = IoFunctions.loadBytes(  new ByteArrayInputStream( data ), Integer.valueOf( 1024 ) );
    assertThat( loaded4, is( data ) );

  }

  @Test(groups="all")
  public void loadChars() {
    
    char[] data     = Utilities.createCharacterBlock();
    File   tempfile = IoFunctions.newTempFile();
    IoFunctions.forWriterDo( tempfile, Encoding.UTF8, $ -> IoFunctions.writeCharacters( $, data ) );
    
    char[] loaded1  = IoFunctions.loadChars( tempfile, null, Encoding.UTF8 );
    assertThat( loaded1, is( data ) );

    char[] loaded2  = IoFunctions.loadChars( tempfile, Integer.valueOf( 1024 ), Encoding.UTF8 );
    assertThat( loaded2, is( data ) );

    char[] loaded3  = IoFunctions.loadChars( new CharArrayReader( data ), null );
    assertThat( loaded3, is( data ) );

    char[] loaded4  = IoFunctions.loadChars(  new CharArrayReader( data ), Integer.valueOf( 1024 ) );
    assertThat( loaded4, is( data ) );

  }

  @Test(groups="all")
  public void loadTest() {
    
    File          testfile  = new File( testdata, "testfile.txt" );
  
    List<String>  text1     = IoFunctions.forReader( testfile, Encoding.UTF8, $ -> IoFunctions.readText( $, false, true ) );
    assertThat( text1, is( notNullValue() ) );
    assertThat( 7, is( text1.size() ) );
    assertThat( text1.toArray(), is( new Object[] { "BEGIN BLÖD", "", "LINE 1", "", "   LINE 2   ", "", "BLABLUB" } ) );

    List<String>  text2     = IoFunctions.forReader( testfile, Encoding.UTF8, $ -> IoFunctions.readText( $, false, false ) );
    assertThat( text2, is( notNullValue() ) );
    assertThat( 4, is( text2.size() ) );
    assertThat( text2.toArray(), is( new Object[] { "BEGIN BLÖD", "LINE 1", "   LINE 2   ", "BLABLUB" } ) );

    List<String>  text3     = IoFunctions.forReader( testfile, Encoding.UTF8, $ -> IoFunctions.readText( $, true, false ) );
    assertThat( text3, is( notNullValue() ) );
    assertThat( 4, is( text3.size() ) );
    assertThat( text3.toArray(), is( new Object[] { "BEGIN BLÖD", "LINE 1", "LINE 2", "BLABLUB" } ) );

    byte[]        textdata  = IoFunctions.loadBytes( testfile, null );

    Reader        reader1   = Encoding.UTF8.openReader( new ByteArrayInputStream( textdata ) );
    List<String>  text4     = IoFunctions.readText( reader1, false, true );
    assertThat( text4, is( notNullValue() ) );
    assertThat( 7, is( text4.size() ) );
    assertThat( text4.toArray(), is( new Object[] { "BEGIN BLÖD", "", "LINE 1", "", "   LINE 2   ", "", "BLABLUB" } ) );

    Reader        reader2   = Encoding.UTF8.openReader( new ByteArrayInputStream( textdata ) );
    List<String>  text5     = IoFunctions.readText( reader2, false, false );
    assertThat( text5, is( notNullValue() ) );
    assertThat( 4, is( text5.size() ) );
    assertThat( text5.toArray(), is( new Object[] { "BEGIN BLÖD", "LINE 1", "   LINE 2   ", "BLABLUB" } ) );

    Reader        reader3   = Encoding.UTF8.openReader( new ByteArrayInputStream( textdata ) );
    List<String>  text6     = IoFunctions.readText( reader3, true, false );
    assertThat( text6, is( notNullValue() ) );
    assertThat( 4, is( text6.size() ) );
    assertThat( text6.toArray(), is( new Object[] { "BEGIN BLÖD", "LINE 1", "LINE 2", "BLABLUB" } ) );

  }
  
  @Test(groups="all")
  public void skip() {
    
    String                str     = "BLA BLUB WAS HERE";
    
    ByteArrayInputStream  bytein  = new ByteArrayInputStream( Encoding.UTF8.encode( str ) );
    IoFunctions.skip( bytein, 4 );
    String                result1 = Encoding.UTF8.decode( IoFunctions.loadBytes( bytein, null ) );
    assertThat( result1, is( "BLUB WAS HERE" ) );
    
    CharArrayReader       charin  = new CharArrayReader( str.toCharArray() );
    IoFunctions.skip( charin, 4 );
    String                result2 = new String( IoFunctions.loadChars( charin, null ) );
    assertThat( result2, is( "BLUB WAS HERE" ) );
    
  }

  @Test(groups="all")
  public void loadFragment() {
    
    String                str       = "BLA BLUB WAS HERE";
    
    ByteArrayInputStream  bytein    = new ByteArrayInputStream( Encoding.UTF8.encode( str ) );
    String                result1   = Encoding.UTF8.decode( IoFunctions.loadFragment( bytein, 4, 4 ) );
    assertThat( result1, is( "BLUB" ) );
    
    File                  testfile  = new File( testdata, "testfile.txt" );
    String                result2   = Encoding.UTF8.decode( IoFunctions.forInputStream( testfile, $ -> IoFunctions.loadFragment( $, 15, 6 ) ) );
    assertThat( result2, is( "LINE 1" ) );
    
  }

  @Test(groups="all")
  public void crc32() {
   
    File    testfile  = new File( testdata, "testfile.gz" );
    
    assertThat( IoFunctions.forInputStream( testfile, IoFunctions::crc32 ), is( 1699530864L ) );
    
    byte[]  data      = IoFunctions.loadBytes( testfile, null );
    assertThat( IoFunctions.crc32( new ByteArrayInputStream( data ) ), is( 1699530864L ) );
    
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
    
    List<String> lines = new ArrayList<>();
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
    assertThat( loaded1, is( lines ) );
    
    File         tempfile1  = IoFunctions.newTempFile();
    IoFunctions.forWriterDo( tempfile1, Encoding.UTF8, $ -> IoFunctions.writeText( $, lines ) );
    List<String> loaded2    = IoFunctions.forReader( tempfile1, Encoding.UTF8, $ -> IoFunctions.readText( $, false, true ) );
    assertThat( loaded2, is( lines ) );
    
    StringBuilder buffer    = new StringBuilder();
    for( int i = 0; i < lines.size(); i++ ) {
      buffer.append( lines.get(i) );
      buffer.append( "\n" );
    }
    
    File         tempfile2  = IoFunctions.newTempFile();
    IoFunctions.forWriterDo( tempfile2, Encoding.UTF8, $ -> IoFunctions.writeText( $, lines ) );
    
    List<String> loaded3    = IoFunctions.forReader( tempfile2, Encoding.UTF8, $ -> IoFunctions.readText( $, false, true ) );
    assertThat( loaded3, is( lines ) );
    
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
    String               current = IoFunctions.forReader( bytein, Encoding.UTF8, IoFunctions::readTextFully );
    assertThat( current, is( text ) );
    
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
    assertThat( list1.size(), is( 26 ) );

    List<File> list2  = IoFunctions.listRecursive( testdata, filter, true, false );
    assertThat( list2.size(), is( 19 ) );

    List<File> list3  = IoFunctions.listRecursive( testdata, filter, false, true );
    assertThat( list3.size(), is( 7 ) );

  }
  
  @Test(groups="all")
  public void listPathes() {
    List<String> list  = IoFunctions.listPathes( testdataAsPath, (p,r) -> ! p.endsWith( "_svn" ) );
    list.forEach( System.err::println );
    assertThat( list.size(), is( 22 ) );
  }
  
  
  @Test(groups="all")
  public void zip() {
    assertTrue( IoFunctions.zip( destfile, directory, null ) );
  }
  
  @Test(dependsOnMethods="zip",groups="all")
  public void unzip() {
    Assert.assertTrue( IoFunctions.unzip( destfile, unpackeddir, null ) );
  }

  @Test(groups="all")
  public void locateDirectory() throws IOException {
    
    File dir1      = IoFunctions.locateDirectory( Iso3166Test.class );
    assertThat( dir1, is( notNullValue() ) );
    File current1  = new File( "target/test-classes" ).getCanonicalFile();
    assertTrue( dir1.equals( current1 ) );

    File dir2      = IoFunctions.locateDirectory( Iso3166Test.class, "target", "test-classes" );
    assertThat( dir2, is( notNullValue() ) );
    File current2  = new File( "." ).getCanonicalFile();
    assertTrue( dir2.equals( current2 ) );
    
  }
  
} /* ENDCLASS */
