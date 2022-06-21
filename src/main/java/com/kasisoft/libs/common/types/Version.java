package com.kasisoft.libs.common.types;

import static com.kasisoft.libs.common.internal.Messages.*;

import com.kasisoft.libs.common.pools.*;

import com.kasisoft.libs.common.text.*;

import com.kasisoft.libs.common.*;

import javax.validation.constraints.*;

/**
 * A simple descriptional datastructure for a version.
 * 
 * @todo [03-JUN-2020:KASI]   Replace the code using Regex
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Version implements Comparable<Version> {

  private int       major;
  private int       minor;
  private Integer   micro;
  private String    qualifier;
  private String    text;

  /**
   * Sets up this version with the supplied settings.
   * 
   * @param majorver   The major version number.
   * @param minorver   The minor version number.
   */
  public Version(int majorver, int minorver) {
    this(majorver, minorver, 0, null);
    micro = null;
    text  = toText();
  }
  
  /**
   * Sets up this version with the supplied settings.
   * 
   * @param majorver       The major version number.
   * @param minorver       The minor version number.
   * @param qualifierstr   A qualifier String.
   */
  public Version(int majorver, int minorver, @NotBlank String qualifierstr) {
    this(majorver, minorver, 0, qualifierstr);
    micro = null;
    text  = toText();
  }

  /**
   * Sets up this version with the supplied settings.
   * 
   * @param majorver       The major version number.
   * @param minorver       The minor version number.
   * @param microver       The micro version number.
   */
  public Version(int majorver, int minorver, int microver) {
    this(majorver, minorver, microver, null);
  }
  
  /**
   * Sets up this version with the supplied settings.
   * 
   * @param majorver       The major version number.
   * @param minorver       The minor version number.
   * @param microver       The micro version number.
   * @param qualifierstr   A qualifier String.
   */
  public Version(int majorver, int minorver, int microver, @NotBlank String qualifierstr) {
    major     = majorver;
    minor     = minorver;
    micro     = Integer.valueOf( microver );
    qualifier = qualifierstr;
    text      = toText();
  }

  /**
   * Creates a new instance based upon the supplied textual description.
   * 
   * @param version        A textual description of a version.
   * @param hasmicro       <code>true</code> <=> Process a micro number.
   * @param hasqualifier   <code>true</code> <=> Process a optional qualifier.
   */
  public Version(@NotBlank String version, boolean hasmicro, boolean hasqualifier) {
    this(version, Boolean.valueOf(hasmicro), Boolean.valueOf(hasqualifier));
  }

  /**
   * Creates a new instance based upon the supplied textual description.
   * 
   * @param version  A textual description of a version.
   */
  public Version(@NotBlank String version) {
    this(version, null, null);
  }

  /**
   * Creates a new instance based upon the supplied textual description.
   * 
   * @param version        A textual description of a version.
   * @param hasmicro       <code>true</code> <=> Process a micro number.
   * @param hasqualifier   <code>true</code> <=> Process a optional qualifier.
   */
  private Version(@NotBlank String version, Boolean hasmicro, Boolean hasqualifier) {

    int idx = 0;
    try {
      
      var input = Buckets.bucketStringFBuilder().allocate().append(version);
      var part   = nextPart(input, '.');
      major      = Integer.parseInt( part );
      idx++;
      
      part       = nextPart(input, '.');
      minor      = Integer.parseInt( part );
      idx++;
      
      if (hasmicro != null) {
        
        if (hasmicro.booleanValue()) {
          part   = nextPart(input, '.', '_');
          micro  = Integer.valueOf(part);
          idx++;
        }
        
        if (hasqualifier.booleanValue()) {
          qualifier = StringFunctions.cleanup(input.toString());
          if (qualifier == null) {
            throw new KclException(error_version_missing_qualifier, idx);
          }
        }
        
      } else {
        
        // this is our flexible approach where we're trying to match as much as possible
        try {
          
          part   = nextPart(input, '.', '_');
          micro  = Integer.valueOf(part);
          idx++;
          
          qualifier = StringFunctions.cleanup(input.toString());
          
        } catch (NumberFormatException ex) {
          // not a valid number so it's obviously the qualifier
          qualifier = part;
        }
        
      }

      text = toText();
      
    } catch (Exception ex) {
      throw new KclException(ex, error_version_cannot_parse_version, version);
    }
    
  }

  public int getMajor() {
    return major;
  }
  
  public int getMinor() {
    return minor;
  }
  
  public Integer getMicro() {
    return micro;
  }
  
  public String getQualifier() {
    return qualifier;
  }

  /**
   * Creates a textual presentation of this version.
   * 
   * @return   A textual presentation of this version.
   */
  public @NotBlank String toText() {
    return toText('.');
  }
  
  /**
   * Creates a textual presentation of this version.
   * 
   * @param qualifierdelim   The delimiter which has to be used for the qualifier (sometimes you might wann use '_').
   * 
   * @return   A textual presentation of this version.
   */
  public @NotBlank String toText(char qualifierdelim) {
    return Buckets.bucketStringFBuilder().forInstance($ -> {
      $.append( major );
      $.append( '.' );
      $.append( minor );
      if (micro != null) {
        $.append('.');
        $.append(micro);
      }
      if (qualifier != null) {
        $.append(qualifierdelim);
        $.append(qualifier);
      }
      return $.toString();
    });
  }

  private String nextPart(StringFBuilder input, char ... characters) {
    String result = null;
    var    pos    = input.indexOf(characters );
    if (pos == -1) {
      result = input.toString();
      input.setLength(0);
    } else {
      result = input.substring(0, pos);
      input.delete(0, pos + 1);
    }
    return StringFunctions.cleanup(result);
  }
  
  @Override
  public String toString() {
    return text;
  }
  
  @Override
  public int compareTo(Version other) {
    if (other == null) {
      return -1;
    }
    var result = Integer.valueOf(major).compareTo( Integer.valueOf(other.major));
    if (result == 0) {
      result = Integer.valueOf(minor).compareTo( Integer.valueOf(other.minor));
    }
    if (result == 0) {
      if ((micro != null) && (other.micro != null)) {
        result = micro.compareTo(other.micro);
      } else if ((micro != null) || (other.micro != null)) {
        if (micro != null) {
          result = -1;
        } else {
          result = 1;
        }
      }
    }
    if (result == 0) {
      result = text.compareTo(other.text);
    }
    return result;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((text == null) ? 0 : text.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Version other = (Version) obj;
    if (text == null) {
      if (other.text != null)
        return false;
    } else if (!text.equals(other.text))
      return false;
    return true;
  }

} /* ENDCLASS */
