package main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserDTO {

    private Integer id;
    private String name;
    private String photo;
    private String email;
    private Boolean moderation;
    private Integer moderationCount;
    private Boolean settings;
}