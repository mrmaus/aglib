package aglib.tika;

import aglib.ContentTypeDetector;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.mime.MimeTypeException;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link ContentTypeDetector} implementation that uses Apache Tika under the hood
 *
 * @author Andrei Maus
 */
public class TikaContentTypeDetector implements ContentTypeDetector {
  private final Tika tika;
  private final TikaConfig config;

  public TikaContentTypeDetector() {
    this(new Tika(), TikaConfig.getDefaultConfig());
  }

  public TikaContentTypeDetector(Tika tika, TikaConfig config) {
    this.tika = tika;
    this.config = config;
  }

  @Override
  public String detect(InputStream in) {
    try {
      return tika.detect(in);
    } catch (IOException e) {
      throw new RuntimeException("Failed to detect content type", e);
    }
  }

  @Override
  public String getDefaultFileExtension(String contentType) {
    try {
      return config.getMimeRepository().forName(contentType).getExtension();
    } catch (MimeTypeException e) {
      throw new RuntimeException("Failed to detect default file extension for content type: " + contentType, e);
    }
  }
}
