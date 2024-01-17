package artgallery.cms.repository;

import artgallery.cms.entity.ExhibitionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionRepository extends JpaRepository<ExhibitionEntity, Long> {

//  @Modifying
//  @Query("delete from ExhibitionEntity e where e.gallery.id = ?1")
//  void deleteAllByGalleryId(Long id);
}
