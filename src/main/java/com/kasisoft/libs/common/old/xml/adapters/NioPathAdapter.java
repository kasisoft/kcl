package com.kasisoft.libs.common.old.xml.adapters;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.nio.file.*;

/**
 * Simple adapter for File types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NioPathAdapter extends TypeAdapter<String, Path> {

  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   */
  public NioPathAdapter() {
    this( null, null, null );
  }

  /**
   * Initializes this adpater to make use of a customized error handling.
   * 
   * @param handler          A custom error handler. Maybe <code>null</code>.
   * @param defval1          A default value for the source type. Maybe <code>null</code>.
   * @param defval2          A default value for the target type. Maybe <code>null</code>.
   */
  public NioPathAdapter( BiConsumer<Object, Exception> handler, String defval1, Path defval2 ) {
    super( handler, defval1, defval2 );
  }
  
  @Override
  public String marshalImpl( @NonNull Path v ) throws Exception {
    return v.normalize().toString().replace( '\\', '/' );
  }

  @Override
  public Path unmarshalImpl( @NonNull String v ) throws Exception {
    return Paths.get( v.replace( '\\', '/' ) ).normalize();
  }

} /* ENDCLASS */
