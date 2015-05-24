package com.kasisoft.libs.common.workspace;

import com.kasisoft.libs.common.config.*;

import java.awt.event.*;

import java.awt.*;

import lombok.*;
import lombok.experimental.*;

/**
 * A ComponentListener implementation which stores the settings of a Component to the Workspace.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WSComponentListener extends ComponentAdapter implements WSListener<Component> {

  SimpleProperty<Rectangle>   property;
  
  /**
   * Sets up this listener using the supplied key allowing to access the Workspace.
   * 
   * @param newproperty   The newproperty used to persist the settings. Not <code>null</code>.
   */
  public WSComponentListener( @NonNull SimpleProperty<Rectangle> newproperty ) {
    property = newproperty;
  }

  @Override
  public void componentMoved( @NonNull ComponentEvent evt ) {
    Component component = evt.getComponent();
    property.setValue( Workspace.getInstance().getProperties(), component.getBounds() );
  }

  @Override
  public void componentResized( @NonNull ComponentEvent evt ) {
    Component component = evt.getComponent();
    property.setValue( Workspace.getInstance().getProperties(), component.getBounds() );
  }

  @Override
  public void configure( Component component ) {
    if( property != null ) {
      Rectangle bounds = property.getValue( Workspace.getInstance().getProperties() );
      if( bounds != null ) {
        component.setBounds( bounds );
      }
    }
  }

} /* ENDCLASS */
