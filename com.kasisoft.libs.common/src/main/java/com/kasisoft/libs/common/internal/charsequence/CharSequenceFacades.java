package com.kasisoft.libs.common.internal.charsequence;

import com.kasisoft.libs.common.internal.*;

import com.kasisoft.libs.common.util.*;

import lombok.experimental.*;

import lombok.*;

import java.util.*;

/**
 * Collection of facades that can be used for char sequence related operations.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CharSequenceFacades {

  static final Map<String,CharSequenceFacade>   FACADES;
  
  static {
    FACADES = new Hashtable<>();
    FACADES.put( StringBuffer   . class . getName(), new StringBufferFacade   () );
    FACADES.put( StringBuilder  . class . getName(), new StringBuilderFacade  () );
    FACADES.put( String         . class . getName(), new StringFacade         () );
    FACADES.put( StringFBuffer  . class . getName(), new StringFBufferFacade  () );
    FACADES.put( StringFBuilder . class . getName(), new StringFBuilderFacade () );
  }
  
  public static <T extends CharSequence> CharSequenceFacade<T> getFacade( @NonNull T input ) {
    CharSequenceFacade<T> result = FACADES.get( input.getClass().getName() );
    if( result == null ) {
      throw new IllegalArgumentException( Messages.unsupported_charsequence.format( input.getClass().getName() ) );
    }
    return result;
  }

} /* ENDCLASS */
