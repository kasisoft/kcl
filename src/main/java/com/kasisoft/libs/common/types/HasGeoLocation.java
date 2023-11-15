package com.kasisoft.libs.common.types;

import jakarta.validation.constraints.*;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public interface HasGeoLocation {

    @NotNull
    GeoLocation getGeoLocation();

    void setGeoLocation(@NotNull GeoLocation geoLocation);

} /* ENDINTERFACE */
