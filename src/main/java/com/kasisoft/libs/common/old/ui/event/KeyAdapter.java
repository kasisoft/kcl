package com.kasisoft.libs.common.old.ui.event;

import java.util.function.*;

import java.awt.event.*;

import lombok.experimental.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KeyAdapter implements KeyListener {

  Consumer<KeyEvent> typed     = $ -> {};
  Consumer<KeyEvent> pressed   = $ -> {};
  Consumer<KeyEvent> released  = $ -> {};
  
  public KeyAdapter typed( Consumer<KeyEvent> handler ) {
    typed = handler != null ? handler : typed;
    return this;
  }

  public KeyAdapter pressed( Consumer<KeyEvent> handler ) {
    pressed = handler != null ? handler : pressed;
    return this;
  }

  public KeyAdapter released( Consumer<KeyEvent> handler ) {
    released = handler != null ? handler : released;
    return this;
  }

  @Override
  public void keyTyped( KeyEvent evt ) {
    typed.accept( evt );
  }

  @Override
  public void keyPressed( KeyEvent evt ) {
    pressed.accept( evt );
  }

  @Override
  public void keyReleased( KeyEvent evt ) {
    released.accept( evt );
  }

} /* ENDCLASS */
