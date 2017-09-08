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
  
  private ExtArrayList<Integer> createInput() {
    ExtArrayList<Integer> result = new ExtArrayList<>();
    int                   nulls  = 0;
    while( nulls < NULLCOUNT ) {
      int irand = (int) (Math.random() * 1000);
      if( irand % 3 == 0 ) {
        result.add( null );
        nulls++;
      } else {
        result.add( Integer.valueOf( irand ) );
      }
    }
    return result;
  }
  
  @Test
  public void sortWithoutDelegate() {
    
    ExtArrayList<Integer> data = createInput();
    
    Collections.sort( data, new NullableComparator<Integer>() );
    int size = data.size();
    data.trim();
    assertThat( data.size(), is( size - NULLCOUNT ) );
    for( int i = 1; i < data.size(); i++ ) {
      assertTrue( data.get(i - 1).intValue() <= data.get(i).intValue() );
    }
    
  }

  @Test
  public void sortWithDelegate() {
    
    ExtArrayList<Integer> data = createInput();
    
    Comparator<Integer> delegate = new Comparator<Integer>() {
      @Override
      public int compare( Integer o1, Integer o2 ) {
        return o2.compareTo( o1 );
      }
    };
    
    Collections.sort( data, new NullableComparator<>( delegate ) );
    int size = data.size();
    data.trim();
    assertThat( data.size(), is( size - NULLCOUNT ) );
    for( int i = 1; i < data.size(); i++ ) {
      assertTrue( data.get(i - 1).intValue() >= data.get(i).intValue() );
    }
    
  }

} /* ENDCLASS */
