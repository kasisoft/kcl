package com.kasisoft.libs.common.buckets;

import com.kasisoft.libs.common.text.StringFBuffer;
import com.kasisoft.libs.common.text.StringFBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Buckets {

  private static Map<Class, Bucket> BUCKETS = new ConcurrentHashMap<>(8);
  
  public static <R> Bucket<List<R>> bucketArrayList() {
    return BUCKETS.computeIfAbsent(ArrayList.class, $ -> new Bucket<ArrayList>(ArrayList::new, ArrayList::clear));
  }

  public static <R> Bucket<List<R>> bucketLinkedList() {
    return BUCKETS.computeIfAbsent(LinkedList.class, $ -> new Bucket<>(LinkedList::new, LinkedList::clear));
  }

  public static Bucket<ByteArrayOutputStream> bucketByteArrayOutputStream() {
    return BUCKETS.computeIfAbsent(ByteArrayOutputStream.class, $ -> new Bucket<>(ByteArrayOutputStream::new, ByteArrayOutputStream::reset));
  }

  public static Bucket<CharArrayWriter> bucketCharArrayWriter() {
    return BUCKETS.computeIfAbsent(CharArrayWriter.class, $ -> new Bucket<>(CharArrayWriter::new, CharArrayWriter::reset));
  }

  public static Bucket<StringFBuffer> bucketStringFBuffer() {
    return BUCKETS.computeIfAbsent(StringFBuffer.class, $ -> new Bucket<>(StringFBuffer::new, $sb -> $sb.setLength(0)));
  }

  public static Bucket<StringFBuilder> bucketStringFBuilder() {
    return BUCKETS.computeIfAbsent(StringFBuilder.class, $ ->new Bucket<>(StringFBuilder::new, $sb -> $sb.setLength(0)));
  }

} /* ENDCLASS */
