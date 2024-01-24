package artgallery.cms.service;

import artgallery.cms.dto.ExhibitionDTO;
import artgallery.cms.dto.ExhibitionDeleteDTO;
import artgallery.cms.entity.ExhibitionEntity;
import artgallery.cms.entity.GalleryEntity;
import artgallery.cms.exception.ExhibitionDoesNotExistException;
import artgallery.cms.exception.MyGalleryDoesNotExistException;
import artgallery.cms.repository.ExhibitionRepository;
import artgallery.cms.repository.GalleryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExhibitionServiceImpl implements ExhibitionService {
  @Autowired
  private KafkaTemplate<String, ExhibitionDeleteDTO> kafkaTemplateExhibition;
  private final ExhibitionRepository exhibitionRepository;
  private final GalleryRepository galleryRepository;

  public List<ExhibitionDTO> getAllExhibitions(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<ExhibitionEntity> orderPage = exhibitionRepository.findAll(pageable);

    List<ExhibitionEntity> exhibitions = orderPage.getContent();
    return mapToExhibitionDtoList(exhibitions);
  }

  public ExhibitionDTO getExhibitionById(long id) throws ExhibitionDoesNotExistException {
    ExhibitionEntity exhibition = exhibitionRepository.findById(id)
      .orElseThrow(() -> new ExhibitionDoesNotExistException(id));
    return mapToExhibitionDto(exhibition);

  }

  public ExhibitionDTO createExhibition(ExhibitionDTO exhibitionDTO) throws MyGalleryDoesNotExistException {
    ExhibitionEntity exhibition = mapToExhibitionEntity(exhibitionDTO);
    ExhibitionEntity createdExhibition = exhibitionRepository.save(exhibition);
    return mapToExhibitionDto(createdExhibition);
  }

  @Transactional
  public ExhibitionDTO updateExhibition(long id, ExhibitionDTO exhibitionDTO) throws ExhibitionDoesNotExistException, MyGalleryDoesNotExistException {
    Optional<ExhibitionEntity> exhibition = exhibitionRepository.findById(id);
    if (exhibition.isPresent()) {
      ExhibitionEntity exhibitionEntity = exhibition.get();
      exhibitionEntity.setName(exhibitionDTO.getName());
      exhibitionEntity.setStartDate(exhibitionDTO.getStartDate());
      exhibitionEntity.setEndDate(exhibitionDTO.getEndDate());
      GalleryEntity galleryEntity = galleryRepository.findById(exhibitionDTO.getGalleryId()).orElseThrow(() ->
        new MyGalleryDoesNotExistException(exhibitionDTO.getGalleryId()));

      exhibitionEntity.setGallery(galleryEntity);
      ExhibitionEntity newExhibition = exhibitionRepository.save(exhibitionEntity);
      return mapToExhibitionDto(newExhibition);
    }
    throw new ExhibitionDoesNotExistException(id);
  }

  @Transactional
  public void deleteExhibition(long id) {
    ExhibitionDeleteDTO exhibition = new ExhibitionDeleteDTO();
    exhibition.setId(id);
    kafkaTemplateExhibition.send("delete-exhibition", exhibition);
    exhibitionRepository.deleteById(id);
  }

  private ExhibitionDTO mapToExhibitionDto(ExhibitionEntity exhibition) {
    return new ExhibitionDTO(exhibition.getId(), exhibition.getName(), exhibition.getStartDate(),
      exhibition.getEndDate(), exhibition.getGallery().getId());

  }

  private List<ExhibitionDTO> mapToExhibitionDtoList(List<ExhibitionEntity> exhibitions) {
    return exhibitions.stream().map(this::mapToExhibitionDto)
      .collect(Collectors.toList());
  }

  private ExhibitionEntity mapToExhibitionEntity(ExhibitionDTO exhibitionDTO) throws MyGalleryDoesNotExistException {
    ExhibitionEntity exhibition = new ExhibitionEntity();
    exhibition.setName(exhibitionDTO.getName());
    exhibition.setStartDate(exhibitionDTO.getStartDate());
    exhibition.setEndDate(exhibitionDTO.getEndDate());
    GalleryEntity galleryEntity = galleryRepository.findById(exhibitionDTO.getGalleryId()).orElseThrow(() ->
      new MyGalleryDoesNotExistException(exhibitionDTO.getGalleryId()));
    exhibition.setGallery(galleryEntity);
    return exhibition;
  }
}
