package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.functional.*;

import com.kasisoft.libs.common.utils.*;

import java.io.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class KOutputStream extends OutputStream {

  private OutputStream   impl;
  
  public KOutputStream(OutputStream impl) {
    this.impl = impl;
  }
  
  @Override
  public void write(int b) {
    PrimitiveFunctions.acceptInt(impl::write, b);
  }

  @Override
  public void write(byte b[]) {
    Functions.accept(impl::write, b);
  }

  @Override
  public void write(byte b[], int off, int len) {
    Functions.run(() -> impl.write(b, off, len));
  }

  @Override
  public void flush() {
    Functions.run(impl::flush);
  }

  @Override
  public void close() {
    Functions.run(impl::close);
  }

} /* ENDCLASS */
