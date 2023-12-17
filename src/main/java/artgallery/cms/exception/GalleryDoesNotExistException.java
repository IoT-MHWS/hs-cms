package artgallery.cms.exception;

public class GalleryDoesNotExistException extends DoesNotExistException {
  public GalleryDoesNotExistException(Long id) {
    super(String.format(("gallery %s does not exist"), id));
  }
}
