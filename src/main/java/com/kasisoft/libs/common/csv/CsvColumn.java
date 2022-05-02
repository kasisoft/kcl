package com.kasisoft.libs.common.csv;

import com.kasisoft.libs.common.constants.*;

import javax.validation.constraints.*;

import java.util.function.*;

import java.util.*;

import java.math.*;

/**
 * A basic description of a csv column.
 *  
 * @author daniel.kasmeroglu@kasisoft.net
 */
public final class CsvColumn<T> {
  
  private static Map<Class<?>, Function<String, ?>> DEFAULT_ADAPTERS = new HashMap<>();
  
  static {
    DEFAULT_ADAPTERS.put(Integer.class     , CsvColumn::toIntegerValue   );
    DEFAULT_ADAPTERS.put(String.class      , CsvColumn::toStringValue    );
    DEFAULT_ADAPTERS.put(Boolean.class     , CsvColumn::toBooleanValue   );
    DEFAULT_ADAPTERS.put(byte[].class      , CsvColumn::toByteArrayValue );
    DEFAULT_ADAPTERS.put(BigDecimal.class  , CsvColumn::toBigDecimalValue);
  }
  
  private String                title;
  private Class<T>              type;
  private boolean               nullable;
  private T                     defval;
  private Function<String, T>   adapter;
  
  public CsvColumn() {
    this(null, (Class<T>) String.class, true, null, null);
  }
  
  public CsvColumn(String title) {
    this(title, (Class<T>) String.class, true, null, null);
  }

  public CsvColumn(String title, Class<T> type, T defValue) {
    this(title, type, false, defValue, null);
  }

  public CsvColumn(String title, Class<T> type, boolean nullable, Function<String, T> adapter) {
    this(title, type, nullable, null, adapter);
  }

  public CsvColumn(String title, Class<T> type) {
    this(title, type, true, null, null);
  }

  public CsvColumn(String title, Class<T> type, boolean nullable, T defval, Function<String, T> adapter) {
    this.title      = title;
    this.type       = type;
    this.nullable   = nullable;
    this.defval     = defval;
    this.adapter    = adapter;
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public Class<T> getType() {
    return type;
  }
  
  public void setType(Class<T> type) {
    this.type = type;
  }
  
  public boolean isNullable() {
    return nullable;
  }
  
  public void setNullable(boolean nullable) {
    this.nullable = nullable;
  }
  
  public T getDefval() {
    return defval;
  }
  
  public void setDefval(T defval) {
    this.defval = defval;
  }
  
  public Function<String, T> getAdatper() {
    return adapter;
  }
  
  public void setAdapter(Function<String, T> adapter) {
    this.adapter = adapter;
  }

  /**
   * Returns the current adapter associated with this column.
   * 
   * @return   The current adapter associated with this column.
   */
  public Function<String, T> getAdapter() {
    var result = adapter;
    if ((result == null) && (type != null)) {
      result = (Function<String, T>) DEFAULT_ADAPTERS.get( type );
    }
    return result;
  }
  
  /**
   * Creates a copy of this instance.
   * 
   * @return   A copy of this instance.
   */
  public @NotNull CsvColumn<T> copy() {
    var result      = new CsvColumn<T>();
    result.type     = type;
    result.nullable = nullable;
    result.defval   = defval;
    result.title    = title;
    result.adapter  = adapter;
    return result;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((adapter == null) ? 0 : adapter.hashCode());
    result = prime * result + ((defval == null) ? 0 : defval.hashCode());
    result = prime * result + (nullable ? 1231 : 1237);
    result = prime * result + ((title == null) ? 0 : title.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CsvColumn other = (CsvColumn) obj;
    if (adapter == null) {
      if (other.adapter != null)
        return false;
    } else if (!adapter.equals(other.adapter))
      return false;
    if (defval == null) {
      if (other.defval != null)
        return false;
    } else if (!defval.equals(other.defval))
      return false;
    if (nullable != other.nullable)
      return false;
    if (title == null) {
      if (other.title != null)
        return false;
    } else if (!title.equals(other.title))
      return false;
    if (type == null) {
      if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CsvColumn [title=" + title + ", type=" + type + ", nullable=" + nullable + ", defval=" + defval
        + ", adapter=" + adapter + "]";
  }

  /**
   * Default adapter for Integer.
   * 
   * @param value   The value to be converted.
   * 
   * @return   The converted value.
   */
  private static Integer toIntegerValue(String value) {
    return Integer.parseInt(value);
  }

  /**
   * Default adapter for String.
   * 
   * @param value   The value to be converted.
   * 
   * @return   The converted value.
   */
  private static String toStringValue(String value) {
    return value;
  }

  /**
   * Default adapter for Boolean.
   * 
   * @param value   The value to be converted.
   * 
   * @return   The converted value.
   */
  private static Boolean toBooleanValue(String value) {
    return Boolean.parseBoolean(value);
  }

  /**
   * Default adapter for byte[].
   * 
   * @param value   The value to be converted.
   * 
   * @return   The converted value.
   */
  private static byte[] toByteArrayValue(String value) {
    if (value != null) {
      return Encoding.UTF8.encode(value);
    } else {
      return null;
    }
  }

  /**
   * Default adapter for BigDecimal.
   * 
   * @param value   The value to be converted.
   * 
   * @return   The converted value.
   */
  private static BigDecimal toBigDecimalValue(String value) {
    return new BigDecimal(value);
  }

  public static <R> CsvColumnBuilder<R> builder() {
    return new CsvColumnBuilder<>();
  }
  
  public static class CsvColumnBuilder<R> {
    
    private CsvColumn<R>   instance = new CsvColumn<>();
    
    private CsvColumnBuilder() {
    }

    public CsvColumnBuilder<R> type(@NotNull Class<R> type) {
      instance.type = type;
      return this;
    }

    public CsvColumnBuilder<R> nullable() {
      return nullable(true);
    }

    public CsvColumnBuilder<R> nullable(boolean nullable) {
      instance.nullable = nullable;
      return this;
    }

    public CsvColumnBuilder<R> defaultValue(R defval) {
      instance.defval = defval;
      return this;
    }

    public CsvColumnBuilder<R> title(String title) {
      instance.title = title;
      return this;
    }

    public CsvColumnBuilder<R> adapter(Function<String, R> adapter) {
      instance.adapter = adapter;
      return this;
    }

    public CsvColumn<R> build() {
      return instance;
    }
    
  } /* ENDCLASS */

} /* ENDCLASS */
