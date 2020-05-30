package com.kasisoft.libs.common.old.internal.text;

import lombok.experimental.*;

import lombok.*;

import com.kasisoft.libs.common.old.internal.*;
import com.kasisoft.libs.common.old.text.*;

import java.util.*;

/**
 * Collection of facades that can be used for char sequence related operations.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CharSequenceFacades {

  private static final Map<String, CharSequenceFacade>      FACADES;
  private static final Map<String, TextProcessingFactory>   FACTORIES;
  
  static {
    FACADES = new HashMap<>();
    FACADES.put( StringBuffer   . class . getName(), new StringBufferFacade   () );
    FACADES.put( StringBuilder  . class . getName(), new StringBuilderFacade  () );
    FACADES.put( String         . class . getName(), new StringFacade         () );
    FACADES.put( StringFBuffer  . class . getName(), new StringFBufferFacade  () );
    FACADES.put( StringFBuilder . class . getName(), new StringFBuilderFacade () );
    FACTORIES = new HashMap<>();
    FACTORIES.put( StringBuffer   . class . getName(), new TextProcessingFactoryImpl<>( StringBuffer   . class ) );
    FACTORIES.put( StringBuilder  . class . getName(), new TextProcessingFactoryImpl<>( StringBuilder  . class ) );
    FACTORIES.put( String         . class . getName(), new TextProcessingFactoryImpl<>( String         . class ) );
    FACTORIES.put( StringFBuffer  . class . getName(), new TextProcessingFactoryImpl<>( StringFBuffer  . class ) );
    FACTORIES.put( StringFBuilder . class . getName(), new TextProcessingFactoryImpl<>( StringFBuilder . class ) );
  }
  
  public static <T extends CharSequence> CharSequenceFacade<T> getFacade( @NonNull T input ) {
    return (CharSequenceFacade<T>) getFacade( input.getClass() );
  }

  public static <T extends CharSequence> CharSequenceFacade<T> getFacade( @NonNull Class<T> inputtype ) {
    CharSequenceFacade<T> result = FACADES.get( inputtype.getName() );
    if( result == null ) {
      throw new IllegalArgumentException( Messages.unsupported_charsequence.format( inputtype.getName() ) );
    }
    return result;
  }

  public static <T extends CharSequence> TextProcessingFactory<T> getTextProcessingFactory( @NonNull T input ) {
    return (TextProcessingFactory<T>) getTextProcessingFactory( input.getClass() );
  }

  public static <T extends CharSequence> TextProcessingFactory<T> getTextProcessingFactory( @NonNull Class<T> inputtype ) {
    TextProcessingFactory<T> result = FACTORIES.get( inputtype.getName() );
    if( result == null ) {
      throw new IllegalArgumentException( Messages.unsupported_charsequence.format( inputtype.getName() ) );
    }
    return result;
  }

} /* ENDCLASS */
