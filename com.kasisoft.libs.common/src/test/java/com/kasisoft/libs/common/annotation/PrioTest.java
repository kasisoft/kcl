package com.kasisoft.libs.common.annotation;

import static org.testng.Assert.*;

import com.kasisoft.libs.common.comparator.*;

import org.testng.annotations.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class PrioTest {

  @Prio(10)
  public static class A {}

  @Prio(10)
  public static class a {}

  @Prio(10)
  public static class B {}

  @Prio(15)
  public static class C {}

  @Prio(4)
  public static class D {}

  @Prio
  public static class E {}

  @Test(groups = "all")
  public void sortPrioritized() {
    List<Class> classes = Arrays.asList( A.class, C.class, B.class, D.class, E.class );
    Collections.sort( classes, new PrioComparator() );
    assertTrue( C.class.isAssignableFrom( classes.get(0) ) );
    assertTrue( A.class.isAssignableFrom( classes.get(1) ) );
    assertTrue( B.class.isAssignableFrom( classes.get(2) ) );
    assertTrue( D.class.isAssignableFrom( classes.get(3) ) );
    assertTrue( E.class.isAssignableFrom( classes.get(4) ) );
  }
  
  @Test(groups = "all")
  public void sortPrioritizedWithSimpleName() {
    List<Class> classes = Arrays.asList( B.class, A.class, C.class, D.class, E.class );
    Collections.sort( classes, new PrioComparator().thenComparing( new SimpleNameComparator() ) );
    assertTrue( C.class.isAssignableFrom( classes.get(0) ) );
    assertTrue( A.class.isAssignableFrom( classes.get(1) ) );
    assertTrue( B.class.isAssignableFrom( classes.get(2) ) );
    assertTrue( D.class.isAssignableFrom( classes.get(3) ) );
    assertTrue( E.class.isAssignableFrom( classes.get(4) ) );
  }

  @Test(groups = "all")
  public void sortPrioritizedWithSimpleNameIgnoreCase() {
    List<Class> classes = Arrays.asList( B.class, A.class, C.class, a.class, D.class, E.class );
    Collections.sort( classes, new PrioComparator().thenComparing( new SimpleNameComparator( true ) ) );
    assertTrue( C.class.isAssignableFrom( classes.get(0) ) );
    assertTrue( A.class.isAssignableFrom( classes.get(1) ) );
    assertTrue( a.class.isAssignableFrom( classes.get(2) ) );
    assertTrue( B.class.isAssignableFrom( classes.get(3) ) );
    assertTrue( D.class.isAssignableFrom( classes.get(4) ) );
    assertTrue( E.class.isAssignableFrom( classes.get(5) ) );
  }

} /* ENDCLASS */
