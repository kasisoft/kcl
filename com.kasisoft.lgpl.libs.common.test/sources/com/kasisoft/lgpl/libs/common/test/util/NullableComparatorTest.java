/**
 * Name........: NullableComparatorTest
 * Description.: Tests for the type 'NullableComparator'.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.test.util;

import com.kasisoft.lgpl.libs.common.util.*;

import org.testng.annotations.*;

import org.testng.*;

import java.util.*;

/**
 * Tests for the type 'NullableComparator'.
 */
@Test(groups="all")
public class NullableComparatorTest {

  private static final int NULLCOUNT = 10;
  
  private ExtArrayList<Integer>   data;
  
  @BeforeMethod
  public void setup() {
    data      = new ExtArrayList<Integer>();
    int nulls = 0;
    while( nulls < NULLCOUNT ) {
      int irand = (int) Math.random() * 1000;
      if( irand % 3 == 0 ) {
        data.add( null );
        nulls++;
      } else {
        data.add( Integer.valueOf( irand ) );
      }
    }
  }
  
  @Test
  public void sortWithoutDelegate() {
    
    Collections.sort( data, new NullableComparator<Integer>() );
    int size = data.size();
    data.trim();
    Assert.assertEquals( data.size(), size - NULLCOUNT );
    for( int i = 1; i < data.size(); i++ ) {
      Assert.assertTrue( data.get(i - 1).intValue() >= data.get(i).intValue() );
    }
    
  }

  @Test
  public void sortWitDelegate() {
    
    Comparator<Integer> delegate = new Comparator<Integer>() {
      public int compare( Integer o1, Integer o2 ) {
        return o2.compareTo( o1 );
      }
    };
    
    Collections.sort( data, new NullableComparator<Integer>( delegate ) );
    int size = data.size();
    data.trim();
    Assert.assertEquals( data.size(), size - NULLCOUNT );
    for( int i = 1; i < data.size(); i++ ) {
      Assert.assertTrue( data.get(i - 1).intValue() <= data.get(i).intValue() );
    }
    
  }

} /* ENDCLASS */
