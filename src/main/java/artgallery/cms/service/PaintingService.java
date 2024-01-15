package artgallery.cms.service;

import artgallery.cms.dto.GalleryExtraDTO;
import artgallery.cms.dto.PaintingDTO;
import artgallery.cms.exception.ArtistDoesNotExistException;
import artgallery.cms.exception.PaintingDoesNotExistException;

import java.util.List;

public interface PaintingService {
  List<PaintingDTO> getAllPaintings(int page, int size);

  PaintingDTO getPaintingById(long id) throws PaintingDoesNotExistException;

  PaintingDTO createPainting(PaintingDTO paintingDTO) throws ArtistDoesNotExistException;

  PaintingDTO updatePainting(long id, PaintingDTO paintingDTO) throws PaintingDoesNotExistException, ArtistDoesNotExistException;

  void deletePainting(long id);

  List<GalleryExtraDTO> getLinksPaintingToGallery(long paintingId) throws PaintingDoesNotExistException;
}
