package com.kasisoft.libs.common.tree;

import javax.swing.tree.DefaultMutableTreeNode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Getter;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NamedTreeNode<T> extends DefaultMutableTreeNode {
  
  private static final long serialVersionUID = 674825043503770345L;

  @Getter
  T               value   = null;
  
  @Getter
  List<String>    parents = new LinkedList<>();
  
  public NamedTreeNode(T val, String name) {
    super(name);
    value   = val;
    parents = Collections.emptyList();
  }
  
  public NamedTreeNode(T val, List<String> segments) {
    super(segments.remove(segments.size() - 1));
    value   = val;
    parents = segments;
  }
  
  public String getName() {
    return (String) getUserObject();
  }
  
  public boolean isEmpty() {
    return getChildCount() == 0;
  }
  
  public @Null NamedTreeNode<T> getChildByName(@NotNull String name) {
    return findChildByName(name).orElse(null);
  }
  
  public @NotNull Optional<NamedTreeNode<T>> findChildByName(@NotNull String name) {
    NamedTreeNode<T> result = null;
    for (var i = 0; i < getChildCount(); i++) {
      var child = (NamedTreeNode<T>) getChildAt(i);
      if (name.equals(child.getName())) {
        result = child;
        break;
      }
    }
    return Optional.ofNullable(result);
  }
  
} /* ENDCLASS */