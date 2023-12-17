package artgallery.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import artgallery.cms.entity.ExhibitionEntity;

@Repository
public interface ExhibitionRepository extends JpaRepository<ExhibitionEntity, Long> {

//  @Modifying
//  @Query("delete from ExhibitionEntity e where e.gallery.id = ?1")
//  void deleteAllByGalleryId(Long id);
}
