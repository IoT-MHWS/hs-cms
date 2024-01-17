package artgallery.cms.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastPaintingCacheConfiguration {

  @Value("${app.hazelcast.maps.paintings-metadata}")
  public String paintingsMetadataMap;
}
