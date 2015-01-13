package com.kasisoft.libs.common.util;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import org.testng.annotations.Test;

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

    assertThat( 0, is( bucket.getSize() ) );
    
    ByteArrayOutputStream bucket1 = bucket.allocate();
    bucket1.write( "Bucket-1".getBytes() );

    ByteArrayOutputStream bucket2 = bucket.allocate();
    bucket2.write( "Bucket-2".getBytes() );

    ByteArrayOutputStream bucket3 = bucket.allocate();
    bucket3.write( "Bucket-3".getBytes() );

    bucket.free( bucket1 );
    assertThat( 1, is( bucket.getSize() ) );
    
    ByteArrayOutputStream bucket4 = bucket.allocate();
    assertThat( 0, is( bucket4.size() ) );
    
  }

} /* ENDCLASS */
