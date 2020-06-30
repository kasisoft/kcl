package com.kasisoft.libs.common.comparator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.AbstractTestCase;
import com.kasisoft.libs.common.utils.MiscFunctions;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ComparatorsTest extends AbstractTestCase {

  @Test
  public void classByName() {
    
    List<Class<?>> types = new ArrayList<>(Arrays.asList(Type1.class, Type2.class, Type3.class, Type4.class, com.kasisoft.libs.common.comparator.subpackage.Type2.class));
    Collections.shuffle(types);
    
    Collections.sort(types, Comparators.CLASS_BY_NAME);
    
    Class<?>[] asArray = types.toArray(new Class<?>[types.size()]);
    assertThat(asArray, is(new Class<?>[] {Type1.class, Type2.class, Type3.class, Type4.class, com.kasisoft.libs.common.comparator.subpackage.Type2.class}));
    
  }

  @Test
  public void classByName__CaseInsensitive() {
    
    List<Class<?>> types = new ArrayList<>(Arrays.asList(Type1.class, Type2.class, Type3.class, Type4.class, com.kasisoft.libs.common.comparator.subpackage.Type2.class));
    Collections.shuffle(types);
    
    Collections.sort(types, Comparators.CLASS_BY_NAME_CI);
    
    Class<?>[] asArray = types.toArray(new Class<?>[types.size()]);
    assertThat(asArray, is(new Class<?>[] {com.kasisoft.libs.common.comparator.subpackage.Type2.class, Type1.class, Type2.class, Type3.class, Type4.class}));
    
  }

  @Test
  public void classBySimpleName() {
    
    List<Class<?>> types = new ArrayList<>(Arrays.asList(Type1.class, Type2.class, Type3.class, Type4.class, com.kasisoft.libs.common.comparator.subpackage.Type2.class));
    Collections.shuffle(types);
    
    Collections.sort(types, Comparators.CLASS_BY_SIMPLE_NAME);
    
    Class<?>[] asArray = types.toArray(new Class<?>[types.size()]);
    assertThat(asArray, is(new Class<?>[] {Type1.class, Type2.class, com.kasisoft.libs.common.comparator.subpackage.Type2.class, Type3.class, Type4.class}));
    
  }

  @Test
  public void classBySimpleName__CaseInsensitive() {
    
    List<Class<?>> types = new ArrayList<>(Arrays.asList(Type1.class, Type2.class, Type3.class, Type4.class, com.kasisoft.libs.common.comparator.subpackage.Type2.class));
    Collections.shuffle(types);
    
    Collections.sort(types, Comparators.CLASS_BY_SIMPLE_NAME_CI);
    
    Class<?>[] asArray = types.toArray(new Class<?>[types.size()]);
    assertThat(asArray, is(new Class<?>[] {Type1.class, com.kasisoft.libs.common.comparator.subpackage.Type2.class, Type2.class, Type3.class, Type4.class}));
    
  }

  @Test
  public void classByPrio() {
    
    List<Class<?>> types = new ArrayList<>(Arrays.asList(Type1.class, Type2.class, Type3.class, Type4.class, com.kasisoft.libs.common.comparator.subpackage.Type2.class));
    Collections.shuffle(types);
    
    Collections.sort(types, Comparators.CLASS_BY_PRIORITY);
    
    Class<?>[] asArray = types.toArray(new Class<?>[types.size()]);
    assertThat(asArray, is(new Class<?>[] {Type3.class, Type2.class, Type1.class, com.kasisoft.libs.common.comparator.subpackage.Type2.class, Type4.class}));
    
  }

  @Test(groups = "all")
  public void nullSafeIntegers() {

    // create random integer values with some nulls in it 
    var nullCount = 50;
    var random    = new Random();
    var data      = new ArrayList<Integer>();
    var nulls     = 0;
    while (nulls < nullCount) {
      var irand = random.nextInt(10000);
      if (irand % 3 == 0) {
        data.add(null);
        nulls++;
      } else {
        data.add(Integer.valueOf(irand));
      }
    }
    
    Collections.sort(data, Comparators.INTEGER_NULLSAFE);
    
    var size = data.size();
    // remove the null values, the remaining ones must be sorted
    MiscFunctions.trim(data);
    
    assertThat(data.size(), is(size - nullCount));
    for (int i = 1; i < data.size(); i++) {
      assertTrue(data.get(i - 1).intValue() <= data.get(i).intValue());
    }
    
  }

} /* ENDCLASS */
