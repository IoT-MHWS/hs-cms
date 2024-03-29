package artgallery.cms.service;

import artgallery.cms.dto.DescriptionDTO;
import artgallery.cms.dto.GalleryDTO;
import artgallery.cms.dto.GalleryPaintingDTO;
import artgallery.cms.dto.PaintingExtraDTO;
import artgallery.cms.exception.GalleryDoesNotExistException;
import artgallery.cms.exception.PaintingDoesNotExistException;

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
