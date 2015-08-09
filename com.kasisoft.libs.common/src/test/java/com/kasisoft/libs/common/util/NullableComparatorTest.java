package com.kasisoft.libs.common.util;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

import lombok.experimental.*;

import lombok.*;

import java.util.*;

/**
 * Tests for the type 'NullableComparator'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Test(groups="all")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NullableComparatorTest {

  static final int NULLCOUNT = 10;
  
  private ExtArrayList<Integer>   data;
  
  @BeforeMethod
  public void setup() {
    data      = new ExtArrayList<>();
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
    assertThat( data.size(), is( size - NULLCOUNT ) );
    for( int i = 1; i < data.size(); i++ ) {
      assertTrue( data.get(i - 1).intValue() >= data.get(i).intValue() );
    }
    
  }

  @Test
  public void sortWithDelegate() {
    
    Comparator<Integer> delegate = new Comparator<Integer>() {
      @Override
      public int compare( Integer o1, Integer o2 ) {
        return o2.compareTo( o1 );
      }
    };
    
    Collections.sort( data, new NullableComparator<Integer>( delegate ) );
    int size = data.size();
    data.trim();
    assertThat( data.size(), is( size - NULLCOUNT ) );
    for( int i = 1; i < data.size(); i++ ) {
      assertTrue( data.get(i - 1).intValue() <= data.get(i).intValue() );
    }
    
  }

} /* ENDCLASS */
