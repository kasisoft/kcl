package com.kasisoft.libs.common.old.io;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import com.kasisoft.libs.common.constants.Encoding;

import com.kasisoft.libs.common.KclException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Optional;

import java.net.URI;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the class 'KInputStream'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Test
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KInputStreamAndKOutputStreamTest {
  
  Path          helloWorldPath;
  File          helloWorldFile;
  String        helloWorldString;
  URI           helloWorldURI;
  URL           helloWorldURL;
  
  @BeforeClass
  public void before() throws Exception {
    helloWorldPath      = Files.createTempFile( "stream-functions-", "txt" );
    helloWorldFile      = Files.createTempFile( "stream-functions-", "txt" ).toFile();
    helloWorldString    = Files.createTempFile( "stream-functions-", "txt" ).toString();
    helloWorldURI       = Files.createTempFile( "stream-functions-", "txt" ).toUri();
    helloWorldURL       = Files.createTempFile( "stream-functions-", "txt" ).toUri().toURL();
    helloWorldPath.toFile().deleteOnExit();
    helloWorldFile.deleteOnExit();
    Paths.get( helloWorldString ).toFile().deleteOnExit();
    Paths.get( helloWorldURI ).toFile().deleteOnExit();
    Paths.get( helloWorldURL.toURI() ).toFile().deleteOnExit();
  }

  @DataProvider
  public Object[][] getData() {
    return new Object[][] {
      { Path   . class , helloWorldPath   , "_helloWorldPath"   },
      { File   . class , helloWorldFile   , "_helloWorldFile"   },
      { String . class , helloWorldString , "_helloWorldString" },
      { URI    . class , helloWorldURI    , "_helloWorldURI"    },
      { URL    . class , helloWorldURL    , "_helloWorldURL"    },
    };
  }
  
  @Test(groups="all", dataProvider="getData")
  public <T> void forOutputStream_Error( Class<T> type, T input, String suffix ) {
    KOutputStream<T> writer = KOutputStream.builder( type ).build();
    Optional<String> result = writer.forOutputStream( input, "STR-0", "STR-1", this::writeCausesError );
    assertNotNull( result );
    assertFalse( result.isPresent() );
  }

  @Test(groups="all", dataProvider="getData", dependsOnMethods="forOutputStream_Error")
  public <T> void forOutputStreamDo_Error( Class<T> type, T input, String suffix ) {
    KOutputStream<T> writer = KOutputStream.builder( type ).build();
    assertFalse( writer.forOutputStreamDo( input, "STR-0", "STR-1", this::writeCausesError ) );
  }

  @Test(groups="all", dataProvider="getData", expectedExceptions=RuntimeException.class, dependsOnMethods="forOutputStreamDo_Error")
  public <T> void forOutputStream_Error_Throw( Class<T> type, T input, String suffix ) {
    KOutputStream<T> writer = KOutputStream.builder( type ).errorHandler( this::throwException ).build();
    writer.forOutputStream( input, "STR-0", "STR-1", this::writeCausesError );
  }

  @Test(groups="all", dataProvider="getData", expectedExceptions=RuntimeException.class, dependsOnMethods="forOutputStream_Error_Throw")
  public <T> void forOutputStreamDo_Error_Throw( Class<T> type, T input, String suffix ) {
    KOutputStream<T> writer = KOutputStream.builder( type ).errorHandler( this::throwException ).build();
    writer.forOutputStreamDo( input, "STR-0", "STR-1", this::writeCausesError );
  }

  @Test(groups="all", dataProvider="getData", dependsOnMethods="forOutputStreamDo_Error_Throw")
  public <T> void forOutputStream( Class<T> type, T input, String suffix ) throws Exception {
    KOutputStream<T> writer = KOutputStream.builder( type ).build();
    Optional<String> result = writer.forOutputStream( input, "STR-0", "STR-1", ($o, $c1, $c2) -> write( $o, $c1, $c2, suffix ) );
    assertNotNull( result );
    assertTrue( result.isPresent() );
    assertThat( result.get(), is( "STR-0_STR-1" + suffix ) );
    assertExistence( input );
  }

  @Test(groups="all", dataProvider="getData", dependsOnMethods="forOutputStream")
  public <T> void forOutputStreamDo( Class<T> type, T input, String suffix ) throws Exception {
    KOutputStream<T> writer = KOutputStream.builder( type ).build();
    assertTrue( writer.forOutputStreamDo( input, "STR-0", "STR-1", ($o, $c1, $c2) -> write( $o, $c1, $c2, suffix ) ) );
    assertExistence( input );
  }

  @Test(groups="all", dataProvider="getData", dependsOnMethods="forOutputStreamDo")
  public <T> void forInputStream( Class<T> type, T input, String suffix ) throws Exception {
    KInputStream<T>  reader = KInputStream.builder( type ).build();
    int              length = length( input );
    Optional<String> result = reader.forInputStream( input, "STR-0", "STR-1", ($i, $c1, $c2) -> read($i, $c1, $c2, length ) );
    assertNotNull( result );
    assertTrue( result.isPresent() );
    assertThat( result.get(), is( "STR-0_STR-1" + suffix + "_STR-0_STR-1" ) );
  }

  @Test(groups="all", dataProvider="getData", dependsOnMethods="forInputStream")
  public <T> void forInputStreamDo( Class<T> type, T input, String suffix ) throws Exception {
    KInputStream<T> reader = KInputStream.builder( type ).build();
    int             length = length( input );
    assertTrue( reader.forInputStreamDo( input, "STR-0", "STR-1", ($i, $c1, $c2) -> read($i, $c1, $c2, length, "STR-0_STR-1" + suffix + "_STR-0_STR-1" ) ) );
  }

  private <T> void throwException( Exception ex, T input ) {
    if( ex instanceof RuntimeException ) {
      throw (RuntimeException) ex;
    } else {
      throw new RuntimeException(ex);
    }
  }
  
  private String writeCausesError( OutputStream outputStream, String context1, String context2 ) {
    throw KclException.wrap( new Exception() );
  }

  private String read( InputStream inputStream, String context1, String context2, int length ) {
    byte[] buffer = new byte[ length ];
    try {
      inputStream.read( buffer );
    } catch( Exception ex ) {
      throw new RuntimeException(ex);
    }
    return Encoding.UTF8.decode( buffer ) + "_" + context1 + "_" + context2;
  }

  private void read( InputStream inputStream, String context1, String context2, int length, String expected ) {
    assertThat( read( inputStream, context1, context2, length ), is( expected ) );
  }

  private String write( OutputStream outputStream, String context1, String context2, String value ) {
    String result = context1 + "_" + context2 + value;
    try {
      outputStream.write( Encoding.UTF8.encode( result ) );
    } catch( Exception ex ) {
      throw new RuntimeException(ex);
    }
    return result;
  }

  private void assertExistence( Object input ) throws Exception {
    if( input instanceof Path ) {
      assertTrue( Files.isRegularFile( (Path) input ) );
    } else if( input instanceof File ) {
      assertExistence( ((File) input).toPath() );
    } else if( input instanceof String ) {
      assertExistence( Paths.get( (String) input ) );
    } else if( input instanceof URI ) {
      assertExistence( Paths.get( (URI) input ) );
    } else if( input instanceof URL ) { 
      assertExistence( ((URL) input).toURI() );
    } else {
      fail();
    }
  }

  private int length( Object input ) throws Exception {
    if( input instanceof Path ) {
      return (int) Files.size( (Path) input );
    } else if( input instanceof File ) {
      return length( ((File) input).toPath() );
    } else if( input instanceof String ) {
      return length( Paths.get( (String) input ) );
    } else if( input instanceof URI ) {
      return length( Paths.get( (URI) input ) );
    } else if( input instanceof URL ) { 
      return length( ((URL) input).toURI() );
    } else {
      fail();
      return -1;
    }
  }
  
} /* ENDCLASS */
