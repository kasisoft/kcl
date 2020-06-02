package com.kasisoft.libs.common.old.xml.adapters;

import com.kasisoft.libs.common.text.StringFunctions;

import java.util.function.BiConsumer;

import java.io.File;

/**
 * An adapter for file pathes.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PathAdapter extends ListAdapter<File> {

  /**
   * Initializes this adapter with a custom error handler.
   * 
   * @param handler     The handler that has to be used. Maybe <code>null</code>.
   * @param delim       The delimiter for the path. Maybe <code>null</code>.
   * @param canonical   <code>true</code> <=> Deliver canonical pathes only.
   */
  public PathAdapter( BiConsumer<Object,Exception> handler, String delim, boolean canonical ) {
    super( handler, getDelimiter( delim ), new FileAdapter( canonical ) );
  }

  /**
   * Initializes this adapter with a custom error handler.
   * 
   * @param handler     The handler that has to be used. Maybe <code>null</code>.
   * @param canonical   <code>true</code> <=> Deliver canonical pathes only.
   */
  public PathAdapter( BiConsumer<Object,Exception> handler, boolean canonical ) {
    super( handler, getDelimiter( null ), new FileAdapter( canonical ) );
  }

  /**
   * Initializes this adapter.
   * 
   * @param delim       The delimiter for the path. Maybe <code>null</code>.
   * @param canonical   <code>true</code> <=> Deliver canonical pathes only.
   */
  public PathAdapter( String delim, boolean canonical ) {
    super( getDelimiter( delim ), new FileAdapter( canonical ) );
  }

  /**
   * Initializes this adapter.
   * 
   * @param canonical   <code>true</code> <=> Deliver canonical pathes only.
   */
  public PathAdapter( boolean canonical ) {
    super( getDelimiter( null ), new FileAdapter( canonical ) );
  }

  /**
   * Returns the delimiter that has to be used with this adapter.
   * 
   * @param delim   The delimiter for this adapter. If <code>null</code> {@link File#pathSeparator} will be used.
   * 
   * @return   The delimiter for this adapter. Neither <code>null</code> nor empty.
   */
  private static String getDelimiter( String delim ) {
    if( StringFunctions.cleanup( delim ) == null ) {
      delim = File.pathSeparator;
    }
    return delim;
  }

} /* ENDCLASS */
