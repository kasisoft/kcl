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
   * Creates a new factory for {@link StringBuilder}.
   * 
   * @return   A new factory for {@link StringBuilder}. Not <code>null</code>.
   */
  public static BucketFactory<StringBuilder> newStringBuilderFactory() {
    return new BucketFactory<StringBuilder>() {

      @Override
      public StringBuilder create() {
        return new StringBuilder();
      }

      @Override
      public StringBuilder reset( StringBuilder object ) {
        object.setLength(0);
        return object;
      }
      
    };
  }
  
  /**
   * Creates a new factory for {@link StringBuffer}.
   * 
   * @return   A new factory for {@link StringBuffer}. Not <code>null</code>.
   */
  public static BucketFactory<StringBuffer> newStringBufferFactory() {
    return new BucketFactory<StringBuffer>() {

      @Override
      public StringBuffer create() {
        return new StringBuffer();
      }

      @Override
      public StringBuffer reset( StringBuffer object ) {
        object.setLength(0);
        return object;
      }
      
    };
  }

  /**
   * Creates a new factory for {@link StringFBuilder}.
   * 
   * @return   A new factory for {@link StringFBuilder}. Not <code>null</code>.
   */
  public static BucketFactory<StringFBuilder> newStringFBuilderFactory() {
    return new BucketFactory<StringFBuilder>() {

      @Override
      public StringFBuilder create() {
        return new StringFBuilder();
      }

      @Override
      public StringFBuilder reset( StringFBuilder object ) {
        object.setLength(0);
        return object;
      }
      
    };
  }
  
  /**
   * Creates a new factory for {@link StringFBuffer}.
   * 
   * @return   A new factory for {@link StringFBuffer}. Not <code>null</code>.
   */
  public static BucketFactory<StringFBuffer> newStringFBufferFactory() {
    return new BucketFactory<StringFBuffer>() {

      @Override
      public StringFBuffer create() {
        return new StringFBuffer();
      }

      @Override
      public StringFBuffer reset( StringFBuffer object ) {
        object.setLength(0);
        return object;
      }
      
    };
  }

  /**
   * Creates a new factory for {@link ByteArrayOutputStream}.
   * 
   * @return   A new factory for {@link ByteArrayOutputStream}. Not <code>null</code>.
   */
  public static BucketFactory<ByteArrayOutputStream> newByteArrayOutputStreamFactory() {
    return new BucketFactory<ByteArrayOutputStream>() {

      @Override
      public ByteArrayOutputStream create() {
        return new ByteArrayOutputStream();
      }

      @Override
      public ByteArrayOutputStream reset( ByteArrayOutputStream object ) {
        object.reset();
        return object;
      }
      
    };
  }

  /**
   * Creates a new factory for {@link ArrayList}.
   * 
   * @return   A new factory for {@link ArrayList}. Not <code>null</code>.
   */
  public static <T> BucketFactory<ArrayList<T>> newArrayListFactory() {
    return new BucketFactory<ArrayList<T>>() {

      @Override
      public ArrayList<T> create() {
        return new ArrayList<T>();
      }

      @Override
      public ArrayList<T> reset( ArrayList object ) {
        object.clear();
        return object;
      }
      
    };
  }

  /**
   * Creates a new factory for {@link Vector}.
   * 
   * @return   A new factory for {@link Vector}. Not <code>null</code>.
   */
  public static <T> BucketFactory<Vector<T>> newVectorFactory() {
    return new BucketFactory<Vector<T>>() {

      @Override
      public Vector<T> create() {
        return new Vector<T>();
      }

      @Override
      public Vector<T> reset( Vector object ) {
        object.clear();
        return object;
      }
      
    };
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
    return new BucketFactory<CharArrayWriter>() {

      @Override
      public CharArrayWriter create() {
        return new CharArrayWriter();
      }

      @Override
      public CharArrayWriter reset( CharArrayWriter object ) {
        object.reset();
        return object;
      }
      
    };
  }

} /* ENDCLASS */
