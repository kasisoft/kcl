/**
 * Name........: ExtArrayList
 * Description.: ArrayList variety which can handle negative indices. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.libs.common.util;

import java.util.*;

import lombok.*;

/**
 * ArrayList variety which can handle negative indices. So an index of -1 points to the last element. An index of -2 
 * to it's predecessor and so on.
 */
public class ExtArrayList<T> extends ArrayList<T> {

  /**
   * Constructs an empty list with an initial capacity of ten.
   */
  public ExtArrayList() {
    super();
  }

  /**
   * Constructs an empty list with the supplied initial capacity.
   * 
   * @param capacity   The initial capacity for this list.
   */
  public ExtArrayList( int capacity ) {
    super( capacity );
  }
  
  /**
   * Constructs this list using given data.
   * 
   * @param collection   The initial data for this list. Not <code>null</code>.
   */
  public ExtArrayList( @NonNull Collection<? extends T> collection ) {
    super( collection );
  }

  /**
   * Constructs this list using given data.
   * 
   * @param items   The initial data for this list. Not <code>null</code>.
   */
  public ExtArrayList( @NonNull T ... items ) {
    super();
    addAll( items );
  }

  /**
   * Returns an adjusted index since this extension supports negative indices as well.
   * 
   * @param index   The index supplied by the user.
   * 
   * @return  The index to use for the original implementation.
   */
  private int adjustIndex( int index ) {
    if( index < 0 ) {
      return size() + index;
    }
    return index;
  }

  @Override
  public void add( int index, @NonNull T element ) {
    super.add( adjustIndex( index ), element );
  }

  @Override
  public boolean addAll( int index, @NonNull Collection<? extends T> collection ) {
    return super.addAll( adjustIndex( index ), collection );
  }

  /**
   * Inserts all of the elements in the specified array into this list, starting at the specified position.
   *
   * @param index   Index at which to insert the first element from the specified array.
   * @param items   The data which has to be inserted. Not <code>null</code>.
   * 
   * @return <code>true</code> <=> This list has changed as a result of the call.
   */
  public boolean addAll( int index, @NonNull T ... items ) {
    index = adjustIndex( index );
    for( T item : items ) {
      super.add( index, item );
      index++;
    }
    return false;
  }
  
  /**
   * Inserts all of the elements in the specified array into this list, starting at the end.
   *
   * @param items   The data which has to be inserted. Not <code>null</code>.
   * 
   * @return <code>true</code> <=> This list has changed as a result of the call.
   */
  public boolean addAll( @NonNull T ... items ) {
    return addAll( size(), items );
  }
  
  @Override
  public T get( int index ) {
    return super.get( adjustIndex( index ) );
  }

  @Override
  public T remove( int index ) {
    return super.remove( adjustIndex( index ) );
  }

  @Override
  protected void removeRange( int from, int to ) {
    super.removeRange( adjustIndex( from ), adjustIndex( to ) );
  }

  @Override
  public T set( int index, @NonNull T element ) {
    return super.set( adjustIndex( index ), element );
  }

  @Override
  public ListIterator<T> listIterator( int index ) {
    return super.listIterator( adjustIndex( index ) );
  }

  @Override
  public List<T> subList( int from, int to ) {
    return super.subList( adjustIndex( from ), adjustIndex( to ) );
  }

  /**
   * Removes <code>null</code> elements at the begin and the end.
   */
  public void trim() {
    while( (! isEmpty()) && (get(0) == null) ) {
      remove(0);
    }
    while( (! isEmpty()) && (get(-1) == null) ) {
      remove(-1);
    }
  }

} /* ENDCLASS */
