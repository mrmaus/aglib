package aglib.img;

/**
 * Image resize operation descriptor
 */
public class ResizeOperation {
  private final Mode mode;
  private final int size;

  public ResizeOperation(Mode mode, int size) {
    this.mode = (mode != null ? mode : Mode.NONE);
    this.size = Math.abs(size);
  }

  public boolean requiresResizing() {
    return this.mode != Mode.NONE;
  }

  public Mode getMode() {
    return mode;
  }

  public int getSize() {
    return size;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ResizeOperation that = (ResizeOperation) o;

    if (size != that.size) return false;
    return mode == that.mode;

  }

  @Override
  public int hashCode() {
    int result = mode.hashCode();
    result = 31 * result + size;
    return result;
  }

  @Override
  public String toString() {
    return "ResizeOperation{" +
        "mode=" + mode +
        ", size=" + size +
        '}';
  }

  public enum Mode {

    /**
     * No image resize requested, image in original size will be returned
     */
    NONE,

    /**
     * Image height and width will be proportionally scaled to fit into the square of given size
     */
    BOX,

    /**
     * Image proportionally scaled to fit the height into the given size
     */
    HEIGHT,

    /**
     * Image proportionally scaled to fit the width into the given size
     */
    WIDTH
  }

}
