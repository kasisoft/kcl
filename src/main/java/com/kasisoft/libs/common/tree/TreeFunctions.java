package com.kasisoft.libs.common.tree;

import com.kasisoft.libs.common.functional.Predicates;
import com.kasisoft.libs.common.functional.TriConsumer;
import com.kasisoft.libs.common.text.StringFunctions;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.function.Function;

import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Collection of various functions.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TreeFunctions {

  private static List<String> split(String str) {
    return Arrays.asList(str.split("/")).stream()
      .map(StringFunctions::cleanup)
      .filter(Predicates.notNull())
      .collect(Collectors.toList())
      ;
  }
  
  private static <T> void addNode(NamedTreeNode<T> parent, NamedTreeNode<T> child) {
    if (child.isEmpty()) {
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
  
  public static <T> NamedTreeNode<T> parenthesize(@NotNull List<T> values, @NotNull Function<T, String> toPath, @Null TriConsumer<String, T, T> addChild) {


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
  
  private static <T> void iterate(NamedTreeNode<T> node, int depth, String prefix, TriConsumer<String, T, T> addChild) {
    for (var i = 0; i < node.getChildCount(); i++) {
      var child = (NamedTreeNode<T>) node.getChildAt(i);
      addChild.accept(depth == 0 ? "/" : prefix, depth == 0 ? null : node.getValue(), child.getValue());
      iterate(child, depth + 1, prefix + "/" + child.getName(), addChild);
    }
  }

} /* ENDCLASS */
