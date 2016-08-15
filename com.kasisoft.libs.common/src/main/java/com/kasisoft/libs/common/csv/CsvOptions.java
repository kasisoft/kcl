package com.kasisoft.libs.common.csv;

import com.kasisoft.libs.common.constants.*;

import lombok.experimental.*;

import lombok.*;

import java.util.stream.*;

import java.util.*;

/**
 * A collection of options used for the csv processing.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter @ToString @EqualsAndHashCode
public final class CsvOptions {

  boolean           titleRow           = false;
  char              delimiter          = ',';
  boolean           disableCr          = true;
  boolean           fillMissingColumns = false;
  Encoding          encoding           = Encoding.UTF8;
  List<CsvColumn>   columns            = new ArrayList<>();
  
  private CsvOptions() {
  }
  
  /**
   * Creates a deep copy of this instance.
   * 
   * @return   A deep copy of this instance. Not <code>null</code>.
   */
  public CsvOptions deepCopy() {
    CsvOptions result         = new CsvOptions();
    result.titleRow           = titleRow;
    result.delimiter          = delimiter;
    result.disableCr          = disableCr;
    result.fillMissingColumns = fillMissingColumns;
    result.encoding           = encoding;
    result.columns            = columns.stream().map( CsvColumn::copy ).collect( Collectors.toList() );
    return result;
  }
  
  public static CsvOptionsBuilder builder() {
    return new CsvOptionsBuilder();
  }
  
  @FieldDefaults(level = AccessLevel.PRIVATE)
  public static class CsvOptionsBuilder {
    
    CsvOptions   instance = new CsvOptions();
    
    private CsvOptionsBuilder() {
    }

    public CsvOptionsBuilder encoding( @NonNull Encoding encoding ) {
      instance.encoding = encoding;
      return this;
    }

    public CsvOptionsBuilder delimiter( char delimiter ) {
      instance.delimiter = delimiter;
      return this;
    }
    
    public CsvOptionsBuilder fillMissingColumns() {
      return fillMissingColumns( true );
    }
    
    public CsvOptionsBuilder fillMissingColumns( boolean enable ) {
      instance.fillMissingColumns = enable;
      return this;
    }
    
    public CsvOptionsBuilder disableCr() {
      return disableCr( true );
    }
    
    public CsvOptionsBuilder disableCr( boolean enable ) {
      instance.disableCr = enable;
      return this;
    }
    
    public CsvOptionsBuilder titleRow() {
      return titleRow( true );
    }
    
    public CsvOptionsBuilder titleRow( boolean enable ) {
      instance.titleRow = enable;
      return this;
    }

    public CsvOptionsBuilder column( @NonNull CsvColumn column ) {
      instance.columns.add( column );
      return this;
    }
    
    public CsvOptionsBuilder columns( @NonNull List<CsvColumn> columns ) {
      instance.columns.clear();
      instance.columns.addAll( columns );
      return this;
    }

    public CsvOptions build() {
      return instance;
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
