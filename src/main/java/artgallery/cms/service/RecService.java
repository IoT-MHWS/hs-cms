package artgallery.cms.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import artgallery.cms.dto.ArtistDTO;
import artgallery.cms.dto.PaintingDTO;
import artgallery.cms.dto.RecDTO;
import artgallery.cms.entity.ArtistEntity;
import artgallery.cms.entity.PaintingEntity;
import artgallery.cms.exception.ArtistDoesNotExistException;
import artgallery.cms.repository.ArtistRepository;
import artgallery.cms.repository.PaintingRepository;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RecService {
  private final PaintingRepository paintingRepository;
  private final ArtistRepository artistRepository;

  public List<ArtistDTO> getFilteredArtists(RecDTO recDto) {
      List<ArtistEntity> artists = artistRepository.findByStyleAndYearOfBirthLessThanEqual(recDto.getStyle(), recDto.getYearOfBirth());
    return mapToArtistDtoList(artists);
  }

  private List<ArtistDTO> mapToArtistDtoList(List<ArtistEntity> artists) {
    return artists.stream()
      .map(artist -> new ArtistDTO(artist.getId(), artist.getName(), artist.getYearOfBirth(), artist.getBio(), artist.getStyle()))
      .collect(Collectors.toList());
  }

  public List<PaintingDTO> getPaintingsByArtistId(Long artistId) throws ArtistDoesNotExistException {
    artistRepository.findById(artistId).orElseThrow(() ->
      new ArtistDoesNotExistException(artistId));
    List<PaintingEntity> paintings = paintingRepository.findByArtistEntityId(artistId);
    return mapToPaintingDtoList(paintings);
  }

  private List<PaintingDTO> mapToPaintingDtoList(List<PaintingEntity> paintings) {
    return paintings.stream()
      .map(this::mapToPaintingDto)
      .collect(Collectors.toList());
  }

  private PaintingDTO mapToPaintingDto(PaintingEntity painting) {
    return new PaintingDTO( painting.getId(), painting.getName(), painting.getYearOfCreation(),
      painting.getArtistEntity().getId());
  }
}
