package aglib;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

/**
 * Simple non-wrapping {@link FileRef} implementation
 */
public class BasicRef implements FileRef {
  private final String fileName;
  private final String contentType;
  private final long contentLength;
  private final long lastModified;

  private Supplier<InputStream> inputStreamSupplier;

  public BasicRef(String fileName,
                  String contentType,
                  long contentLength,
                  long lastModified,
                  Supplier<InputStream> inputStreamSupplier) {
    this.fileName = fileName;
    this.contentType = contentType;
    this.contentLength = contentLength;
    this.lastModified = lastModified;
    this.inputStreamSupplier = inputStreamSupplier;
  }

  @Override
  public String getFileName() {
    return fileName;
  }

  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public long getContentLength() {
    return contentLength;
  }

  @Override
  public long getLastModified() {
    return lastModified;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    if (inputStreamSupplier != null) {
      return inputStreamSupplier.get();
    }
    return null;
  }
}
