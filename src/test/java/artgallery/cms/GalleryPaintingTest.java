package artgallery.cms;

import artgallery.cms.dto.*;
import artgallery.cms.entity.Style;
import artgallery.cms.exception.ArtistDoesNotExistException;
import artgallery.cms.exception.GalleryDoesNotExistException;
import artgallery.cms.exception.PaintingDoesNotExistException;
import artgallery.cms.service.ArtistService;
import artgallery.cms.service.GalleryService;
import artgallery.cms.service.PaintingService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class GalleryPaintingTest extends AuthorizedControllerTest {
  @Autowired
  private MockMvc mockMvc;

  static private GalleryDTO galleryDTO;
  static private ArtistDTO artistDTO;
  static private PaintingDTO paintingDTO;
  static private DescriptionDTO descriptionDTO;

  @Autowired
  GalleryService galleryService;
  @Autowired
  ArtistService artistService;
  @Autowired
  PaintingService paintingService;

  @BeforeEach
  void createDTOs() throws ArtistDoesNotExistException {
    galleryDTO = new GalleryDTO();
    galleryDTO.setName("gallery");
    galleryDTO.setAddress("here");
    galleryDTO = galleryService.createGallery(galleryDTO);

    artistDTO = new ArtistDTO();
    artistDTO.setName("artist");
    artistDTO.setYearOfBirth(2000);
    artistDTO.setStyle(Style.CUBISM);
    artistDTO = artistService.createArtist(artistDTO);

    paintingDTO = new PaintingDTO();
    paintingDTO.setName("painting");
    paintingDTO.setYearOfCreation(90);
    paintingDTO.setArtistId(artistDTO.getId());
    paintingDTO = paintingService.createPainting(paintingDTO);

    descriptionDTO = new DescriptionDTO();
    descriptionDTO.setDescription("description");
  }

  @Test
  void testGalleryPaintingCreate() throws Exception {
    String request = objectMapper.writeValueAsString(descriptionDTO);

    MvcResult result = mockMvc.perform(
      MockMvcRequestBuilders.
        put("/api/v1/galleries/{id}/paintings/{paintingId}", galleryDTO.getId(), paintingDTO.getId())
        .content(request)
        .headers(authHeaders)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
    ).andReturn();
    MockHttpServletResponse response = result.getResponse();

    GalleryPaintingDTO resultDTO = objectMapper.readValue(response.getContentAsString(), GalleryPaintingDTO.class);

    assertAll(
      () -> assertEquals(201, response.getStatus()),
      () -> assertEquals(galleryDTO.getId(), resultDTO.getGalleryId()),
      () -> assertEquals(paintingDTO.getId(), resultDTO.getPaintingId()),
      () -> assertEquals(descriptionDTO.getDescription(), resultDTO.getDescription())
    );
  }


  @Nested
  class CreatedGalleryPaintingTest {
    @BeforeEach
    public void createGalleryPainting() throws GalleryDoesNotExistException, PaintingDoesNotExistException {
      galleryService.createOrUpdateLinkGalleryToPainting(galleryDTO.getId(), paintingDTO.getId(), descriptionDTO, false);
    }

    @Test
    void testGalleryPaintingUpdate() throws Exception {
      DescriptionDTO descriptionDTO = new DescriptionDTO();
      descriptionDTO.setDescription("new-description");

      String request = objectMapper.writeValueAsString(descriptionDTO);

      MvcResult result = mockMvc.perform(
        MockMvcRequestBuilders.
          put("/api/v1/galleries/{id}/paintings/{paintingId}", galleryDTO.getId(), paintingDTO.getId())
          .content(request)
          .headers(authHeaders)
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON)
      ).andReturn();
      MockHttpServletResponse response = result.getResponse();

      GalleryPaintingDTO resultDTO = objectMapper.readValue(response.getContentAsString(), GalleryPaintingDTO.class);

      assertAll(
        () -> assertEquals(200, response.getStatus()),
        () -> assertEquals(galleryDTO.getId(), resultDTO.getGalleryId()),
        () -> assertEquals(paintingDTO.getId(), resultDTO.getPaintingId()),
        () -> assertEquals(descriptionDTO.getDescription(), resultDTO.getDescription())
      );
    }

    @Test
    void testGalleryPaintingDelete() throws Exception {
      MvcResult result = mockMvc.perform(
        MockMvcRequestBuilders.
          delete("/api/v1/galleries/{id}/paintings/{paintingId}", galleryDTO.getId(), paintingDTO.getId())
          .headers(authHeaders)
          .accept(MediaType.APPLICATION_JSON)
      ).andReturn();
      assertEquals(204, result.getResponse().getStatus());
    }

    @Test
    void testGalleryPaintingListing() throws Exception {
      MvcResult result = mockMvc.perform(
        MockMvcRequestBuilders.
          get("/api/v1/galleries/{id}/paintings", galleryDTO.getId())
          .headers(authHeaders)
          .accept(MediaType.APPLICATION_JSON)
      ).andReturn();
      MockHttpServletResponse response = result.getResponse();

      PaintingExtraDTO[] results = objectMapper.readValue(response.getContentAsString(), PaintingExtraDTO[].class);

      assertAll(
        () -> assertEquals(200, response.getStatus()),
        () -> assertNotEquals(0, results.length)
      );
      assertAll(
        () -> assertEquals(paintingDTO.getName(), results[0].getName()),
        () -> assertEquals(paintingDTO.getYearOfCreation(), results[0].getYearOfCreation()),
        () -> assertEquals(paintingDTO.getId(), results[0].getId()),
        () -> assertEquals(paintingDTO.getArtistId(), results[0].getArtistId()),
        () -> assertEquals(descriptionDTO.getDescription(), results[0].getDescription())
      );
    }

    @Test
    void testPaintingGalleryListing() throws Exception {
      MvcResult result = mockMvc.perform(
        MockMvcRequestBuilders.
          get("/api/v1/paintings/{id}/galleries", paintingDTO.getId())
          .headers(authHeaders)
          .accept(MediaType.APPLICATION_JSON)
      ).andReturn();
      MockHttpServletResponse response = result.getResponse();

      GalleryExtraDTO[] results = objectMapper.readValue(response.getContentAsString(), GalleryExtraDTO[].class);

      assertAll(
        () -> assertEquals(200, response.getStatus()),
        () -> assertNotEquals(0, results.length)
      );
      assertAll(
        () -> assertEquals(galleryDTO.getName(), results[0].getName()),
        () -> assertEquals(galleryDTO.getAddress(), results[0].getAddress()),
        () -> assertEquals(galleryDTO.getId(), results[0].getId()),
        () -> assertEquals(descriptionDTO.getDescription(), results[0].getDescription())
      );
    }
  }

  @AfterEach
  public void deleteGallery() {
    galleryService.deleteLink(galleryDTO.getId(), paintingDTO.getId());
    galleryService.deleteGallery(galleryDTO.getId());
    paintingService.deletePainting(paintingDTO.getId());
    artistService.deleteArtist(artistDTO.getId());
  }
}
