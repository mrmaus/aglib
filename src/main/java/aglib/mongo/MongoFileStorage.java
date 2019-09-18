package aglib.mongo;

import aglib.FileRef;
import aglib.storage.FileStorage;
import aglib.storage.StorageException;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.IOException;
import java.io.InputStream;

/**
 * {@link FileStorage} implementation that uses MongoDB GridFS
 */
public class MongoFileStorage implements FileStorage {
  private final GridFSBucket fs;

  public MongoFileStorage(GridFSBucket fs) {
    this.fs = fs;
  }

  @Override
  public FileRef getRef(String id) {
    try (final MongoCursor<GridFSFile> it = fs.find(new BasicDBObject("_id", new ObjectId(id))).iterator()) {
      if (it.hasNext()) {
        return new MongoFile(it.next());
      }
      return null; //todo: optional!
    }
  }

  @Override
  public void delete(String id) throws StorageException {
    fs.delete(new ObjectId(id));
  }

  @Override
  public InputStream openInputStream(String id) {
    return fs.openDownloadStream(new ObjectId(id));
  }

  @Override
  public String upload(FileRef ref) {
    final InputStream in;
    try {
      in = ref.getInputStream();
    } catch (IOException e) {
      throw new RuntimeException("Unable to open stream with provided FileRef", e);
    }

    final Document meta = new Document();
    if (ref.getContentType() != null) {
      meta.put(MongoFile.META_CONTENT_TYPE, ref.getContentType());
    }

    final GridFSUploadOptions options = new GridFSUploadOptions().metadata(meta);
    final ObjectId id = fs.uploadFromStream(ref.getFileName(), in, options);

    return id.toString();
  }
}
