/**
 * Name........: Primitive
 * Description.: Declarations used to identify primitive types.
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.constants;

import com.kasisoft.lgpl.tools.diagnostic.*;

/**
 * Declarations used to identify primitive types.
 */
@KDiagnostic
public enum Primitive {

  PBoolean             ( Boolean   . TYPE , Boolean   . class , boolean [] . class , 0                   , 0                   ) ,
  PByte                ( Byte      . TYPE , Byte      . class , byte    [] . class , Byte    . MIN_VALUE , Byte    . MAX_VALUE ) ,
  PChar                ( Character . TYPE , Character . class , char    [] . class , 0                   , 0                   ) ,
  PShort               ( Short     . TYPE , Short     . class , short   [] . class , Short   . MIN_VALUE , Short   . MAX_VALUE ) ,
  PInt                 ( Integer   . TYPE , Integer   . class , int     [] . class , Integer . MIN_VALUE , Integer . MAX_VALUE ) ,
  PLong                ( Long      . TYPE , Long      . class , long    [] . class , Long    . MIN_VALUE , Long    . MAX_VALUE ) ,
  PFloat               ( Float     . TYPE , Float     . class , float   [] . class , 0                   , 0                   ) ,
  PDouble              ( Double    . TYPE , Double    . class , double  [] . class , 0                   , 0                   ) ;
  
  private Class<?>     primitiveclass;
  private Class<?>     arrayclass;
  private Class<?>     clazz;
  private long         min;
  private long         max;
  
  /**
   * Sets up this enumeration value.
   * 
   * @param primitive   The primitive type class. Not <code>null</code>.
   * @param objclazz    The object type class. Not <code>null</code>.
   * @param arraytype   The array type class. Not <code>null</code>.
   * @param minval      The minimum value.
   * @param maxval      The maximum value.
   */
  Primitive( Class<?> primitive, Class<?> objclazz, Class<?> arraytype, long minval, long maxval ) {
    primitiveclass = primitive;
    clazz          = objclazz;
    arrayclass     = arraytype;
    min            = minval;
    max            = maxval;
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
   * Returns <code>true</code> if this type supports the usage of {@link #getMin()} and {@link #getMax()}.
   * 
   * @return   <code>true</code> <=> Using {@link #getMin()} and {@link #getMax()} is supported.
   */
  public boolean supportsMinMax() {
    return (this == PByte ) || (this == PShort) || (this == PInt  ) || (this == PLong );
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
   * Returns an array of this type consisting of the supplied number of items.
   * 
   * @param size   The number of items.
   * 
   * @return   The array of this type.
   */
  public Object newArray( @KIPositive(name="size", zero=true) int size ) {
              if( this == PBoolean )    { return new boolean [ size ];
    } else    if( this == PByte    )    { return new byte    [ size ];
    } else    if( this == PChar    )    { return new char    [ size ];
    } else    if( this == PShort   )    { return new short   [ size ];
    } else    if( this == PInt     )    { return new int     [ size ];
    } else    if( this == PLong    )    { return new long    [ size ];
    } else    if( this == PFloat   )    { return new float   [ size ];
    } else /* if( this == PDouble  ) */ { return new double  [ size ];
    }
  }
  
  /**
   * Returns the length of an array instance.
   * 
   * @param arrayobj    An array instance. Not <code>null</code>.
   * 
   * @return   The length of an array instance.
   */
  public int length( 
    @KInstance(name="arrayobj", 
      allowed = {boolean[].class, byte[].class, char[].class, short[].class, int[].class, long[].class, float[].class, double[].class}
    ) 
    Object arrayobj 
  ) {
              if( this == PBoolean )    { return ((boolean []) arrayobj).length;
    } else    if( this == PByte    )    { return ((byte    []) arrayobj).length;
    } else    if( this == PChar    )    { return ((char    []) arrayobj).length;
    } else    if( this == PShort   )    { return ((short   []) arrayobj).length;
    } else    if( this == PInt     )    { return ((int     []) arrayobj).length;
    } else    if( this == PLong    )    { return ((long    []) arrayobj).length;
    } else    if( this == PFloat   )    { return ((float   []) arrayobj).length;
    } else /* if( this == PDouble  ) */ { return ((double  []) arrayobj).length;
    }
  }
  
  /**
   * Returns the Primitive identified through it's array type.
   * 
   * @param obj   An instance of the array. Not <code>null</code>.
   * 
   * @return   The Primitive constant or <code>null</code> in case of an invalid array type.
   */
  public static final Primitive byArrayType(
    @KNotNull(name="obj")
    Object obj 
  ) {
              if( obj instanceof boolean [] )    { return PBoolean;
    } else    if( obj instanceof byte    [] )    { return PByte;
    } else    if( obj instanceof char    [] )    { return PChar;
    } else    if( obj instanceof short   [] )    { return PShort;
    } else    if( obj instanceof int     [] )    { return PInt;
    } else    if( obj instanceof long    [] )    { return PLong;
    } else    if( obj instanceof float   [] )    { return PFloat;
    } else /* if( obj instanceof double  [] ) */ { return PDouble;
    }
  }
  
  /**
   * Returns the Primitive identified through it's object type.
   * 
   * @param obj   An instance of the object. Not <code>null</code>.
   * 
   * @return   The Primitive constant or <code>null</code> in case of an invalid object type.
   */
  public static final Primitive byObjectType(
    @KNotNull(name="obj")
    Object obj 
  ) {
              if( obj instanceof Boolean   )    { return PBoolean;
    } else    if( obj instanceof Byte      )    { return PByte;
    } else    if( obj instanceof Character )    { return PChar;
    } else    if( obj instanceof Short     )    { return PShort;
    } else    if( obj instanceof Integer   )    { return PInt;
    } else    if( obj instanceof Long      )    { return PLong;
    } else    if( obj instanceof Float     )    { return PFloat;
    } else /* if( obj instanceof Double    ) */ { return PDouble;
    }
  }

} /* ENDENUM */
