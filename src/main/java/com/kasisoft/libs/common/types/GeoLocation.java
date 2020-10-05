package com.kasisoft.libs.common.types;

import lombok.experimental.FieldDefaults;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.Data;

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
