package com.kasisoft.libs.common.config;

import com.kasisoft.libs.common.text.*;

import lombok.experimental.*;

import lombok.*;

import java.util.*;


/**
 * A simple helper which provides infos to be used across all properties.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PropertiesConfig {

  String               varFormatter;
  boolean              resolve;
  Map<String,String>   replacements;
  
  public PropertiesConfig( String newvarformatter, boolean newresolve ) {
    varFormatter  = newvarformatter;
    resolve       = newresolve;
    replacements  = ConfigurationHelper.createReplacementMap( varFormatter );
  }
  
  /**
   * This function resolves property values while replacing it's expressions.
   * 
   * @param value   The current property value. Neither <code>null</code> nor empty.
   * 
   * @return   The resolved property value. Neither <code>null</code> nor empty.
   */
  public String resolve( @NonNull String value ) {
    if( resolve ) {
      return StringFunctions.replace( value, replacements );
    } else {
      return value;
    }
  }
  
} /* ENDCLASS */