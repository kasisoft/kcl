package com.kasisoft.libs.common.internal.charsequence;

import com.kasisoft.libs.common.util.*;

import java.util.*;

import lombok.*;

/**
 * Collection of facades that can be used for char sequence related operations.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class CharSequenceFacades {

  private static final Map<String,CharSequenceFacade>   FACADES;
  
  static {
    FACADES = new Hashtable<String,CharSequenceFacade>();
    FACADES.put( StringBuffer   . class . getName(), new StringBufferFacade   () );
    FACADES.put( StringBuilder  . class . getName(), new StringBuilderFacade  () );
    FACADES.put( String         . class . getName(), new StringFacade         () );
    FACADES.put( StringFBuffer  . class . getName(), new StringFBufferFacade  () );
    FACADES.put( StringFBuilder . class . getName(), new StringFBuilderFacade () );
  }
  
  public static <T extends CharSequence> CharSequenceFacade<T> getFacade( @NonNull T input ) {
    CharSequenceFacade<T> result = FACADES.get( input.getClass().getName() );
    if( result == null ) {
      throw new IllegalArgumentException( String.format( "Unsupported CharSequence type '%s'", input.getClass().getName() ) );
    }
    return result;
  }

} /* ENDCLASS */
