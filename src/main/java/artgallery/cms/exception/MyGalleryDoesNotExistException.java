package artgallery.cms.exception;

public class MyGalleryDoesNotExistException extends DoesNotExistException {
  public MyGalleryDoesNotExistException(Long id) {
    super(String.format(("gallery %s does not exist"), id));
  }
}
