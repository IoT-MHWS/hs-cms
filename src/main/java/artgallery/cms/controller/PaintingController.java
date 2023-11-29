package artgallery.cms.controller;

import artgallery.cms.dto.PaintingDTO;
import artgallery.cms.exception.ArtistDoesNotExistException;
import artgallery.cms.exception.PaintingDoesNotExistException;
import artgallery.cms.service.PaintingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/paintings")
@RequiredArgsConstructor
public class PaintingController {
  private final PaintingService paintingService;

  @GetMapping("/")
  public ResponseEntity<?> getAllPaintings(@RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size) {
    return ResponseEntity.ok().body(paintingService.getAllPaintings(page, size));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getPaintingById(@PathVariable("id") long id) throws PaintingDoesNotExistException {
    return ResponseEntity.ok().body(paintingService.getPaintingById(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> createPainting(@RequestBody PaintingDTO req) throws ArtistDoesNotExistException {
    return ResponseEntity.status(HttpStatus.CREATED).body(paintingService.createPainting(req));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> updatePainting(@PathVariable("id") long id, @RequestBody PaintingDTO req)
      throws PaintingDoesNotExistException, ArtistDoesNotExistException {
    paintingService.updatePainting(id, req);
    return ResponseEntity.ok().body("ok");
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> deletePainting(@PathVariable("id") long id) {
    paintingService.deletePainting(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{paintingId}/galleries")
  public ResponseEntity<?> getLinksToGalleries(@PathVariable long paintingId) throws PaintingDoesNotExistException {
    return ResponseEntity.ok().body(paintingService.getLinksPaintingToGallery(paintingId));
  }

}
