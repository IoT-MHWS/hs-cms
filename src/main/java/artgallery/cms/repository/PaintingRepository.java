package artgallery.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import artgallery.cms.entity.PaintingEntity;

import java.util.List;

@Repository
public interface PaintingRepository extends JpaRepository<PaintingEntity, Long> {
  List<PaintingEntity> findByArtistEntityId(Long artistId);
}
