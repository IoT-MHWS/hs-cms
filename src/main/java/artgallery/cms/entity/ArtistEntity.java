package artgallery.cms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "artist")
public class ArtistEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "name")
  @NotNull(message = "name must not be null")
  private String name;

  private Integer yearOfBirth;

  @Column(name = "bio")
  private String bio;

  @NotNull(message = "must be not null")
  @Enumerated(EnumType.STRING)
  @Column(name = "style")
//  private Style style;
  private Style style;
}
