/**
 * Name........: SystemProcess
 * Description.: Convenience class for the Runtime.exec method.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.sys;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.thread.*;
import com.kasisoft.libs.common.util.*;

import java.util.*;

import java.io.*;

import lombok.*;

/**
 * Convenience class for the Runtime.exec method.
 */
public class SystemProcess {

  private OutputStream         outstream;
  private OutputStream         errstream;
  private Exception            exception;
  private File                 executable;
  private File                 workingdir;
  private boolean              environment;
  private Map<String,String>   variables;
  private int                  returncode;
  
  /**
   * Sets up this convenience class to use the supplied executable for the creation of a system process.
   * 
   * @param exec   The executable to use for the creation of a system process. Must be a valid file.
   */
  public SystemProcess( @NonNull File exec ) {
    exception   = null;
    outstream   = null;
    errstream   = null;
    environment = true;
    executable  = exec;
    workingdir  = null;
    returncode  = 0;
    variables   = new Hashtable<String,String>();
  }

  /**
   * Adds the supplied variable to this process so it get's passed to the executed command.
   * 
   * @param key     The name of the variable. Neither <code>null</code> nor empty.
   * @param value   The value of the variable. Not <code>null</code>.
   */
  public synchronized void addVariable( @NonNull String key, @NonNull String value ) {
    variables.put( key, value );
  }
  
  /**
   * Removes the supplied variable, so it won't be passed to the executed command anymore.
   * 
   * @param key   The name of the variable which has to be removed. 
   *              Neither <code>null</code> nor empty.
   */
  public synchronized void removeVariable( @NonNull String key ) {
    variables.remove( key );
  }
  
  /**
   * Marks the environment as inheritable (default: <code>true</code>) for the subprocess.
   * 
   * @param useenvironment   <code>true</code> <=> The current environment will be inherited by the subprocess.
   */
  public synchronized void setInheritEnvironment( boolean useenvironment ) {
    environment = useenvironment;
  }
  
  /**
   * Returns <code>true</code> if the subprocess inherits the environment.
   * 
   * @return   <code>true</code> <=> The subprocess inherits the environment.
   */
  public boolean isInheritEnvironment() {
    return environment;
  }

  /**
   * Changes the working directory for the subprocess.
   * 
   * @param newworkingdir   The new working directory for the subprocess. Maybe <code>null</code>
   */
  public synchronized void setWorkingDir( File newworkingdir ) {
    workingdir = newworkingdir;
  }

  /**
   * Returns the working directory used for the subprocess. Maybe <code>null</code>.
   * 
   * @return   The working directory used for the subprocess. Maybe <code>null</code>.
   */
  public File getWorkingDir() {
    return workingdir;
  }
  
  /**
   * Returns the executable used to run the system process.
   * 
   * @return   The executable used to run the system process.
   */
  public File getExecutable() {
    return executable;
  }

  /**
   * Changes the OutputStream used to delegate the output to. The default is <code>System.out</code>.
   * 
   * @param output   The OutputStream used to delegate the output to. Maybe <code>null</code>.
   */
  public synchronized void setOutputStream( OutputStream output ) {
    outstream   = output;
  }

  /**
   * Returns the OutputStream used to delegate the output to.
   * 
   * @return   The OutputStream used to delegate the output to. Maybe <code>null</code>.
   */
  public OutputStream getOutputStream() {
    return outstream;
  }

  /**
   * Changes the error stream used to delegate the output to. The default is <code>System.err</code>.
   * 
   * @param output   The error stream used to delegate the output to. Maybe <code>null</code>.
   */
  public synchronized void setErrorStream( OutputStream output ) {
    errstream   = output;
  }

  /**
   * Returns the error stream used to delegate the output to.
   * 
   * @return   The error stream used to delegate the output to. Maybe <code>null</code>.
   */
  public OutputStream getErrorStream() {
    return errstream;
  }

  /**
   * Returns the Exception that came up while the process has been executed.
   * 
   * @return   The Exception that came up while the process has been executed.
   */
  public Exception getException() {
    return exception;
  }

  /**
   * Creates a list of environment variables depending on the current settings.
   * 
   * @return   A list of environment variables depending on the current settings. Maybe <code>null</code>.
   */
  private String[] createEnvironment() {
    String[] result = null;
    if( environment ) {
      if( ! variables.isEmpty() ) {
        // if we don't have an extension we can use the value 'null' to indicate that
        // the current environment has to be inherited. in the other case we must create
        // a copy of the current environment and modify it with the extending variables.
        result = extendProperties();
      }
    } else {
      result = getExtendingEnvironment();
    }
    return result;
  }
  
  /**
   * Creates a command vector using the supplied arguments.
   * 
   * @param args   A list of arguments to be used for the command execution. Maybe <code>null</code>.
   *          
   * @return   A command vector providing the executable and all arguments. Not <code>null</code>.
   */
  private String[] createCommandVector( String ... args ) {
    String[] result = null;
    if( (args != null) && (args.length > 0) ) {
      result    = new String[ args.length + 1 ];
      System.arraycopy( args, 0, result, 1, args.length );
      result[0] = executable.getAbsolutePath();
    } else {
      result    = new String[] { executable.getAbsolutePath() };
    }
    return result;
  }
  
  /**
   * Runs the system process with the supplied args.
   * 
   * @param args   The arguments used to pass to the system process. If any of these args contains a space or something 
   *               like that it has to be quoted before. Not <code>null</code>.
   *               
   * @return  The exitcode from the subprocess or a failure code.
   */
  public synchronized FailureCode execute( @NonNull String ... args ) {

    FailureCode result    = FailureCode.Success;
    Thread      outcopier = null;
    Thread      errcopier = null;
    exception             = null;
    returncode            = 0;
    
    try {

      Process process = Runtime.getRuntime().exec( createCommandVector( args ), createEnvironment(), workingdir );

      OutputStream out = outstream != null ? outstream : System.out;
      OutputStream err = errstream != null ? errstream : System.err;
      
      ByteCopierRunnable outrunnable = new ByteCopierRunnable(); 
      ByteCopierRunnable errrunnable = new ByteCopierRunnable(); 
      outrunnable.configure( process.getInputStream(), out );
      errrunnable.configure( process.getErrorStream(), err );
      
      outcopier  = new Thread( outrunnable );
      errcopier  = new Thread( errrunnable );

      outcopier.start();
      errcopier.start();

      returncode = process.waitFor();

    } catch( IOException ex ) {
      exception = ex;
      result    = FailureCode.IO;
    } catch( InterruptedException ex ) {
      result    = FailureCode.Timeout;
    } finally {
      MiscFunctions.joinThread( outcopier );
      MiscFunctions.joinThread( errcopier );
    }

    return result;

  }
  
  /**
   * Returns the returncode supplied by the last command execution.
   * 
   * @return   The returncode supplied by the last command execution.
   */
  public int getReturncode() {
    return returncode;
  }
  
  /**
   * Returns an environment which only contains the variables added to this process.
   * 
   * @return   An environment which only contains the variables added to this process. Not <code>null</code>.
   */
  private String[] getExtendingEnvironment() {
    return toEnvp( variables );
  }

  /**
   * Returns an environment which is a merger of the current environment and the variables
   * added to this process.
   * 
   * @return   An environment which is a merger of the current environment and the variables added to this process.
   */
  private String[] extendProperties() {
    Map<String,String> map = new Hashtable<String,String>();
    map.putAll( System.getenv() );
    map.putAll( variables       );
    return toEnvp( map );
  }
  
  /**
   * Generates an array of Strings usable as an environment for the execution of a command.
   * 
   * @param map    A Map containing pairs of variable names and their values. Not <code>null</code>.
   * 
   * @return   An array of Strings usable as an environment for the execution of a command. Not <code>null</code>.
   */
  private String[] toEnvp( Map<String,String> map ) {
    String[]                           result   = new String[ map.size() ];
    Iterator<Map.Entry<String,String>> iterator = map.entrySet().iterator();
    int                                i        = 0;
    while( iterator.hasNext() ) {
      Map.Entry<String,String> pair = iterator.next();
      result[i]                     = String.format( "%s=%s", pair.getKey(), pair.getValue() );
      i++;
    }
    return result;
  }
  
} /* ENDCLASS */