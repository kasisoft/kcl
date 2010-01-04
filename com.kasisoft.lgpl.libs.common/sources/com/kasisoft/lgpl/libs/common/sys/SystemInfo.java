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
public class SystemInfo {

  private static final String VAL_OS      = SystemProperty.OsName.getValue();
  
  private static final String OS_SUN      = "SunOS";
  private static final String OS_SOLARIS  = "Solaris";
  private static final String OS_LINUX    = "Linux";
  private static final String OS_WIN95    = "Windows 95";
  private static final String OS_WIN98    = "Windows 98";
  private static final String OS_WINME    = "Windows Me";
  private static final String OS_WINNT    = "Windows NT";
  private static final String OS_WIN2000  = "Windows 2000";
  private static final String OS_WINXP    = "Windows XP";
  private static final String OS_WINDOWS7 = "Windows 7";
  private static final String OS_AMIGA    = "Amiga OS";
  private static final String OS_AROS     = "AROS";
  private static final String OS_MACOSX   = "Mac OS X";
  private static final String OS_MORPHOS  = "MorphOS";
  
  /**
   * Prevent instantiation.
   */
  private SystemInfo() {
  }

  /**
   * Returns <code>true</code> in case we're working on a Mac system.
   * 
   * @return  <code>true</code> <=> We're working on a Mac system.
   */
  public static final boolean isMac() {
    return VAL_OS.equals( OS_MACOSX );
  }

  /**
   * Returns <code>true</code> in case we're working on a MorphOS system.
   * 
   * @return  <code>true</code> <=> We're working on a MorphOS system.
   */
  public static final boolean isMorphos() {
    return VAL_OS.equals( OS_MORPHOS );
  }
  
  /**
   * Returns <code>true</code> in case we're working on a Unix derivative.
   *
   * @return  <code>true</code> <=> We're working on a Unix derivative.
   */
  public static final boolean isUnix() {
    return isSolaris() || isLinux();
  }
  
  /**
   * Returns <code>true</code> on Solaris system.
   *
   * @return  <code>true</code> <=> We're working on a Solaris system.
   */
  public static final boolean isSolaris() {
    return VAL_OS.equals( OS_SUN ) || VAL_OS.equals( OS_SOLARIS );
  }
  
  /**
   * Returns <code>true</code> on Linux systems.
   *
   * @return  <code>true</code> <=> We're working on a Linux system.
   */
  public static final boolean isLinux() {
    return VAL_OS.equals( OS_LINUX );
  }
  
  /**
   * Returns <code>true</code> on Windows systems.
   *
   * @return  <code>true</code> <=> We're working on a Windows system.
   */
  public static final boolean isWindows() {
    return
     isWindows95   () || isWindows98   () || isWindowsMe   () || 
     isWindows2000 () || isWindowsNT   () || isWindowsXP   ();
  }
  
  /**
   * Returns <code>true</code> on Windows 95 systems.
   *
   * @return  <code>true</code> <=> We're working on a Windows 95 system.
   */
  public static final boolean isWindows95() {
    return VAL_OS.equals( OS_WIN95 );
  }
  
  /**
   * Returns <code>true</code> on Windows 98 systems.
   *
   * @return  <code>true</code> <=> We're working on a Windows 98 system.
   */
  public static final boolean isWindows98() {
    return VAL_OS.equals( OS_WIN98 );
  }
  
  /**
   * Returns <code>true</code> on Windows Me systems.
   *
   * @return  <code>true</code> <=> We're working on a Windows Me system.
   */
  public static final boolean isWindowsMe() {
    return VAL_OS.equals( OS_WINME );
  }
  
  /**
   * Returns <code>true</code> on Windows NT systems.
   *
   * @return  <code>true</code> <=> We're working on Windows NT systems.
   */
  public static final boolean isWindowsNT() {
    return VAL_OS.equals( OS_WINNT );
  }
  
  /**
   * Returns <code>true</code> on Windows 2000 systems.
   *
   * @return  <code>true</code> <=> We're working on Windows 2000 systems.
   */
  public static final boolean isWindows2000() {
    return VAL_OS.equals( OS_WIN2000 );
  }
  
  /**
   * Returns <code>true</code> on Windows XP systems.
   *
   * @return  <code>true</code> <=> We're working on Windows XP systems.
   */
  public static final boolean isWindowsXP() {
    return VAL_OS.equals( OS_WINXP );
  }
  
  /**
   * Returns <code>true</code> on Windows 7 systems.
   * 
   * @return   <code>true</code> <=> On Windows systems. 
   */
  public static final boolean isWindows7() {
    return VAL_OS.equals( OS_WINDOWS7 );
  }
  
  /**
   * Returns <code>true</code> on Amiga systems.
   *
   * @return  <code>true</code> <=> We're working on Amiga systems.
   */
  public static final boolean isAmigaOS() {
    return VAL_OS.equals( OS_AMIGA );
  }
  
  /**
   * Returns <code>true</code> on AROS systems.
   *
   * @return  <code>true</code> <=> We're working on AROS systems.
   */
  public static final boolean isAROS() {
    return VAL_OS.equals( OS_AROS );
  }
  
  /**
   * Returns <code>true</code> in case the operating system is amiga like.
   * 
   * @return   <code>true</code> <=> The operating system is amiga like.
   */
  public static final boolean isAmigaLike() {
    return isAmigaOS() || isAROS() || isMorphos();
  }
  
  /**
   * Returns the name of the OS.
   *
   * @return  The name of the OS
   */
  public static final String getOsName() {
    return VAL_OS;
  }
  
} /* ENDCLASS */
