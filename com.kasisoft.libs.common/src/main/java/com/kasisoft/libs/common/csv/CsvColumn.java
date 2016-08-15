package com.kasisoft.libs.common.csv;

import lombok.experimental.*;

import lombok.*;

import java.util.function.*;

/**
 * A basic description of a csv column.
 *  
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public final class CsvColumn<T> {
  
  Class<T>              type;
  boolean               nullable;
  T                     defval;
  String                title;
  Function<String, T>   adapter;
  
  /**
   * Creates a copy of this instance.
   * 
   * @return   A copy of this instance. Not <code>null</code>.
   */
  public CsvColumn<T> copy() {
    CsvColumn<T> result = new CsvColumn<>();
    result.type         = type;
    result.nullable     = nullable;
    result.defval       = defval;
    result.title        = title;
    result.adapter      = adapter;
    return result;
  }
  
} /* ENDCLASS */
