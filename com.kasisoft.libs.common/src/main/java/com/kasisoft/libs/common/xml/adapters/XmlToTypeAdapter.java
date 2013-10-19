/**
 * Name........: XmlToTypeAdapter
 * Description.: A simple helper class which allows to use the TypeAdapter implementations in an XML context.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.xml.adapters;

import javax.xml.bind.annotation.adapters.*;

/**
 * A simple helper class which allows to use the TypeAdapter implementations in an XML context.
 */
public class XmlToTypeAdapter<S,T> extends XmlAdapter<S,T>{

  private TypeAdapter<S,T>   typeadapter;
  
  public XmlToTypeAdapter( TypeAdapter<S,T> adapter ) {
    typeadapter = adapter;
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public T unmarshal( S v ) throws Exception {
    return typeadapter.unmarshal(v);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public S marshal( T v ) throws Exception {
    return typeadapter.marshal(v);
  }

} /* ENDCLASS */
