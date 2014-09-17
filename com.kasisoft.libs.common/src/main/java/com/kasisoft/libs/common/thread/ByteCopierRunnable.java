package com.kasisoft.libs.common.thread;

import com.kasisoft.libs.common.base.*;
import com.kasisoft.libs.common.constants.*;
import com.kasisoft.libs.common.util.*;

import java.io.*;

import lombok.*;

/**
 * A Runnable which is used to copy data from an InputStream to an OutputStream.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ByteCopierRunnable extends AbstractRunnable<CopyingProgress> {

  private InputStream       source;
  private OutputStream      destination;
  private boolean           configured;
  private CopyingProgress   progress;
  
  private byte[]            buffer;
  private boolean           owned;
  private Integer           size;
  
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
    if( progress == null ) {
      progress = new CopyingProgress();
    }
    progress.setDatatype( Primitive.PByte );
    progress.setTotal(0);
    progress.setCurrent(0);
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
  
  /**
   * Returns an instance of buffers used for this runnable.
   * 
   * @return   An instance of buffers used for this runnable. Not <code>null</code>.
   */
  private Buffers<byte[]> getBuffers() {
    return Primitive.PByte.getBuffers();
  }
  
  @Override
  protected void execute() {
    
    if( ! configured ) {
      return;
    }
      
    try {
      
      if( owned ) {
        buffer  = getBuffers().allocate( size );
      }
      
      progress.setTotal(-1);
      progress( progress );
      
      int read = source.read( buffer );
      while( (! isStopped()) && (read != -1) ) {
        
        if( read > 0 ) {
          
          destination.write( buffer, 0, read );
         
          // update the written amount
          progress.setCurrent( progress.getCurrent() + read );
          progress( progress );

        }
        
        read = source.read( buffer );
        
      }
      
    } catch( IOException ex ) {
      handleIOFailure( ex );
    } finally {
      if( owned ) {
        getBuffers().release( buffer );
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
    throw FailureException.newFailureException( FailureCode.IO, ex );
  }
  
} /* ENDCLASS */
