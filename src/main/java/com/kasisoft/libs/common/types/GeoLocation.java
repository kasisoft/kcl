package com.kasisoft.libs.common.types;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Data @AllArgsConstructor @NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GeoLocation {

  double        latitude;
  double        longitude;
  
  public boolean isValid() {
    return !(Double.isNaN(latitude) || Double.isNaN(longitude));
  }
  
} /* ENDCLASS */
