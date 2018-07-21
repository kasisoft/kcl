package com.kasisoft.libs.common.thread;

import static com.kasisoft.libs.common.constants.Primitive.*;

import com.kasisoft.libs.common.base.*;

import lombok.experimental.*;

import lombok.*;

import java.io.*;

/**
 * A Runnable which is used to copy data from a Reader to a Writer.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CharCopierRunnable extends AbstractRunnable {

  Reader    source;
  Writer    destination;
  boolean   configured;
  
  char[]    buffer;
  boolean   owned;
  Integer   size;
  
  /**
   * Initialises this Runnable implementation using a locally allocated buffer of a default size.
   */
  public CharCopierRunnable() {
    this( (Integer) null );
  }

  /**
   * Initialises this Runnable implementation using the supplied block as a buffer.
   * 
   * @param allocated   The buffer used for copying purposes. If <code>null</code> buffers will be allocated locally.
   */
  public CharCopierRunnable( char[] allocated ) {
    reset();
    size      = null;
    owned     = allocated == null;
    buffer    = allocated;
  }

  /**
   * Initialises this Runnable implementation using a locally allocated buffer with the supplied size.
   * 
   * @param buffsize   The size to be used. If <code>null</code> a default size is being used.
   */
  public CharCopierRunnable( Integer buffsize ) {
    reset();
    size    = buffsize;
    owned   = true;
    buffer  = null;
  }

  /**
   * Initialises the current object state.
   */
  private void reset() {
    configured  = false;
    source      = null;
    destination = null;
  }
  
  /**
   * Configures this Runnable to copy some characters.
   * 
   * @param from   The Reader providing the content. Not <code>null</code>.
   * @param to     The Writer receiving this content. Not <code>null</code>.
   */
  public void configure( @NonNull Reader from, @NonNull Writer to ) {
    source      = from;
    destination = to;
    configured  = true;
  }
  
  @Override
  protected void execute() {
    
    if( ! configured ) {
      return;
    }
      
    try {
      
      if( owned ) {
        buffer  = PChar.allocate( size );
      }
      
      int read = source.read( buffer );
      while( (! isStopped()) && (read != -1) ) {
        
        if( read > 0 ) {
          destination.write( buffer, 0, read );
        }
        
        read = source.read( buffer );
        
      }
      
    } catch( IOException ex ) {
      handleIOFailure( ex );
    } finally {
      if( owned ) {
        PChar.release( buffer );
        buffer = null;
      }
      reset();
    }
    
  }
  
  /**
   * Provides behaviour for the occurrence of an IOException. Default behaviour is throwing a FailureException.
   * 
   * @param ex   The cause of the failure. Not <code>null</code>.
   */
  protected void handleIOFailure( @NonNull IOException ex ) {
    throw FailureCode.IO.newException( ex );
  }
  
} /* ENDCLASS */
