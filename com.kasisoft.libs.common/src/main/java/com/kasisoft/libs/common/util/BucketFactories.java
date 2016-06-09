package com.kasisoft.libs.common.util;

import java.util.*;

import java.io.*;

/**
 * Helpers generating BucketFactory instances for widely used types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class BucketFactories {

  /**
   * Creates a new factory for {@link TreeMap}.
   * 
   * @return   A new factory for {@link TreeMap}. Not <code>null</code>.
   */
  public static <K, V> BucketFactory<TreeMap<K, V>> newTreeMapFactory() {
    return new DefaultBucketFactory<TreeMap<K, V>, Integer>( TreeMap::new, TreeMap::clear );
  }

  /**
   * Creates a new factory for {@link Hashtable}.
   * 
   * @return   A new factory for {@link Hashtable}. Not <code>null</code>.
   */
  public static <K, V> BucketFactory<Hashtable<K, V>> newHashtableFactory() {
    return new DefaultBucketFactory<Hashtable<K, V>, Integer>( Hashtable::new, Hashtable::clear );
  }

  /**
   * Creates a new factory for {@link HashMap}.
   * 
   * @return   A new factory for {@link HashMap}. Not <code>null</code>.
   */
  public static <K,V> BucketFactory<HashMap<K, V>> newHashMapFactory() {
    return new DefaultBucketFactory<HashMap<K, V>, Integer>( HashMap::new, HashMap::clear );
  }
    
  /**
   * Creates a new factory for {@link StringBuilder}.
   * 
   * @return   A new factory for {@link StringBuilder}. Not <code>null</code>.
   */
  public static BucketFactory<StringBuilder> newStringBuilderFactory() {
    return new DefaultBucketFactory<StringBuilder, Integer>( StringBuilder::new, StringBuilder::setLength, 0 );
  }
  
  /**
   * Creates a new factory for {@link StringBuffer}.
   * 
   * @return   A new factory for {@link StringBuffer}. Not <code>null</code>.
   */
  public static BucketFactory<StringBuffer> newStringBufferFactory() {
    return new DefaultBucketFactory<StringBuffer, Integer>( StringBuffer::new, StringBuffer::setLength, 0 );
  }

  /**
   * Creates a new factory for {@link StringFBuilder}.
   * 
   * @return   A new factory for {@link StringFBuilder}. Not <code>null</code>.
   */
  public static BucketFactory<StringFBuilder> newStringFBuilderFactory() {
    return new DefaultBucketFactory<StringFBuilder, Integer>( StringFBuilder::new, StringFBuilder::setLength, 0 );
  }
  
  /**
   * Creates a new factory for {@link StringFBuffer}.
   * 
   * @return   A new factory for {@link StringFBuffer}. Not <code>null</code>.
   */
  public static BucketFactory<StringFBuffer> newStringFBufferFactory() {
    return new DefaultBucketFactory<StringFBuffer, Integer>( StringFBuffer::new, StringFBuffer::setLength, 0 );
  }

  /**
   * Creates a new factory for {@link ByteArrayOutputStream}.
   * 
   * @return   A new factory for {@link ByteArrayOutputStream}. Not <code>null</code>.
   */
  public static BucketFactory<ByteArrayOutputStream> newByteArrayOutputStreamFactory() {
    return new DefaultBucketFactory<ByteArrayOutputStream, Void>( ByteArrayOutputStream::new, ByteArrayOutputStream::reset );
  }

  /**
   * Creates a new factory for {@link ArrayList}.
   * 
   * @return   A new factory for {@link ArrayList}. Not <code>null</code>.
   */
  public static <T> BucketFactory<ArrayList<T>> newArrayListFactory() {
    return new DefaultBucketFactory<ArrayList<T>, Void>( ArrayList::new, ArrayList::clear );
  }

  /**
   * Creates a new factory for {@link Vector}.
   * 
   * @return   A new factory for {@link Vector}. Not <code>null</code>.
   */
  public static <T> BucketFactory<Vector<T>> newVectorFactory() {
    return new DefaultBucketFactory<Vector<T>, Void>( Vector::new, Vector::clear );
  }

  /**
   * Creates a new factory for {@link StringWriter}.
   * 
   * @return   A new factory for {@link StringWriter}. Not <code>null</code>.
   */
  public static BucketFactory<StringWriter> newStringWriterFactory() {
    return new BucketFactory<StringWriter>() {

      @Override
      public StringWriter create() {
        return new StringWriter();
      }

      @Override
      public StringWriter reset( StringWriter object ) {
        object.getBuffer().setLength(0);
        return object;
      }
      
    };
  }
  
  /**
   * Creates a new factory for {@link CharArrayWriter}.
   * 
   * @return   A new factory for {@link CharArrayWriter}. Not <code>null</code>.
   */
  public static BucketFactory<CharArrayWriter> newCharArrayWriterFactory() {
    return new DefaultBucketFactory<CharArrayWriter, Void>( CharArrayWriter::new, CharArrayWriter::reset );
  }

} /* ENDCLASS */
