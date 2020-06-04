package com.kasisoft.libs.common.comparator;

import javax.validation.constraints.NotNull;

/**
 * A comparator based upon the Class names of classes.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ClassNameComparator extends AbstractStringComparator<Class> {

  public ClassNameComparator() {
    super(false);
  }

  public ClassNameComparator(boolean ignoreCase) {
    super(ignoreCase);
  }
  
  @Override
  public int compare(@NotNull Class o1, @NotNull Class o2) {
    return compare(o1.getName(), o2.getName());
  }
  
} /* ENDCLASS */
