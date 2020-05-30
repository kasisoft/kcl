package com.kasisoft.libs.common.old.lang;

import lombok.*;

import com.kasisoft.libs.common.old.annotation.*;
import com.kasisoft.libs.common.old.base.*;

import java.util.*;

import java.lang.annotation.*;
import java.lang.reflect.*;

/**
 * Collections of reflection based functionalities.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class ReflectionFunctions {

  public static boolean isAnnotated( @NonNull Class<?> toTest,  Class<? extends Annotation> ... annotations ) {
    if( (annotations != null) && (annotations.length > 0) ) {
      for( Class<? extends Annotation> annotation : annotations ) {
        if( toTest.getAnnotation( annotation ) != null ) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Returns the constructor associated with a specific type.
   * 
   * @param type     The type used to access the constructor. Not <code>null</code>. 
   * @param params   The parameter types for the constructor. Maybe <code>null</code>.
   * 
   * @return   The Constructor if there is one apropriate one. Maybe <code>null</code>.
   */
  public static Constructor getConstructor( @NonNull Class<?> type, Class<?> ... params ) {
    try {
      return type.getDeclaredConstructor( params );
    } catch( Exception ex ) {
      return null;
    }
  }
  
  /**
   * Returns the method associated with a specific type.
   * 
   * @param type     The type used to access the method. Not <code>null</code>.
   * @param name     The name of the method. Neither <code>null</code> nor empty. 
   * @param params   The parameter types for the method. Maybe <code>null</code>.
   * 
   * @return   The Constructor if there is one apropriate one. Maybe <code>null</code>.
   */
  public static Method getMethod( @NonNull Class<?> type, @NonNull String name, Class<?> ... params ) {
    try {
      return type.getDeclaredMethod( name, params );
    } catch( Exception ex ) {
      return null;
    }
  }

  /**
   * Instantiates the supplied class with the supplied arguments.
   * 
   * @param classname   The class that shall be instantiated. Neither <code>null</code> nor empty.
   * @param args        The arguments which have to be passed to the constructor. If omitted the default constructor 
   *                    will be used. If passed each element must be non-<code>null</code> in order to determine the 
   *                    parameter type.
   * 
   * @return   <code>null</code> <=> If the class could not be instantiated otherwise the instance itself.
   */
  public static Object newInstance( @NonNull String classname, Object ... args ) {
    try {
      Class clazz = Class.forName( classname );
      if( (args == null) || (args.length == 0) ) {
        return clazz.newInstance();
      } else {
        Class[] params = new Class[ args.length ];
        for( int i = 0; i < params.length; i++ ) {
          params[i] = args[i].getClass();
        }
        try {
          return clazz.getConstructor( params ).newInstance( args );
        } catch( NoSuchMethodException ex ) {
          Constructor[] constructors = clazz.getDeclaredConstructors();
          Constructor   constructor  = findMatchingConstructor( constructors, params );
          if( constructor != null ) {
            return constructor.newInstance( args );
          } else {
            throw KclException.wrap( ex );
          }
        }
      }
    } catch( Exception ex ) { 
      throw KclException.wrap( ex );
    }
  }
  
  /**
   * Identifies a constructor by it's signature. This might be necessary if the appropriate Constructor uses an 
   * interface, so using a concrete type might fail to locate the right Constructor.
   *  
   * @param candidates   The possible candidates of Constructors. Not <code>null</code>.
   * @param params       The current signature used to locate the Constructor. Not <code>null</code>.
   * 
   * @return   The Constructor if it could be found. Maybe <code>null</code>.
   */
  private static Constructor findMatchingConstructor( @NonNull Constructor[] candidates, @NonNull Class<?>[] params ) {
    for( Constructor constructor : candidates ) {
      Class[] expectedparams = constructor.getParameterTypes();
      if( (expectedparams != null) && (expectedparams.length == params.length) ) {
        boolean matches = true;
        for( int i = 0; i < expectedparams.length; i++ ) {
          if( ! expectedparams[i].isAssignableFrom( params[i] ) ) {
            matches = false;
            break;
          }
        }
        if( matches ) {
          return constructor;
        }
      }
    }
    return null;
  }
  
  @NonNull
  public static List<Class> getGenericsTypes( @NonNull Method method ) {
    return getGenericsTypesFromMember( method );
  }

  @NonNull
  public static List<Class> getGenericsTypes( @NonNull Field field ) {
    return getGenericsTypesFromMember( field );
  }

  private static List<Class> getGenericsTypesFromMember( AnnotatedElement member ) {
    List<Class>   result  = Collections.emptyList();
    GenericsType  type    = member.getAnnotation( GenericsType.class );
    GenericsTypes types   = member.getAnnotation( GenericsTypes.class );
    List<Class>   list1   = type  != null ? Arrays.asList( type.value() ) : null;
    List<Class>   list2   = (types != null) && (types.value().length > 0) ? Arrays.asList( types.value() ) : null;
    if( (list1 != null) && (list2 != null) ) {
      result = new ArrayList<>( list1 );
      result.addAll( list2 );
    } else if( list1 != null ) {
      result = list1;
    } else if( list2 != null ) {
      result = list2;
    }
    return result;
  }

} /* ENDCLASS */

