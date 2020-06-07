package com.kasisoft.libs.common.utils;

import javax.validation.constraints.Null;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class WrapperFunctions {

  public static <R> List<R> toExtendedList(@Null List<R> list) {
    if (list == null) {
      return null;
    }
    return new ExtendedList<>(list);
  }
  
  public static Map<String, String> toMap(@Null Properties properties) {
    if (properties == null) {
      return null;
    }
    return new PropertiesMap(properties);
  }
  
  @AllArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  private static class ExtendedList<T> implements List<T> {
    
    List<T>   origin;
    
    private int adjustIndex(int index) {
      return MiscFunctions.adjustIndex(size(), index, false);
    }

    private int adjustIndexEnd(int index) {
      return MiscFunctions.adjustIndex(size(), index, true);
    }

    @Override
    public boolean add(T element) {
      return origin.add(element);
    }
    
    @Override
    public void add(int index, T element) {
      origin.add(adjustIndex(index), element);
    }

    @Override
    public boolean addAll(@NonNull Collection<? extends T> collection) {
      return addAll(size(), collection);
    }
    
    @Override
    public boolean addAll(int index, @NonNull Collection<? extends T> collection) {
      return origin.addAll(adjustIndex(index), collection);
    }

    @Override
    public T get(int index) {
      return origin.get(adjustIndex(index));
    }

    @Override
    public T remove(int index) {
      return origin.remove(adjustIndex(index));
    }

    @Override
    public T set(int index, T element) {
      return origin.set(adjustIndex(index), element);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
      return origin.listIterator(adjustIndex(index));
    }

    @Override
    public List<T> subList(int from, int to) {
      return origin.subList(adjustIndex(from), adjustIndexEnd(to));
    }

    @Override
    public int size() {
      return origin.size();
    }

    @Override
    public boolean isEmpty() {
      return origin.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
      return origin.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
      return origin.iterator();
    }

    @Override
    public Object[] toArray() {
      return origin.toArray();
    }

    @Override
    public <R> R[] toArray(R[] a) {
      return origin.toArray(a);
    }

    @Override
    public boolean remove(Object o) {
      return origin.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
      return origin.containsAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
      return origin.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
      return origin.retainAll(c);
    }

    @Override
    public void clear() {
      origin.clear();
    }

    @Override
    public int indexOf(Object o) {
      return origin.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
      return origin.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
      return origin.listIterator();
    }

  } /* ENDCLASS */

  @FieldDefaults(level = AccessLevel.PRIVATE)
  @AllArgsConstructor
  private static class PropertiesMap implements Map<String, String> {

    Properties   properties;

    @Override
    public int size() {
      return properties.size();
    }

    @Override
    public boolean isEmpty() {
      return properties.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
      return properties.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
      return properties.containsValue(value);
    }

    @Override
    public String get(Object key) {
      return (String) properties.get(key);
    }

    @Override
    public String put(String key, String value) {
      return (String) properties.put(key, value);
    }

    @Override
    public String remove(Object key) {
      return (String) properties.remove( key );
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> map) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
      properties.clear();
    }

    @Override
    public Set<String> keySet() {
      throw new UnsupportedOperationException();
    }

    @SuppressWarnings("cast")
    @Override
    public Collection<String> values() {
      Collection collection = properties.values();
      return (Collection<String>) collection;
    }

    @Override
    public Set<Map.Entry<String, String>> entrySet() {
      throw new UnsupportedOperationException();
    }
    
  } /* ENDCLASS */

} /* ENDCLASS */
