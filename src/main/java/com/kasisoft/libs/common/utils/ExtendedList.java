package com.kasisoft.libs.common.utils;

import jakarta.validation.constraints.*;

import java.util.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ExtendedList<T> implements List<T> {

    private List<T> origin;

    public ExtendedList(List<T> origin) {
        this.origin = origin;
    }

    @Override
    public boolean add(T element) {
        return origin.add(element);
    }

    @Override
    public void add(int index, T element) {
        origin.add(MiscFunctions.adjustIndex(size(), index, false), element);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends T> collection) {
        return addAll(size(), collection);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends T> collection) {
        return origin.addAll(MiscFunctions.adjustIndex(size(), index, false), collection);
    }

    @Override
    public T get(int index) {
        return origin.get(MiscFunctions.adjustIndex(size(), index, false));
    }

    @Override
    public T remove(int index) {
        return origin.remove(MiscFunctions.adjustIndex(size(), index, false));
    }

    @Override
    public T set(int index, T element) {
        return origin.set(MiscFunctions.adjustIndex(size(), index, false), element);
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return origin.listIterator(MiscFunctions.adjustIndex(size(), index, false));
    }

    @Override
    public List<T> subList(int from, int to) {
        return origin.subList(MiscFunctions.adjustIndex(size(), from, false), MiscFunctions.adjustIndex(size(), to, true));
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
