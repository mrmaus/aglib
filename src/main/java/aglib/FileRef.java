package aglib;

import java.io.IOException;
import java.io.InputStream;

/**
 * Abstract reference to a file which can reside on local FS, in database or remote system
 */
public interface FileRef {

  /**
   * Some systems might provide unique file identifier
   *
   * @return file unique ID (if available); null otherwise
   */
  default String getId() {
    return null;
  }

  /**
   * @return content MIME type as string if available; null otherwise
   */
  default String getContentType() {
    return null;
  }

  /**
   * @return file extension that is most suitable for this content type
   */
  default String getFileExtension() {
    final String name = getFileName();
    if (name != null) {
      final int dotIndex = name.lastIndexOf('.');
      if (dotIndex > -1) {
        return name.substring(dotIndex + 1);
      }
    }
    return null;
  }

  String getFileName();

  /**
   * @return size of the content in bytes; implementation may return -1 if length is expensive to calculate
   */
  default long getContentLength() {
    return -1;
  }

  /**
   * @return last modification timestamp (in millis) of the content; -1 if timestamp is unknown
   */
  default long getLastModified() {
    return -1;
  }

  /**
   * @return referenced file input stream if possible; null otherwise
   */
  default InputStream getInputStream() throws IOException {
    throw new UnsupportedOperationException("Current FileRef can't create stream");
  }
}
