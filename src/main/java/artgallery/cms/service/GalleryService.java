package artgallery.cms.service;

import artgallery.cms.dto.DescriptionDTO;
import artgallery.cms.dto.GalleryDTO;
import artgallery.cms.dto.GalleryPaintingDTO;
import artgallery.cms.dto.PaintingExtraDTO;
import artgallery.cms.entity.GalleryEntity;
import artgallery.cms.entity.GalleryPaintingEntity;
import artgallery.cms.entity.PaintingEntity;
import artgallery.cms.exception.GalleryDoesNotExistException;
import artgallery.cms.exception.PaintingDoesNotExistException;
import artgallery.cms.repository.ExhibitionRepository;
import artgallery.cms.repository.GalleryPaintingRepository;
import artgallery.cms.repository.GalleryRepository;
import artgallery.cms.repository.PaintingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface GalleryService {

  List<GalleryDTO> getAllGalleries(int page, int size);

  GalleryDTO getGalleryById(long id) throws GalleryDoesNotExistException;

  GalleryDTO createGallery(GalleryDTO galleryDto);

  GalleryDTO updateGallery(long id, GalleryDTO galleryDto) throws GalleryDoesNotExistException;

  void deleteGallery(long id);

  List<PaintingExtraDTO> getLinksGalleryToPainting(long galleryId) throws GalleryDoesNotExistException;

  GalleryPaintingDTO createOrUpdateLinkGalleryToPainting(long galleryId, long paintingId, DescriptionDTO linkDto, boolean exists)
    throws GalleryDoesNotExistException, PaintingDoesNotExistException;

  boolean existsByGalleryIdAndPaintingId(long galleryId, long paintingId);

  void deleteLink(long galleryId, long paintingId);

}
