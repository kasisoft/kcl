package com.kasisoft.libs.common.util;

import org.testng.annotations.Test;

import org.testng.*;

import java.util.*;

/**
 * Test for the class 'Bucket'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class BucketTest {
  
  @Test(groups="all")
  public void runAllocations() {
    
    Bucket<List<String>> bucket = new Bucket<>( new ListBucketFactory<String>() );

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
  
  private static class ListBucketFactory<T> implements BucketFactory<List<T>> {

    @Override
    public <P extends List<T>> P create() {
      return (P) new ArrayList<>();
    }

    @Override
    public <P extends List<T>> P reset( List<T> object ) {
      object.clear();
      return (P) object;
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */