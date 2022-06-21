package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.functional.*;

import com.kasisoft.libs.common.utils.*;

import java.io.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class KWriter extends Writer {

  private Writer    impl;
  
  public KWriter(Writer impl) {
    this.impl = impl;
  }

  @Override
  public void write(int c) {
    PrimitiveFunctions.acceptInt(impl::write, c);
  }

  @Override
  public void write(char cbuf[]) {
    Functions.accept(impl::write, cbuf);
  }

  @Override
  public void write(char cbuf[], int off, int len) {
    Functions.run(() -> impl.write(cbuf, off, len));
  }

  @Override
  public void write(String str) {
    Functions.accept(impl::write, str);
  }

  @Override
  public void write(String str, int off, int len) {
    Functions.run(() -> impl.write(str, off, len));
  }

  @Override
  public Writer append(CharSequence csq) {
    return Functions.apply(impl::append, csq);
  }

  @Override
  public Writer append(CharSequence csq, int start, int end) {
    return Functions.get(() -> impl.append(csq, start, end));
  }

  @Override
  public Writer append(char c) {
    return Functions.get(() -> impl.append(c));
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
