package artgallery.cms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import artgallery.cms.dto.DescriptionDTO;
import artgallery.cms.dto.GalleryDTO;
import artgallery.cms.exception.GalleryDoesNotExistException;
import artgallery.cms.exception.PaintingDoesNotExistException;
import artgallery.cms.service.GalleryService;

@RestController
@RequestMapping("/api/v1/galleries")
@RequiredArgsConstructor
public class GalleryController {
  private final GalleryService galleryService;

  @GetMapping("/")
  public ResponseEntity<?> getAllGalleries(@RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {
    return ResponseEntity.ok().body(galleryService.getAllGalleries(page, size));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getGalleryById(@PathVariable("id") long id) throws GalleryDoesNotExistException {
    return ResponseEntity.ok().body(galleryService.getGalleryById(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> createGallery(@RequestBody GalleryDTO req) {
    return ResponseEntity.status(HttpStatus.CREATED).body(galleryService.createGallery(req));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> updateGallery(@PathVariable("id") long id, @RequestBody GalleryDTO req) throws GalleryDoesNotExistException {
    return ResponseEntity.status(HttpStatus.OK).body(galleryService.updateGallery(id, req));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> deleteGallery(@PathVariable("id") long id) {
    galleryService.deleteGallery(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{galleryId}/paintings")
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> getLinksToPaintings(@PathVariable long galleryId) throws GalleryDoesNotExistException {
    return ResponseEntity.ok().body(galleryService.getLinksGalleryToPainting(galleryId));
  }

  @PutMapping("/{galleryId}/paintings/{paintingId}")
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> createOrUpdateLink(@PathVariable long galleryId, @PathVariable long paintingId,
      @RequestBody DescriptionDTO linkDto) throws GalleryDoesNotExistException, PaintingDoesNotExistException {
    boolean isNewLink = (galleryService.existsByGalleryIdAndPaintingId(galleryId, paintingId));
    return ResponseEntity.status(isNewLink ? HttpStatus.CREATED : HttpStatus.OK)
        .body(galleryService.createOrUpdateLinkGalleryToPainting(galleryId, paintingId, linkDto, isNewLink));
  }

  @DeleteMapping("/{galleryId}/paintings/{paintingId}")
  @PreAuthorize("hasRole('MODERATOR') ")
  public ResponseEntity<?> deleteLink(@PathVariable long galleryId, @PathVariable long paintingId) {
      galleryService.deleteLink(galleryId, paintingId);
      return ResponseEntity.noContent().build();
  }

}
