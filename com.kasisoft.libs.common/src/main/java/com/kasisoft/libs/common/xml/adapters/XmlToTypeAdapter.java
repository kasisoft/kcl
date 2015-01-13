package com.kasisoft.libs.common.xml.adapters;

import javax.xml.bind.annotation.adapters.*;

import lombok.*;
import lombok.experimental.*;

/**
 * A simple helper class which allows to use the TypeAdapter implementations in an XML context.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class XmlToTypeAdapter<S,T> extends XmlAdapter<S,T>{

  TypeAdapter<S,T>   typeadapter;
  
  public XmlToTypeAdapter( TypeAdapter<S,T> adapter ) {
    typeadapter = adapter;
  }
  
  @Override
  public T unmarshal( @NonNull S v ) throws Exception {
    return typeadapter.unmarshal(v);
  }

  @Override
  public S marshal( @NonNull T v ) throws Exception {
    return typeadapter.marshal(v);
  }

} /* ENDCLASS */
