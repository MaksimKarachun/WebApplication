package main.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

    private Boolean result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RegisterErrorDTO errors;
}
