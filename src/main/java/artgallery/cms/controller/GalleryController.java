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
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/api/v1/galleries")
@RequiredArgsConstructor
public class GalleryController {
  private final GalleryService galleryService;

  @GetMapping("/")
  public ResponseEntity<?> getAllGalleries(@Min(0) @RequestParam(value = "page", defaultValue = "0") int page,
      @Min(0) @Max(50) @RequestParam(value = "size", defaultValue = "10") int size) {
    return ResponseEntity.ok().body(galleryService.getAllGalleries(page, size));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getGalleryById(@Min(0) @PathVariable("id") long id) throws GalleryDoesNotExistException {
    return ResponseEntity.ok().body(galleryService.getGalleryById(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> createGallery(@Valid @RequestBody GalleryDTO req) {
    return ResponseEntity.status(HttpStatus.CREATED).body(galleryService.createGallery(req));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> updateGallery(@Min(0) @PathVariable("id") long id, @Valid @RequestBody GalleryDTO req) throws GalleryDoesNotExistException {
    return ResponseEntity.status(HttpStatus.OK).body(galleryService.updateGallery(id, req));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> deleteGallery(@Min(0) @PathVariable("id") long id) {
    galleryService.deleteGallery(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{galleryId}/paintings")
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> getLinksToPaintings(@Min(0) @PathVariable long galleryId) throws GalleryDoesNotExistException {
    return ResponseEntity.ok().body(galleryService.getLinksGalleryToPainting(galleryId));
  }

  @PutMapping("/{galleryId}/paintings/{paintingId}")
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> createOrUpdateLink(@Min(0) @PathVariable long galleryId, @Min(0) @PathVariable long paintingId,
      @Valid @RequestBody DescriptionDTO linkDto) throws GalleryDoesNotExistException, PaintingDoesNotExistException {
    boolean exists = (galleryService.existsByGalleryIdAndPaintingId(galleryId, paintingId));
    return ResponseEntity.status(exists ? HttpStatus.OK : HttpStatus.CREATED)
        .body(galleryService.createOrUpdateLinkGalleryToPainting(galleryId, paintingId, linkDto, exists));
  }

  @DeleteMapping("/{galleryId}/paintings/{paintingId}")
  @PreAuthorize("hasRole('MODERATOR') ")
  public ResponseEntity<?> deleteLink(@Min(0) @PathVariable long galleryId, @Min(0) @PathVariable long paintingId) {
      galleryService.deleteLink(galleryId, paintingId);
      return ResponseEntity.noContent().build();
  }

}
