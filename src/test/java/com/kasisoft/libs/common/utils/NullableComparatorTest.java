package com.kasisoft.libs.common.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Tests for the type 'NullableComparator'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Test(groups = "all")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NullableComparatorTest {

  static final int NULLCOUNT = 10;
  
  private List<Integer> createInput() {
    var result = WrapperFunctions.toExtendedList(new ArrayList<Integer>());
    var nulls  = 0;
    while (nulls < NULLCOUNT) {
      var irand = (int) (Math.random() * 1000);
      if (irand % 3 == 0) {
        result.add(null);
        nulls++;
      } else {
        result.add(Integer.valueOf(irand));
      }
    }
    return result;
  }
  
  @Test(groups = "all")
  public void sortWithoutDelegate() {
    
    var data = createInput();
    
    Collections.sort(data, new NullableComparator<Integer>());
    var size = data.size();
    MiscFunctions.trim(data);
    
    assertThat(data.size(), is(size - NULLCOUNT));
    for (int i = 1; i < data.size(); i++) {
      assertTrue(data.get(i - 1).intValue() <= data.get(i).intValue());
    }
    
  }

  @Test(groups = "all")
  public void sortWithDelegate() {
    
    var data = createInput();
    
    var delegate = new Comparator<Integer>() {
      @Override
      public int compare(Integer o1, Integer o2) {
        return o2.compareTo(o1);
      }
    };
    
    Collections.sort(data, new NullableComparator<>(delegate));
    var size = data.size();
    MiscFunctions.trim(data);
    assertThat(data.size(), is(size - NULLCOUNT));
    
    for (var i = 1; i < data.size(); i++) {
      assertTrue(data.get(i - 1).intValue() >= data.get(i).intValue());
    }
    
  }

} /* ENDCLASS */
