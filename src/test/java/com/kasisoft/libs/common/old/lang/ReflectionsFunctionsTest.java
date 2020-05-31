package com.kasisoft.libs.common.old.lang;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.fail;

import com.kasisoft.libs.common.KclException;

import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;

/**
 * Test for various functions of the class 'MiscFunctions'.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReflectionsFunctionsTest {

  @Test(groups="all")
  public void newInstance() {
    Object object = ReflectionFunctions.newInstance( String.class.getName(), "Frosch".getBytes() );
    assertThat( object, is( (Object) "Frosch" ) );
  }

  @Test(expectedExceptions=KclException.class, groups="all")
  public void newInstanceFailure() {
    ReflectionFunctions.newInstance( String.class.getName(), new float[12] );
    fail();
  }

  @Test(groups="all")
  public void getConstructor() {
    assertThat( ReflectionFunctions.getConstructor( ByteArrayOutputStream.class ), is( notNullValue() ) );
    assertNull( ReflectionFunctions.getConstructor( ByteArrayInputStream.class ) );
  }

  @Test(groups="all")
  public void getMethod() {
    assertThat( ReflectionFunctions.getMethod( ByteArrayOutputStream.class, "reset" ), is( notNullValue() ) );
    assertNull( ReflectionFunctions.getMethod( ByteArrayOutputStream.class, "bibo" ) );
  }

//  @Test(groups="all")
//  public void isAnnotated() {
//    assertTrue( ReflectionFunctions.isAnnotated( IsAnnotated.class, SuppressWarnings.class, BindingType.class ) );
//    assertFalse( ReflectionFunctions.isAnnotated( IsAnnotated.class, SuppressWarnings.class ) );
//  }
//
//  @SuppressWarnings("")
//  @BindingType
//  public static class IsAnnotated {
//    
//  } /* ENDCLASS */
  
} /* ENDCLASS */
