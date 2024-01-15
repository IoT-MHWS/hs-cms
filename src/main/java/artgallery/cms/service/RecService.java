package artgallery.cms.service;

import artgallery.cms.dto.ArtistDTO;
import artgallery.cms.dto.PaintingDTO;
import artgallery.cms.dto.RecDTO;
import artgallery.cms.exception.ArtistDoesNotExistException;

import java.util.List;

public interface RecService {
  List<ArtistDTO> getFilteredArtists(RecDTO recDto);

  List<PaintingDTO> getPaintingsByArtistId(Long artistId) throws ArtistDoesNotExistException;
}
