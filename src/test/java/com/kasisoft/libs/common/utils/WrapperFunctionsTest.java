package com.kasisoft.libs.common.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class WrapperFunctionsTest {

  @Test(groups = "all")
  public void toExtendedList__newList() {
    
    var list1 = WrapperFunctions.toExtendedList(new ArrayList<String>());
    assertThat(list1.size(), is(0));
    
    var list2 = WrapperFunctions.toExtendedList(Arrays.asList("BLA", "BLUB"));
    assertThat(list2.size(), is(2));
    assertThat(list2.get(0), is("BLA"));
    assertThat(list2.get(1), is("BLUB"));
    
  }
  
  @Test(groups = "all")
  public void toExtendedList__add() {
    
    var list1 = WrapperFunctions.toExtendedList(new ArrayList<String>());
    list1.add("BLA");
    
    list1.add(0, "BLUB");
    assertThat(list1.size(), is(2));
    assertThat(list1.get(0), is("BLUB"));
    assertThat(list1.get(1), is("BLA"));
    
    list1.add(-1, "FROG");
    assertThat(list1.size(), is(3));
    assertThat(list1.get(0), is("BLUB"));
    assertThat(list1.get(1), is("FROG"));
    assertThat(list1.get(2), is("BLA"));

    list1.addAll(-1, Arrays.asList("BLAU", "KRAUT"));
    assertThat(list1.size(), is(5));
    assertThat(list1.get(0), is("BLUB"));
    assertThat(list1.get(1), is("FROG"));
    assertThat(list1.get(2), is("BLAU"));
    assertThat(list1.get(3), is("KRAUT"));
    assertThat(list1.get(4), is("BLA"));

  }
  
  @Test(groups = "all")
  public void toExtendedList__sublist() {
    
    var list1 = WrapperFunctions.toExtendedList(Arrays.asList("BLA", "BLUB", "BLAU", "KRAUT", "FROG"));
    var list2 = list1.subList(1, -1);
    assertThat(list2, is( notNullValue()));
    assertThat(list2.size(), is(3));
    assertThat(list2.get(0), is("BLUB"));
    assertThat(list2.get(1), is("BLAU"));
    assertThat(list2.get(2), is("KRAUT"));

  }
  
} /* ENDCLASS */
