package artgallery.cms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Data
@Table(name = "exhibition")
public class ExhibitionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "name")
  private String name;

  @NotNull(message = "date must be not null")
  @Column(name = "start_date")
  private Date startDate;

  @NotNull(message = "date must be not null")
  @Column(name = "end_date")
  private Date endDate;

  @ManyToOne()
  @JoinColumn(name = "gallery_id") //, referencedColumnName = "id"
  private GalleryEntity gallery;
}
