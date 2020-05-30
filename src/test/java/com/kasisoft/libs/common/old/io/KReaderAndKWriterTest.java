package com.kasisoft.libs.common.old.io;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import com.kasisoft.libs.common.old.base.*;

import org.testng.annotations.*;

import java.util.Optional;

import java.net.*;

import java.nio.file.*;

import java.io.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Tests for the class 'KInputStream'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Test
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KReaderAndKWriterTest {
  
  Path          helloWorldPath;
  File          helloWorldFile;
  String        helloWorldString;
  URI           helloWorldURI;
  URL           helloWorldURL;
  
  @BeforeClass
  public void before() throws Exception {
    helloWorldPath      = Files.createTempFile( "char-stream-functions-", "txt" );
    helloWorldFile      = Files.createTempFile( "char-stream-functions-", "txt" ).toFile();
    helloWorldString    = Files.createTempFile( "char-stream-functions-", "txt" ).toString();
    helloWorldURI       = Files.createTempFile( "char-stream-functions-", "txt" ).toUri();
    helloWorldURL       = Files.createTempFile( "char-stream-functions-", "txt" ).toUri().toURL();
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
  public <T> void forWriter_Error( Class<T> type, T input, String suffix ) {
    KWriter<T>       writer = KWriter.builder( type ).build();
    Optional<String> result = writer.forWriter( input, "STR-0", "STR-1", this::writeCausesError );
    assertNotNull( result );
    assertFalse( result.isPresent() );
  }

  @Test(groups="all", dataProvider="getData", dependsOnMethods="forWriter_Error")
  public <T> void forWriterDo_Error( Class<T> type, T input, String suffix ) {
    KWriter<T>       writer = KWriter.builder( type ).build();
    assertFalse( writer.forWriterDo( input, "STR-0", "STR-1", this::writeCausesError ) );
  }

  @Test(groups="all", dataProvider="getData", expectedExceptions=RuntimeException.class, dependsOnMethods="forWriterDo_Error")
  public <T> void forWriter_Error_Throw( Class<T> type, T input, String suffix ) {
    KWriter<T> writer = KWriter.builder( type ).errorHandler( this::throwException ).build();
    writer.forWriter( input, "STR-0", "STR-1", this::writeCausesError );
  }

  @Test(groups="all", dataProvider="getData", expectedExceptions=RuntimeException.class, dependsOnMethods="forWriter_Error_Throw")
  public <T> void forWriterDo_Error_Throw( Class<T> type, T input, String suffix ) {
    KWriter<T> writer = KWriter.builder( type ).errorHandler( this::throwException ).build();
    writer.forWriterDo( input, "STR-0", "STR-1", this::writeCausesError );
  }

  @Test(groups="all", dataProvider="getData", dependsOnMethods="forWriterDo_Error_Throw")
  public <T> void forWriter( Class<T> type, T input, String suffix ) throws Exception {
    KWriter<T>       writer = KWriter.builder( type ).build();
    Optional<String> result = writer.forWriter( input, "STR-0", "STR-1", ($o, $c1, $c2) -> write( $o, $c1, $c2, suffix ) );
    assertNotNull( result );
    assertTrue( result.isPresent() );
    assertThat( result.get(), is( "STR-0_STR-1" + suffix ) );
    assertExistence( input );
  }

  @Test(groups="all", dataProvider="getData", dependsOnMethods="forWriter")
  public <T> void forWriterDo( Class<T> type, T input, String suffix ) throws Exception {
    KWriter<T> writer = KWriter.builder( type ).build();
    assertTrue( writer.forWriterDo( input, "STR-0", "STR-1", ($o, $c1, $c2) -> write( $o, $c1, $c2, suffix ) ) );
    assertExistence( input );
  }

  @Test(groups="all", dataProvider="getData", dependsOnMethods="forWriterDo")
  public <T> void forReader( Class<T> type, T input, String suffix ) throws Exception {
    KReader<T>       reader = KReader.builder( type ).build();
    int              length = length( input );
    Optional<String> result = reader.forReader( input, "STR-0", "STR-1", ($i, $c1, $c2) -> read( $i, $c1, $c2, length ) );
    assertNotNull( result );
    assertTrue( result.isPresent() );
    assertThat( result.get(), is( "STR-0_STR-1" + suffix + "_STR-0_STR-1" ) );
  }

  @Test(groups="all", dataProvider="getData", dependsOnMethods="forReader")
  public <T> void forReaderDo( Class<T> type, T input, String suffix ) throws Exception {
    KReader<T> reader = KReader.builder( type ).build();
    int        length = length( input );
    assertTrue( reader.forReaderDo( input, "STR-0", "STR-1", ($i, $c1, $c2) -> read($i, $c1, $c2, length, "STR-0_STR-1" + suffix + "_STR-0_STR-1" ) ) );
  }

  private <T> void throwException( Exception ex, T input ) {
    if( ex instanceof RuntimeException ) {
      throw (RuntimeException) ex;
    } else {
      throw new RuntimeException(ex);
    }
  }
  
  private String writeCausesError( Writer reader, String context1, String context2 ) {
    throw KclException.wrap( new Exception() );
  }

  private String read( Reader reader, String context1, String context2, int length ) {
    char[] buffer = new char[ length ];
    try {
      reader.read( buffer );
    } catch( Exception ex ) {
      throw new RuntimeException(ex);
    }
    return new String( buffer ) + "_" + context1 + "_" + context2;
  }

  private void read( Reader reader, String context1, String context2, int length, String expected ) {
    assertThat( read( reader, context1, context2, length ), is( expected ) );
  }

  private String write( Writer writer, String context1, String context2, String value ) {
    String result = context1 + "_" + context2 + value;
    try {
      writer.write( result.toCharArray() );
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
