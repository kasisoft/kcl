package com.kasisoft.libs.common.tree;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.MatcherAssert.*;

import static org.hamcrest.Matchers.*;

import com.kasisoft.libs.common.text.*;
import com.kasisoft.libs.common.types.*;

import org.junit.jupiter.api.*;

import java.util.*;

/**
 * Test for various functions of the class 'TreeFunctions'.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class TreeFunctionsTest {

  private static final List<String> PARENTHESIZE_DATA = Arrays.asList(
    "/root1",
    "/root1/child1",
    "/root1/child1/child7",
    "/root1/child1/child8",
    "/root1/child1/child9",
    "/root1/child1/child10",
    "/root1/child2",
    "/root1/child2/child11",
    "/root1/child2/child12",
    "/root1/child2/child13",
    "/root1/child3",
    "/root1/child3/child14",
    "/root1/child4",
    "/root1/child5",
    "/root1/child6",
    "/root2",
    "/root2/child1",
    "/root2/child1/child7",
    "/root2/child1/child8",
    "/root2/child1/child9",
    "/root2/child1/child10",
    "/root2/child2",
    "/root2/child2/child11",
    "/root2/child2/child12",
    "/root2/child2/child13",
    "/root2/child3",
    "/root2/child3/child14",
    "/root2/child4",
    "/root2/child5",
    "/root2/child6",
    "/root3/child1/child2/child3"
  );

  @Test
  public void parenthesize() {
    var children = new HashSet<Tupel<String>>();
    TreeFunctions.parenthesize(
      PARENTHESIZE_DATA,
      $ -> $,
      ($s, $p, $c) -> children.add(new Tupel<String>(new String[] {$s, $p, $c}))
    );
    assertThat(children.size(), is(34));
    for (var child : children) {
      if (child.getValues()[1] != null) {
        String parent = child.getValues()[1];
        String sub    = child.getValues()[2];
        assertTrue( sub.startsWith( parent ) );
      }
    }
  }

  private static final String EXPECTED_FOR_TREE_NODE = ""
                                                       + "[1,root1]\n"
                                                       + "[2,child1]\n"
                                                       + "[3,child7]\n"
                                                       + "[3,child8]\n"
                                                       + "[3,child9]\n"
                                                       + "[3,child10]\n"
                                                       + "[2,child2]\n"
                                                       + "[3,child11]\n"
                                                       + "[3,child12]\n"
                                                       + "[3,child13]\n"
                                                       + "[2,child3]\n"
                                                       + "[3,child14]\n"
                                                       + "[2,child4]\n"
                                                       + "[2,child5]\n"
                                                       + "[2,child6]\n"
                                                       + "[1,root2]\n"
                                                       + "[2,child1]\n"
                                                       + "[3,child7]\n"
                                                       + "[3,child8]\n"
                                                       + "[3,child9]\n"
                                                       + "[3,child10]\n"
                                                       + "[2,child2]\n"
                                                       + "[3,child11]\n"
                                                       + "[3,child12]\n"
                                                       + "[3,child13]\n"
                                                       + "[2,child3]\n"
                                                       + "[3,child14]\n"
                                                       + "[2,child4]\n"
                                                       + "[2,child5]\n"
                                                       + "[2,child6]\n"
                                                       + "[1,root3]\n"
                                                       + "[2,child1]\n"
                                                       + "[3,child2]\n"
                                                       + "[4,child3]\n"
                                                       ;

  @Test
  public void forTreeNodeDo() {
    StringFBuilder builder = new StringFBuilder();
    TreeFunctions.forTreeNodeDo(
      PARENTHESIZE_DATA,
      $ -> $,
      $ -> {
        builder.appendF("[%d,%s]\n", $.getLevel(), $.getName());
      }
    );
    assertThat(builder.toString(), is(EXPECTED_FOR_TREE_NODE));
  }

  @SuppressWarnings("unused")
  private static final String EXPECTED_FOR_TREE_NODE_WITH_ROOT = ""
                                                                 + "[0,root]\n"
                                                                 + "[1,root1]\n"
                                                                 + "[2,child1]\n"
                                                                 + "[3,child7]\n"
                                                                 + "[3,child8]\n"
                                                                 + "[3,child9]\n"
                                                                 + "[3,child10]\n"
                                                                 + "[2,child2]\n"
                                                                 + "[3,child11]\n"
                                                                 + "[3,child12]\n"
                                                                 + "[3,child13]\n"
                                                                 + "[2,child3]\n"
                                                                 + "[3,child14]\n"
                                                                 + "[2,child4]\n"
                                                                 + "[2,child5]\n"
                                                                 + "[2,child6]\n"
                                                                 + "[1,root2]\n"
                                                                 + "[2,child1]\n"
                                                                 + "[3,child7]\n"
                                                                 + "[3,child8]\n"
                                                                 + "[3,child9]\n"
                                                                 + "[3,child10]\n"
                                                                 + "[2,child2]\n"
                                                                 + "[3,child11]\n"
                                                                 + "[3,child12]\n"
                                                                 + "[3,child13]\n"
                                                                 + "[2,child3]\n"
                                                                 + "[3,child14]\n"
                                                                 + "[2,child4]\n"
                                                                 + "[2,child5]\n"
                                                                 + "[2,child6]\n"
                                                                 + "[1,root3]\n"
                                                                 + "[2,child1]\n"
                                                                 + "[3,child2]\n"
                                                                 + "[4,child3]\n"
                                                                 ;

  @Test
  public void forTreeNodeDo__WithRoot() {
    StringFBuilder builder = new StringFBuilder();
    TreeFunctions.forTreeNodeDo(
      PARENTHESIZE_DATA,
      $ -> $,
      false,
      $ -> {
        builder.appendF("[%d,%s]\n", $.getLevel(), $.getName());
      }
    );
    assertThat(builder.toString(), is(EXPECTED_FOR_TREE_NODE_WITH_ROOT));
  }

  private static final String EXPECTED_FOR_TREE_VALUE = ""
                                                        + "[0,root1]\n"
                                                        + "[1,child1]\n"
                                                        + "[2,child7]\n"
                                                        + "[2,child8]\n"
                                                        + "[2,child9]\n"
                                                        + "[2,child10]\n"
                                                        + "[1,child2]\n"
                                                        + "[2,child11]\n"
                                                        + "[2,child12]\n"
                                                        + "[2,child13]\n"
                                                        + "[1,child3]\n"
                                                        + "[2,child14]\n"
                                                        + "[1,child4]\n"
                                                        + "[1,child5]\n"
                                                        + "[1,child6]\n"
                                                        + "[0,root2]\n"
                                                        + "[1,child1]\n"
                                                        + "[2,child7]\n"
                                                        + "[2,child8]\n"
                                                        + "[2,child9]\n"
                                                        + "[2,child10]\n"
                                                        + "[1,child2]\n"
                                                        + "[2,child11]\n"
                                                        + "[2,child12]\n"
                                                        + "[2,child13]\n"
                                                        + "[1,child3]\n"
                                                        + "[2,child14]\n"
                                                        + "[1,child4]\n"
                                                        + "[1,child5]\n"
                                                        + "[1,child6]\n"
                                                        + "[0,root3]\n"
                                                        + "[1,child1]\n"
                                                        + "[2,child2]\n"
                                                        + "[3,child3]\n"
                                                        ;


  @Test
  public void forTreeValueDo() {
    StringFBuilder builder = new StringFBuilder();
    TreeFunctions.forTreeValueDo(
      PARENTHESIZE_DATA,
      $ -> $,
      ($value, $level) -> builder.appendF("[%d,%s]\n", $level, $value)
    );
    assertThat(builder.toString(), is(EXPECTED_FOR_TREE_VALUE));
  }

  private static final String EXPECTED_FOR_TREE_VALUE_WITH_ROOT = ""
                                                                  + "[0,root]\n"
                                                                  + "[1,root1]\n"
                                                                  + "[2,child1]\n"
                                                                  + "[3,child7]\n"
                                                                  + "[3,child8]\n"
                                                                  + "[3,child9]\n"
                                                                  + "[3,child10]\n"
                                                                  + "[2,child2]\n"
                                                                  + "[3,child11]\n"
                                                                  + "[3,child12]\n"
                                                                  + "[3,child13]\n"
                                                                  + "[2,child3]\n"
                                                                  + "[3,child14]\n"
                                                                  + "[2,child4]\n"
                                                                  + "[2,child5]\n"
                                                                  + "[2,child6]\n"
                                                                  + "[1,root2]\n"
                                                                  + "[2,child1]\n"
                                                                  + "[3,child7]\n"
                                                                  + "[3,child8]\n"
                                                                  + "[3,child9]\n"
                                                                  + "[3,child10]\n"
                                                                  + "[2,child2]\n"
                                                                  + "[3,child11]\n"
                                                                  + "[3,child12]\n"
                                                                  + "[3,child13]\n"
                                                                  + "[2,child3]\n"
                                                                  + "[3,child14]\n"
                                                                  + "[2,child4]\n"
                                                                  + "[2,child5]\n"
                                                                  + "[2,child6]\n"
                                                                  + "[1,root3]\n"
                                                                  + "[2,child1]\n"
                                                                  + "[3,child2]\n"
                                                                  + "[4,child3]\n"
                                                                  ;


  @Test
  public void forTreeValueDo__WithRoot() {
    StringFBuilder builder = new StringFBuilder();
    TreeFunctions.forTreeValueDo(
      PARENTHESIZE_DATA,
      $ -> $,
      false,
      ($value, $level) -> builder.appendF("[%d,%s]\n", $level, $value)
    );
    assertThat(builder.toString(), is(EXPECTED_FOR_TREE_VALUE_WITH_ROOT));
  }

} /* ENDCLASS */
