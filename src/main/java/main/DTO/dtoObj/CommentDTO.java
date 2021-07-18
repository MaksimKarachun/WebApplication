package main.DTO.dtoObj;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDTO {

    Integer id;
    Long timestamp;
    String text;
    UserForCommentDTO user;
}
