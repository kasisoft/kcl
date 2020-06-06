package com.kasisoft.libs.common.types;

import javax.validation.constraints.NotNull;

import java.awt.Dimension;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Data
@AllArgsConstructor @NoArgsConstructor
public class KSize {

  int   width;
  int   height;
  
  public KSize(@NotNull Dimension dim) {
    width   = dim.width;
    height  = dim.height;
  }
  
} /* ENDCLASS */
