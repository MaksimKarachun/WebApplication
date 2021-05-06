package main.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @NotNull
    @Column(name = "is_active", columnDefinition = "TINYINT")
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_status", columnDefinition = "enum('NEW', 'DECLINED', 'ACCEPTED') default 'NEW'")
    @NotNull
    private ModerationStatus moderationStatus;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id", referencedColumnName = "id")
    @NotNull
    private User moderatorId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull
    private User user;

    @NotNull
    private Date time;

    @NotNull
    private String title;

    @NotNull
    private String text;

    @Column(name = "view_count")
    @NotNull
    private int viewCount;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "tag2post",
            joinColumns = {@JoinColumn(name = "post_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")})
    List<Tag> tags;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<PostVote> postVotes;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<PostComment> postComments;
}
