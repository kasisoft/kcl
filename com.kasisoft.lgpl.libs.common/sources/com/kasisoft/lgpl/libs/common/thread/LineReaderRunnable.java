/**
 * Name........: LineReaderRunnable
 * Description.: A Runnable implementation which is used to load text lines from a Reader.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.thread;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.libs.common.base.*;
import com.kasisoft.lgpl.tools.diagnostic.*;

import java.util.*;

import java.io.*;

/**
 * A Runnable implementation which is used to load text lines from a Reader.
 */
public class LineReaderRunnable extends AbstractRunnable {

  private BufferedReader   reader;
  private List<String>     destination;
  
  private boolean          trim;
  private boolean          emptylines;
  
  /**
   * Initialises this Runnable aimed to copy the content from a Reader into a list.
   * 
   * @param input      The Reader instance providing the text. Not <code>null</code>.
   * @param receiver   The list used to receive the content. Be aware that the interface does not
   *                   require the method {@link List#add(Object)} to be implemented, so you need
   *                   to pass an apropriate implementation type. sNot <code>null</code>.
   */
  public LineReaderRunnable( 
    @KNotNull(name="input")      Reader         input, 
    @KNotNull(name="receiver")   List<String>   receiver 
  ) {
    reader      = new BufferedReader( input );
    destination = receiver;
    trim        = false;
    emptylines  = true;
  }
  
  /**
   * Enables/disables trimming of single lines.
   * 
   * @param enable   <code>true</code> <=> Lines shall be trimmed.
   */
  public void setTrim( boolean enable ) {
    trim = enable;
  }
  
  /**
   * Returns <code>true</code> if lines will be trimmed.
   * 
   * @return   <code>true</code> <=> Lines will be trimmed.
   */
  public boolean isTrim() {
    return trim;
  }
  
  /**
   * Returns <code>true</code> if empty lines shall be read, too.
   * 
   * @param enable   <code>true</code> <=> Empty lines shall be read, too.s
   */
  public void setEmptyLines( boolean enable ) {
    emptylines = enable;
  }
  
  /**
   * Returns <code>true</code> if empty lines shall be read.
   * 
   * @return   <code>true</code> <=> Empty lines shall be read.
   */
  public boolean isEmptyLines() {
    return emptylines;
  }
  
  /**
   * {@inheritDoc}
   */
  public void execute() {
    try {
      int    complete = 0;
      String line     = reader.readLine();
      while( line != null ) {
        if( trim ) {
          line = line.trim();
        }
        if( (line.length() > 0) || emptylines ) {
          destination.add( line );
        }
        complete++;
        onIteration( complete );
        line = reader.readLine();
      }
    } catch( IOException ex ) {
      handleIOFailure( ex );
    }
  }

  /**
   * Will be invoked when some data has been transferred.
   * 
   * @param complete   The current number of lines that have been processed.
   */
  protected void onIteration( int completes ) {
  }

  /**
   * Provides behaviour for the occurrence of an IOException. Default behaviour is throwing
   * a FailureException.
   * 
   * @param ex   The cause of the failure.
   */
  protected void handleIOFailure( IOException ex ) {
    throw new FailureException( FailureCode.IO, ex );
  }

} /* ENDCLASS */
