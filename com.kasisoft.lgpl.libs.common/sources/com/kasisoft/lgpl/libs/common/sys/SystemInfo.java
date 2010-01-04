/**
 * Name........: SystemInfo
 * Description.: Simple class that provides some system related informations.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.sys;

import com.kasisoft.lgpl.libs.common.constants.*;

/**
 * Simple class that provides some system related informations.
 */
public enum SystemInfo {
  
  Sun           ( "SunOS"         ),
  Solaris       ( "Solaris"       ),
  Linux         ( "Linux"         ),
  Windows95     ( "Windows 95"    ),
  Windows98     ( "Windows 98"    ),
  WindowsME     ( "Windows Me"    ),
  WindowsNT     ( "Windows NT"    ),
  Windows2000   ( "Windows 2000"  ),
  WindowsXP     ( "Windows XP"    ),
  Windows7      ( "Windows 7"     ),
  Amiga         ( "Amiga OS"      ),
  Aros          ( "AROS"          ),
  MacOSX        ( "Mac OS X"      ),
  Morphos       ( "MorphOS"       );
  
  private String    key;
  private boolean   isrunning;
  
  /**
   * Prevent instantiation.
   */
  SystemInfo( String oskey ) {
    key       = oskey;
    isrunning = oskey.equals( SystemProperty.OsName.getValue() );
  }

  /**
   * Returns <code>true</code> if this operating system is currently active.
   * 
   * @return   <code>true</code> <=> This operating system is currently active.
   */
  public boolean isActive() {
    return isrunning;
  }

  /**
   * Returns the key used to identify the operating system.
   * 
   * @return   The key used to identify the operating system. Neither <code>null</code> nor empty.
   */
  public String getKey() {
    return key;
  }
  
  /**
   * Returns <code>true</code> in case we're working on a Unix derivative.
   *
   * @return  <code>true</code> <=> We're working on a Unix derivative.
   */
  public boolean isUnixLike() {
    return (this == Linux) || (this == Solaris);
  }
  
  /**
   * Returns <code>true</code> on Windows systems.
   *
   * @return  <code>true</code> <=> We're working on a Windows system.
   */
  public boolean isWindowsLike() {
    return
     ( this == Windows95   ) || ( this == Windows98 ) || ( this == WindowsME ) || 
     ( this == Windows2000 ) || ( this == WindowsNT ) || ( this == WindowsXP ) ||
     ( this == Windows7    );
  }
  
  /**
   * Returns <code>true</code> in case the operating system is amiga like.
   * 
   * @return   <code>true</code> <=> The operating system is amiga like.
   */
  public boolean isAmigaLike() {
    return (this == Amiga) || (this == Aros) || (this == Morphos);
  }

  /**
   * Returns the currently actively used operating system if it could be determined. If it's not
   * available a value of <code>null</code> is returned. Check for the property {@link SystemProperty#OsName}
   * to get the actual key for the running operating system in that case.
   * 
   * @return   The constant used to identify the operating system. Maybe <code>null</code>.
   */
  public static final SystemInfo getRunningOS() {
    for( SystemInfo info : SystemInfo.values() ) {
      if( info.isActive() ) {
        return info;
      }
    }
    return null;
  }
  
} /* ENDENUM */
