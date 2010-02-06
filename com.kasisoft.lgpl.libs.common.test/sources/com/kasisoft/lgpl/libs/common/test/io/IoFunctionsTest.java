/**
 * Name........: IoFunctionsTest
 * Description.: Tests for the class 'IoFunctions'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.io;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.libs.common.io.*;

import com.kasisoft.lgpl.libs.common.test.framework.*;

import org.testng.annotations.*;

import org.testng.*;

import java.io.*;

/**
 * Tests for the class 'IoFunctions'.
 * 
 * @todo [06-Feb-2010:KASI]   Tests for the close methods are still missing.
 */
public class IoFunctionsTest {

  @Test
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
  
  @Test
  public void allocateAndRelease() {
    
    Integer defaultsize = CommonProperty.BufferCount.getValue();
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

  @Test
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

  @Test
  public void copyFiles() {
    
    File    tempfile1 = Utilities.createRandomBytesFile();
    File    tempfile2 = IoFunctions.newTempFile();
    IoFunctions.copy( tempfile1, tempfile2 );
    Assert.assertEquals( tempfile2, tempfile1, "Comparing files." );
    
  }
  
  @Test
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
  
  @Test
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

  @Test
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

//  public static final List<String> readText( 
//    @KNotNull(name="input")   Reader    input, 
//                              boolean   trim, 
//                              boolean   emptylines 
//
//  public static final List<String> readText(
//    @KNotNull(name="input")   Reader   input 
//  ) {
//    return readText( input, false, true );
//  }
//  
//  public static final List<String> readText( 
//    @KFile(name="input")   File       input, 
//                           boolean    trim, 
//                           boolean    emptylines,
//                           Encoding   encoding
//
//  public static final List<String> readText( 
//    @KFile(name="input")   File       input,
//                           Encoding   encoding
//
//  public static final void skip( 
//    @KNotNull(name="input")                 InputStream   input, 
//    @KIPositive(name="offset", zero=true)   int           offset 
//  
//  public static final void skip( 
//    @KNotNull(name="input")                 Reader   input, 
//    @KIPositive(name="offset", zero=true)   int      offset 
//
//  public static final byte[] loadFragment( 
//    @KNotNull(name="input")                 InputStream   input, 
//    @KIPositive(name="offset", zero=true)   int           offset, 
//    @KIPositive(name="length")              int           length 
//  
//  public static final byte[] loadFragment( 
//    @KFile(name="file")                     File   file, 
//    @KIPositive(name="offset", zero=true)   int    offset, 
//    @KIPositive(name="length")              int    length 
//  
//  public static final boolean isGZIP( 
//    @KNotNull(name="buffer")   byte[]   buffer 
//
//  public static final boolean isGZIP( 
//    @KFile(name="file")   File   file 
//
//  public static final long crc32( 
//    @KNotNull(name="instream")   InputStream   instream, 
//                                 CRC32         crc, 
//                                 Integer       buffersize 
//
//  public static final long crc32( 
//    @KNotNull(name="instream")   InputStream   instream 
//
//  public static final long crc32( 
//    @KFile(name="file")   File      file, 
//                          CRC32     crc, 
//                          Integer   buffersize 
//
//  public static final long crc32( 
//    @KFile(name="file")   File   file 
//
//  public static final boolean delete( @KNotNull(name="files") File ... files ) {
//  
//  public static final void writeText( 
//    @KNotNull(name="output")   OutputStream   output, 
//    @KNotNull(name="lines")    List<String>   lines, 
//                               Encoding       encoding 
//
//  public static final void writeText( 
//    @KFile(name="file", right=KFile.Right.Write)   File           file, 
//    @KNotNull(name="lines")                        List<String>   lines, 
//                                                   Encoding       encoding 
//  
//  public static final void writeText( 
//    @KNotNull(name="output")   OutputStream   output, 
//    @KNotNull(name="text")     String         text, 
//                               Encoding       encoding 
//  
//  public static final void writeText( 
//    @KFile(name="file", right=KFile.Right.Write)   File       file, 
//    @KNotNull(name="text")                         String     text, 
//                                                   Encoding   encoding 
//  
//  public static final List<File> listRecursive( 
//    @KDirectory(name="dir")   File         dir, 
//                              FileFilter   filter, 
//                              boolean      includefiles, 
//                              boolean      includedirs 
//
//  public static final List<File> listRecursive( 
//    @KDirectory(name="dir")   File         dir, 
//    @KNotNull(name="filter")  FileFilter   filter 
//  
//  public static final boolean zip( 
//    @KFile(name="zipfile", right=KFile.Right.Write)   File      zipfile, 
//    @KDirectory(name="dir")                           File      dir, 
//                                                      Integer   buffersize 
//
//  public static final boolean unzip( 
//    @KFile(name="zipfile")        File      zipfile, 
//    @KDirectory(name="destdir")   File      destdir, 
//                                  Integer   buffersize 

} /* ENDCLASS */
