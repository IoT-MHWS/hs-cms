package artgallery.cms.service;

import artgallery.cms.dto.ExhibitionDTO;
import artgallery.cms.exception.ExhibitionDoesNotExistException;
import artgallery.cms.exception.GalleryDoesNotExistException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ExhibitionService {
  List<ExhibitionDTO> getAllExhibitions(int page, int size);

  ExhibitionDTO getExhibitionById(long id) throws ExhibitionDoesNotExistException;

  ExhibitionDTO createExhibition(ExhibitionDTO exhibitionDTO) throws GalleryDoesNotExistException;

  ExhibitionDTO updateExhibition(long id, ExhibitionDTO exhibitionDTO) throws ExhibitionDoesNotExistException, GalleryDoesNotExistException;

  void deleteExhibition(long id);

}
