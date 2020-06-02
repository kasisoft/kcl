package com.kasisoft.libs.common.old.net;

import com.kasisoft.libs.common.old.constants.Digest;
import com.kasisoft.libs.common.old.io.IoFunctions;
import com.kasisoft.libs.common.text.StringFunctions;

import java.net.ServerSocket;
import java.net.Socket;

import java.io.IOException;

import lombok.NonNull;

/**
 * Collection of net related helper functions.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class NetFunctions {

  /**
   * Suppress instantiation.
   */
  private NetFunctions() {
  }
  
  /**
   * Blocks the current Thread while waiting for a message to be transferred.
   * 
   * @param port   The port to listen to.
   * 
   * @return   The message that has been transferred. <code>null</code> means that an exception has been raised.
   */
  public static byte[] waitForMessage( int port ) {
    return waitForMessage( port, null );
  }
  
  /**
   * Blocks the current Thread while waiting for a message to be transferred.
   * 
   * @param port      The port to listen to.
   * @param timeout   An optional timeout value. Maybe <code>null</code>.
   * 
   * @return   The message that has been transferred. <code>null</code> means that an exception has been raised.
   */
  public static byte[] waitForMessage( int port, Integer timeout ) {
    try( ServerSocket socket = new ServerSocket( port ) ) {
      if( timeout != null ) {
        socket.setSoTimeout( timeout.intValue() );
      }
      try( Socket client = socket.accept() ) {
        return IoFunctions.loadBytes( client.getInputStream(), null );
      }
    } catch( IOException ex ) {
      return null;
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
  public static boolean sendMessage( @NonNull String address, int port, byte[] message ) {
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
  public static boolean sendMessage( @NonNull String address, int port, byte[] message, Integer timeout ) {
    try( Socket socket = new Socket( address, port ) ) {
      if( timeout != null ) {
        socket.setSoTimeout( timeout.intValue() );
      }
      if( (message != null) && (message.length > 0) ) {
        socket.getOutputStream().write( message );
      }
      return true;
    } catch( IOException ex ) {
      return false;
    }
  }
  
  public static String getGravatarLink( String email, Integer size ) {
    // @spec [25-11-2017:KASI] https://en.gravatar.com/site/implement/hash/ 
    String result = null;
    if( email != null ) {
      result = StringFunctions.cleanup( email.toLowerCase() );
      result = Digest.MD5.digestToString( result.getBytes() );
      result = String.format( "https://www.gravatar.com/avatar/%s", result );
      if( size != null ) {
        result += String.format( "?s=%d", size );
      }
    }
    return result;

  }

} /* ENDCLASS */