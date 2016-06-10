package com.kasisoft.libs.common.util;

import lombok.*;

import java.text.*;

/**
 * A simple descriptional datastructure for a version.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 * 
 * @deprecated [10-Jun-2016:KASI]   This type had been moved into the package com.kasisoft.libs.common.model
 *                                  and will be removed with version 2.3.
 */
@Deprecated
public class Version extends com.kasisoft.libs.common.model.Version {

  /**
   * Sets up this version with the supplied settings.
   * 
   * @param majorver   The major version number.
   * @param minorver   The minor version number.
   */
  public Version( int majorver, int minorver ) {
    super( majorver, minorver );
  }
  
  /**
   * Sets up this version with the supplied settings.
   * 
   * @param majorver       The major version number.
   * @param minorver       The minor version number.
   * @param qualifierstr   A qualifier String. Either <code>null</code> or not empty.
   */
  public Version( int majorver, int minorver, String qualifierstr ) {
    super( majorver, minorver, qualifierstr );
  }

  /**
   * Sets up this version with the supplied settings.
   * 
   * @param majorver       The major version number.
   * @param minorver       The minor version number.
   * @param microver       The micro version number. Maybe <code>null</code>.
   */
  public Version( int majorver, int minorver, int microver ) {
    super( majorver, minorver, microver );
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
    super( majorver, minorver, microver, qualifierstr );
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
  public Version( @NonNull String version, boolean hasmicro, boolean hasqualifier ) throws ParseException {
    super( version, hasmicro, hasqualifier );
  }

  /**
   * Creates a new instance based upon the supplied textual description.
   * 
   * @param version  A textual description of a version. Neither <code>null</code> nor empty.
   * 
   * @throws ParseException   The textual presentation is invalid.
   */
  public Version( @NonNull String version ) throws ParseException {
    super( version );
  }

} /* ENDCLASS */
