package com.kasisoft.libs.common.converters;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface KConverter<F, T> {

    T decode(F encoded);

    F encode(T decoded);

} /* ENDINTERFACE */
