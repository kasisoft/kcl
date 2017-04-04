package com.kasisoft.libs.common.ui.component;

import com.kasisoft.libs.common.config.*;
import com.kasisoft.libs.common.text.*;
import com.kasisoft.libs.common.workspace.*;
import com.kasisoft.libs.common.xml.adapters.*;

import javax.swing.*;

import javax.xml.bind.*;

import java.util.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Field that provides a password input.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KPasswordTextField extends JPasswordField implements WorkspacePersistent {

  private static final int[] SHIFTS = new int[] {
    16, 7, 3, -18, 901  
  };
  
  SimpleProperty<String>   property;

  public KPasswordTextField( @NonNull String wsprop ) {
    String prop = StringFunctions.cleanup( wsprop );
    if( prop != null ) {
      property = new SimpleProperty<>( prop, new StringAdapter() );
    }
  }
  
  @Override
  public String getPersistentProperty() {
    return property != null ? property.getKey() : null;
  }

  @Override
  public void loadPersistentSettings() {
    if( property != null ) {
      String pwd = StringFunctions.cleanup( property.getValue( Workspace.getInstance().getProperties() ) );
      if( pwd != null ) {
        setText( decode( pwd, SHIFTS ) );
      }
    }
  }

  @Override
  public void savePersistentSettings() {
    if( property != null ) {
      String pwd = encode( getPassword(), SHIFTS );
      if( pwd != null ) {
        property.setValue( Workspace.getInstance().getProperties(), pwd );
      }
    }
  }
  
  private String encode( char[] passwd, int[] shifts ) {
    String        result  = null;
    if( (passwd != null) && (passwd.length > 0) ) {
      StringBuilder builder = new StringBuilder();
      int           i       = 0;
      for( int ch : passwd ) {
        int shift = shifts[ i % shifts.length ];
        i++;
        int val   = ch + shift;
        builder.append( val );
        builder.append( "," );
      }
      Arrays.fill( passwd, '\2' );
      result = DatatypeConverter.printBase64Binary( builder.toString().getBytes() );
    }
    return result;
  }
  
  private String decode( String passwdEnc, int[] shifts ) {
    String result = null;
    if( passwdEnc != null ) {
      byte[]   data   = DatatypeConverter.parseBase64Binary( passwdEnc );
      String[] strs   = new String( data ).split(",");
      char[]   chars  = new char[ strs.length ];
      int      i      = 0;
      for( String str : strs ) {
        int shift = shifts[ i % shifts.length ];
        int val   = Integer.parseInt( str ) - shift;
        chars[i]  = (char) val;
        i++;
      }
      result = new String( chars );
    }
    return result;
  }
  
} /* ENDCLASS */
