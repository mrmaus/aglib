package aglib.storage;

import aglib.FileRef;

import java.io.IOException;
import java.io.InputStream;

/**
 * Simple {@link FileStorage} delegate
 */
public class DelegatingFileStorage implements FileStorage {
  private final FileStorage delegate;

  public DelegatingFileStorage(FileStorage delegate) {
    if (delegate == null) {
      throw new IllegalArgumentException("delegate depo is null");
    }
    this.delegate = delegate;
  }

  @Override
  public String upload(FileRef ref) throws StorageException {
    return delegate.upload(ref);
  }

  @Override
  public FileRef getRef(String id) throws StorageException {
    return delegate.getRef(id);
  }

  @Override
  public void delete(String id) throws StorageException {
    delegate.delete(id);
  }

  @Override
  public InputStream openInputStream(String id) throws IOException {
    return delegate.openInputStream(id);
  }
}
