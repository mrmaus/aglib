package aglib;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * {@link FileRef} implementation for {@link File}
 */
public class SystemFileRef implements FileRef {
  private final File file;
  private final String contentType;

  public SystemFileRef(File file) {
    this(file, null);
  }

  public SystemFileRef(File file, String contentType) {
    if (file == null) {
      throw new IllegalArgumentException("File parameter is NULL");
    }

    this.file = file;
    this.contentType = contentType;
  }

  @Override
  public long getContentLength() {
    return file.length();
  }

  @Override
  public long getLastModified() {
    return file.lastModified();
  }

  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public String getFileName() {
    return file.getName();
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return new FileInputStream(file);
  }
}
