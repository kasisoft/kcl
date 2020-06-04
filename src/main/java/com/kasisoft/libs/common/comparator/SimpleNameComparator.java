package com.kasisoft.libs.common.comparator;

import javax.validation.constraints.NotNull;

/**
 * A comparator based upon the SimpleNames of classes.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class SimpleNameComparator extends AbstractStringComparator<Class> {

  public SimpleNameComparator() {
    super(false);
  }
  
  public SimpleNameComparator(boolean ignoreCase) {
    super(ignoreCase);
  }
  
  @Override
  public int compare(@NotNull Class o1, @NotNull Class o2) {
    return compare(o1.getSimpleName(), o2.getSimpleName());
  }
  
} /* ENDCLASS */
