package main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthCheckResponse {

    private boolean result;
/*    @JsonInclude(JsonInclude.Include.NON_NULL)
    private User user;*/
}
