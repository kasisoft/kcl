package com.kasisoft.libs.common.internal.text;

import com.kasisoft.libs.common.internal.text.op.*;

import com.kasisoft.libs.common.model.*;
import com.kasisoft.libs.common.text.*;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

import java.util.*;

/**
 * Default implementation of a factory providing text operations. This implementation is based upon the use
 * of the {@link CharSequenceFacade} types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TextProcessingFactoryImpl<T extends CharSequence> implements TextProcessingFactory<T> {
  
  CharSequenceFacade   facade;
  
  public TextProcessingFactoryImpl( @NonNull Class<T> type ) {
    facade = CharSequenceFacades.getFacade( type );
  }

  @Override
  public Function<T, T> replace( String key, String value ) {
    return new KeyValueReplacer<>( facade, key, value );
  }

  @Override
  public Function<T, T> replace( Map<String, String> replacements ) {
    return new KeyValueByMapReplacer<>( facade, replacements );
  }

  @Override
  public Function<T, T> replace( List<Pair<String, String>> replacements ) {
    return new KeyValueByPairsReplacer<>( facade, replacements );
  }
  
} /* ENDCLASS */
