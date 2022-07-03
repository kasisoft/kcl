package com.kasisoft.libs.common.types

/**
 * @author daniel.kasmeroglu@kasisoft.com
 */
data class GeoLocation(var latitude: Double, var longitude: Double) {

    fun isValid(): Boolean = !(latitude.isNaN() || longitude.isNaN())

} /* ENDCLASS */
