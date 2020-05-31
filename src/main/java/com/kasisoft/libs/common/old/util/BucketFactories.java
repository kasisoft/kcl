package com.kasisoft.libs.common.old.util;

import com.kasisoft.libs.common.old.text.StringFBuffer;
import com.kasisoft.libs.common.old.text.StringFBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.StringWriter;

/**
 * Helpers generating BucketFactory and Bucket instances for widely used types.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class BucketFactories {

  /**
   * Creates a new factory for {@link TreeMap}.
   * 
   * @return   A new factory for {@link TreeMap}. Not <code>null</code>.
   */
  public static <K, V, R extends Map<K, V>> BucketFactory<R> newTreeMapFactory() {
    return (BucketFactory<R>) new DefaultBucketFactory<TreeMap<K, V>, Integer>( TreeMap::new, TreeMap::clear );
  }

  public static <K, V> Bucket<Map<K, V>> newTreeMapBucket() {
    return new Bucket<>( newTreeMapFactory() );
  }

  /**
   * Creates a new factory for {@link Hashtable}.
   * 
   * @return   A new factory for {@link Hashtable}. Not <code>null</code>.
   */
  public static <K, V, R extends Map<K, V>> BucketFactory<R> newHashtableFactory() {
    return (BucketFactory<R>) new DefaultBucketFactory<Hashtable<K, V>, Integer>( Hashtable::new, Hashtable::clear );
  }

  public static <K, V> Bucket<Map<K, V>> newHashtableBucket() {
    return new Bucket<>( newHashtableFactory() );
  }

  /**
   * Creates a new factory for {@link HashMap}.
   * 
   * @return   A new factory for {@link HashMap}. Not <code>null</code>.
   */
  public static <K,V, R extends Map<K, V>> BucketFactory<R> newHashMapFactory() {
    return (BucketFactory<R>) new DefaultBucketFactory<HashMap<K, V>, Integer>( HashMap::new, HashMap::clear );
  }
  
  public static <K, V> Bucket<Map<K, V>> newHashMapBucket() {
    return new Bucket<>( newHashMapFactory() );
  }
    
  /**
   * Creates a new factory for {@link StringBuilder}.
   * 
   * @return   A new factory for {@link StringBuilder}. Not <code>null</code>.
   */
  public static <R extends CharSequence> BucketFactory<R> newStringBuilderFactory() {
    return (BucketFactory<R>) new DefaultBucketFactory<>( StringBuilder::new, StringBuilder::setLength, 0 );
  }
  
  public static <R extends CharSequence> Bucket<R> newStringBuilderBucket() {
    return new Bucket<>( newStringBuilderFactory() );
  }
  
  /**
   * Creates a new factory for {@link StringBuffer}.
   * 
   * @return   A new factory for {@link StringBuffer}. Not <code>null</code>.
   */
  public static <R extends CharSequence> BucketFactory<R> newStringBufferFactory() {
    return (BucketFactory<R>) new DefaultBucketFactory<>( StringBuffer::new, StringBuffer::setLength, 0 );
  }

  public static <R extends CharSequence> Bucket<R> newStringBufferBucket() {
    return new Bucket<>( newStringBufferFactory() );
  }
  
  /**
   * Creates a new factory for {@link StringFBuilder}.
   * 
   * @return   A new factory for {@link StringFBuilder}. Not <code>null</code>.
   */
  public static <R extends CharSequence> BucketFactory<R> newStringFBuilderFactory() {
    return (BucketFactory<R>) new DefaultBucketFactory<>( StringFBuilder::new, StringFBuilder::setLength, 0 );
  }
  
  public static <R extends CharSequence> Bucket<R> newStringFBuilderBucket() {
    return new Bucket<>( newStringFBuilderFactory() );
  }
  
  /**
   * Creates a new factory for {@link StringFBuffer}.
   * 
   * @return   A new factory for {@link StringFBuffer}. Not <code>null</code>.
   */
  public static <R extends CharSequence> BucketFactory<R> newStringFBufferFactory() {
    return (BucketFactory<R>) new DefaultBucketFactory<>( StringFBuffer::new, StringFBuffer::setLength, 0 );
  }

  public static <R extends CharSequence> Bucket<R> newStringFBufferBucket() {
    return new Bucket<>( newStringFBufferFactory() );
  }
  
  /**
   * Creates a new factory for {@link ByteArrayOutputStream}.
   * 
   * @return   A new factory for {@link ByteArrayOutputStream}. Not <code>null</code>.
   */
  public static BucketFactory<ByteArrayOutputStream> newByteArrayOutputStreamFactory() {
    return new DefaultBucketFactory<ByteArrayOutputStream, Void>( ByteArrayOutputStream::new, ByteArrayOutputStream::reset );
  }

  public static Bucket<ByteArrayOutputStream> newByteArrayOutputStreamBucket() {
    return new Bucket<>( newByteArrayOutputStreamFactory() );
  }
  
  /**
   * Creates a new factory for {@link ArrayList}.
   * 
   * @return   A new factory for {@link ArrayList}. Not <code>null</code>.
   */
  public static <T, R extends List<T>> BucketFactory<R> newArrayListFactory() {
    return (BucketFactory<R>) new DefaultBucketFactory<ArrayList<T>, Void>( ArrayList::new, ArrayList::clear );
  }

  public static <R> Bucket<List<R>> newArrayListBucket() {
    return new Bucket<>( newArrayListFactory() );
  }
  
  /**
   * Creates a new factory for {@link Vector}.
   * 
   * @return   A new factory for {@link Vector}. Not <code>null</code>.
   */
  public static <T, R extends List<T>> BucketFactory<R> newVectorFactory() {
    return (BucketFactory<R>) new DefaultBucketFactory<Vector<T>, Void>( Vector::new, Vector::clear );
  }

  public static <R> Bucket<List<R>> newVectorBucket() {
    return new Bucket<>( newVectorFactory() );
  }
  
  /**
   * Creates a new factory for {@link StringWriter}.
   * 
   * @return   A new factory for {@link StringWriter}. Not <code>null</code>.
   */
  public static BucketFactory<StringWriter> newStringWriterFactory() {
    return new DefaultBucketFactory<StringWriter, Void>( StringWriter::new, $ -> $.getBuffer().setLength(0) );
  }
  
  public static Bucket<StringWriter> newStringWriterBucket() {
    return new Bucket<>( newStringWriterFactory() );
  }
  
  /**
   * Creates a new factory for {@link CharArrayWriter}.
   * 
   * @return   A new factory for {@link CharArrayWriter}. Not <code>null</code>.
   */
  public static BucketFactory<CharArrayWriter> newCharArrayWriterFactory() {
    return new DefaultBucketFactory<CharArrayWriter, Void>( CharArrayWriter::new, CharArrayWriter::reset );
  }

  public static Bucket<CharArrayWriter> newCharArrayWriterBucket() {
    return new Bucket<>( newCharArrayWriterFactory() );
  }
  
} /* ENDCLASS */
