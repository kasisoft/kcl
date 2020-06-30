package com.kasisoft.libs.common.io;

import com.kasisoft.libs.common.functional.Functions;
import com.kasisoft.libs.common.utils.PrimitiveFunctions;

import java.io.InputStream;
import java.io.OutputStream;

import lombok.experimental.FieldDefaults;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@AllArgsConstructor 
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KInputStream extends InputStream {

  InputStream   impl;

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
  public void skipNBytes(long n) {
    PrimitiveFunctions.acceptLong(impl::skipNBytes, n);
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
