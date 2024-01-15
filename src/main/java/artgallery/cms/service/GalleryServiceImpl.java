package artgallery.cms.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import artgallery.cms.repository.ExhibitionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import artgallery.cms.dto.DescriptionDTO;
import artgallery.cms.dto.GalleryDTO;
import artgallery.cms.dto.GalleryPaintingDTO;
import artgallery.cms.dto.PaintingExtraDTO;
import artgallery.cms.entity.GalleryEntity;
import artgallery.cms.entity.GalleryPaintingEntity;
import artgallery.cms.entity.PaintingEntity;
import artgallery.cms.exception.GalleryDoesNotExistException;
import artgallery.cms.exception.PaintingDoesNotExistException;
import artgallery.cms.repository.GalleryPaintingRepository;
import artgallery.cms.repository.GalleryRepository;
import artgallery.cms.repository.PaintingRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GalleryServiceImpl implements GalleryService {
  private final GalleryRepository galleryRepository;
  private final GalleryPaintingRepository galleryPaintingRepository;
  private final PaintingRepository paintingRepository;
  private final ExhibitionRepository exhibitionRepository;

  public List<GalleryDTO> getAllGalleries(int page, int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<GalleryEntity> galleryPage = galleryRepository.findAll(pageable);

    List<GalleryEntity> galleries = galleryPage.getContent();
    return mapToGalleryDtoList(galleries);
  }


  public GalleryDTO getGalleryById(long id) throws GalleryDoesNotExistException{
    GalleryEntity optionalGallery = galleryRepository.findById(id)
      .orElseThrow(() -> new GalleryDoesNotExistException(id));
    return mapToGalleryDto(optionalGallery);

  }

  @Transactional
  public GalleryDTO createGallery(GalleryDTO galleryDto) {
    GalleryEntity gallery = mapToGalleryEntity(galleryDto);
    galleryRepository.save(gallery);
    return mapToGalleryDto(gallery);
  }

  @Transactional
  public GalleryDTO updateGallery(long id, GalleryDTO galleryDto) throws GalleryDoesNotExistException {
    Optional<GalleryEntity> optionalGallery = galleryRepository.findById(id);
    if (optionalGallery.isPresent()) {
      GalleryEntity existingGallery = optionalGallery.get();
      existingGallery.setName(galleryDto.getName());
      existingGallery.setAddress(galleryDto.getAddress());
      GalleryEntity updatedGallery = galleryRepository.save(existingGallery);
      return mapToGalleryDto(updatedGallery);
    }
    throw new GalleryDoesNotExistException(id);
  }


  @Transactional
  public void deleteGallery(long id) {
    galleryPaintingRepository.deleteAllByGalleryId(id);
    galleryRepository.deleteById(id);
  }

  public List<PaintingExtraDTO> getLinksGalleryToPainting(long galleryId) throws GalleryDoesNotExistException {
    galleryRepository.findById(galleryId).orElseThrow(() -> new GalleryDoesNotExistException(galleryId));
    List<GalleryPaintingEntity> links = galleryPaintingRepository.findByGalleryId(galleryId);
    List<PaintingEntity> paintings = links.stream()
      .map(GalleryPaintingEntity::getPainting)
      .toList();
    List<Long> paintingIds = paintings.stream()
      .map(PaintingEntity::getId)
      .collect(Collectors.toList());
    List<PaintingEntity> paintingList = paintingRepository.findAllById(paintingIds);
    return paintingList.stream()
      .map(painting -> {
        PaintingExtraDTO dto = new PaintingExtraDTO();
        dto.setId(painting.getId());
        dto.setName(painting.getName());
        dto.setYearOfCreation(painting.getYearOfCreation());
        dto.setArtistId(painting.getArtistEntity().getId());
        Optional<GalleryPaintingEntity> gp = takeGalleryPaintingByGalleryIdAndPaintingId(galleryId, painting.getId());
        dto.setDescription(gp.get().getDescription());
        return dto;
      })
      .collect(Collectors.toList());
  }

  private Optional<GalleryPaintingEntity> takeGalleryPaintingByGalleryIdAndPaintingId(long galleryId, long paintingId) {
    return galleryPaintingRepository.findByGalleryIdAndPaintingId(galleryId, paintingId);
  }

  @Transactional
  public GalleryPaintingDTO createOrUpdateLinkGalleryToPainting(long galleryId, long paintingId, DescriptionDTO linkDto, boolean exists)
    throws GalleryDoesNotExistException, PaintingDoesNotExistException {
    GalleryEntity gallery = galleryRepository.findById(galleryId)
      .orElseThrow(() -> new GalleryDoesNotExistException(galleryId));
    PaintingEntity painting = paintingRepository.findById(paintingId)
      .orElseThrow(() -> new PaintingDoesNotExistException(paintingId));
    GalleryPaintingEntity link;
    if (exists) {
      link = galleryPaintingRepository
        .findByGalleryIdAndPaintingId(galleryId, paintingId)
        .orElseThrow(() -> new RuntimeException("Impossible error"));
    } else {
      link = new GalleryPaintingEntity();
      link.setGallery(gallery);
      link.setPainting(painting);
    }
    link.setDescription(linkDto.getDescription());
    galleryPaintingRepository.save(link);
    return mapToGallery2PaintingDto(link);
  }

  public boolean existsByGalleryIdAndPaintingId(long galleryId, long paintingId) {
    return galleryPaintingRepository.existsByGalleryIdAndPaintingId(galleryId, paintingId);
  }

  @Transactional
  public void deleteLink(long galleryId, long paintingId) {
    if (galleryPaintingRepository.existsByGalleryIdAndPaintingId(galleryId, paintingId)) {
      galleryPaintingRepository.deleteGalleryPaintingEntityByGalleryIdAndPaintingId(galleryId, paintingId);
    }
  }

  private GalleryDTO mapToGalleryDto(GalleryEntity gallery) {
    return new GalleryDTO(gallery.getId(), gallery.getName(), gallery.getAddress());
  }

  private List<GalleryDTO> mapToGalleryDtoList(List<GalleryEntity> galleries) {
    return galleries.stream().map(this::mapToGalleryDto)
      .collect(Collectors.toList());
  }


  private GalleryEntity mapToGalleryEntity(GalleryDTO galleryDto) {
    GalleryEntity gallery = new GalleryEntity();
    gallery.setName(galleryDto.getName());
    gallery.setAddress(galleryDto.getAddress());
    return gallery;
  }

  private GalleryPaintingDTO mapToGallery2PaintingDto(GalleryPaintingEntity link) {
    return new GalleryPaintingDTO(link.getId(), link.getGallery().getId(), link.getPainting().getId(), link.getDescription());
  }
}
