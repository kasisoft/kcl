package com.kasisoft.libs.common.constants;

import javax.swing.*;
import javax.swing.border.*;

import javax.validation.constraints.*;

import lombok.experimental.FieldDefaults;

import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Values to specify an alignment.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Alignment {

  Left   (SwingConstants.LEFT  , TitledBorder.LEFT  , true),
  Center (SwingConstants.CENTER, TitledBorder.CENTER, true),
  Right  (SwingConstants.RIGHT , TitledBorder.RIGHT , true),
  Top    (SwingConstants.TOP   , TitledBorder.LEFT  , false),
  Middle (SwingConstants.CENTER, TitledBorder.LEFT  , false),
  Bottom (SwingConstants.BOTTOM, TitledBorder.LEFT  , false);
  
  int       alignment;
  int       titledBorderX;
  boolean   horizontal;
  
  public void set(@NotNull JComponent component) {
    if (horizontal) {
      if (component instanceof JLabel) {
        ((JLabel) component).setHorizontalAlignment(alignment);
      } else if (component instanceof JTextField) {
        ((JTextField) component).setHorizontalAlignment(alignment);
      }
    } else {
      if (component instanceof JLabel) {
        ((JLabel) component).setVerticalAlignment(alignment);
      } else if (component instanceof JTextField) {
        ((JTextField) component).setAlignmentY(this == Top ? 0.0f : (this == Middle ? 0.5f : 1.0f));
      }
    }
  }

} /* ENDENUM */