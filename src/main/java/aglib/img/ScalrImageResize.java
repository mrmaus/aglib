package aglib.img;

import aglib.FileRef;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * {@link ImageResize} implementation that uses Scalr <a href="https://github.com/thebuzzmedia/imgscalr">https://github.com/thebuzzmedia/imgscalr</a>
 *
 * @author Andrei Maus
 */
public class ScalrImageResize implements ImageResize {

  private final Scalr.Method quality;

  public ScalrImageResize() {
    this(Scalr.Method.QUALITY);
  }

  public ScalrImageResize(Scalr.Method quality) {
    this.quality = quality;
  }

  @Override
  public byte[] resize(FileRef ref, ResizeOperation resizeOperation, InputStream in) throws IOException {
    final BufferedImage sourceImage = ImageIO.read(in);

    final BufferedImage image = doResize(resizeOperation, sourceImage);

    final ByteArrayOutputStream out = new ByteArrayOutputStream();
    ImageIO.write(image, ref.getFileExtension(), out);

    return out.toByteArray();
  }

  private BufferedImage doResize(ResizeOperation resizeOperation, BufferedImage src) {
    final int size = resizeOperation.getSize();

    switch (resizeOperation.getMode()) {
      case BOX: {
        if (src.getHeight() > size || src.getWidth() > size) {
          return Scalr.resize(src, quality, Scalr.Mode.AUTOMATIC, size);
        }
        break;
      }

      case HEIGHT: {
        if (size < src.getHeight()) {
          return Scalr.resize(src, quality, Scalr.Mode.FIT_TO_HEIGHT, size);
        }
        break;
      }

      case WIDTH: {
        if (size < src.getWidth()) {
          return Scalr.resize(src, quality, Scalr.Mode.FIT_TO_WIDTH, size);
        }
      }
    }
    return src;
  }
}
