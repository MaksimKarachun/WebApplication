package main.DTO.dtoObj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserForCommentDTO {

    private Integer id;
    private String name;
    private String photo;
}
