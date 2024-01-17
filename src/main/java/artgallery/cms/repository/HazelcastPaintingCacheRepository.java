package artgallery.cms.repository;

import artgallery.cms.dto.cache.PaintingMetadata;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.stereotype.Repository;

@Repository
public class HazelcastPaintingCacheRepository implements PaintingCacheRepository {
  private final IMap<Long, PaintingMetadata> paintingsMetadataMap;

  public HazelcastPaintingCacheRepository(HazelcastInstance hazelcastClient, HazelcastPaintingCacheConfiguration configuration) {
    this.paintingsMetadataMap = hazelcastClient.getMap(configuration.paintingsMetadataMap);
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
