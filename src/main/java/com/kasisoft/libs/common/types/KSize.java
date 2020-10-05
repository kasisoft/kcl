package com.kasisoft.libs.common.types;

import javax.validation.constraints.*;

import java.awt.*;

import lombok.experimental.FieldDefaults;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.Data;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Data
@AllArgsConstructor @NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KSize {

  int   width;
  int   height;
  
  public KSize(@NotNull Dimension dim) {
    width   = dim.width;
    height  = dim.height;
  }
  
} /* ENDCLASS */
