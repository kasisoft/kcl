package com.kasisoft.libs.common.thread;

import com.kasisoft.libs.common.base.*;

import java.util.*;

import java.io.*;

import lombok.*;
import lombok.experimental.*;

/**
 * A Runnable implementation which is used to load text lines from a Reader.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LineReaderRunnable extends AbstractRunnable {

  BufferedReader            reader;
  List<String>              destination;
  boolean                   configured;
  
  @Getter @Setter boolean   trim;
  @Getter @Setter boolean   emptyLines;
  
  /**
   * Initialises this Runnable aimed to copy the content from a Reader into a list.
   */
  public LineReaderRunnable() {
    trim        = false;
    emptyLines  = true;
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
  public LineReaderRunnable( @NonNull Reader input, @NonNull List<String> receiver ) {
    this();
    configure( input, receiver );
  }
  
  /**
   * Initialises the object state. This only affects the parameters which can be set using 
   * {@link #configure(Reader, List)}.
   */
  private void reset() {
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
  public void configure( @NonNull Reader input, @NonNull List<String> receiver ) {
    reader        = new BufferedReader( input );
    destination   = receiver;
    configured    = true;
  }
  
  @Override
  public void execute() {
    if( configured ) {
      try {
        
        String line     = reader.readLine();
        while( line != null ) {
          
          if( trim ) {
            line = line.trim();
          }
          
          if( (line.length() > 0) || emptyLines ) {
            destination.add( line );
          }
          
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
  protected void handleIOFailure( @NonNull IOException ex ) {
    throw FailureException.newFailureException( FailureCode.IO, ex );
  }

} /* ENDCLASS */
