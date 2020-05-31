package com.kasisoft.libs.common.old.util;

import static com.kasisoft.libs.common.old.io.DefaultIO.PATH_READER_EX;
import static com.kasisoft.libs.common.old.io.DefaultIO.PATH_WRITER_EX;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.old.constants.SysProperty;
import com.kasisoft.libs.common.old.io.IoFunctions;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import java.io.File;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Test(groups="all")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourceExtractorTest {

  ResourceExtractor   extractor;
  Path                currentdir;
  Path                fileXsd;
  Path                fileProps;
  Path                fileVars;

  @BeforeClass
  public void setup() throws Exception {
    currentdir  = new File(".").getCanonicalFile().toPath().resolve( "target/export" );
    fileXsd     = currentdir.resolve( "dodo/http.xsd" );
    fileProps   = currentdir.resolve( "bibo/fred.properties" );
    fileVars    = currentdir.resolve( "vars.properties" );
    extractor = new ResourceExtractor()
      .extractionManifest( "old/extraction.properties" )
      .destinationDir( () -> currentdir )
      ;
    Files.deleteIfExists( fileXsd   );
    Files.deleteIfExists( fileProps );
    Files.deleteIfExists( fileVars  );
  }

  @Test
  public void extract() {

    assertFalse( Files.isRegularFile( fileXsd   ) );
    assertFalse( Files.isRegularFile( fileProps ) );
    assertFalse( Files.isRegularFile( fileVars  ) );

    extractor.execute( new String[] { "--extract" } );

    assertTrue( Files.isRegularFile( fileXsd   ) );
    assertTrue( Files.isRegularFile( fileProps ) );
    assertTrue( Files.isRegularFile( fileVars  ) );

  }

  @Test(dependsOnMethods = "extract")
  public void extractButDontChange() {

    PATH_WRITER_EX.forWriterDo( fileXsd   , "", IoFunctions::writeText );
    PATH_WRITER_EX.forWriterDo( fileProps , "", IoFunctions::writeText );
    PATH_WRITER_EX.forWriterDo( fileVars  , "", IoFunctions::writeText );

    assertThat( fileXsd   . toFile().length(), is(0L) );
    assertThat( fileProps . toFile().length(), is(0L) );
    assertThat( fileVars  . toFile().length(), is(0L) );

    // extract again but files already exist, so there will be no change
    extractor.execute( new String[] { "--extract" } );

    assertThat( fileXsd   . toFile().length(), is(0L) );
    assertThat( fileProps . toFile().length(), is(0L) );
    assertThat( fileVars  . toFile().length(), is(0L) );

  }

  @Test(dependsOnMethods = "extractButDontChange")
  public void extractAndForce() {

    assertThat( fileXsd   . toFile().length(), is(0L) );
    assertThat( fileProps . toFile().length(), is(0L) );
    assertThat( fileVars  . toFile().length(), is(0L) );

    // extract again but files already exist, so there will be no change
    extractor.execute( new String[] { "--extract-force" } );

    assertTrue( fileXsd   . toFile().length() > 0L );
    assertTrue( fileProps . toFile().length() > 0L );
    assertTrue( fileVars  . toFile().length() > 0L );

  }

  @Test(dependsOnMethods = "extractAndForce")
  public void extractWhileSubstituting() {

    extractor
      .canBeSubstituted( $ -> $.toString().endsWith( "vars.properties" ) )
      .systemProperties()
      .substitution( "dir", currentdir.toString() );

    PATH_WRITER_EX.forWriterDo( fileXsd, "", IoFunctions::writeText );
    IoFunctions.delete( fileProps );

    assertThat( fileXsd.toFile().length(), is(0L) );
    assertFalse( Files.isRegularFile( fileProps ) );

    // extract again but files already exist, so there will be no change
    extractor.execute( new String[] { "--extract-force" } );

    assertTrue( fileXsd   . toFile().length() > 0L );
    assertTrue( fileProps . toFile().length() > 0L );
    assertTrue( fileVars  . toFile().length() > 0L );

    String text = PATH_READER_EX.forReader( fileVars, IoFunctions::readTextFully ).orElse( null );
    assertTrue( text.contains( String.format( "directory=%s", currentdir.toString() ) ) );
    assertTrue( text.contains( String.format( "version=%s", SysProperty.ClassVersion.getValue().toString() ) ) );

  }

} /* ENDCLASS */
