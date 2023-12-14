package artgallery.cms.dto;

import artgallery.cms.entity.Style;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecDTO {
  private Integer yearOfBirth;
  private Style style;
}
