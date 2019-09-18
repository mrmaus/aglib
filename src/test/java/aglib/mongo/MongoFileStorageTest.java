package aglib.mongo;

import aglib.BasicRef;
import aglib.FileRef;
import com.mongodb.MongoClient;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class MongoFileStorageTest {

  private MongoFileStorage storage;

  private MongodExecutable executable;

  @Before
  public void setUp() throws Exception {
    final int port = Network.getFreeServerPort();

    final IMongodConfig config = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
        .net(new Net(port, Network.localhostIsIPv6()))
        .build();

    final MongodStarter starter = MongodStarter.getDefaultInstance();
    executable = starter.prepare(config);
    executable.start();

    final MongoClient mongoClient = new MongoClient("localhost", port);
    final GridFSBucket bucket = GridFSBuckets.create(mongoClient.getDatabase("junit"));

    storage = new MongoFileStorage(bucket);
  }

  @After
  public void tearDown() {
    executable.stop();
  }

  @Test
  public void testUploadResource() throws Exception {
    final BasicRef ref = new BasicRef("test.jpg", "image/jpeg", -1, -1, this::getImage);
    final String id = storage.upload(ref);
    assertNotNull(id);

    final FileRef r = storage.getRef(id);
    assertEquals("image/jpeg", r.getContentType());
    assertEquals("jpg", r.getFileExtension());
    assertTrue(Pattern.compile("[\\d\\w]+\\.jpg").matcher(r.getFileName()).matches());
    assertEquals(1_208_113, r.getContentLength());
    assertTrue(r.getLastModified() > 0);


    assertArrayEquals(IOUtils.toByteArray(getImage()), IOUtils.toByteArray(storage.openInputStream(id)));

  }

  private InputStream getImage() {
    return getClass().getResourceAsStream("test.jpg");
  }

}