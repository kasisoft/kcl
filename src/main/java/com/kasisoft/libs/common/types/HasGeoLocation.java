package com.kasisoft.libs.common.types;

import javax.validation.constraints.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface HasGeoLocation {

  @NotNull GeoLocation getGeoLocation();
  
  void setGeoLocation(@NotNull GeoLocation geoLocation);
  
} /* ENDINTERFACE */
