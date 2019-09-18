package aglib.mongo;

import aglib.FileRef;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.Document;

/**
 * {@link FileRef} instance that wraps MongoDB GridFS file reference
 *
 * @author Andrei Maus
 */
public class MongoFile implements FileRef {
  static final String META_CONTENT_TYPE = "contentType";

  private final GridFSFile file;

  @Override
  public String getId() {
    return file.getId() == null ? null : String.valueOf(file.getId());
  }

  MongoFile(GridFSFile file) {
    this.file = file;
  }

  @Override
  public String getContentType() {
    final Document metadata = file.getMetadata();
    return metadata == null ? null : metadata.getString(META_CONTENT_TYPE);
  }

  @Override
  public long getLastModified() {
    return file.getUploadDate().getTime();
  }

  @Override
  public long getContentLength() {
    return file.getLength();
  }

  @Override
  public String getFileName() {
    return file.getFilename();
  }
}
