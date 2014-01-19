/**
 * Name........: SystemInfo
 * Description.: Simple class that provides some system related informations.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.sys;

import com.kasisoft.libs.common.constants.*;

import lombok.*;

/**
 * Simple class that provides some system related informations.
 */
public enum SystemInfo {
  
  FreeBSD       ( "FreeBSD"       , true  , "$%s", "$(%s)" ),
  HPUX          ( "HP UX"         , true  , "$%s", "$(%s)" ),
  Sun           ( "SunOS"         , true  , "$%s", "$(%s)" ),
  Solaris       ( "Solaris"       , true  , "$%s", "$(%s)" ),
  Linux         ( "Linux"         , true  , "$%s", "$(%s)" ),
  Windows95     ( "Windows 95"    , false , "%%%s%%" ),
  Windows98     ( "Windows 98"    , false , "%%%s%%" ),
  WindowsME     ( "Windows Me"    , false , "%%%s%%" ),
  WindowsNT     ( "Windows NT"    , false , "%%%s%%" ),
  Windows2000   ( "Windows 2000"  , false , "%%%s%%" ),
  WindowsXP     ( "Windows XP"    , false , "%%%s%%" ),
  WindowsVista  ( "Windows Vista" , false , "%%%s%%" ),
  Windows7      ( "Windows 7"     , false , "%%%s%%" ),
  Amiga         ( "Amiga OS"      , false , "$%s" ),
  Aros          ( "AROS"          , false , "$%s" ),
  MacOS         ( "Mac OS"        , false , "$%s" ),
  MacOSX        ( "Mac OS X"      , false , "$%s" ),
  Morphos       ( "MorphOS"       , false , "$%s" ),
  ThisMachine   ();
  
  static {
    SystemInfo active = null;
    for( SystemInfo info : SystemInfo.values() ) {
      if( info.isrunning && (info != ThisMachine) ) {
        active = info;
        break;
      }
    }
    if( active == null ) {
      active = ThisMachine;
    }
    LocalData.active = active;
  }
  
  private String     key;
  private boolean    isrunning;
  private boolean    casesensitive;
  private String[]   varformats;
  
  SystemInfo( String oskey, boolean sensitive, String ... varkeys ) {
    key           = oskey;
    isrunning     = oskey.equals( SysProperty.OsName.getValue( System.getProperties() ) );
    casesensitive = sensitive;
    varformats    = varkeys;
  }
  
  SystemInfo() {
    key           = SysProperty.OsName.getValue( System.getProperties() );
    isrunning     = true;
    // conservative guessing
    casesensitive = false; 
    varformats    = new String[] { "$%s" };
  }
  
  /**
   * Returns a list of variable keys formatted according to the corresponding operating system.
   * These formatted keys can be used to perform replacements.
   * 
   * @param keyname   The name of the key. Neither <code>null</code> nor empty.
   * 
   * @return   The variable keys matching the os. Neither <code>null</code> nor empty.
   */
  public String[] getVariableKeys( @NonNull String keyname ) {
    String[] result = new String[ varformats.length ];
    for( int i = 0; i < varformats.length; i++ ) {
      result[i] = String.format( varformats[i], keyname );
    }
    return result;
  }
  
  /**
   * Returns <code>true</code> if the filesystem is case sensitive.
   * 
   * @ks.note [09-Dec-2012:KASI]   This information is just a close guess. Case sensitivity of a filesystem might as well 
   *                               depend on the technical specifications of the filesystem itself.
   * 
   * @return   <code>true</code> <=> The filesystem is case sensitive.
   */
  public boolean isCaseSensitiveFS() {
    return casesensitive;
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
    return (this == Linux) || (this == Solaris) || (this == FreeBSD) || (this == HPUX);
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
     ( this == WindowsVista) || ( this == Windows7    );
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
   * Returns the currently actively used operating system if it could be determined. If it's not available a value of 
   * <code>null</code> is returned. Check for the property {@link SystemProperty#OsName} to get the actual key for the 
   * running operating system in that case.
   * 
   * @return   The constant used to identify the operating system. Not <code>null</code>.
   */
  public static SystemInfo getRunningOS() {
    return LocalData.active;
  }
  
  private static class LocalData {
    
    private static SystemInfo   active = null;
    
  } /* ENDCLASS */
  
} /* ENDENUM */
