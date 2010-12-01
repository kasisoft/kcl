/**
 * Name........: NetFunctions
 * Description.: Collection of net related helper functions.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.net;

import com.kasisoft.lgpl.libs.common.constants.*;

import com.kasisoft.lgpl.libs.common.io.*;

import com.kasisoft.lgpl.libs.common.base.*;
import com.kasisoft.lgpl.tools.diagnostic.*;

import java.net.*;

import java.io.*;

/**
 * Collection of net related helper functions.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class NetFunctions {

  /**
   * Suppress instantiation.
   */
  private NetFunctions() {
  }
  
  /**
   * Launches a FailureException if it's been desired.
   * 
   * @param fail   <code>true</code> <=> Launch the FailureException.
   * @param ex     The causing exception. Not <code>null</code>. 
   */
  private static final void causeException( boolean fail, IOException ex ) {
    if( fail ) {
      throw new FailureException( FailureCode.IO, ex );
    }
  }
  
  /**
   * Closes the supplied Socket.
   * 
   * @param fail     <code>true</code> <=> Raise an ExtendedRuntimeException in case closing
   *                                       the socket caused an Exception.
   * @param socket   The Socket which has to be closed. Maybe <code>null</code>.
   */
  public static final void close( boolean fail, Socket socket ) {
    if( socket != null ) {
      try {
        socket.close();
      } catch( IOException ex ) {
        causeException( fail, ex );
      }
    }
  }

  /**
   * Closes the supplied Socket.
   * 
   * @param socket   The Socket which has to be closed. Maybe <code>null</code>.
   * @param fail     <code>true</code> <=> Raise an ExtendedRuntimeException in case closing
   *                                       the socket caused an Exception.
   */
  public static final void close( boolean fail, ServerSocket socket ) {
    if( socket != null ) {
      try {
        socket.close();
      } catch( IOException ex ) {
        causeException( fail, ex );
      }
    }
  }

  /**
   * Closes the supplied Socket.
   * 
   * @param socket   The Socket which has to be closed. Maybe <code>null</code>.
   */
  public static final void close( Socket socket ) {
    close( false, socket );
  }

  /**
   * Closes the supplied Socket.
   * 
   * @param socket    The Socket which has to be closed. Maybe <code>null</code>.
   */
  public static final void close( ServerSocket socket ) {
    close( false, socket );
  }
  
  /**
   * Blocks the current Thread while waiting for a message to be transferred.
   * 
   * @param port   The port to listen to.
   * 
   * @return   The message that has been transferred. <code>null</code> means that an
   *           exception has been raised.
   */
  public static final byte[] waitForMessage( int port ) {
    return waitForMessage( port, null );
  }
  
  /**
   * Blocks the current Thread while waiting for a message to be transferred.
   * 
   * @param port      The port to listen to.
   * @param timeout   An optional timeout value. Maybe <code>null</code>.
   * 
   * @return   The message that has been transferred. <code>null</code> means that an
   *           exception has been raised.
   */
  public static final byte[] waitForMessage( int port, Integer timeout ) {
    ServerSocket socket = null;
    Socket       client = null;
    try {
      socket = new ServerSocket( port );
      if( timeout != null ) {
        socket.setSoTimeout( timeout.intValue() );
      }
      client = socket.accept();
      return IoFunctions.loadBytes( client.getInputStream(), null );
    } catch( IOException ex ) {
      return null;
    } finally {
      close( client );
      close( socket );
    }
  }

  /**
   * Simple function which sends a message to a specific address.
   * 
   * @param address   The address to send the message to. Neither <code>null</code> nor empty.
   * @param port      The port used to connect to. Must be a positive value.
   * @param message   The message to send. Maybe <code>null</code> or empty.
   * 
   * @return   <code>true</code> <=> The message could be sent successfully.
   */
  public static final boolean sendMessage( String address, int port, byte[] message ) {
    return sendMessage( address, port, message, null );
  }

  /**
   * Simple function which sends a message to a specific address.
   * 
   * @param address   The address to send the message to. Neither <code>null</code> nor empty.
   * @param port      The port used to connect to. Must be a positive value.
   * @param message   The message to send. Maybe <code>null</code> or empty.
   * @param timeout   The timeout in milliseconds until the message had to be sent. Maybe <code>null</code>.
   * 
   * @return   <code>true</code> <=> The message could be sent successfully.
   */
  public static final boolean sendMessage( 
    @KNotEmpty(name="address")   String    address, 
    @KIPositive(name="port")     int       port, 
                                 byte[]    message, 
                                 Integer   timeout 
  ) {
    Socket  socket = null;
    try {
      socket = new Socket( address, port );
      if( timeout != null ) {
        socket.setSoTimeout( timeout.intValue() );
      }
      if( (message != null) && (message.length > 0) ) {
        socket.getOutputStream().write( message );
      }
      return true;
    } catch( UnknownHostException ex ) {
      System.err.println( "DODO" );
      ex.printStackTrace();
      return false;
    } catch( IOException          ex ) {
      System.err.println( "DODO" );
      ex.printStackTrace();
      return false;
    } finally {
      close( socket );
    }
  }

} /* ENDCLASS */
