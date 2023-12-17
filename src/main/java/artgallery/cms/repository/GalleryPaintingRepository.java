package artgallery.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import artgallery.cms.entity.GalleryPaintingEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface GalleryPaintingRepository extends JpaRepository<GalleryPaintingEntity, Long> {
  boolean existsByGalleryId(Long id);
  boolean existsByPaintingId(Long id);

  @Modifying
  @Query("delete from GalleryPaintingEntity e where e.gallery.id = ?1")
  void deleteAllByGalleryId(Long id);

  @Modifying
  @Query("delete from GalleryPaintingEntity e where e.painting.id = ?1")
  void deleteAllByPaintingId(Long id);
  void deleteGalleryPaintingEntityByGalleryIdAndPaintingId(long id0, long id1);

  boolean existsByGalleryIdAndPaintingId(long galleryId, long paintingId);

  List<GalleryPaintingEntity> findByGalleryId(long galleryId);

  Optional<GalleryPaintingEntity> findByGalleryIdAndPaintingId(long galleryId, long paintingId);

  List<GalleryPaintingEntity> findByPaintingId(long paintingId);
}
