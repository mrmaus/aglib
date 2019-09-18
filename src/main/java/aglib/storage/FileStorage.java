package aglib.storage;

import aglib.FileRef;

import java.io.IOException;
import java.io.InputStream;

/**
 * Storage for {@link FileRef} content
 */
public interface FileStorage {

  /**
   * Stores provided {@link FileRef} data
   *
   * @param ref {@link FileRef} instance
   * @return uploaded content ID; can be used later in {@link #getRef(String)} call for example to retrieve the content
   * @throws StorageException in case of internal malfunctioning
   */
  String upload(FileRef ref) throws StorageException;

  /**
   * Retrieves the content from the store
   *
   * @param id content unique ID
   * @return {@link FileRef} instance; can return NULL if content not found
   * @throws StorageException in case of internal malfunctioning
   */
  FileRef getRef(String id) throws StorageException;

  /**
   * Permanently removes file from the depot
   *
   * @param id file unique ID
   * @throws StorageException
   */
  void delete(String id) throws StorageException;

  /**
   * Opens stream for reading content; depending on the implementation the stream can be opened multiple times or just
   * one, so the consumer must always assume that stream can be opened only once
   *
   * @return {@link InputStream} instance; NULL if file doesn't exist
   */
  InputStream openInputStream(String id) throws IOException;
}
