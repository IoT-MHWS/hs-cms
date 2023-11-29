package artgallery.cms.exception;

public class GalleryDoesNotExistException extends Exception {
  public GalleryDoesNotExistException(Long id) {
    super(String.format(("gallery %s does not exist"), id));
  }
}
