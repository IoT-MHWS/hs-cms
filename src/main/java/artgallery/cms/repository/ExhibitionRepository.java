package artgallery.cms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import artgallery.cms.entity.ExhibitionEntity;

import java.util.Optional;

@Repository
public interface ExhibitionRepository extends JpaRepository<ExhibitionEntity, Long> {
}
