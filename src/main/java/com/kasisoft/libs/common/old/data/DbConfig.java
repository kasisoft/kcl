package com.kasisoft.libs.common.old.data;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.Data;

/**
 * Configuration allowing to setup a connection to a jdbc database.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public final class DbConfig {

  Database   db;
  String     username;
  String     password;
  String     url;
  
} /* ENDCLASS */
