package artgallery.cms.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaintingDTO {
  private long id;
  @NotNull
  private String name;
  private Integer yearOfCreation;
  private Long artistId;
}
