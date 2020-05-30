package com.kasisoft.libs.common.old.thread;

import lombok.experimental.*;

import lombok.*;

import java.util.concurrent.atomic.*;

import java.util.concurrent.*;

/**
 * This {@link ThreadFactory} essentially resembles the default implementation with the difference that it
 * allows to configure a naming pattern.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomThreadFactory implements ThreadFactory {

  private static final AtomicInteger POOLNUMBER = new AtomicInteger(1);
  
  ThreadGroup     group;
  AtomicInteger   threadNumber;
  String          naming;
  int             poolNum;

  /**
   * Initializes this factory.
   */
  public CustomThreadFactory() {
    this( "pool-%1$d-thread-%2$d" );
  }

  /**
   * Initializes this factory using the supplied naming format. The format must use indexes to reference the 
   * arguments. There are currently two arguments:
   * 
   * <ul>
   *   <li>pool number : int</li>
   *   <li>thread number : int</li>
   * </ul>
   * 
   * @param namingFmt   The formatting String for the thread creation. Not <code>null</code>.
   */
  public CustomThreadFactory( @NonNull String namingFmt ) {
      SecurityManager sm = System.getSecurityManager();
      threadNumber       = new AtomicInteger(1);
      group              = (sm != null) ? sm.getThreadGroup() : Thread.currentThread().getThreadGroup();
      poolNum            = POOLNUMBER.getAndIncrement();
      naming             = namingFmt;
  }

  @Override
  public Thread newThread( Runnable r ) {
    Thread result = new Thread( group, r, String.format( naming, poolNum, threadNumber.getAndIncrement() ), 0 );
    if( result.isDaemon() ) {
      result.setDaemon( false );
    }
    if( result.getPriority() != Thread.NORM_PRIORITY ) {
      result.setPriority( Thread.NORM_PRIORITY );
    }
    return result;
  }

} /* ENDCLASS */
