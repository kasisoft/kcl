package com.kasisoft.libs.common.types;

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
public record GeoLocation(double latitude, double longitude) {

    public boolean isValid() {
        return !(Double.isNaN(latitude) || Double.isNaN(longitude));
    }

} /* ENDRECORD */
