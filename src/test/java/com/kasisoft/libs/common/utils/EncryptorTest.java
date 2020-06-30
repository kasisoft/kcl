package com.kasisoft.libs.common.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class EncryptorTest {

  private static final String ALGORITHM = "DES";

  private static final String CIPHER    = "DES/CBC/PKCS5Padding";
  
  private static final String SECRET    = "CK3gFoo+ddY=";
  
  @Test(groups = "all")
  public void cipherAndAlgorithm() {
    
    var encryptor = new Encryptor(CIPHER, ALGORITHM);
    var strdata   = "gollum";
    var data      = strdata.getBytes();
    
    // byte[]
    
    var encrypted1 = encryptor.encrypt((byte[]) null); 
    assertNotNull(encrypted1); 
    assertFalse(encrypted1.isPresent());

    var encrypted2 = encryptor.encrypt(data); 
    assertNotNull(encrypted2); 
    assertTrue(encrypted2.isPresent());
    
    var decrypted1 = encryptor.decrypt((byte[]) null);
    assertNotNull(decrypted1); 
    assertFalse(decrypted1.isPresent());

    var decrypted2 = encryptor.decrypt(encrypted2.get());
    assertNotNull(decrypted2); 
    assertTrue(decrypted2.isPresent());
    assertThat(decrypted2.get(), is(data));

    // String
    
    var encrypted3 = encryptor.encrypt((String) null); 
    assertNotNull(encrypted3); 
    assertFalse(encrypted3.isPresent());

    var encrypted4 = encryptor.encrypt(strdata); 
    assertNotNull(encrypted4); 
    assertTrue(encrypted4.isPresent());
    
    var decrypted3 = encryptor.decrypt((String) null);
    assertNotNull(decrypted3); 
    assertFalse(decrypted3.isPresent());

    var decrypted4 = encryptor.decrypt(encrypted4.get());
    assertNotNull(decrypted4); 
    assertTrue(decrypted4.isPresent());
    assertThat(decrypted4.get(), is(strdata));
    
  }

  @Test(groups = "all")
  public void cipherAndAlgorithmAndSecret() {
    
    var encryptor = new Encryptor(CIPHER, ALGORITHM, SECRET);
    var strdata   = "gollum";
    var data      = strdata.getBytes();
    
    // byte[]
    
    var encrypted1 = encryptor.encrypt((byte[]) null); 
    assertNotNull(encrypted1); 
    assertFalse(encrypted1.isPresent());

    var encrypted2 = encryptor.encrypt(data); 
    assertNotNull(encrypted2); 
    assertTrue(encrypted2.isPresent());
    
    var decrypted1 = encryptor.decrypt((byte[]) null);
    assertNotNull(decrypted1); 
    assertFalse(decrypted1.isPresent());

    var decrypted2 = encryptor.decrypt(encrypted2.get());
    assertNotNull(decrypted2); 
    assertTrue(decrypted2.isPresent());
    assertThat(decrypted2.get(), is(data));

    // String
    
    var encrypted3 = encryptor.encrypt((String) null); 
    assertNotNull(encrypted3); 
    assertFalse(encrypted3.isPresent());

    var encrypted4 = encryptor.encrypt(strdata); 
    assertNotNull(encrypted4); 
    assertTrue(encrypted4.isPresent());
    
    var decrypted3 = encryptor.decrypt((String) null);
    assertNotNull(decrypted3); 
    assertFalse(decrypted3.isPresent());

    var decrypted4 = encryptor.decrypt(encrypted4.get());
    assertNotNull(decrypted4); 
    assertTrue(decrypted4.isPresent());
    assertThat(decrypted4.get(), is(strdata));
    
  }

  @Test(groups = "all")
  public void cipherAndAlgorithmAndSecretAndSalt() {
    
    var encryptor = new Encryptor(CIPHER, ALGORITHM, SECRET, "01234567".getBytes());
    var strdata   = "gollum";
    var data      = strdata.getBytes();
    
    // byte[]
    
    var encrypted1 = encryptor.encrypt((byte[]) null); 
    assertNotNull(encrypted1); 
    assertFalse(encrypted1.isPresent());

    var encrypted2 = encryptor.encrypt(data); 
    assertNotNull(encrypted2); 
    assertTrue(encrypted2.isPresent());
    
    var decrypted1 = encryptor.decrypt((byte[]) null);
    assertNotNull(decrypted1); 
    assertFalse(decrypted1.isPresent());

    var decrypted2 = encryptor.decrypt(encrypted2.get());
    assertNotNull(decrypted2); 
    assertTrue(decrypted2.isPresent());
    assertThat(decrypted2.get(), is(data));

    // String
    
    var encrypted3 = encryptor.encrypt((String) null); 
    assertNotNull(encrypted3); 
    assertFalse(encrypted3.isPresent());

    var encrypted4 = encryptor.encrypt(strdata); 
    assertNotNull(encrypted4); 
    assertTrue(encrypted4.isPresent());
    
    var decrypted3 = encryptor.decrypt((String) null);
    assertNotNull(decrypted3); 
    assertFalse(decrypted3.isPresent());

    var decrypted4 = encryptor.decrypt(encrypted4.get());
    assertNotNull(decrypted4); 
    assertTrue(decrypted4.isPresent());
    assertThat(decrypted4.get(), is(strdata));
    
  }

} /* ENDCLASS */
