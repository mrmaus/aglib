package aglib;

import java.io.InputStream;

/**
 * Infers some additional properties of provided content data
 */
public interface ContentTypeDetector {

  /**
   * Detects the MIME type for the provided content. Implementation must not change the state of the stream as much as
   * possible and must expect that the same stream instance will be used after this call (use mark/reset on stream as an
   * option)
   *
   * @param in {@link InputStream} instance with content; implementation should never close the stream!
   * @return MIME type as string for the provided content; can return NULL
   */
  String detect(InputStream in);

  /**
   * Attempts to guess best possible file extension for the provided MIME type
   *
   * @param contentType MIME content type as string
   * @return preferable file extension for the specified MIME type; can return NULL
   */
  String getDefaultFileExtension(String contentType);
}
