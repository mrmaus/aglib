package aglib.storage;

/**
 * Storage main (and currently the only) exception
 */
public class StorageException extends RuntimeException {
  public StorageException(String message, Throwable cause) {
    super(message, cause);
  }
}
