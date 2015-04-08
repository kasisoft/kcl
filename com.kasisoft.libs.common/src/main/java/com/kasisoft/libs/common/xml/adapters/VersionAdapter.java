package com.kasisoft.libs.common.xml.adapters;

import com.kasisoft.libs.common.util.*;

import lombok.*;
import lombok.experimental.*;

/**
 * An adapter used to Version into Strings and vice versa.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VersionAdapter extends TypeAdapter<String,Version> {

  boolean   micro;
  boolean   qualifier;
  boolean   all;
  
  /**
   * Initializes this adapter which does NOT provide any kind of error information. Errors will only result in 
   * <code>null</code> values.
   */
  public VersionAdapter() {
    this( null, true, true );
  }

  /**
   * Initializes this adapter to make use of a customized error handling.
   * 
   * @param handler        A custom error handler. Maybe <code>null</code>.
   * @param hasmicro       <code>true</code> <=> The version is supposed to provide a micro number.
   * @param hasqualifier   <code>true</code> <=> The version is supposed to provide a qualifier number.
   */
  public VersionAdapter( SimpleErrorHandler handler, boolean hasmicro, boolean hasqualifier ) {
    this( handler, null, null, hasmicro, hasqualifier );
  }
  
  /**
   * Initializes this adapter with the supplied conversion options.
   * 
   * @param hasmicro      <code>true</code> <=> The version is supposed to provide a micro number.
   * @param hasqualifier  <code>true</code> <=> The version is supposed to provide a qualifier number.
   */
  public VersionAdapter( boolean hasmicro, boolean hasqualifier ) {
    this( null, null, null, hasmicro, hasqualifier );
  }
  
  /**
   * Initializes this adapter to make use of a customized error handling.
   * 
   * @param handler       A custom error handler. Maybe <code>null</code>.
   * @param defval1       A default value for the source type. Maybe <code>null</code>.
   * @param defval2       A default value for the target type. Maybe <code>null</code>.
   * @param hasmicro      <code>true</code> <=> The version is supposed to provide a micro number.
   * @param hasqualifier  <code>true</code> <=> The version is supposed to provide a qualifier number.
   */
  public VersionAdapter( SimpleErrorHandler handler, String defval1, Version defval2, boolean hasmicro, boolean hasqualifier ) {
    super( handler, defval1, defval2 );
    micro     = hasmicro;
    qualifier = hasqualifier;
  }

  /**
   * Initializes this which accepts multiple version formats.
   * 
   * @param matchall   <code>true</code> <=> Try to match everything.
   */
  public VersionAdapter( boolean matchall ) {
    this( null, null, null, matchall );
  }
  
  /**
   * Initializes this adapter to make use of a customized error handling.
   * 
   * @param handler    A custom error handler. Maybe <code>null</code>.
   * @param matchall   <code>true</code> <=> Try to match everything.
   */
  public VersionAdapter( SimpleErrorHandler handler, boolean matchall ) {
    this( handler, null, null, matchall );
  }

  /**
   * Initializes this adapter to make use of a customized error handling.
   * 
   * @param defval1    A default value for the source type. Maybe <code>null</code>.
   * @param defval2    A default value for the target type. Maybe <code>null</code>.
   * @param matchall   <code>true</code> <=> Try to match everything.
   */
  public VersionAdapter( String defval1, Version defval2, boolean matchall ) {
    this( null, defval1, defval2, matchall );
  }
  
  /**
   * Initializes this adapter to make use of a customized error handling.
   * 
   * @param handler    A custom error handler. Maybe <code>null</code>.
   * @param defval1    A default value for the source type. Maybe <code>null</code>.
   * @param defval2    A default value for the target type. Maybe <code>null</code>.
   * @param matchall   <code>true</code> <=> Try to match everything.
   */
  public VersionAdapter( SimpleErrorHandler handler, String defval1, Version defval2, boolean matchall ) {
    super( handler, defval1, defval2 );
    micro     = false;
    qualifier = false;
    all       = matchall;
  }

  @Override
  public String marshalImpl( @NonNull Version v ) {
    return String.valueOf(v);
  }

  @Override
  public Version unmarshalImpl( @NonNull String v ) throws Exception {
    if( all ) {
      return new Version( v );
    } else {
      return new Version( v, micro, qualifier );
    }
  }

} /* ENDCLASS */
