package main.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.dto.responseDto.RegisterErrorDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

    Boolean result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    RegisterErrorDTO errors;
}
