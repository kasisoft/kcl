package com.kasisoft.libs.common.util;

import org.testng.annotations.Test;

import org.testng.*;

import java.io.*;

/**
 * Test for the class 'DefaultBucketFactory'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class DefaultBucketFactoryTest {

  @Test(groups="all")
  public void runAllocations() throws Exception {
    
    Bucket<ByteArrayOutputStream> bucket = new Bucket<>( new DefaultBucketFactory<ByteArrayOutputStream>( ByteArrayOutputStream.class ) );

    Assert.assertEquals( 0, bucket.getSize() );
    
    ByteArrayOutputStream bucket1 = bucket.allocate();
    bucket1.write( "Bucket-1".getBytes() );

    ByteArrayOutputStream bucket2 = bucket.allocate();
    bucket2.write( "Bucket-2".getBytes() );

    ByteArrayOutputStream bucket3 = bucket.allocate();
    bucket3.write( "Bucket-3".getBytes() );

    bucket.free( bucket1 );
    Assert.assertEquals( 1, bucket.getSize() );
    
    ByteArrayOutputStream bucket4 = bucket.allocate();
    Assert.assertEquals( 0, bucket4.size() );
    
  }

} /* ENDCLASS */
