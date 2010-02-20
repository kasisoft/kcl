/**
 * Name........: ExtArrayList
 * Description.: ArrayList variety which can handle negative indices. 
 * Author......: Daniel Kasmeroglu
 * E-Mail......: daniel.kasmeroglu@kasisoft.net
 * Company.....: Kasisoft
 * License.....: LGPL
 */
package com.kasisoft.lgpl.libs.common.util;

import com.kasisoft.lgpl.tools.diagnostic.*;

import java.util.*;

/**
 * ArrayList variety which can handle negative indices. So an index of -1 points to the last
 * element. An index of -2 to it's predecessor and so on.
 */
@KDiagnostic
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
   * @param collection   The initial data for this list.
   */
  public ExtArrayList( Collection<? extends T> collection ) {
    super( collection );
  }

  /**
   * Constructs this list using given data.
   * 
   * @param items   The initial data for this list.
   */
  public ExtArrayList( T ... items ) {
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

  /**
   * {@inheritDoc}
   */
  public void add( int index, T element ) {
    super.add( adjustIndex( index ), element );
  }

  /**
   * {@inheritDoc}
   */
  public boolean addAll( int index, Collection<? extends T> collection ) {
    return super.addAll( adjustIndex( index ), collection );
  }

  /**
   * Inserts all of the elements in the specified array into this
   * list, starting at the specified position.
   *
   * @param index   Index at which to insert the first element from the
   *                specified array.
   * @param items   The data which has to be inserted.
   * 
   * @return <code>true</code> <=> This list has changed as a result of the call.
   */
  public boolean addAll( int index, T ... items ) {
    index = adjustIndex( index );
    for( T item : items ) {
      super.add( index, item );
      index++;
    }
    return false;
  }
  
  /**
   * Inserts all of the elements in the specified array into this
   * list, starting at the end.
   *
   * @param items   The data which has to be inserted.
   * 
   * @return <code>true</code> <=> This list has changed as a result of the call.
   */
  public boolean addAll( T ... items ) {
    return addAll( size(), items );
  }
  
  /**
   * {@inheritDoc}
   */
  public T get( int index ) {
    return super.get( adjustIndex( index ) );
  }

  /**
   * {@inheritDoc}
   */
  public T remove( int index ) {
    return super.remove( adjustIndex( index ) );
  }

  /**
   * {@inheritDoc}
   */
  protected void removeRange( int from, int to ) {
    super.removeRange( adjustIndex( from ), adjustIndex( to ) );
  }

  /**
   * {@inheritDoc}
   */
  public T set( int index, T element ) {
    return super.set( adjustIndex( index ), element );
  }

  /**
   * {@inheritDoc}
   */
  public ListIterator<T> listIterator( int index ) {
    return super.listIterator( adjustIndex( index ) );
  }

  /**
   * {@inheritDoc}
   */
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
