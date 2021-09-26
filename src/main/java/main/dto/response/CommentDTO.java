package main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDTO {

    private Integer id;
    private Long timestamp;
    private String text;
    private UserForCommentDTO user;
}
