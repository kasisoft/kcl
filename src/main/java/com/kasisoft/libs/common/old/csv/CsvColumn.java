package com.kasisoft.libs.common.old.csv;

import com.kasisoft.libs.common.old.constants.Encoding;
import com.kasisoft.libs.common.old.util.MiscFunctions;

import java.util.function.Function;

import java.util.HashMap;
import java.util.Map;

import java.math.BigDecimal;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;

/**
 * A basic description of a csv column.
 *  
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data @NoArgsConstructor @AllArgsConstructor
public final class CsvColumn<T> {
  
  private static Map<Class<?>, Function<String, ?>> DEFAULT_ADAPTERS = new HashMap<>();
  
  static {
    DEFAULT_ADAPTERS.put( Integer.class     , CsvColumn::toIntegerValue     );
    DEFAULT_ADAPTERS.put( String.class      , CsvColumn::toStringValue      );
    DEFAULT_ADAPTERS.put( Boolean.class     , CsvColumn::toBooleanValue     );
    DEFAULT_ADAPTERS.put( byte[].class      , CsvColumn::toByteArrayValue   );
    DEFAULT_ADAPTERS.put( BigDecimal.class  , CsvColumn::toBigDecimalValue  );
  }
  
  String                title;
  Class<T>              type;
  boolean               nullable;
  T                     defval;
  Function<String, T>   adapter;
  
  public CsvColumn( String title ) {
    this( title, (Class<T>) String.class, true, null, null );
  }

  public CsvColumn( String title, Class<T> type, T defValue ) {
    this( title, type, false, defValue, null );
  }

  public CsvColumn( String title, Class<T> type, boolean nullable, Function<String, T> adapter ) {
    this( title, type, nullable, null, adapter );
  }

  public CsvColumn( String title, Class<T> type ) {
    this( title, type, true, null, null );
  }

  /**
   * Returns the current adapter associated with this column.
   * 
   * @return   The current adapter associated with this column. Maybe <code>null</code>.
   */
  public Function<String, T> getAdapter() {
    Function<String, T> result = adapter;
    if( (result == null) && (type != null) ) {
      result = (Function<String, T>) DEFAULT_ADAPTERS.get( type );
    }
    return result;
  }
  
  /**
   * Creates a copy of this instance.
   * 
   * @return   A copy of this instance. Not <code>null</code>.
   */
  public CsvColumn<T> copy() {
    val result = new CsvColumn<T>();
    result.type         = type;
    result.nullable     = nullable;
    result.defval       = defval;
    result.title        = title;
    result.adapter      = adapter;
    return result;
  }
  
  /**
   * Default adapter for Integer.
   * 
   * @param value   The value to be converted. Maybe <code>null</code>.
   * 
   * @return   The converted value. Maybe <code>null</code>.
   */
  private static Integer toIntegerValue( String value ) {
    return MiscFunctions.parseInt( value );
  }

  /**
   * Default adapter for String.
   * 
   * @param value   The value to be converted. Maybe <code>null</code>.
   * 
   * @return   The converted value. Maybe <code>null</code>.
   */
  private static String toStringValue( String value ) {
    return value;
  }

  /**
   * Default adapter for Boolean.
   * 
   * @param value   The value to be converted. Maybe <code>null</code>.
   * 
   * @return   The converted value. Maybe <code>null</code>.
   */
  private static Boolean toBooleanValue( String value ) {
    return MiscFunctions.parseBoolean( value );
  }

  /**
   * Default adapter for byte[].
   * 
   * @param value   The value to be converted. Maybe <code>null</code>.
   * 
   * @return   The converted value. Maybe <code>null</code>.
   */
  private static byte[] toByteArrayValue( String value ) {
    if( value != null ) {
      return Encoding.UTF8.encode( value );
    } else {
      return null;
    }
  }

  /**
   * Default adapter for BigDecimal.
   * 
   * @param value   The value to be converted. Maybe <code>null</code>.
   * 
   * @return   The converted value. Maybe <code>null</code>.
   */
  private static BigDecimal toBigDecimalValue( String value ) {
    return new BigDecimal( value );
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