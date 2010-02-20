/**
 * Name........: CharCopierRunnable
 * Description.: A Runnable which is used to copy data from a Reader to a Writer. 
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
 * A Runnable which is used to copy data from a Reader to a Writer.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class CharCopierRunnable extends AbstractRunnable {

  private static Buffers<char[]> buffers = null;
  
  private Reader    source;
  private Writer    destination;
  private char[]    buffer;
  private Integer   buffersize;

  /**
   * A Thread which copies content from a Reader to a Writer.
   * 
   * @param from   The InputStream providing the content. Not <code>null</code>.
   * @param to     The OutputStream receiving this content. Not <code>null</code>.
   */
  public CharCopierRunnable( 
    @KNotNull(name="from")   Reader   from, 
    @KNotNull(name="to")     Writer   to 
  ) {
    source      = from;
    destination = to;
    buffer      = null;
    buffersize  = CommonProperty.BufferCount.getValue();
  }
  
  /**
   * A Thread which copies content from a Reader to a Writer.
   * 
   * @param from   The InputStream providing the content. Not <code>null</code>.
   * @param to     The OutputStream receiving this content. Not <code>null</code>.
   * @param size   The size of the buffer. A value of <code>null</code> indicates to use the
   *               default size.
   */
  public CharCopierRunnable( 
    @KNotNull(name="from")     Reader   from, 
    @KNotNull(name="to")       Writer   to, 
    @KIPositive(name="size")   int      size 
  ) {
    source      = from;
    destination = to;
    buffer      = null;
    buffersize  = Integer.valueOf( size );
  }

  /**
   * A Thread which copies content from a Reader to a Writer.
   * 
   * @param from   The InputStream providing the content. Not <code>null</code>.
   * @param to     The OutputStream receiving this content. Not <code>null</code>. 
   * @param mem    The buffer to be used for the copying process. Not <code>null</code>.
   */
  public CharCopierRunnable( 
    @KNotNull(name="from")   Reader   from, 
    @KNotNull(name="to")     Writer   to, 
    @KNotNull(name="mem")    char[]   mem 
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
  private synchronized Buffers<char[]> getBuffers() {
    if( buffers == null ) {
      buffers = Buffers.newBuffers( Primitive.PChar );
    }
    return buffers;
  }
  
  /**
   * {@inheritDoc}
   */
  protected void execute() {
    if( buffersize != null ) {
      buffer = getBuffers().allocate( buffersize );
    }
    try {
      int done = 0;
      int read = source.read( buffer );
      while( (! isStopped()) && (read != -1) ) {
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
   * @param done      The complete number of chars that have been written.
   * @param written   The number of chars that have been written in this iteration.
   */
  protected void onIteration( int done, int written ) {
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
