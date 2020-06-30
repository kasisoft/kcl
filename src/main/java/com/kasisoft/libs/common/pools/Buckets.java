package com.kasisoft.libs.common.pools;

import com.kasisoft.libs.common.text.StringFBuffer;
import com.kasisoft.libs.common.text.StringFBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.CRC32;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.StringWriter;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Buckets {

  public static Map<Class, Bucket> BUCKETS = new ConcurrentHashMap<>(8);
  
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

  public static Bucket<StringBuilder> bucketStringBuilder() {
    return BUCKETS.computeIfAbsent(StringBuilder.class, $ -> new Bucket<>(StringBuilder::new, $sb -> $sb.setLength(0)));
  }

  public static Bucket<StringBuffer> bucketStringBuffer() {
    return BUCKETS.computeIfAbsent(StringBuffer.class, $ ->new Bucket<>(StringBuffer::new, $sb -> $sb.setLength(0)));
  }

  public static <K, V> Bucket<HashMap<K, V>> bucketHashMap() {
    return BUCKETS.computeIfAbsent(HashMap.class, $ ->new Bucket<>(HashMap::new, HashMap::clear));
  }

  public static Bucket<CRC32> bucketCRC32() {
    return BUCKETS.computeIfAbsent(CRC32.class, $ ->new Bucket<>(CRC32::new, CRC32::reset));
  }

  public static Bucket<StringWriter> bucketStringWriter() {
    return BUCKETS.computeIfAbsent(StringWriter.class, $ ->new Bucket<>(StringWriter::new, $sw -> $sw.getBuffer().setLength(0)));
  }

} /* ENDCLASS */
