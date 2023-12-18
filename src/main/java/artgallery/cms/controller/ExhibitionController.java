package artgallery.cms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import artgallery.cms.dto.ExhibitionDTO;
import artgallery.cms.exception.ExhibitionDoesNotExistException;
import artgallery.cms.exception.GalleryDoesNotExistException;
import artgallery.cms.service.ExhibitionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/exhibitions")
@RequiredArgsConstructor
public class ExhibitionController {
  private final ExhibitionService exhibitionService;

  @GetMapping("/")
  public ResponseEntity<?> getAllExhibitions(@Min(0) @RequestParam(value = "page", defaultValue = "0") int page,
      @Min(0) @Max(50) @RequestParam(value = "size", defaultValue = "10") int size) {
    return ResponseEntity.ok().body(exhibitionService.getAllExhibitions(page, size));
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getExhibitionById(@NotNull @Min(0) @PathVariable("id") long id) throws ExhibitionDoesNotExistException {
    return ResponseEntity.ok().body(exhibitionService.getExhibitionById(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> createExhibition(@Valid @RequestBody ExhibitionDTO req) throws GalleryDoesNotExistException {
    return ResponseEntity.status(HttpStatus.CREATED).body(exhibitionService.createExhibition(req));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> updateExhibition(@NotNull @Min(0) @PathVariable("id") long id, @Valid @RequestBody ExhibitionDTO req)
      throws ExhibitionDoesNotExistException, GalleryDoesNotExistException {
    exhibitionService.updateExhibition(id, req);
    return ResponseEntity.ok().body("ok");
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> deleteExhibition(@NotNull @Min(0) @PathVariable("id") long id) {
    exhibitionService.deleteExhibition(id);
    return ResponseEntity.noContent().build();
  }

}
