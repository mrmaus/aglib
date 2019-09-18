package aglib.img;

import aglib.FileRef;
import aglib.storage.FileStorage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_NOT_MODIFIED;

/**
 * Helper class for loading image, resizing it and flushing to response; HTTP servlet/filter or spring controller can
 * easily delegate to this class
 *
 * @author Andrei Maus
 */
public class ImageStorageWebHelper {

  private final FileStorage fileStorage;

  /**
   * HTTP cache-control setting
   */
  private int cacheSeconds = 0;

  /**
   * If provided, image size can be changed via url parameter
   */
  private ImageResize imageResize;

  public ImageStorageWebHelper(FileStorage fileStorage) {
    if (fileStorage == null) {
      throw new IllegalArgumentException("depo argument is null");
    }
    this.fileStorage = fileStorage;
  }


  /**
   * Loads file and flushes it to response as body content
   *
   * @param id       file ID
   * @param request  {@link HttpServletRequest} instance
   * @param response {@link HttpServletResponse} instance
   * @throws IOException
   */
  public void render(String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
    render(id, null, request, response);
  }


  /**
   * Loads image and flushes it to response as body content, resizing it as requested
   *
   * @param id              file ID
   * @param resizeOperation {@link ResizeOperation} instance
   * @param request         {@link HttpServletRequest} instance
   * @param response        {@link HttpServletResponse} instance
   * @throws IOException
   */
  public void render(final String id,
                     final ResizeOperation resizeOperation,
                     final HttpServletRequest request,
                     final HttpServletResponse response) throws IOException {
    if (id == null || id.isEmpty()) {
      response.setStatus(SC_NOT_FOUND);
      return;
    }

    final FileRef ref = fileStorage.getRef(id);

    if (ref == null) {
      response.setStatus(SC_NOT_FOUND);
      return;
    }

    if (ref.getLastModified() > 0) {
      try {
        final long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        final long lastModified = ref.getLastModified() / 1000 * 1000;
        if (ifModifiedSince == lastModified) {
          response.sendError(SC_NOT_MODIFIED);
          return;
        }
      } catch (IllegalArgumentException e) {
        //ignore: content data will be sent to the caller
      }
      response.setDateHeader("Last-Modified", ref.getLastModified());
    }

    if (cacheSeconds > 0) {
      response.setHeader("Cache-Control", "max-age=" + cacheSeconds);
      response.setDateHeader("Expires", System.currentTimeMillis() + cacheSeconds * 1000L);
    }

    response.setContentType(ref.getContentType());

    try (final InputStream in = fileStorage.openInputStream(id)) {
      if (shouldResize(resizeOperation)) { //using re-sized image if requested
        final byte[] out = imageResize.resize(ref, resizeOperation, in);
        response.setContentLength(out.length);
        response.getOutputStream().write(out);
      } else {
        response.setContentLength((int) ref.getContentLength());
        copy(in, response.getOutputStream());
      }
    }
  }

  private boolean shouldResize(ResizeOperation resizeOperation) {
    return resizeOperation != null && resizeOperation.requiresResizing() && imageResize != null;
  }

  static void copy(InputStream in, OutputStream out) throws IOException {
    final byte[] buffer = new byte[1024 * 4];
    int n;
    while (-1 != (n = in.read(buffer))) {
      out.write(buffer, 0, n);
    }
  }

  public void setCacheSeconds(int cacheSeconds) {
    this.cacheSeconds = cacheSeconds;
  }

  public void setImageResize(ImageResize imageResize) {
    this.imageResize = imageResize;
  }
}
