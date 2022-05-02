package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.functional.*;
import com.kasisoft.libs.common.utils.*;

import java.io.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class KInputStream extends InputStream {

  private InputStream   impl;

  public KInputStream(InputStream impl) {
    this.impl = impl;
  }
  
  @Override
  public int read() {
    return PrimitiveFunctions.getInt(impl::read);
  }

  @Override
  public int read(byte b[]) {
    return PrimitiveFunctions.applyInt(impl::read, b);
  }

  @Override
  public int read(byte b[], int off, int len) {
    return PrimitiveFunctions.getInt(() -> impl.read(b, off, len));
  }

  @Override
  public byte[] readAllBytes() {
    return Functions.get(impl::readAllBytes);
  }

  @Override
  public byte[] readNBytes(int len) {
    return Functions.apply(impl::readNBytes, len);
  }

  @Override
  public int readNBytes(byte[] b, int off, int len) {
    return PrimitiveFunctions.getInt(() -> impl.readNBytes(b, off, len));
  }

  @Override
  public long skip(long n) {
    return PrimitiveFunctions.applyLong($ -> impl.skip($), n);
  }

  @Override
  public int available() {
    return PrimitiveFunctions.getInt(impl::available);
  }

  @Override
  public void close() {
    Functions.run(impl::close);
  }

  @Override
  public synchronized void mark(int readlimit) {
    PrimitiveFunctions.acceptInt(impl::mark, readlimit);
  }

  @Override
  public synchronized void reset() {
    Functions.run(impl::reset);
  }

  @Override
  public boolean markSupported() {
    return PrimitiveFunctions.getBoolean(impl::markSupported);
  }

  @Override
  public long transferTo(OutputStream out) {
    return PrimitiveFunctions.applyLong(impl::transferTo, out);
  }

} /* ENDCLASS */
