package artgallery.cms.service;

import artgallery.cms.dto.ExhibitionDTO;
import artgallery.cms.exception.ExhibitionDoesNotExistException;
import artgallery.cms.exception.MyGalleryDoesNotExistException;

import java.util.List;

public interface ExhibitionService {
  List<ExhibitionDTO> getAllExhibitions(int page, int size);

  ExhibitionDTO getExhibitionById(long id) throws ExhibitionDoesNotExistException;

  ExhibitionDTO createExhibition(ExhibitionDTO exhibitionDTO) throws MyGalleryDoesNotExistException;

  ExhibitionDTO updateExhibition(long id, ExhibitionDTO exhibitionDTO) throws ExhibitionDoesNotExistException, MyGalleryDoesNotExistException;

  void deleteExhibition(long id);

}
