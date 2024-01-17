package artgallery.cms.repository;

import artgallery.cms.dto.cache.PaintingMetadata;

public interface PaintingCacheRepository {
  PaintingMetadata getPaintingMetadata(Long id);

  void setPaintingMetadata(Long id, PaintingMetadata data);

  void deletePaintingMetadata(Long id);
}
