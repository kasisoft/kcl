package com.kasisoft.libs.common.util;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

import org.testng.annotations.*;

import java.util.Optional;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class EncryptorTest {

  private static final String ALGORITHM = "DES";

  private static final String CIPHER    = "DES/CBC/PKCS5Padding";
  
  private static final String SECRET    = "CK3gFoo+ddY=";
  
  @Test(groups = "all")
  public void cipherAndAlgorithm() {
    
    Encryptor encryptor = new Encryptor( CIPHER, ALGORITHM )
      .withErrorHandler( $ -> { throw new IllegalArgumentException($); } );

    String    strdata   = "gollum";
    byte[]    data      = strdata.getBytes();
    
    // byte[]
    
    Optional<byte[]> encrypted1 = encryptor.encrypt( (byte[]) null ); 
    assertNotNull( encrypted1 ); 
    assertFalse( encrypted1.isPresent() );

    Optional<byte[]> encrypted2 = encryptor.encrypt( data ); 
    assertNotNull( encrypted2 ); 
    assertTrue( encrypted2.isPresent() );
    
    Optional<byte[]> decrypted1 = encryptor.decrypt( (byte[]) null );
    assertNotNull( decrypted1 ); 
    assertFalse( decrypted1.isPresent() );

    Optional<byte[]> decrypted2 = encryptor.decrypt( encrypted2.get() );
    assertNotNull( decrypted2 ); 
    assertTrue( decrypted2.isPresent() );
    assertThat( decrypted2.get(), is( data ) );

    // String
    
    Optional<String> encrypted3 = encryptor.encrypt( (String) null ); 
    assertNotNull( encrypted3 ); 
    assertFalse( encrypted3.isPresent() );

    Optional<String> encrypted4 = encryptor.encrypt( strdata ); 
    assertNotNull( encrypted4 ); 
    assertTrue( encrypted4.isPresent() );
    
    Optional<String> decrypted3 = encryptor.decrypt( (String) null );
    assertNotNull( decrypted3 ); 
    assertFalse( decrypted3.isPresent() );

    Optional<String> decrypted4 = encryptor.decrypt( encrypted4.get() );
    assertNotNull( decrypted4 ); 
    assertTrue( decrypted4.isPresent() );
    assertThat( decrypted4.get(), is( strdata ) );
    
  }

  @Test(groups = "all")
  public void cipherAndAlgorithmAndSecret() {
    
    Encryptor encryptor = new Encryptor( CIPHER, ALGORITHM, SECRET )
      .withErrorHandler( $ -> { throw new IllegalArgumentException($); } );

    String    strdata   = "gollum";
    byte[]    data      = strdata.getBytes();
    
    // byte[]
    
    Optional<byte[]> encrypted1 = encryptor.encrypt( (byte[]) null ); 
    assertNotNull( encrypted1 ); 
    assertFalse( encrypted1.isPresent() );

    Optional<byte[]> encrypted2 = encryptor.encrypt( data ); 
    assertNotNull( encrypted2 ); 
    assertTrue( encrypted2.isPresent() );
    
    Optional<byte[]> decrypted1 = encryptor.decrypt( (byte[]) null );
    assertNotNull( decrypted1 ); 
    assertFalse( decrypted1.isPresent() );

    Optional<byte[]> decrypted2 = encryptor.decrypt( encrypted2.get() );
    assertNotNull( decrypted2 ); 
    assertTrue( decrypted2.isPresent() );
    assertThat( decrypted2.get(), is( data ) );

    // String
    
    Optional<String> encrypted3 = encryptor.encrypt( (String) null ); 
    assertNotNull( encrypted3 ); 
    assertFalse( encrypted3.isPresent() );

    Optional<String> encrypted4 = encryptor.encrypt( strdata ); 
    assertNotNull( encrypted4 ); 
    assertTrue( encrypted4.isPresent() );
    
    Optional<String> decrypted3 = encryptor.decrypt( (String) null );
    assertNotNull( decrypted3 ); 
    assertFalse( decrypted3.isPresent() );

    Optional<String> decrypted4 = encryptor.decrypt( encrypted4.get() );
    assertNotNull( decrypted4 ); 
    assertTrue( decrypted4.isPresent() );
    assertThat( decrypted4.get(), is( strdata ) );
    
  }

  @Test(groups = "all")
  public void cipherAndAlgorithmAndSecretAndSalt() {
    
    Encryptor encryptor = new Encryptor( CIPHER, ALGORITHM, SECRET, "01234567".getBytes() )
      .withErrorHandler( $ -> { throw new IllegalArgumentException($); } );

    String    strdata   = "gollum";
    byte[]    data      = strdata.getBytes();
    
    // byte[]
    
    Optional<byte[]> encrypted1 = encryptor.encrypt( (byte[]) null ); 
    assertNotNull( encrypted1 ); 
    assertFalse( encrypted1.isPresent() );

    Optional<byte[]> encrypted2 = encryptor.encrypt( data ); 
    assertNotNull( encrypted2 ); 
    assertTrue( encrypted2.isPresent() );
    
    Optional<byte[]> decrypted1 = encryptor.decrypt( (byte[]) null );
    assertNotNull( decrypted1 ); 
    assertFalse( decrypted1.isPresent() );

    Optional<byte[]> decrypted2 = encryptor.decrypt( encrypted2.get() );
    assertNotNull( decrypted2 ); 
    assertTrue( decrypted2.isPresent() );
    assertThat( decrypted2.get(), is( data ) );

    // String
    
    Optional<String> encrypted3 = encryptor.encrypt( (String) null ); 
    assertNotNull( encrypted3 ); 
    assertFalse( encrypted3.isPresent() );

    Optional<String> encrypted4 = encryptor.encrypt( strdata ); 
    assertNotNull( encrypted4 ); 
    assertTrue( encrypted4.isPresent() );
    
    Optional<String> decrypted3 = encryptor.decrypt( (String) null );
    assertNotNull( decrypted3 ); 
    assertFalse( decrypted3.isPresent() );

    Optional<String> decrypted4 = encryptor.decrypt( encrypted4.get() );
    assertNotNull( decrypted4 ); 
    assertTrue( decrypted4.isPresent() );
    assertThat( decrypted4.get(), is( strdata ) );
    
  }

} /* ENDCLASS */
