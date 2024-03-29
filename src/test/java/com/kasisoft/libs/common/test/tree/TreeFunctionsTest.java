package com.kasisoft.libs.common.test.tree;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import com.kasisoft.libs.common.tree.*;

import java.util.function.*;

import java.util.*;

/**
 * Test for various functions of the class {@link TreeFunctions}
 *
 * @author daniel.kasmeroglu@kasisoft.com
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
        var children = new HashSet<List<String>>();
        TreeFunctions.parenthesize(PARENTHESIZE_DATA, Function.identity(), ($s, $p, $c) -> children.add(Arrays.asList($s, $p, $c)));
        assertThat(children.size(), is(34));
        for (var child : children) {
            if (child.get(1) != null) {
                var parent = child.get(1);
                var sub    = child.get(2);
                assertTrue(sub.startsWith(parent));
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
        + "[4,child3]\n";

    @Test
    public void forTreeNodeDo() {
        var builder = new StringBuilder();
        TreeFunctions.forTreeNodeDo(PARENTHESIZE_DATA, Function.identity(), $ -> {
            builder.append("[%d,%s]\n".formatted($.getLevel(), $.getName()));
        });
        assertThat(builder.toString(), is(EXPECTED_FOR_TREE_NODE));
    }

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
        var builder = new StringBuilder();
        TreeFunctions.forTreeNodeDo(PARENTHESIZE_DATA, Function.identity(), false, $ -> {
            builder.append("[%d,%s]\n".formatted($.getLevel(), $.getName()));
        });
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
        var builder = new StringBuilder();
        TreeFunctions.forTreeValueDo(PARENTHESIZE_DATA, Function.identity(), ($value, $level) -> builder.append("[%d,%s]\n".formatted($level, $value)));
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
        var builder = new StringBuilder();
        TreeFunctions.forTreeValueDo(PARENTHESIZE_DATA, Function.identity(), false, ($value, $level) -> builder.append("[%d,%s]\n".formatted($level, $value)));
        assertThat(builder.toString(), is(EXPECTED_FOR_TREE_VALUE_WITH_ROOT));
    }

} /* ENDCLASS */
