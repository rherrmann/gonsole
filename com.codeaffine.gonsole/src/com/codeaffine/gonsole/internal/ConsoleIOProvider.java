package com.codeaffine.gonsole.internal;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public interface ConsoleIOProvider {

  OutputStream getPromptStream();
  OutputStream getOutputStream();
  OutputStream getErrorStream();
  InputStream getInputStream();
  Charset getCharacterEncoding();
  String getLineDelimiter();
}
