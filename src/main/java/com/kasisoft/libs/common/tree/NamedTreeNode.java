package com.kasisoft.libs.common.tree;

import javax.swing.tree.*;

import javax.validation.constraints.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class NamedTreeNode<T> extends DefaultMutableTreeNode {
  
  private static final long serialVersionUID = 674825043503770345L;

  private T             value;
  private List<String>  parents;
  
  public NamedTreeNode(@NotNull T val, @NotBlank String name) {
    super(name);
    value   = val;
    parents = new LinkedList<>();
  }
  
  public NamedTreeNode(@NotNull T val, @NotNull List<String> segments) {
    super(segments.remove(segments.size() - 1));
    value   = val;
    parents = new LinkedList<>(segments);
  }
  
  public T getValue() {
    return value;
  }
  
  public List<String> getParents() {
    return parents;
  }
  
  public @NotBlank String getName() {
    return (String) getUserObject();
  }
  
  public boolean isEmpty() {
    return getChildCount() == 0;
  }
  
  public NamedTreeNode<T> getChildByName(@NotBlank String name) {
    return findChildByName(name).orElse(null);
  }
  
  public @NotNull Optional<NamedTreeNode<T>> findChildByName(@NotBlank String name) {
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