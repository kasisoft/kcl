package com.kasisoft.libs.common.old.ui.event;

import java.util.function.*;

import java.awt.event.*;

import lombok.experimental.*;

import lombok.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComponentAdapter implements ComponentListener {

  Consumer<ComponentEvent> resized  = $ -> {};
  Consumer<ComponentEvent> moved    = $ -> {};
  Consumer<ComponentEvent> shown    = $ -> {};
  Consumer<ComponentEvent> hidden   = $ -> {};
  
  public ComponentAdapter resized( Consumer<ComponentEvent> handler ) {
    resized = handler != null ? handler : resized;
    return this;
  }

  public ComponentAdapter moved( Consumer<ComponentEvent> handler ) {
    moved = handler != null ? handler : moved;
    return this;
  }

  public ComponentAdapter shown( Consumer<ComponentEvent> handler ) {
    shown = handler != null ? handler : shown;
    return this;
  }

  public ComponentAdapter hidden( Consumer<ComponentEvent> handler ) {
    hidden = handler != null ? handler : hidden;
    return this;
  }

  @Override
  public void componentResized( ComponentEvent evt ) {
    resized.accept( evt );
  }

  @Override
  public void componentMoved( ComponentEvent evt ) {
    moved.accept( evt );
  }

  @Override
  public void componentShown( ComponentEvent evt ) {
    shown.accept( evt );
  }

  @Override
  public void componentHidden( ComponentEvent evt ) {
    hidden.accept( evt );
  }

} /* ENDCLASS */
