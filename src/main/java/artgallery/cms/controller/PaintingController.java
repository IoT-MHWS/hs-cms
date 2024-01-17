package artgallery.cms.controller;

import artgallery.cms.dto.PaintingDTO;
import artgallery.cms.exception.ArtistDoesNotExistException;
import artgallery.cms.exception.PaintingDoesNotExistException;
import artgallery.cms.service.PaintingService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
  public ResponseEntity<?> getAllPaintings(@Min(0) @RequestParam(value = "page", defaultValue = "0") int page,
                                           @Min(0) @Max(50) @RequestParam(value = "size", defaultValue = "10") int size) {
    return ResponseEntity.ok().body(paintingService.getAllPaintings(page, size));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getPaintingById(@Min(0) @PathVariable("id") long id) throws PaintingDoesNotExistException {
    return ResponseEntity.ok().body(paintingService.getPaintingById(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> createPainting(@Valid @RequestBody PaintingDTO req) throws ArtistDoesNotExistException {
    return ResponseEntity.status(HttpStatus.CREATED).body(paintingService.createPainting(req));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> updatePainting(@Min(0) @PathVariable("id") long id, @Valid @RequestBody PaintingDTO req)
    throws PaintingDoesNotExistException, ArtistDoesNotExistException {
    paintingService.updatePainting(id, req);
    return ResponseEntity.ok().body("ok");
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> deletePainting(@Min(0) @PathVariable("id") long id) {
    paintingService.deletePainting(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{paintingId}/galleries")
  public ResponseEntity<?> getLinksToGalleries(@Min(0) @PathVariable long paintingId) throws PaintingDoesNotExistException {
    return ResponseEntity.ok().body(paintingService.getLinksPaintingToGallery(paintingId));
  }

}
