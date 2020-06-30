package com.kasisoft.libs.common.types;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Data
@NoArgsConstructor @AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OutParam<T> {

  T value;
  
} /* ENDCLASS */
