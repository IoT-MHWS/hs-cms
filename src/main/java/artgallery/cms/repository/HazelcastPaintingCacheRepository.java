package artgallery.cms.repository;

import artgallery.cms.dto.cache.PaintingMetadata;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final HazelcastInstance hazelcastClient;
  private IMap<Long, String> paintingsMetadataMap;

  public HazelcastPaintingCacheRepository(ClientConfig config, HazelcastPaintingCacheConfiguration configuration) {
    ClientStateListener clientStateListener = new ClientStateListener(config);
    this.hazelcastClient = HazelcastClient.newHazelcastClient(config);

    CompletableFuture.runAsync(() -> {
      try {
        if (clientStateListener.awaitConnected()) {
          paintingsMetadataMap = hazelcastClient.getMap(configuration.paintingsMetadataMap);
          log.info("hazelcast maps initialized");
        }
      } catch (InterruptedException ex) {
        log.error(ex.getMessage());
        Thread.currentThread().interrupt();
      }
    });
  }

  @Override
  public PaintingMetadata getPaintingMetadata(Long id) {
    try {
      var data = paintingsMetadataMap.get(id);
      return data == null ? null : objectMapper.readValue(data, PaintingMetadata.class);
    } catch (JsonProcessingException ex) {
      log.error(ex.getMessage());
      return null;
    }
  }

  @Override
  public void setPaintingMetadata(Long id, PaintingMetadata data) {
    try {
      paintingsMetadataMap.set(id, objectMapper.writeValueAsString(data));
    } catch (JsonProcessingException ex) {
      log.error(ex.getMessage());
    }
  }

  @Override
  public void deletePaintingMetadata(Long id) {
    paintingsMetadataMap.delete(id);
  }
}
