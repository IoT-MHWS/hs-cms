package artgallery.cms.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@Entity
@Data
@Table(name = "gallery_painting")
public class GalleryPaintingEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @ManyToOne
  @JoinColumn(name = "gallery_id", referencedColumnName = "id")
  private GalleryEntity gallery;

  @ManyToOne
  @JoinColumn(name = "painting_id", referencedColumnName = "id")
  private PaintingEntity painting;

  @Column(name = "description")
  private String description;

}
