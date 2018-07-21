package com.kasisoft.libs.common.thread;

import static com.kasisoft.libs.common.constants.Primitive.*;

import com.kasisoft.libs.common.base.*;

import java.io.*;

import lombok.experimental.*;

import lombok.*;

/**
 * A Runnable which is used to copy data from an InputStream to an OutputStream.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ByteCopierRunnable extends AbstractRunnable {

  InputStream     source;
  OutputStream    destination;
  boolean         configured;
  
  byte[]          buffer;
  boolean         owned;
  Integer         size;
  
  /**
   * Initialises this Runnable implementation using a locally allocated buffer of a default size.
   */
  public ByteCopierRunnable() {
    this( (Integer) null );
  }

  /**
   * Initialises this Runnable implementation using the supplied block as a buffer.
   * 
   * @param allocated   The buffer used for copying purposes. If <code>null</code> buffers will be allocated locally.
   */
  public ByteCopierRunnable( byte[] allocated ) {
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
  public ByteCopierRunnable( Integer buffsize ) {
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
   * Configures this Runnable to copy some bytes.
   * 
   * @param from   The InputStream providing the content. Not <code>null</code>.
   * @param to     The OutputStream receiving this content. Not <code>null</code>. 
   */
  public void configure( @NonNull InputStream from, @NonNull OutputStream to ) {
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
        buffer  = PByte.allocate( size );
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
        PByte.release( buffer );
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
