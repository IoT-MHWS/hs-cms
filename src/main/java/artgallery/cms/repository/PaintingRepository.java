package artgallery.cms.repository;

import artgallery.cms.entity.PaintingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaintingRepository extends JpaRepository<PaintingEntity, Long> {
  List<PaintingEntity> findByArtistEntityId(Long artistId);
}
