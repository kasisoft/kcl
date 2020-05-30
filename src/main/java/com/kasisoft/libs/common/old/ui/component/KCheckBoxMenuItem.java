package com.kasisoft.libs.common.old.ui.component;

import com.kasisoft.libs.common.old.config.*;
import com.kasisoft.libs.common.old.text.*;
import com.kasisoft.libs.common.old.workspace.*;
import com.kasisoft.libs.common.old.xml.adapters.*;

import javax.swing.*;

import lombok.experimental.*;

import lombok.*;

/**
 * Field used to provide a checkable menu item.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KCheckBoxMenuItem extends JCheckBoxMenuItem implements WorkspacePersistent {

  String                    property;
  SimpleProperty<Boolean>   propertyChecked;

  public KCheckBoxMenuItem( String title, String wsprop ) {
    super( title );
    property = StringFunctions.cleanup( wsprop );
    if( property != null ) {
      propertyChecked = new SimpleProperty<>( property, new BooleanAdapter() );
    }
  }
  
  @Override
  public void loadPersistentSettings() {
    if( propertyChecked != null ) {
      Boolean checked = propertyChecked.getValue( Workspace.getInstance().getProperties() );
      if( checked != null ) {
        setSelected( checked.booleanValue() );
      }
    }
  }

  @Override
  public void savePersistentSettings() {
    if( propertyChecked != null ) {
      propertyChecked.setValue( Workspace.getInstance().getProperties(), Boolean.valueOf( isSelected() ) );
    }
  }

} /* ENDCLASS */
