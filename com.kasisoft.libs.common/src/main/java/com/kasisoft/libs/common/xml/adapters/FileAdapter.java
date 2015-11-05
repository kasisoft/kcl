package com.kasisoft.libs.common.xml.adapters;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.io.*;

/**
 * Simple adapter for File types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileAdapter extends TypeAdapter<String,File> {

  boolean   canonical;
  
  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   */
  public FileAdapter() {
    this( null, null, null, false );
  }

  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   * 
   * @param canonicalfiles   <code>true</code> <=> Deliver canonical files only.
   */
  public FileAdapter( boolean canonicalfiles ) {
    this( null, null, null, canonicalfiles );
  }

  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler   A custom error handler. Maybe <code>null</code>.
   * @param defval1   A default value for the source type. Maybe <code>null</code>.
   * @param defval2   A default value for the target type. Maybe <code>null</code>.
   */
  public FileAdapter( BiConsumer<Object,Exception> handler, String defval1, File defval2 ) {
    this( handler, defval1, defval2, false );
  }

  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler          A custom error handler. Maybe <code>null</code>.
   * @param defval1          A default value for the source type. Maybe <code>null</code>.
   * @param defval2          A default value for the target type. Maybe <code>null</code>.
   * @param canonicalfiles   <code>true</code> <=> Deliver canonical files only.
   */
  public FileAdapter( BiConsumer<Object,Exception> handler, String defval1, File defval2, boolean canonicalfiles ) {
    super( handler, defval1, defval2 );
    canonical = canonicalfiles;
  }
  
  @Override
  public String marshalImpl( @NonNull File v ) throws Exception {
    if( canonical ) {
      v = v.getCanonicalFile();
    }
    return v.getPath().replace( '\\', '/' );
  }

  @Override
  public File unmarshalImpl( @NonNull String v ) throws Exception {
    File result = new File( v.replace( '\\', '/' ).replace( '/', File.separatorChar ) );
    if( canonical ) {
      result = result.getCanonicalFile();
    }
    return result;
  }

} /* ENDCLASS */
