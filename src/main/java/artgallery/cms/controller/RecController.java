package artgallery.cms.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import artgallery.cms.dto.RecDTO;
import artgallery.cms.exception.ArtistDoesNotExistException;
import artgallery.cms.service.RecService;

@RestController
@RequestMapping("/api/v1/recs")
@RequiredArgsConstructor
public class RecController {
  private final RecService recService;

  @PostMapping("/rec-artists")
  public ResponseEntity<?> getFilteredArtists(@RequestBody RecDTO req) {
    return ResponseEntity.ok().body(recService.getFilteredArtists(req));
  }

  @GetMapping("/artists/{artistId}/paintings")
  public ResponseEntity<?> getPaintingsByArtistId(@PathVariable Long artistId) throws ArtistDoesNotExistException {
    return ResponseEntity.ok().body(recService.getPaintingsByArtistId(artistId));
  }

}
