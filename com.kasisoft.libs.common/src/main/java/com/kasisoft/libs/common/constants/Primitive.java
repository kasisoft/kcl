/**
 * Name........: Primitive
 * Description.: Declarations used to identify primitive types.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.util.*;

import java.util.*;

import lombok.*;

/**
 * Declarations used to identify primitive types.
 */
public enum Primitive {

  PBoolean  ( Boolean   . TYPE , Boolean   . class , boolean [] . class , Boolean   [] . class, 0                   , 0                   ) ,
  PByte     ( Byte      . TYPE , Byte      . class , byte    [] . class , Byte      [] . class, Byte    . MIN_VALUE , Byte    . MAX_VALUE ) ,
  PChar     ( Character . TYPE , Character . class , char    [] . class , Character [] . class, 0                   , 0                   ) ,
  PShort    ( Short     . TYPE , Short     . class , short   [] . class , Short     [] . class, Short   . MIN_VALUE , Short   . MAX_VALUE ) ,
  PInt      ( Integer   . TYPE , Integer   . class , int     [] . class , Integer   [] . class, Integer . MIN_VALUE , Integer . MAX_VALUE ) ,
  PLong     ( Long      . TYPE , Long      . class , long    [] . class , Long      [] . class, Long    . MIN_VALUE , Long    . MAX_VALUE ) ,
  PFloat    ( Float     . TYPE , Float     . class , float   [] . class , Float     [] . class, 0                   , 0                   ) ,
  PDouble   ( Double    . TYPE , Double    . class , double  [] . class , Double    [] . class, 0                   , 0                   ) ;
  
  private Class<?>     primitiveclass;
  private Class<?>     arrayclass;
  private Class<?>     clazz;
  private Class<?>     objectarrayclass;
  private long         min;
  private long         max;
  private Buffers      buffers;
  private boolean      minmax;
  
  /**
   * Sets up this enumeration value.
   * 
   * @param primitive         The primitive type class. Not <code>null</code>.
   * @param objclazz          The object type class. Not <code>null</code>.
   * @param arraytype         The array type class. Not <code>null</code>.
   * @param objectarraytype   The object array type class. Not <code>null</code>. 
   * @param minval            The minimum value.
   * @param maxval            The maximum value.
   */
  Primitive( Class<?> primitive, Class<?> objclazz, Class<?> arraytype, Class<?> objectarraytype, long minval, long maxval ) {
    primitiveclass = primitive;
    clazz          = objclazz;
    arrayclass     = arraytype;
    min            = minval;
    max            = maxval;
    buffers        = null;
    minmax         = minval != maxval;
    LocalData.primitivemap.put( primitive       , this );
    LocalData.primitivemap.put( objclazz        , this );
    LocalData.primitivemap.put( arraytype       , this );
    LocalData.primitivemap.put( objectarraytype , this );
  }
  
  /**
   * Returns <code>true</code> if this type supports the usage of {@link #getMin()} and {@link #getMax()}.
   * 
   * @return   <code>true</code> <=> Using {@link #getMin()} and {@link #getMax()} is supported.
   */
  public boolean supportsMinMax() {
    return minmax;
  }
  
  /**
   * Returns the minimum value for this type. Not supported for PChar, PBoolean, PFloat and PDouble.
   * 
   * @return   The minimum value for this type.
   */
  public long getMin() {
    return min;
  }
  
  /**
   * Returns the maximum value for this type. Not supported for PChar, PBoolean, PFloat and PDouble.
   * 
   * @return   The maximum value for this type.
   */
  public long getMax() {
    return max;
  }
  
  /**
   * Returns the unsigned maximum value for this type. Not supported for PChar, PBoolean, PFloat, PDouble and PLong.
   * 
   * @return   The unsigned maximum value for this type. 
   */
  public long getUnsignedMax() {
    if( this == PLong ) {
      return 0;
    }
    return 2 * max + 1;
  }
  
  /**
   * Returns the primitive class instance.
   * 
   * @return   The primitive class instance. Not <code>null</code>.
   */
  public Class<?> getPrimitiveClass() {
    return primitiveclass;
  }
  
  /**
   * Returns the object class instance.
   * 
   * @return   The object class instance. Not <code>null</code>.
   */
  public Class<?> getObjectClass() {
    return clazz;
  }
  
  /**
   * Returns the array class instance.
   * 
   * @return   The array class instance. Not <code>null</code>.
   */
  public Class<?> getArrayClass() {
    return arrayclass;
  }
  
  /**
   * Returns the object array class instance.
   *  
   * @return   The object array class instance. Not <code>null</code>.
   */
  public Class<?> getObjectArrayClass() {
    return objectarrayclass;
  }
  
  /**
   * Returns an array of this type consisting of the supplied number of items.
   * 
   * @param size   The number of items.
   * 
   * @return   The array of this type. Not <code>null</code>.
   */
  public <T> T newArray( int size ) {
    switch( this ) {
    case PBoolean : return (T) new boolean [ size ];
    case PByte    : return (T) new byte    [ size ];
    case PChar    : return (T) new char    [ size ];
    case PShort   : return (T) new short   [ size ];
    case PInt     : return (T) new int     [ size ];
    case PLong    : return (T) new long    [ size ];
    case PFloat   : return (T) new float   [ size ];
    /* case PDouble: */
    default       : return (T) new double  [ size ];
    }
  }

  /**
   * Returns an array of this type consisting of the supplied number of items.
   * 
   * @param size   The number of items.
   * 
   * @return   The array of the corresponding object type. Not <code>null</code>.
   */
  public <T> T newObjectArray( int size ) {
    switch( this ) {
    case PBoolean : return (T) new Boolean    [ size ];
    case PByte    : return (T) new Byte       [ size ];
    case PChar    : return (T) new Character  [ size ];
    case PShort   : return (T) new Short      [ size ];
    case PInt     : return (T) new Integer    [ size ];
    case PLong    : return (T) new Long       [ size ];
    case PFloat   : return (T) new Float      [ size ];
    /* case PDouble: */
    default       : return (T) new Double     [ size ];
    }
  }

  /**
   * Returns the {@link Buffers} instance for this type.
   * 
   * @return   The {@link Buffers} instance for this type. Not <code>null</code>.
   */
  public synchronized <T> Buffers<T> getBuffers() {
    if( buffers == null ) {
      buffers = Buffers.newBuffers( this );
    }
    return buffers;
  }
  
  /**
   * Returns the length of an array instance.
   * 
   * @param arrayobj    An array instance. Not <code>null</code>.
   * 
   * @return   The length of an array instance.
   */
  public int length( @NonNull Object arrayobj ) {
    boolean primitivevariety = arrayobj.getClass() == arrayclass;
    if( primitivevariety ) {
      switch( this ) {
      case PBoolean : return ((boolean []) arrayobj).length;
      case PByte    : return ((byte    []) arrayobj).length;
      case PChar    : return ((char    []) arrayobj).length;
      case PShort   : return ((short   []) arrayobj).length;
      case PInt     : return ((int     []) arrayobj).length;
      case PLong    : return ((long    []) arrayobj).length;
      case PFloat   : return ((float   []) arrayobj).length;
        /* case PDouble: */
      default       : return ((double  []) arrayobj).length;
      }
    } else {
      return ((Object[]) arrayobj).length;
    }
  }
  
  /**
   * Returns the Primitive identified through it's array type.
   * 
   * @param obj   An instance of the array. Not <code>null</code>.
   * 
   * @return   The Primitive constant or <code>null</code> in case of an invalid array type.
   */
  public static Primitive byArrayType( @NonNull Object obj ) {
    return LocalData.primitivemap.get( obj.getClass() );
  }
  
  /**
   * Returns the Primitive identified through it's object type.
   * 
   * @param obj   An instance of the object. Not <code>null</code>.
   * 
   * @return   The Primitive constant or <code>null</code> in case of an invalid object type.
   */
  public static Primitive byObjectType( @NonNull Object obj ) {
    return LocalData.primitivemap.get( obj.getClass() );
  }

  private static class LocalData {
    
    private static Map<Class<?>,Primitive>   primitivemap = new Hashtable<Class<?>,Primitive>();
    
  } /* ENDCLASS */
  
} /* ENDENUM */
