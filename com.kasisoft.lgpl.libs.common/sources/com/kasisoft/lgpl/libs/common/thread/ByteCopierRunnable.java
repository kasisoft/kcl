/**
 * Name........: ByteCopierRunnable
 * Description.: A Runnable which is used to copy data from an InputStream to an OutputStream. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.thread;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.libs.common.util.*;

import com.kasisoft.lgpl.libs.common.base.*;
import com.kasisoft.lgpl.tools.diagnostic.*;

import java.io.*;

/**
 * A Runnable which is used to copy data from an InputStream to an OutputStream. Be aware that this
 * is a general purpose implementation, so if you intend to copy files you should use 
 * the java.io.FileChannel instead in order to make use of system specific copying operations.
 */
@KDiagnostic
public class ByteCopierRunnable implements Runnable {

  private static Buffers<byte[]> buffers = null;
  
  private InputStream    source;
  private OutputStream   destination;
  private byte[]         buffer;
  private Integer        buffersize;
  
  /**
   * A Thread which copies content from one stream to another one.
   * 
   * @param from   The InputStream providing the content. Not <code>null</code>.
   * @param to     The OutputStream receiving this content. Not <code>null</code>. 
   */
  public ByteCopierRunnable( 
    @KNotNull(name="from")   InputStream    from, 
    @KNotNull(name="to")     OutputStream   to 
  ) {
    source      = from;
    destination = to;
    buffer      = null;
    buffersize  = CommonProperty.BufferCount.getValue();
  }
  
  /**
   * A Thread which copies content from one stream to another one.
   * 
   * @param from   The InputStream providing the content. Not <code>null</code>.
   * @param to     The OutputStream receiving this content. Not <code>null</code>. 
   * @param size   The size of the buffer. Not <code>null</code>.
   */
  public ByteCopierRunnable( 
    @KNotNull(name="from")     InputStream    from, 
    @KNotNull(name="to")       OutputStream   to,
    @KIPositive(name="size")   int            size 
  ) {
    source      = from;
    destination = to;
    buffer      = null;
    buffersize  = Integer.valueOf( size );
  }

  /**
   * A Thread which copies content from one stream to another one.
   * 
   * @param from   The InputStream providing the content. Not <code>null</code>.
   * @param to     The OutputStream receiving this content. Not <code>null</code>.
   * @param mem    The buffer to be used for the copying process. Not <code>null</code>.
   */
  public ByteCopierRunnable( 
    @KNotNull(name="from")   InputStream    from, 
    @KNotNull(name="to")     OutputStream   to, 
    @KNotNull(name="mem")    byte[]         mem 
  ) {
    source      = from;
    destination = to;
    buffer      = mem;
    buffersize  = null;
  }

  /**
   * Returns an instance of buffers used for this runnable.
   * 
   * @return   An instance of buffers used for this runnable. Not <code>null</code>.
   */
  private synchronized Buffers<byte[]> getBuffers() {
    if( buffers == null ) {
      buffers = Buffers.newBuffers( Primitive.PByte );
    }
    return buffers;
  }
  
  /**
   * {@inheritDoc}
   */
  public void run() {
    if( buffersize != null ) {
      buffer = getBuffers().allocate( buffersize );
    }
    try {
      int done = 0;
      int read = source.read( buffer );
      while( (! Thread.currentThread().isInterrupted()) && (read != -1) ) {
        if( read > 0 ) {
          destination.write( buffer, 0, read );
          done += read;
          onIteration( done, read );
        }
        read = source.read( buffer );
      }
    } catch( IOException ex ) {
      handleIOFailure( ex );
    } finally {
      if( buffersize != null ) {
        getBuffers().release( buffer );
      }
      buffer = null;
    }
  }

  /**
   * Will be invoked when some data has been written.
   * 
   * @param done      The complete number of bytes that have been written.
   * @param written   The number of bytes that have been written in this iteration.
   */
  protected void onIteration( int done, int written ) {
  }
  
  /**
   * Provides behaviour for the occurrence of an IOException. Default behaviour is throwing
   * an ExtendedRuntimeException.
   * 
   * @param ex   The cause of the failure.
   */
  protected void handleIOFailure( IOException ex ) {
    throw new FailureException( FailureCode.IO, ex );
  }
  
} /* ENDCLASS */
