package main.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "post_votes")
@Data
public class PostVote {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private int id;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "FK_vote_user_id"))
  private User user;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "FK_vote_post_id"))
  private Post post;

  @DateTimeFormat(pattern = "yyyy.MM.dd HH-mm")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH-mm")
  @Column(columnDefinition = "DATETIME", nullable = false)
  private Date time;

  @Column(columnDefinition = "TINYINT", nullable = false)
  private byte value;

}
