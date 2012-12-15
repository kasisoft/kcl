/**
 * Name........: SwingFunctions
 * Description.: Collection of Swing related utility functions.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.swing;

import com.kasisoft.lgpl.libs.common.workspace.*;
import com.kasisoft.lgpl.tools.diagnostic.*;

import java.awt.*;

/**
 * Collection of Swing related utility functions.
 */
@KDiagnostic(loggername="com.kasisoft.lgpl.libs.common")
public class SwingFunctions {

  
  /**
   * Modifies the boundaries of a component so it's being centered related to another component.
   * 
   * @param relocatable   The component which boundaries have to be adjusted. Not <code>null</code>.
   * @param related       The related container. If <code>null</code> the screen dimensions are used.
   *                      You can alternatively use {@link #center(Component)}.
   */
  public static final void center( @KNotNull(name="relocatable") Component relocatable, Component related ) {
    if( related == null ) {
      center( relocatable );
    } else {
      Dimension relosize    = relocatable.getSize();
      Dimension relatedsize = related.getSize();
      int       width       = relatedsize.width  - relosize.width;
      int       height      = relatedsize.height - relosize.height;
      Point     location    = related.getLocation();
      relocatable.setBounds( location.x + width / 2, location.y + height / 2, relosize.width, relosize.height );
    }
  }
  

  /**
   * Modifies the boundaries of the supplied component so it will be centered on the screen. If the
   * size of the component exceeds the size of the screen it's location will be modified accordingly
   * to make sure it's in a visible area.
   * 
   * @param relocatable   The component which boundaries have to be adjusted. Not <code>null</code>.
   */
  public static final void center( @KNotNull(name="relocatable") Component relocatable ) {
    Dimension relosize    = relocatable.getSize();
    Dimension screensize  = Toolkit.getDefaultToolkit().getScreenSize();
    int       width       = screensize.width  - relosize.width;
    int       height      = screensize.height - relosize.height;
    relocatable.setBounds( Math.max( width / 2, 0 ), Math.max( height / 2, 0 ), relosize.width, relosize.height );
  }
  
  /**
   * Restores the bounds of a component. The restauration only happens if there were settings. This
   * function is typically called when a component will be initialised.
   * 
   * @param property    The property used to access the restored bounds. Neither <code>null</code> nor empty.
   * @param component   The component which has to be configured. Not <code>null</code>.
   */
  public static final void restoreBounds( @KNotEmpty(name="property") String property, @KNotNull(name="component") Component component ) {
    Rectangle bounds = Workspace.getInstance().getRectangle( property );
    if( bounds != null ) {
      component.setBounds( bounds );
    }
  }

} /* ENDCLASS */
