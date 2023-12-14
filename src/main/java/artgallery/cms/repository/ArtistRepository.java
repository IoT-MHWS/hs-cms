package artgallery.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import artgallery.cms.entity.ArtistEntity;
import artgallery.cms.entity.Style;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, Long> {
  List<ArtistEntity> findByStyleAndYearOfBirthLessThanEqual(Style style, Integer yearOfBirth);
}

