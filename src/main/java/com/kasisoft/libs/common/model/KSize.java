package com.kasisoft.libs.common.model;

import java.awt.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Data
@AllArgsConstructor @NoArgsConstructor
public class KSize {

  int   width;
  int   height;
  
  public KSize( Dimension dim ) {
    width   = dim.width;
    height  = dim.height;
  }
  
} /* ENDCLASS */
