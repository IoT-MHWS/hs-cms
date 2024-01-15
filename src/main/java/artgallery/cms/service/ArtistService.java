package artgallery.cms.service;

import artgallery.cms.dto.ArtistDTO;
import artgallery.cms.exception.ArtistDoesNotExistException;
import org.springframework.data.domain.Page;

public interface ArtistService {

  Page<ArtistDTO> getAllArtists(int page, int size);

  ArtistDTO getArtistById(long id) throws ArtistDoesNotExistException;

  ArtistDTO createArtist(ArtistDTO artistDTO);

  ArtistDTO updateArtist(long id, ArtistDTO artistDTO) throws ArtistDoesNotExistException;

  void deleteArtist(long id);

}
