package com.kasisoft.libs.common.constants;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import javax.validation.constraints.NotNull;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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

  Left   (SwingConstants.LEFT   , TitledBorder.LEFT  ),
  Center (SwingConstants.CENTER , TitledBorder.CENTER),
  Right  (SwingConstants.RIGHT  , TitledBorder.RIGHT ),
  Top    (SwingConstants.TOP    , TitledBorder.LEFT  ),
  Middle (SwingConstants.CENTER , TitledBorder.LEFT  ),
  Bottom (SwingConstants.BOTTOM , TitledBorder.LEFT  );
  
  int   alignment;
  int   titledBorderX;
  
  public void set(@NotNull JComponent component) {
    if (component instanceof JLabel) {
      ((JLabel) component).setHorizontalAlignment(alignment);
    } else if (component instanceof JTextField) {
      ((JTextField) component).setHorizontalAlignment(alignment);
    }
  }

} /* ENDENUM */