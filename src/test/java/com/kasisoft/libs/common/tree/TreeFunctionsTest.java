package com.kasisoft.libs.common.tree;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertTrue;

import com.kasisoft.libs.common.types.Tupel;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Test for various functions of the class 'TreeFunctions'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@SuppressWarnings("deprecation")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreeFunctionsTest {

  private static final List<String> PARENTHESIZE_DATA = Arrays.asList(
//    "/root1",
//    "/root1/child1",
    "/root1/child1/child7"
//    "/root1/child1/child8",
//    "/root1/child1/child9",
//    "/root1/child1/child10",
//    "/root1/child2",
//    "/root1/child2/child11",
//    "/root1/child2/child12",
//    "/root1/child2/child13",
//    "/root1/child3",
//    "/root1/child3/child14",
//    "/root1/child4",
//    "/root1/child5",
//    "/root1/child6",
//    "/root2",
//    "/root2/child1",
//    "/root2/child1/child7",
//    "/root2/child1/child8",
//    "/root2/child1/child9",
//    "/root2/child1/child10",
//    "/root2/child2",
//    "/root2/child2/child11",
//    "/root2/child2/child12",
//    "/root2/child2/child13",
//    "/root2/child3",
//    "/root2/child3/child14",
//    "/root2/child4",
//    "/root2/child5",
//    "/root2/child6",
//    "/root3/child1/child2/child3"
  );

  @Test(groups = "all")
  public void parenthesize() {
    var children = new HashSet<Tupel<String>>();
    TreeFunctions.parenthesize(
      PARENTHESIZE_DATA,
      $ -> $,
      ($s, $p, $c) -> children.add(new Tupel<>($s, $p, $c))
    );
    for (Tupel<String> t : children) {
      System.err.println("[" + t.getFirst() + "] -> '" + t.getLast() + "'");
    }
    assertThat(children.size(), is(34));
    for (var child : children) {
      if (child.getValues()[1] != null) {
        String parent = child.getValues()[1];
        String sub    = child.getValues()[2];
        assertTrue( sub.startsWith( parent ) );
      }
    }
  }

} /* ENDCLASS */
