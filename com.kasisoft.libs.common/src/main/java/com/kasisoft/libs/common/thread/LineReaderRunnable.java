/**
 * Name........: LineReaderRunnable
 * Description.: A Runnable implementation which is used to load text lines from a Reader.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.thread;

import com.kasisoft.libs.common.base.*;

import java.util.*;

import java.io.*;

/**
 * A Runnable implementation which is used to load text lines from a Reader.
 */
public class LineReaderRunnable extends AbstractRunnable<LineReaderProgress> {

  private BufferedReader       reader;
  private List<String>         destination;
  private LineReaderProgress   progress;
  private boolean              configured;
  
  private boolean              trim;
  private boolean              emptylines;
  
  /**
   * Initialises this Runnable aimed to copy the content from a Reader into a list.
   */
  public LineReaderRunnable() {
    progress    = new LineReaderProgress();
    trim        = false;
    emptylines  = true;
    reset();
  }

  /**
   * Initialises this Runnable aimed to copy the content from a Reader into a list.
   * 
   * @param input      The Reader instance providing the text. Not <code>null</code>.
   * @param receiver   The list used to receive the content. Be aware that the interface does not require the method 
   *                   {@link List#add(Object)} to be implemented, so you need to pass an apropriate implementation type. 
   *                   Not <code>null</code>.
   */
  public LineReaderRunnable( Reader input, List<String> receiver ) {
    this();
    configure( input, receiver );
  }
  
  /**
   * Initialises the object state. This only affects the parameters which can be set using 
   * {@link #configure(Reader, List)}.
   */
  private void reset() {
    progress.setTotal(0);
    progress.setCurrent(0);
    reader      = null;
    destination = null;
    configured  = false;
  }
  
  /**
   * Configures this Runnable if this has not been done yet.
   * 
   * @param input      The Reader instance providing the text. Not <code>null</code>.
   * @param receiver   The list used to receive the content. Be aware that the interface does not require the method 
   *                   {@link List#add(Object)} to be implemented, so you need to pass an apropriate implementation type. 
   *                   Not <code>null</code>.
   */
  public void configure( Reader input, List<String> receiver ) {
    reader        = new BufferedReader( input );
    destination   = receiver;
    configured    = true;
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
  @Override
  public void execute() {
    if( configured ) {
      try {
        
        progress.setTotal(-1);
        progress( progress );
        
        String line     = reader.readLine();
        while( line != null ) {
          
          if( trim ) {
            line = line.trim();
          }
          
          if( (line.length() > 0) || emptylines ) {
            destination.add( line );
          }
          
          progress.setCurrent( progress.getCurrent() + 1 );
          progress( progress );
          
          line = reader.readLine();
        }
      } catch( IOException ex ) {
        handleIOFailure( ex );
      } finally {
        reset();
      }
    }
  }

  /**
   * Provides behaviour for the occurrence of an IOException. Default behaviour is throwing a FailureException.
   * 
   * @param ex   The cause of the failure.
   */
  protected void handleIOFailure( IOException ex ) {
    throw new FailureException( FailureCode.IO, ex );
  }

} /* ENDCLASS */
