package com.kasisoft.libs.common.tree;

import com.kasisoft.libs.common.functional.*;

import com.kasisoft.libs.common.text.*;

import javax.validation.constraints.*;

import java.util.function.*;

import java.util.stream.*;

import java.util.*;

/**
 * Functions to process a list of values in a tree like fashion assuming there's a mapping for each value to generate
 * a treepath.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class TreeFunctions {

  private static List<String> split(@NotBlank String str) {
    return Arrays.asList(str.split("/")).stream()
      .map(StringFunctions::cleanup)
      .filter(Predicates.notNull())
      .collect(Collectors.toCollection(LinkedList::new))
      ;
  }

  private static <T> void addNode(@NotNull NamedTreeNode<T> parent, @NotNull NamedTreeNode<T> child) {
    if (child.getParents().isEmpty()) {
      // we need to add the lead
      parent.add(child);
    } else {
      var childName = child.getParents().remove(0);
      var childNode = parent.getChildByName(childName);
      if (childNode == null) {
        // artificial node
        childNode = new NamedTreeNode<>(null, childName);
        parent.add(childNode);
      }
      addNode(childNode, child);
    }
  }

  public static <T> NamedTreeNode<T> parenthesize(@NotNull List<T> values, @NotNull Function<T, String> toPath) {
    return parenthesize(values, toPath, null);
  }

  public static <T> NamedTreeNode<T> parenthesize(@NotNull List<T> values, @NotNull Function<T, String> toPath, TriConsumer<String, T, T> addChild) {

    // create a list of named nodes first: the name is the leaf, the parents will be the parental segments of this nodes
    // tree path
    var nodes = new ArrayList<NamedTreeNode<T>>();
    values.stream()
      .map($ -> new NamedTreeNode($, split(toPath.apply($))))
      .forEach(nodes::add)
      ;

    var result = new NamedTreeNode<T>(null, split("root"));
    nodes.forEach($ -> addNode(result, $));

    if (addChild != null) {
      iterate(result, 0, "", addChild);
    }

    return result;

  }

  private static <T> void iterate(@NotNull NamedTreeNode<T> node, int depth, @NotNull String prefix, @NotNull TriConsumer<String, T, T> addChild) {
    for (var i = 0; i < node.getChildCount(); i++) {
      var child = (NamedTreeNode<T>) node.getChildAt(i);
      addChild.accept(depth == 0 ? "/" : prefix, depth == 0 ? null : node.getValue(), child.getValue());
      iterate(child, depth + 1, prefix + "/" + child.getName(), addChild);
    }
  }

  public static <T> void forTreeNodeDo(@NotNull List<T> values, @NotNull Function<T, String> toPath, @NotNull Consumer<NamedTreeNode<T>> handleNode) {
    forTreeNodeDo(values, toPath, true, handleNode);
  }

  public static <T> void forTreeNodeDo(@NotNull List<T> values, @NotNull Function<T, String> toPath, boolean skipArtificialRoot, @NotNull Consumer<NamedTreeNode<T>> handleNode) {
    NamedTreeNode<T> root = parenthesize(values, toPath, null);
    iterateTree(root, skipArtificialRoot, handleNode);
  }

  private static <T> void iterateTree(@NotNull NamedTreeNode<T> node, boolean skip, @NotNull Consumer<NamedTreeNode<T>> handleNode) {
    if (!skip) {
      handleNode.accept(node);
    }
    for (int i = 0; i < node.getChildCount(); i++) {
      iterateTree((NamedTreeNode<T>) node.getChildAt(i), false, handleNode);
    }
  }

  public static <T> void forTreeValueDo(@NotNull List<T> values, @NotNull Function<T, String> toPath, @NotNull BiConsumer<T, Integer> handleValue) {
    forTreeValueDo(values, toPath, true, handleValue);
  }

  public static <T> void forTreeValueDo(@NotNull List<T> values, @NotNull Function<T, String> toPath, boolean skipArtificialRoot, @NotNull BiConsumer<T, Integer> handleValue) {
    var                        offset         = skipArtificialRoot ? 1 : 0;
    Consumer<NamedTreeNode<T>> handleTreeNode = $ -> handleValue.accept((T) $.getUserObject(), $.getLevel() - offset);
    forTreeNodeDo(values, toPath, skipArtificialRoot, handleTreeNode);
  }

} /* ENDCLASS */
