package com.kasisoft.libs.common.old.model;

import lombok.experimental.*;

import lombok.*;

import com.kasisoft.libs.common.old.text.*;

import java.text.*;

/**
 * A simple descriptional datastructure for a version.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@EqualsAndHashCode(of = "text")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Version implements Comparable<Version> {

  @Getter int       major;
  @Getter int       minor;
  @Getter Integer   micro;
  @Getter String    qualifier;
          String    text;

  /**
   * Sets up this version with the supplied settings.
   * 
   * @param majorver   The major version number.
   * @param minorver   The minor version number.
   */
  public Version( int majorver, int minorver ) {
    this( majorver, minorver, 0, null );
    micro = null;
    text  = toText();
  }
  
  /**
   * Sets up this version with the supplied settings.
   * 
   * @param majorver       The major version number.
   * @param minorver       The minor version number.
   * @param qualifierstr   A qualifier String. Either <code>null</code> or not empty.
   */
  public Version( int majorver, int minorver, String qualifierstr ) {
    this( majorver, minorver, 0, qualifierstr );
    micro = null;
    text  = toText();
  }

  /**
   * Sets up this version with the supplied settings.
   * 
   * @param majorver       The major version number.
   * @param minorver       The minor version number.
   * @param microver       The micro version number. Maybe <code>null</code>.
   */
  public Version( int majorver, int minorver, int microver ) {
    this( majorver, minorver, microver, null );
  }
  
  /**
   * Sets up this version with the supplied settings.
   * 
   * @param majorver       The major version number.
   * @param minorver       The minor version number.
   * @param microver       The micro version number. Maybe <code>null</code>.
   * @param qualifierstr   A qualifier String. Either <code>null</code> or not empty.
   */
  public Version( int majorver, int minorver, int microver, String qualifierstr ) {
    major     = majorver;
    minor     = minorver;
    micro     = Integer.valueOf( microver );
    qualifier = qualifierstr;
    text      = toText();
  }

  /**
   * Creates a new instance based upon the supplied textual description.
   * 
   * @param version        A textual description of a version. Neither <code>null</code> nor empty.
   * @param hasmicro       <code>true</code> <=> Process a micro number.
   * @param hasqualifier   <code>true</code> <=> Process a optional qualifier.
   * 
   * @throws ParseException   The textual presentation is invalid.
   */
  public Version( String version, boolean hasmicro, boolean hasqualifier ) throws ParseException {
    this( version, Boolean.valueOf( hasmicro ), Boolean.valueOf( hasqualifier ) );
  }

  /**
   * Creates a new instance based upon the supplied textual description.
   * 
   * @param version  A textual description of a version. Neither <code>null</code> nor empty.
   * 
   * @throws ParseException   The textual presentation is invalid.
   */
  public Version( String version ) throws ParseException {
    this( version, null, null );
  }

  /**
   * Creates a new instance based upon the supplied textual description.
   * 
   * @param version        A textual description of a version. Neither <code>null</code> nor empty.
   * @param hasmicro       <code>true</code> <=> Process a micro number.
   * @param hasqualifier   <code>true</code> <=> Process a optional qualifier.
   * 
   * @throws ParseException   The textual presentation is invalid.
   */
  private Version( @NonNull String version, Boolean hasmicro, Boolean hasqualifier ) throws ParseException {

    int idx = 0;
    try {
      
      StringBuilder input = new StringBuilder( version );
      String part         = nextPart( input, '.' );
      major               = Integer.parseInt( part );
      idx++;
      
      part                = nextPart( input, '.' );
      minor               = Integer.parseInt( part );
      idx++;
      
      if( hasmicro != null ) {
        
        if( hasmicro.booleanValue() ) {
          part   = nextPart( input, '.', '_' );
          micro  = Integer.valueOf( part );
          idx++;
        }
        
        if( hasqualifier.booleanValue() ) {
          qualifier = StringFunctions.cleanup( input.toString() );
          if( qualifier == null ) {
            throw new Exception();
          }
        }
        
      } else {
        
        // this is our flexible approach where we're trying to match as much as possible
        try {
          
          part   = nextPart( input, '.', '_' );
          micro  = Integer.valueOf( part );
          idx++;
          
          qualifier = StringFunctions.cleanup( input.toString() );
          
        } catch( NumberFormatException ex ) {
          // not a valid number so it's obviously the qualifier
          qualifier = part;
        }
        
      }

      text      = toText();
      
    } catch( Exception ex ) {
      throw new ParseException( version, idx );
    }
    
  }

  /**
   * Creates a textual presentation of this version.
   * 
   * @return   A textual presentation of this version. Neither <code>null</code> nor empty.
   */
  public String toText() {
    return toText('.');
  }
  
  /**
   * Creates a textual presentation of this version.
   * 
   * @param qualifierdelim   The delimiter which has to be used for the qualifier (sometimes you might wann use '_').
   * 
   * @return   A textual presentation of this version. Neither <code>null</code> nor empty.
   */
  public String toText( char qualifierdelim ) {
    StringBuilder builder = new StringBuilder();
    builder.append( major );
    builder.append( '.' );
    builder.append( minor );
    if( micro != null ) {
      builder.append( '.' );
      builder.append( micro );
    }
    if( qualifier != null ) {
      builder.append( qualifierdelim );
      builder.append( qualifier );
    }
    return builder.toString();
  }

  private String nextPart( StringBuilder input, char ... characters ) {
    String result = null;
    int    pos    = StringFunctions.indexOf( 0, input, characters );
    if( pos == -1 ) {
      result = input.toString();
      input.setLength(0);
    } else {
      result = input.substring( 0, pos );
      input.delete( 0, pos + 1 );
    }
    return StringFunctions.cleanup( result );
  }
  
  @Override
  public String toString() {
    return text;
  }
  
  @Override
  public int compareTo( Version other ) {
    if( other == null ) {
      return -1;
    }
    int result = Integer.valueOf( major ).compareTo( Integer.valueOf( other.major ) );
    if( result == 0 ) {
      result = Integer.valueOf( minor ).compareTo( Integer.valueOf( other.minor ) );
    }
    if( result == 0 ) {
      if( (micro != null) && (other.micro != null) ) {
        result = micro.compareTo( other.micro );
      } else if( (micro != null) || (other.micro != null) ) {
        if( micro != null ) {
          result = -1;
        } else {
          result = 1;
        }
      }
    }
    if( result == 0 ) {
      result = text.compareTo( other.text );
    }
    return result;
  }

} /* ENDCLASS */
