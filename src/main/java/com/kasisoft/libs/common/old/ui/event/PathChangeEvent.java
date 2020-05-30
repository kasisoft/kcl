package com.kasisoft.libs.common.old.ui.event;

import lombok.experimental.*;

import lombok.*;

import javax.swing.*;

import java.util.*;

import java.nio.file.*;

/**
 * This event indicates a change of a Path.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PathChangeEvent extends EventObject {

  @Getter
  Path   path;

  /**
   * Sets up this event used to trigger a change of a Path.
   * 
   * @param source     The component that triggered the Path change.
   * @param newfile    The new Path instance.
   */
  public PathChangeEvent( JComponent source, Path newpath ) {
    super( source );
    path = newpath;
  }

  /**
   * Returns the component that triggered this event.
   * 
   * @return   The component that triggered this event.
   */
  public JComponent getComponent() {
    return (JComponent) getSource();
  }

} /* ENDCLASS */