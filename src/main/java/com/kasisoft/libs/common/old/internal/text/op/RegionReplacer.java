package com.kasisoft.libs.common.old.internal.text.op;

import com.kasisoft.libs.common.old.internal.text.CharSequenceFacade;
import com.kasisoft.libs.common.old.model.Tupel;

import java.util.function.BiFunction;
import java.util.function.Function;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Replacements of certain regions.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegionReplacer<T extends CharSequence> implements Function<T, T> {

  CharSequence                       open;
  CharSequence                       close;
  CharSequence                       replacement;
  boolean                            nested;
  CharSequenceFacade<T>              facade;
  BiFunction<T, Tupel<Boolean>, T>   impl;

  public RegionReplacer( CharSequenceFacade<T> csfacade, CharSequence openVal, CharSequence closeVal, CharSequence replacementVal, boolean nestedVal ) {
    facade      = csfacade;
    open        = openVal;
    close       = closeVal;
    replacement = replacementVal;
    nested      = nestedVal;
    impl        = open.equals( close ) ? this::regionReplaceSimple : this::regionReplace;
  }
  
  @Override
  public T apply( T input ) {
    Tupel<Boolean> changes = new Tupel<>( true );
    T              result  = impl.apply( input, changes );
    while( nested && changes.getFirst() ) {
      result = impl.apply( result, changes );
    }
    return result;
  }

  private T regionReplace( T result, Tupel<Boolean> changes ) {
    changes.setValues( false );
    int last = -1;
    int idx  = indexOf( result, 0 );
    while( idx != -1 ) {
      boolean isopen = facade.containsAt( result, idx, open );
      if( isopen ) {
        last = idx;
      } else {
        if( last != -1 ) {
          result = facade.delete( result, last, idx + close.length() );
          result = facade.insert( result, last, replacement );
          changes.setValues( true );
          idx    = last + replacement.length();
          last   = -1;
        }
      }
      idx = indexOf( result, idx + 1 );
    }
    return result;
  }

  private T regionReplaceSimple( T result, Tupel<Boolean> changes ) {
    changes.setValues( false );
    int last = -1;
    int idx  = facade.indexOf( result, open, 0 );
    while( idx != -1 ) {
      if( last != -1 ) {
        result = facade.delete( result, last, idx + close.length() );
        result = facade.insert( result, last, replacement );
        changes.setValues( true );
        idx    = last + replacement.length();
        last   = -1;
      } else {
        last = idx;
      }
      idx = facade.indexOf( result, open, idx + 1 );
    }
    return result;
  }

  private int indexOf( T input, int pos ) {
    int openIdx  = facade.indexOf( input, open, pos );
    int closeIdx = facade.indexOf( input, close, pos );
    if( (openIdx == -1) && (closeIdx == -1) ) {
      return -1;
    } else if( (openIdx != -1) && (closeIdx != -1) ) {
      return Math.min( openIdx, closeIdx );
    } else if( openIdx != -1 ) {
      return openIdx;
    } else {
      return closeIdx;
    }
  }

} /* ENDCLASS */
