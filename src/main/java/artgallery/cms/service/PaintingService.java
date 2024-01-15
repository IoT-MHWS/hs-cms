package artgallery.cms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import artgallery.cms.dto.PaintingDeleteDTO;
import artgallery.cms.dto.GalleryExtraDTO;
import artgallery.cms.dto.PaintingDTO;
import artgallery.cms.entity.ArtistEntity;
import artgallery.cms.entity.GalleryEntity;
import artgallery.cms.entity.GalleryPaintingEntity;
import artgallery.cms.entity.PaintingEntity;
import artgallery.cms.exception.ArtistDoesNotExistException;
import artgallery.cms.exception.PaintingDoesNotExistException;
import artgallery.cms.repository.ArtistRepository;
import artgallery.cms.repository.GalleryPaintingRepository;
import artgallery.cms.repository.GalleryRepository;
import artgallery.cms.repository.PaintingRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaintingService {
  @Autowired
  private KafkaTemplate<String, PaintingDeleteDTO> kafkaTemplate;
  private final PaintingRepository paintingRepository;
  private final ArtistRepository artistRepository;
  @Autowired
  private final GalleryPaintingRepository galleryPaintingRepository;
  private final GalleryRepository galleryRepository;


  public List<PaintingDTO> getAllPaintings(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<PaintingEntity> paintingPage = paintingRepository.findAll(pageable);

    List<PaintingEntity> paintings = paintingPage.getContent();
    return mapToPaintingDtoList(paintings);
  }

  public PaintingDTO getPaintingById(long id) throws PaintingDoesNotExistException{
    PaintingEntity painting = paintingRepository.findById(id)
      .orElseThrow(() -> new PaintingDoesNotExistException(id));
    return mapToPaintingDto(painting);
  }

  public PaintingDTO createPainting(PaintingDTO paintingDTO) throws ArtistDoesNotExistException {
    PaintingEntity painting = mapToPaintingEntity(paintingDTO, takeArtist(paintingDTO.getArtistId()));
    paintingRepository.save(painting);
    return mapToPaintingDto(painting);
  }

    @Transactional
    public PaintingDTO updatePainting(long id, PaintingDTO paintingDTO) throws PaintingDoesNotExistException, ArtistDoesNotExistException{
    Optional<PaintingEntity> painting = paintingRepository.findById(id);
    if (painting.isPresent()) {
      PaintingEntity p = painting.get();
      p.setName(paintingDTO.getName());
      p.setYearOfCreation(paintingDTO.getYearOfCreation());
      ArtistEntity ott = artistRepository.findById(paintingDTO.getArtistId()).orElseThrow(() ->
        new ArtistDoesNotExistException(paintingDTO.getArtistId()));
      p.setArtistEntity(ott);
      PaintingEntity newPainting = paintingRepository.save(p);
      return mapToPaintingDto(newPainting);
    }
    throw new PaintingDoesNotExistException(id);
  }

  @Transactional
  public void deletePainting(long id) {
    PaintingDeleteDTO paintingDeleteDTO = new PaintingDeleteDTO();
    paintingDeleteDTO.setId(id);
    kafkaTemplate.send("delete-painting", paintingDeleteDTO);

    galleryPaintingRepository.deleteAllByPaintingId(id);
    paintingRepository.deleteById(id);
  }

  public List<GalleryExtraDTO> getLinksPaintingToGallery(long paintingId) throws PaintingDoesNotExistException {
    paintingRepository.findById(paintingId).orElseThrow(() -> new PaintingDoesNotExistException(paintingId));
    List<GalleryPaintingEntity> links = galleryPaintingRepository.findByPaintingId(paintingId);
    List<GalleryEntity> galleries = links.stream()
      .map(GalleryPaintingEntity::getGallery)
      .toList();
    List<Long> galleryIds = galleries.stream()
      .map(GalleryEntity::getId)
      .collect(Collectors.toList());
    List<GalleryEntity> galleryList = galleryRepository.findAllById(galleryIds);
    return galleryList.stream()
      .map(gallery -> {
        GalleryExtraDTO dto = new GalleryExtraDTO();
        dto.setId(gallery.getId());
        dto.setName(gallery.getName());
        dto.setAddress(gallery.getAddress());
        Optional<GalleryPaintingEntity> gp = takeGalleryPaintingByGalleryIdAndPaintingId(gallery.getId(), paintingId);
        dto.setDescription(gp.get().getDescription());
        return dto;
      })
      .collect(Collectors.toList());
  }

  private Optional<GalleryPaintingEntity> takeGalleryPaintingByGalleryIdAndPaintingId(long galleryId, long paintingId) {
    return galleryPaintingRepository.findByGalleryIdAndPaintingId(galleryId, paintingId);
  }


  private PaintingDTO mapToPaintingDto(PaintingEntity paintingEntity) {
    return new PaintingDTO(paintingEntity.getId(), paintingEntity.getName(),
      paintingEntity.getYearOfCreation(), paintingEntity.getArtistEntity().getId());
  }

  private List<PaintingDTO> mapToPaintingDtoList(List<PaintingEntity> paintings) {
    return paintings.stream().map(this::mapToPaintingDto)
      .collect(Collectors.toList());
  }

  private ArtistEntity takeArtist(Long artistId) throws ArtistDoesNotExistException {
    return artistRepository.findById(artistId).orElseThrow(() ->
      new ArtistDoesNotExistException(artistId));
  }

  private PaintingEntity mapToPaintingEntity(PaintingDTO paintingDTO, ArtistEntity artist){
    PaintingEntity paintingEntity = new PaintingEntity();
    paintingEntity.setName(paintingDTO.getName());
    paintingEntity.setYearOfCreation(paintingDTO.getYearOfCreation());
    paintingEntity.setArtistEntity(artist);
    return paintingEntity;
  }
}
