package com.kasisoft.libs.common.comparator;

import com.kasisoft.libs.common.annotation.Prio;

import javax.validation.constraints.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * A comparator for prioritized types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrioComparator implements Comparator<Class> {

  Map<String, Integer>   prios = new HashMap<>();
  
  @Override
  public int compare(@NotNull Class o1, @NotNull Class o2) {
    int prio1 = getPrio(o1);
    int prio2 = getPrio(o2);
    return Integer.compare(prio2, prio1);
  }
  
  private int getPrio(Class clazz) {
    var result = 0;
    var key    = clazz.getName();
    if (prios.containsKey(key)) {
      result = prios.get(key);
    } else {
      var prio = (Prio) clazz.getAnnotation(Prio.class);
      if (prio != null) {
        result = prio.value();
      }
      prios.put(key, result);
    }
    return result;
  }
  
} /* ENDCLASS */
