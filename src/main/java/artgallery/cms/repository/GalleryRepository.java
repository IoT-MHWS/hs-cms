package artgallery.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import artgallery.cms.entity.GalleryEntity;

@Repository
public interface GalleryRepository extends JpaRepository<GalleryEntity, Long> {
}
