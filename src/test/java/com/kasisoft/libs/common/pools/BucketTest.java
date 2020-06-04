package com.kasisoft.libs.common.pools;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.text.StringFBuilder;

import org.testng.annotations.Test;

import java.util.List;

/**
 * Test for the class 'Bucket'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class BucketTest {
  
  @Test(groups="all")
  public void arrayList() {
    
    Bucket<List<String>> bucket = Buckets.bucketArrayList();

    assertThat( 0, is( bucket.getSize() ) );
    
    List<String> obj1  = bucket.allocate();
    obj1.add( "Data-1" );

    List<String> obj2  = bucket.allocate();
    obj2.add( "Data-2" );

    List<String> obj3  = bucket.allocate();
    obj3.add( "Data-3" );

    bucket.free( obj1 );
    assertThat( 1, is( bucket.getSize() ) );
    
    List<String> obj4  = bucket.allocate();
    assertTrue( obj4.isEmpty() );
    
  }

  @Test(groups="all")
  public void stringFBuilder() {
    
    Bucket<StringFBuilder> bucket = Buckets.bucketStringFBuilder();

    assertThat( 0, is( bucket.getSize() ) );
    
    StringFBuilder obj1  = bucket.allocate();
    obj1.append( "Data-1" );

    StringFBuilder obj2  = bucket.allocate();
    obj2.append( "Data-2" );

    StringFBuilder obj3  = bucket.allocate();
    obj3.append( "Data-3" );

    bucket.free( obj1 );
    assertThat( 1, is( bucket.getSize() ) );
    
    StringFBuilder obj4  = bucket.allocate();
    assertThat( obj4.length(), is(0) );
    
  }

} /* ENDCLASS */