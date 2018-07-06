package com.kasisoft.libs.common.ui.event;

import java.util.function.*;

import java.awt.event.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class MouseAdapter implements MouseListener, MouseMotionListener {

  Consumer<MouseEvent>    dragged   = $ -> {};
  Consumer<MouseEvent>    moved     = $ -> {};
  Consumer<MouseEvent>    clicked   = $ -> {};
  Consumer<MouseEvent>    pressed   = $ -> {};
  Consumer<MouseEvent>    released  = $ -> {};
  Consumer<MouseEvent>    entered   = $ -> {};
  Consumer<MouseEvent>    exited    = $ -> {};
  
  public MouseAdapter dragged( Consumer<MouseEvent> handler ) {
    dragged = handler != null ? handler : dragged;
    return this;
  }
  
  public MouseAdapter moved( Consumer<MouseEvent> handler ) {
    moved = handler != null ? handler : moved;
    return this;
  }

  public MouseAdapter clicked( Consumer<MouseEvent> handler ) {
    clicked = handler != null ? handler : clicked;
    return this;
  }

  public MouseAdapter pressed( Consumer<MouseEvent> handler ) {
    pressed = handler != null ? handler : pressed;
    return this;
  }

  public MouseAdapter released( Consumer<MouseEvent> handler ) {
    released = handler != null ? handler : released;
    return this;
  }

  public MouseAdapter entered( Consumer<MouseEvent> handler ) {
    entered = handler != null ? handler : entered;
    return this;
  }

  public MouseAdapter exited( Consumer<MouseEvent> handler ) {
    exited = handler != null ? handler : exited;
    return this;
  }

  @Override
  public void mouseDragged( MouseEvent evt ) {
    dragged.accept( evt );
  }

  @Override
  public void mouseMoved( MouseEvent evt ) {
    moved.accept( evt );
  }

  @Override
  public void mouseClicked( MouseEvent evt ) {
    clicked.accept( evt );
  }

  @Override
  public void mousePressed( MouseEvent evt ) {
    pressed.accept( evt );
  }

  @Override
  public void mouseReleased( MouseEvent evt ) {
    released.accept( evt );
  }

  @Override
  public void mouseEntered( MouseEvent evt ) {
    entered.accept( evt );
  }

  @Override
  public void mouseExited( MouseEvent evt ) {
    exited.accept( evt );
  }

} /* ENDCLASS */
