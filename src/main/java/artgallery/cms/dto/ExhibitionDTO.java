package artgallery.cms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionDTO {
  private long id;
  @NotNull
  private String name;
  @NotNull
  private Date startDate;
  @NotNull
  private Date endDate;
  private Long galleryId;
}
