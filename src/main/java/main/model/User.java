package main.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @Column(name = "is_moderator", columnDefinition = "TINYINT", nullable = false)
    private boolean isModerator;

    @DateTimeFormat(pattern = "yyyy.MM.dd HH-mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH-mm")
    @Column(name = "reg_time", columnDefinition = "DATETIME", nullable = false)
    private Date regTime;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    private String code;

    @Column(columnDefinition = "TEXT")
    private String photo;

    public Role getRole() {
        return isModerator ? Role.MODERATOR : Role.USER;
    }

}
