package main.DTO.response;

import lombok.*;
import main.DTO.dtoObj.CommentDTO;
import main.DTO.dtoObj.UserDTO;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostByIdResponse {

    private Integer id;
    private Long timestamp;
    private Boolean active;
    private UserDTO user;
    private String title;
    private String text;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer viewCount;
    private List<CommentDTO> comments;
    private List<String> tags;
}
