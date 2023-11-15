package com.kasisoft.libs.common.data;

/**
 * Configuration allowing to setup a connection to a jdbc database.
 *
 * @author daniel.kasmeroglu@kasisoft.com
 */
public record DbConfig(String url, Database db, String username, String password) {
} /* ENDRECORD */
