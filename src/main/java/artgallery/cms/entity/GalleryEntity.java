package artgallery.cms.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "gallery")
public class GalleryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "name")
  private String name;

  @Column(name = "address")
  private String address;

  @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<ExhibitionEntity> exhibitions;

}
