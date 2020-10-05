package com.kasisoft.libs.common.pools;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

/**
 * Test for the class 'Bucket'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class BucketTest {
  
  @Test(groups = "all")
  public void arrayList() {
    
    var bucket = Buckets.bucketArrayList();
    bucket.reset();
    
    assertThat(0, is(bucket.getSize()));
    
    var obj1  = bucket.allocate();
    obj1.add("Data-1");

    var obj2  = bucket.allocate();
    obj2.add("Data-2");

    var obj3  = bucket.allocate();
    obj3.add("Data-3");

    bucket.free(obj1);
    assertThat(1, is(bucket.getSize()));
    
    var obj4  = bucket.allocate();
    assertTrue(obj4.isEmpty());
    
  }

  @Test(groups = "all")
  public void stringFBuilder() {
    
    var bucket = Buckets.bucketStringFBuilder();
    bucket.reset();
    
    assertThat(0, is(bucket.getSize()));
    
    var obj1 = bucket.allocate();
    obj1.append("Data-1");

    var obj2 = bucket.allocate();
    obj2.append("Data-2");

    var obj3 = bucket.allocate();
    obj3.append("Data-3");

    bucket.free(obj1);
    assertThat(1, is(bucket.getSize()));
    
    var obj4  = bucket.allocate();
    assertThat(obj4.length(), is(0));
    
  }

} /* ENDCLASS */