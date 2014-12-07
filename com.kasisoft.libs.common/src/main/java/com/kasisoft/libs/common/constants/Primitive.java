package com.kasisoft.libs.common.constants;

import com.kasisoft.libs.common.util.*;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Declarations used to identify primitive types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum Primitive {

  PBoolean  ( Boolean   . TYPE , Boolean   . class , boolean [] . class , Boolean   [] . class, 0                   , 0                   ) ,
  PByte     ( Byte      . TYPE , Byte      . class , byte    [] . class , Byte      [] . class, Byte    . MIN_VALUE , Byte    . MAX_VALUE ) ,
  PChar     ( Character . TYPE , Character . class , char    [] . class , Character [] . class, 0                   , 0                   ) ,
  PShort    ( Short     . TYPE , Short     . class , short   [] . class , Short     [] . class, Short   . MIN_VALUE , Short   . MAX_VALUE ) ,
  PInt      ( Integer   . TYPE , Integer   . class , int     [] . class , Integer   [] . class, Integer . MIN_VALUE , Integer . MAX_VALUE ) ,
  PLong     ( Long      . TYPE , Long      . class , long    [] . class , Long      [] . class, Long    . MIN_VALUE , Long    . MAX_VALUE ) ,
  PFloat    ( Float     . TYPE , Float     . class , float   [] . class , Float     [] . class, 0                   , 0                   ) ,
  PDouble   ( Double    . TYPE , Double    . class , double  [] . class , Double    [] . class, 0                   , 0                   ) ;
  
  /** Not <code>null</code>. */
  @Getter Class<?>    primitiveClass;
  
  /** Not <code>null</code>. */
  @Getter Class<?>    arrayClass;
  
  /** Not <code>null</code>. */
  @Getter Class<?>    objectClass;
  
  /** Not <code>null</code>. */
  @Getter Class<?>    objectArrayClass;
  
  @Getter long        min;
  @Getter long        max;
  
  Buffers             buffers;
  boolean             supportsMinMax;
  
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
    primitiveClass    = primitive;
    objectClass       = objclazz;
    objectArrayClass  = objectarraytype;
    arrayClass        = arraytype;
    min               = minval;
    max               = maxval;
    buffers           = null;
    supportsMinMax    = minval != maxval;
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
    return supportsMinMax;
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
    boolean primitivevariety = arrayobj.getClass() == arrayClass;
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
   * Delivers the primitive type associated with the supplied object. The supplied object may be an object type, an 
   * array of the primitive type or an array of the object type.
   * 
   * @param obj   The value which primitive equivalent should be returned. Maybe <code>null</code>.
   * 
   * @return   The primitive equivalent for the supplied type. Maybe <code>null</code>.
   */
  public static Primitive byType( Object obj ) {
    if( obj == null ) {
      return null;
    } else {
      return LocalData.primitivemap.get( obj.getClass() );
    }
  }

  private static class LocalData {
    
    private static Map<Class<?>,Primitive>   primitivemap = new Hashtable<>();
    
  } /* ENDCLASS */
  
} /* ENDENUM */
