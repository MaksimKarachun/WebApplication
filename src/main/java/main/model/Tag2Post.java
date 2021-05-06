package main.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "tag2post")
public class Tag2Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private int id;

    @OneToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    @NotNull
    private Post post;

    @OneToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "id")
    @NotNull
    private Tag tag;
}
