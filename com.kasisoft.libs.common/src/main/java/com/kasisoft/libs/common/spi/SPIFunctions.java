package com.kasisoft.libs.common.spi;

import com.kasisoft.libs.common.base.*;

import lombok.*;

import java.util.*;

import java.lang.reflect.*;

/**
 * Collection of various functions.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class SPIFunctions {

  /**
   * Prevent instantiation.
   */
  private SPIFunctions() {
  }

  /**
   * Small helper which is used to load all SPI services currently available.
   * 
   * @param servicetype   The desired service type. Not <code>null</code>
   * 
   * @return   A list with all SPI services currently available. Not <code>null</code>.
   */
  public static <T> List<T> loadSPIServices( @NonNull Class<T> servicetype ) {
    List<T> result = new ArrayList<>();
    ServiceLoader.load( servicetype ).forEach( service -> result.add( service ) );
    return result;
  }

  /**
   * Small helper which is used to load all SPI services currently available.
   * 
   * @param servicetype     The desired service type. Not <code>null</code>
   * @param configuration   A configuration to be used for {@link Configurable} instances. Not <code>null</code>.
   * 
   * @return   A list with all SPI services currently available. Not <code>null</code>.
   * 
   * @throws FailureException in case one SPI could not be configured properly.
   */
  public static <T> List<T> loadSPIServices( @NonNull Class<T> servicetype, @NonNull Properties configuration ) throws FailureException {
    List<T> result = loadSPIServices( servicetype );
    result.stream().filter( s -> s instanceof Configurable ).forEach( s -> ((Configurable) s).configure( configuration ) );
    return result;
  }
  
  /**
   * This function allows to wrap a list of interfaces into a guarding class which only takes the interface 
   * implementation within a constructor. The purpose is to provide a class that guarantuees the contract.
   * 
   * @param list            The list of implementations that shall be wrapped. Not <code>null</code>.
   * @param guardingclass   The guarding class that shall be used. Not <code>null</code>.
   * 
   * @return   A list of protected instances. Not <code>null</code>.
   */
  public static <T> List<T> createGuards( @NonNull List<T> list, @NonNull Class<T> guardingclass ) {
    List<T> result = new ArrayList<>();
    if( ! list.isEmpty() ) {
      try {
        T              first       = list.get(0);
        Constructor<T> constructor = guardingclass.getConstructor( first.getClass() );
        for( T element : list ) {
          result.add( constructor.newInstance( element ) );
        }
      } catch( NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex ) {
        throw FailureCode.Reflections.newException( ex );
      }
    }
    return result;
  }
  
} /* ENDCLASS */
