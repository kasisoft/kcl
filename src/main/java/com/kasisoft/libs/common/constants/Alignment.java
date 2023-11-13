package com.kasisoft.libs.common.constants;

import javax.swing.border.*;

import javax.swing.*;

import jakarta.validation.constraints.*;

/**
 * Values to specify an alignment.
 *
 * @author daniel.kasmeroglu@kasisoft.net
 */
public enum Alignment {

  Left   (SwingConstants.LEFT  , TitledBorder.LEFT  , true),
  Center (SwingConstants.CENTER, TitledBorder.CENTER, true),
  Right  (SwingConstants.RIGHT , TitledBorder.RIGHT , true),
  Top    (SwingConstants.TOP   , TitledBorder.LEFT  , false),
  Middle (SwingConstants.CENTER, TitledBorder.LEFT  , false),
  Bottom (SwingConstants.BOTTOM, TitledBorder.LEFT  , false);

  private int       alignment;
  private int       titledBorderX;
  private boolean   horizontal;

  Alignment(int alignment, int titledBorderX, boolean horizontal) {
    this.alignment      = alignment;
    this.titledBorderX  = titledBorderX;
    this.horizontal     = horizontal;
  }

  public int getAlignment() {
    return alignment;
  }

  public int getTitledBorderX() {
    return titledBorderX;
  }

  public boolean isHorizontal() {
    return horizontal;
  }

  public void set(@NotNull JComponent component) {
    if (horizontal) {
      if (component instanceof JLabel label) {
        label.setHorizontalAlignment(alignment);
      } else if (component instanceof JTextField textfield) {
        textfield.setHorizontalAlignment(alignment);
      }
    } else {
      if (component instanceof JLabel label) {
        label.setVerticalAlignment(alignment);
      } else if (component instanceof JTextField textfield) {
        textfield.setAlignmentY(this == Top ? 0.0f : (this == Middle ? 0.5f : 1.0f));
      }
    }
  }

} /* ENDENUM */