package main.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "post_comments")
@Data
public class PostComment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private int id;

  @Column(name = "parent_id")
  private Integer parentId;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "FK_postCom_post_id"))
  private Post post;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "FK_postCom_user_id"))
  private User user;

  @DateTimeFormat(pattern = "yyyy.MM.dd HH-mm")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH-mm")
  @Column(columnDefinition = "DATETIME", nullable = false)
  private Date time;

  @Column(columnDefinition = "TEXT", nullable = false)
  private String text;

}
