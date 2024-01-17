package artgallery.cms.repository;

import artgallery.cms.dto.cache.PaintingMetadata;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.util.ClientStateListener;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

@Repository
@Slf4j
public class HazelcastPaintingCacheRepository implements PaintingCacheRepository {

  private final HazelcastInstance hazelcastClient;
  private IMap<Long, PaintingMetadata> paintingsMetadataMap;

  public HazelcastPaintingCacheRepository(ClientConfig config, HazelcastPaintingCacheConfiguration configuration) {
    ClientStateListener clientStateListener = new ClientStateListener(config);
    this.hazelcastClient = HazelcastClient.newHazelcastClient(config);

    CompletableFuture.runAsync(() -> {
      try {
        if (clientStateListener.awaitConnected()) {
          paintingsMetadataMap = hazelcastClient.getMap(configuration.paintingsMetadataMap);
          log.info("hazelcast maps initialized");
        }
      } catch (InterruptedException ex){
        ex.printStackTrace();
      }
    });
  }

  @Override
  public PaintingMetadata getPaintingMetadata(Long id) {
    return paintingsMetadataMap.get(id);
  }

  @Override
  public void setPaintingMetadata(Long id, PaintingMetadata data) {
    paintingsMetadataMap.set(id, data);
  }

  @Override
  public void deletePaintingMetadata(Long id) {
    paintingsMetadataMap.delete(id);
  }
}
