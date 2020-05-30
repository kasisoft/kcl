package com.kasisoft.libs.common.old.constants;

import com.kasisoft.libs.common.old.internal.constants.*;

import java.util.function.*;

import java.util.*;

import lombok.*;

/**
 * Declarations used to identify primitive types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface Primitive<PA, O> extends Comparable<Primitive> {

  Primitive <boolean [], Boolean  > PBoolean = PrimitiveImpl.INTERNAL_BOOLEAN;
  Primitive <byte    [], Byte     > PByte    = PrimitiveImpl.INTERNAL_BYTE;
  Primitive <char    [], Character> PChar    = PrimitiveImpl.INTERNAL_CHAR;
  Primitive <short   [], Short    > PShort   = PrimitiveImpl.INTERNAL_SHORT;
  Primitive <int     [], Integer  > PInt     = PrimitiveImpl.INTERNAL_INT;
  Primitive <long    [], Long     > PLong    = PrimitiveImpl.INTERNAL_LONG;
  Primitive <float   [], Float    > PFloat   = PrimitiveImpl.INTERNAL_FLOAT;
  Primitive <double  [], Double   > PDouble  = PrimitiveImpl.INTERNAL_DOUBLE;
  
  Class<?> getPrimitiveClass();
  
  Class<PA> getArrayClass();
  
  Class<O> getObjectClass();
  
  Class<O[]> getObjectArrayClass();
  
  long getMin();
  
  long getMax();
  
  /**
   * Tries to find a char sequence within a data block.
   * 
   * @param buffer     The data block being investigated. Not <code>null</code>.
   * @param sequence   The char sequence to search for. Not <code>null</code>.
   * 
   * @return   The index of the char sequence or -1 in case there's no sequence.
   */
  int indexOf( @NonNull PA buffer, @NonNull PA sequence );
  
  /**
   * Tries to find a char sequence within a data block.
   * 
   * @param buffer     The data block being investigated. Not <code>null</code>.
   * @param sequence   The char sequence to search for. Not <code>null</code>.
   * @param pos        The offset where to begin the search.
   * 
   * @return   The index of the char sequence or -1 in case there's no sequence.
   */
  int indexOf( @NonNull PA buffer, @NonNull PA sequence, int pos );
  
  /**
   * Tries to find the last byte sequence within a data block.
   * 
   * @param data       The data block being investigated. Not <code>null</code>.
   * @param sequence   The byte sequence to search for. Not <code>null</code>.
   * 
   * @return   The index of the last byte sequence or -1 in case there's no sequence.
   */
  int lastIndexOf( @NonNull PA buffer, @NonNull PA sequence );

  /**
   * Tries to find the last byte sequence within a data block.
   * 
   * @param buffer     The data block being investigated. Not <code>null</code>.
   * @param sequence   The byte sequence to search for. Not <code>null</code>.
   * @param pos        The offset where to begin the search.
   * 
   * @return   The index of the last byte sequence or -1 in case there's no sequence.
   */
  int lastIndexOf( @NonNull PA buffer, @NonNull PA sequence, int pos );
  
  /**
   * Creates a new byte sequence while inserting one into a data block. If the index is outside of the destination no 
   * insertion takes place.
   * 
   * @param source      The current data block which will be modified. Not <code>null</code>.
   * @param additional  The byte sequence which has to be inserted. Not <code>null</code>.
   * @param index       The location where to insert the byte sequence.
   * 
   * @return   The modified data block. Not <code>null</code>.
   */
  PA insert( @NonNull PA source, @NonNull PA additional, int index );

  <R> R forValues( O[] values, BiFunction<O, R, R> func );
  
  <R> R forValues( O[] values, R initial, BiFunction<O, R, R> func );

  <R> R forValues( PA values, BiFunction<O, R, R> func );
  
  <R> R forValues( PA values, R initial, BiFunction<O, R, R> func );
  
  O or( PA values );

  O and( PA values );

  O or( O ... values );

  O and( O ... values );

  O max( PA values );

  O min( PA values );

  PA concat( PA ... parts );
       
  PA copy( @NonNull PA input );

  PA copy( @NonNull PA input, int newlength );

  PA copyOfRange( @NonNull PA input, int from, int to );

  O randomValue();

  PA randomArray( int length );
  
  PA randomArray( int length, Supplier<O> random );

  boolean compare( @NonNull PA data, @NonNull PA tocompare );
  
  boolean compare( @NonNull PA data, @NonNull PA tocompare, int offset );
  
  PA cleanup( PA input );
  
  O[] cleanup( O ... input );
  
  O[] cleanup( Predicate<O> isNotSet, O ... input );

  int countSet( O ... input );
  
  int countSet( Predicate<O> isSet, O ... input );
  
  int countUnset( O ... input );
  
  int countUnset( Predicate<O> isNotSet, O ... input );

  List<O> toList( PA primitiveArray );

  O[] toObjectArray( PA primitiveArray );

  PA toPrimitiveArray( O[] objectArray );

  /**
   * Returns <code>true</code> if this type supports the usage of {@link #getMin()} and {@link #getMax()}.
   * 
   * @return   <code>true</code> <=> Using {@link #getMin()} and {@link #getMax()} is supported.
   */
  boolean supportsMinMax();
  
  /**
   * Returns the unsigned maximum value for this type. Not supported for PChar, PBoolean, PFloat, PDouble and PLong.
   * 
   * @return   The unsigned maximum value for this type. 
   */
  long getUnsignedMax();
  
  /**
   * Returns an array of this type consisting of the supplied number of items.
   * 
   * @param size   The number of items.
   * 
   * @return   The array of this type. Not <code>null</code>.
   */
  PA newArray( int size );

  /**
   * Returns an array of this type consisting of the supplied number of items.
   * 
   * @param size   The number of items.
   * 
   * @return   The array of the corresponding object type. Not <code>null</code>.
   */
  O[] newObjectArray( int size );

  /**
   * Returns the length of an array instance.
   * 
   * @param arrayobj    An array instance. Maybe <code>null</code>.
   * 
   * @return   The length of an array instance.
   */
  int length( Object arrayobj );
  
  /**
   * Allocates a block with the current default size.
   * 
   * @return  A block with the current default size. Neither <code>null</code> nor empty.
   */
  PA allocate();
  
  /**
   * Allocates a block with a specified size. The returned block doesn't necessarily have the desired size so the 
   * returned block might be larger.
   * 
   * @param size   The requested size of the returned buffer. A value of <code>null</code> means that the default size 
   *               has to be used (see {@link CommonProperty#BufferCount}).
   * 
   * @return   A block with the requested size. Neither <code>null</code> nor empty.
   */
  PA allocate( Integer size );
  
  /**
   * Releases the supplied byte array so it can be reallocated later.
   * 
   * @param data   The data that can be reallocated later. Not <code>null</code>.
   */
  void release( @NonNull PA data );

  /**
   * Executes the supplied function while providing a buffer.
   * 
   * @param function   The function that will be executed using the buffer. Not <code>null</code>.
   * 
   * @return   The result of the supplied function. Maybe <code>null</code>.
   */
  <R> R withBuffer( @NonNull Function<PA, R> function );

  /**
   * Executes the supplied function while providing a buffer.
   *
   * @param size       The requested size of the returned buffer. A value of <code>null</code> means that the default 
   *                   size has to be used (see {@link CommonProperty#BufferCount}).
   * @param function   The function that will be executed using the buffer. Not <code>null</code>.
   * 
   * @return   The result of the supplied function. Maybe <code>null</code>.
   */
  <R> R withBuffer( Integer size, Function<PA, R> function );

  /**
   * Executes the supplied function while providing a buffer.
   * 
   * @param function   The function that will be executed using the buffer. Not <code>null</code>.
   */
  void withBufferDo( Consumer<PA> consumer );

  /**
   * Executes the supplied function while providing a buffer.
   * 
   * @param size       The requested size of the returned buffer. A value of <code>null</code> means that the default 
   *                   size has to be used (see {@link CommonProperty#BufferCount}).
   * @param function   The function that will be executed using the buffer. Not <code>null</code>.
   */
  void withBufferDo( Integer size, Consumer<PA> consumer );
  
  static Primitive[] values() {
    return PrimitiveImpl.valuesImpl();
  }
  
  /**
   * Delivers the primitive type associated with the supplied object. The supplied object may be an object type, an 
   * array of the primitive type or an array of the object type.
   * 
   * @param obj   The value which primitive equivalent should be returned. Maybe <code>null</code>.
   * 
   * @return   The primitive equivalent for the supplied type. Maybe <code>null</code>.
   */
  static Primitive byType( Object obj ) {
    return PrimitiveImpl.byTypeImpl( obj );
  }
  
} /* ENDINTERFACE */
