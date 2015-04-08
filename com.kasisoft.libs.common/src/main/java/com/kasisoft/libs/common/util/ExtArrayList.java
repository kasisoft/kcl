package com.kasisoft.libs.common.util;

import java.util.*;

import lombok.*;
import lombok.experimental.*;

/**
 * ArrayList variety which can handle negative indices. So an index of -1 points to the last element. An index of -2 
 * to it's predecessor and so on.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExtArrayList<T> extends ArrayList<T> {

  @Getter
  boolean   suppressnull;
  
  /**
   * Constructs an empty list with an initial capacity of ten.
   */
  public ExtArrayList() {
    this( false );
  }

  /**
   * Constructs an empty list with the supplied initial capacity.
   * 
   * @param capacity   The initial capacity for this list.
   */
  public ExtArrayList( int capacity ) {
    this( false, capacity );
  }
  
  /**
   * Constructs this list using given data.
   * 
   * @param collection   The initial data for this list. Not <code>null</code>.
   */
  public ExtArrayList( @NonNull Collection<? extends T> collection ) {
    this( false, collection );
  }

  /**
   * Constructs this list using given data.
   * 
   * @param items   The initial data for this list. Not <code>null</code>.
   */
  public ExtArrayList( @NonNull T ... items ) {
    this( false, items );
  }

  /**
   * Constructs an empty list with an initial capacity of ten.
   * 
   * @param suppressnull   <code>true</code> <=> Do NOT add <code>null</code> elements.
   */
  public ExtArrayList( boolean suppressnull ) {
    super();
    this.suppressnull = suppressnull;
  }

  /**
   * Constructs an empty list with the supplied initial capacity.
   * 
   * @param suppressnull   <code>true</code> <=> Do NOT add <code>null</code> elements.
   * @param capacity       The initial capacity for this list.
   */
  public ExtArrayList( boolean suppressnull, int capacity ) {
    super( capacity );
    this.suppressnull = suppressnull;
  }
  
  /**
   * Constructs this list using given data.
   * 
   * @param suppressnull   <code>true</code> <=> Do NOT add <code>null</code> elements.
   * @param collection     The initial data for this list. Not <code>null</code>.
   */
  public ExtArrayList( boolean suppressnull, Collection<? extends T> collection ) {
    super( collection.size() );
    this.suppressnull = suppressnull;
    addAll( collection );
  }

  /**
   * Constructs this list using given data.
   * 
   * @param suppressnull   <code>true</code> <=> Do NOT add <code>null</code> elements.
   * @param items          The initial data for this list. Not <code>null</code>.
   */
  public ExtArrayList( boolean suppressnull, T ... items ) {
    super( items.length );
    this.suppressnull = suppressnull;
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
  
  private boolean isValid( T element ) {
    return (! suppressnull) || (element != null);
  }

  @Override
  public boolean add( T element ) {
    if( isValid( element ) ) {
      return super.add( element );
    } else {
      return false;
    }
  }
  
  @Override
  public void add( int index, T element ) {
    if( isValid( element ) ) {
      super.add( adjustIndex( index ), element );
    }
  }

  @Override
  public boolean addAll( @NonNull Collection<? extends T> collection ) {
    return addAll( size(), collection );
  }
  
  @Override
  public boolean addAll( int index, @NonNull Collection<? extends T> collection ) {
    int oldcount = size();
    index        = adjustIndex( index );
    for( T item : collection ) {
      if( isValid( item ) ) {
        super.add( index, item );
        index++;
      }
    }
    return size() != oldcount;
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
    int oldcount = size();
    index        = adjustIndex( index );
    for( T item : items ) {
      if( isValid( item ) ) {
        super.add( index, item );
        index++;
      }
    }
    return size() != oldcount;
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
  public T set( int index, T element ) {
    if( isValid( element ) ) {
      return super.set( adjustIndex( index ), element );
    } else {
      return super.remove( adjustIndex( index ) );
    }
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
