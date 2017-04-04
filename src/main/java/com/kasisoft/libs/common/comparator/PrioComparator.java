package com.kasisoft.libs.common.comparator;

import com.kasisoft.libs.common.annotation.*;

import java.util.*;

import lombok.experimental.*;

import lombok.*;

/**
 * A comparator for prioritized types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrioComparator implements Comparator<Class> {

  Map<String, Integer>   prios = new HashMap<>();
  
  @Override
  public int compare( Class o1, Class o2 ) {
    int prio1 = getPrio( o1 );
    int prio2 = getPrio( o2 );
    return Integer.compare( prio2, prio1 );
  }
  
  private int getPrio( Class clazz ) {
    int     result = 0;
    String  key    = clazz.getName();
    if( prios.containsKey( key ) ) {
      result = prios.get( key );
    } else {
      Prio prio = (Prio) clazz.getAnnotation( Prio.class );
      if( prio != null ) {
        result    = prio.value();
      }
      prios.put( key, result );
    }
    return result;
  }
  
} /* ENDCLASS */
