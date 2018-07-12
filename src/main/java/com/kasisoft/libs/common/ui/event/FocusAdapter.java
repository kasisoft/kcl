package com.kasisoft.libs.common.ui.event;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.function.Consumer;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FocusAdapter implements FocusListener {

  Consumer<FocusEvent>      gained = $ -> {};
  Consumer<FocusEvent>      lost   = $ -> {};
  
  public FocusAdapter gained( Consumer<FocusEvent> handler ) {
    gained = handler != null ? handler : gained;
    return this;
  }

  public FocusAdapter lost( Consumer<FocusEvent> handler ) {
    lost = handler != null ? handler : lost;
    return this;
  }

  @Override
  public void focusGained( FocusEvent evt ) {
    gained.accept( evt );
  }

  @Override
  public void focusLost( FocusEvent evt ) {
    lost.accept( evt );
  }

} /* ENDCLASS */
