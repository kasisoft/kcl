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
  public static BucketFactory<ArrayList> newArrayListFactory() {
    return new BucketFactory<ArrayList>() {

      @Override
      public ArrayList create() {
        return new ArrayList();
      }

      @Override
      public ArrayList reset( ArrayList object ) {
        object.clear();
        return object;
      }
      
    };
  }

} /* ENDCLASS */
