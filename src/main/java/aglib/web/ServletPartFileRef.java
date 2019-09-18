package aglib.web;


import aglib.FileRef;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;

/**
 * {@link FileRef} for servlet 3.0 {@link javax.servlet.http.Part}
 */
public class ServletPartFileRef implements FileRef {
  private final Part part;

  public ServletPartFileRef(Part part) {
    if (part == null) {
      throw new IllegalArgumentException("Part is NULL");
    }
    this.part = part;
  }

  @Override
  public long getContentLength() {
    return part.getSize();
  }

  @Override
  public String getContentType() {
    return part.getContentType();
  }

  @Override
  public String getFileName() {
    return part.getSubmittedFileName();
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return part.getInputStream();
  }
}
