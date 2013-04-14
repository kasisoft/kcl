/**
 * Name........: BucketTest
 * Description.: Test for the class 'Bucket'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import org.testng.annotations.Test;

import java.util.*;

import junit.framework.*;

/**
 * Test for the class 'Bucket'.
 */
@Test(groups="all")
public class BucketTest {
  
  @Test
  public void runAllocations() {
    
    ListBucket<String> bucket = new ListBucket<String>();

    Assert.assertEquals( 0, bucket.getSize() );
    
    List<String>       list1  = bucket.allocate();
    list1.add( "List-1" );

    List<String>       list2  = bucket.allocate();
    list2.add( "List-2" );

    List<String>       list3  = bucket.allocate();
    list3.add( "List-3" );

    bucket.free( list1 );
    Assert.assertEquals( 1, bucket.getSize() );
    
    List<String>       list4  = bucket.allocate();
    Assert.assertTrue( list4.isEmpty() );
    
  }
  
  private static class ListBucket<T> extends Bucket<List<T>> {

    /**
     * {@inheritDoc}
     */
    @Override
    public <P extends List<T>> P create() {
      return (P) new ArrayList<T>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <P extends List<T>> P reset( List<T> object ) {
      object.clear();
      return (P) object;
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */