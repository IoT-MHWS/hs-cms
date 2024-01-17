package artgallery.cms.controller;

import artgallery.cms.dto.RecDTO;
import artgallery.cms.exception.ArtistDoesNotExistException;
import artgallery.cms.service.RecService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recs")
@RequiredArgsConstructor
public class RecController {
  private final RecService recService;

  @PostMapping("/rec-artists")
  public ResponseEntity<?> getFilteredArtists(@Valid @RequestBody RecDTO req) {
    return ResponseEntity.ok().body(recService.getFilteredArtists(req));
  }

  @GetMapping("/artists/{artistId}/paintings")
  public ResponseEntity<?> getPaintingsByArtistId(@Min(0) @PathVariable Long artistId) throws ArtistDoesNotExistException {
    return ResponseEntity.ok().body(recService.getPaintingsByArtistId(artistId));
  }

}
