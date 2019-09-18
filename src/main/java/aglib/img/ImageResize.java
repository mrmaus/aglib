package aglib.img;

import aglib.FileRef;

import java.io.IOException;
import java.io.InputStream;

/**
 * Image resizing API
 */
public interface ImageResize {

  /**
   * Resize image according to provided specification and return new image instance; operation can return same image
   * instance if resize was not required/performed
   *
   * @param src             image to resize
   * @param resizeOperation resize specification
   * @return re-sized image bytes
   */
  byte[] resize(FileRef ref, ResizeOperation resizeOperation, InputStream src) throws IOException;
}
