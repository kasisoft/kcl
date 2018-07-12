package com.kasisoft.libs.common.util;

import com.kasisoft.libs.common.function.*;
import com.kasisoft.libs.common.io.*;
import com.kasisoft.libs.common.text.*;

import java.util.function.*;

import java.util.*;

import java.net.*;

import java.nio.file.*;

import lombok.experimental.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResourceExtractor {

  String                argExtract;
  String                argExtractShort;
  String                argExtractForce;
  String                argExtractForceShort;
  String                extractionManifest;
  Supplier<Path>        destinationDir;
  Predicate<Path>       canBeSubstituted;
  Map<String, String>   substitutions;
  String                varFormatter;
  Consumer<String>      handleMissingResource;
  Runnable              handleAfterExtraction;
  
  public ResourceExtractor() {
    argExtract            = "--extract";
    argExtractShort       = "-e";
    argExtractForce       = "--extract-force";
    argExtractForceShort  = "-ef";
    extractionManifest    = "extraction.properties";
    destinationDir        = () -> getDestinationDir();
    canBeSubstituted      = Predicates.acceptNone();
    substitutions         = new HashMap<>();
    varFormatter          = "${%s}";
    handleMissingResource = $ -> {};
    handleAfterExtraction = () -> {};
  }
  
  public ResourceExtractor handleAfterExtraction( Runnable runnable ) {
    handleAfterExtraction = runnable != null ? runnable : handleAfterExtraction;
    return this;
  }

  public ResourceExtractor handleMissingResource( Consumer<String> handler ) {
    handleMissingResource = handler != null ? handler : handleMissingResource;
    return this;
  }

  public ResourceExtractor varFormatter( String formatter ) {
    varFormatter = formatter != null ? formatter : varFormatter;
    return this;
  }
  
  public ResourceExtractor substitution( @NonNull String key, @NonNull String value ) {
    substitutions.put( String.format( varFormatter, key ), value );
    return this;
  }

  public ResourceExtractor environment() {
    substitutions.putAll( MiscFunctions.createEnvironmentReplacements( varFormatter ) );
    return this;
  }

  public ResourceExtractor systemProperties() {
    substitutions.putAll( MiscFunctions.createSystemPropertiesReplacements( varFormatter ) );
    return this;
  }
  
  public ResourceExtractor canBeSubstituted( Predicate<Path> substitute ) {
    canBeSubstituted = substitute != null ? substitute : canBeSubstituted;
    return this;
  }
  
  public ResourceExtractor destinationDir( Supplier<Path> destination ) {
    destinationDir = destination != null ? destination : destinationDir;
    return this;
  }
  
  public ResourceExtractor extractionManifest( String manifest ) {
    extractionManifest = manifest != null ? manifest : extractionManifest;
    return this;
  }

  public ResourceExtractor argExtract( String arg ) {
    return argExtract( arg, null );
  }
  
  public ResourceExtractor argExtract( String arg, String argShort ) {
    argExtract      = arg      != null ? arg      : argExtract;
    argExtractShort = argShort != null ? argShort : argExtractShort;
    return this;
  }
  
  public ResourceExtractor argExtractForce( String arg ) {
    return argExtractForce( arg, null );
  }
  
  public ResourceExtractor argExtractForce( String arg, String argShort ) {
    argExtractForce       = arg      != null ? arg      : argExtractForce;
    argExtractForceShort  = argShort != null ? argShort : argExtractForceShort;
    return this;
  }
  
  private Path getDestinationDir() {
    return MiscFunctions.locateDirectory( ResourceExtractor.class );
  }
  
  public void execute( String[] args ) {
    boolean extract = false;
    boolean force   = false;
    for( String arg : args ) {
      if( argExtract.equals( arg ) || argExtractShort.equals( arg ) ) {
        extract = true;
      } else if( argExtractForce.equals( arg ) || argExtractForceShort.equals( arg ) ) {
        extract = true;
        force   = true;
      }
    }
    if( extract ) {
      if( extract( force ) ) {
        handleAfterExtraction.run();
      }
    }
  }
  
  public boolean extract( boolean force ) {
    boolean result      = false;
    URL     manifesturl = MiscFunctions.getResource( extractionManifest );
    if( manifesturl != null ) {
      Path                destination = destinationDir.get();
      Properties          properties  = IoFunctions.loadProperties( manifesturl );
      Enumeration<String> names       = (Enumeration<String>) properties.propertyNames();
      while( names.hasMoreElements() ) {
        String name  = names.nextElement();
        String value = properties.getProperty( name );
        result = extract( destination, removeLeadingSlash( name ), removeLeadingSlash( value ), force ) || result;
      }
    } else {
      handleMissingResource.accept( extractionManifest );
    }
    return result;
  }
  
  private String removeLeadingSlash( String value ) {
    return StringFunctions.trim( value, "/", true );
  }
  
  private boolean extract( Path destdir, String source, String destination, boolean force ) {
    boolean result = false;
    Path    dest   = destdir.resolve( destination );
    Path    parent = dest.getParent();
    IoFunctions.mkdirs( parent );
    if( (! Files.isRegularFile( dest )) || force ) {
      result = extract( source, dest );
    }
    return result;
  }
  
  private boolean extract( String source, Path dest ) {
    boolean result = false;
    URL     url    = ResourceExtractor.class.getClassLoader().getResource( source );
    if( url != null ) {
      IoFunctions.forOutputStreamDo( dest, $o -> {
        IoFunctions.forInputStreamDo( url, $i -> {
          IoFunctions.copy( $i, $o );
        });
      });
      if( canBeSubstituted.test( dest ) && (!substitutions.isEmpty()) ) {
        String text = IoFunctions.forReader( dest, IoFunctions::readTextFully );
        text        = StringFunctions.replace( text, substitutions );
        IoFunctions.forWriterDo( dest, text, IoFunctions::writeText );
      }
      result = true;
    } else {
      handleMissingResource.accept( extractionManifest );
    }
    return result;
  }
  
} /* ENDCLASS */
