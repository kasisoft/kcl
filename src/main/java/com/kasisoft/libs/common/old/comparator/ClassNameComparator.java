package com.kasisoft.libs.common.old.comparator;

/**
 * A comparator based upon the Class names of classes.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ClassNameComparator extends AbstractStringComparator<Class> {

  public ClassNameComparator() {
    super( false );
  }

  public ClassNameComparator( boolean ignoreCase ) {
    super( ignoreCase );
  }
  
  @Override
  public int compare( Class o1, Class o2 ) {
    return compare( o1.getName(), o2.getName() );
  }
  
} /* ENDCLASS */
