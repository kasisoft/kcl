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
  
  public static <R> CsvColumnBuilder<R> builder() {
    return new CsvColumnBuilder<>();
  }
  
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class CsvColumnBuilder<R> {
    
    CsvColumn<R>   instance = new CsvColumn<>();
    
    private CsvColumnBuilder() {
    }

    public CsvColumnBuilder<R> type( @NonNull Class<R> type ) {
      instance.type = type;
      return this;
    }

    public CsvColumnBuilder<R> nullable() {
      return nullable( true );
    }

    public CsvColumnBuilder<R> nullable( boolean nullable ) {
      instance.nullable = nullable;
      return this;
    }

    public CsvColumnBuilder<R> defaultValue( R defval ) {
      instance.defval = defval;
      return this;
    }

    public CsvColumnBuilder<R> title( String title ) {
      instance.title = title;
      return this;
    }

    public CsvColumnBuilder<R> adapter( Function<String, R> adapter ) {
      instance.adapter = adapter;
      return this;
    }

    public CsvColumn<R> build() {
      return instance;
    }
    
  } /* ENDCLASS */

  
} /* ENDCLASS */
