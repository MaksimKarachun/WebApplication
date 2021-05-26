package main.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import main.api.response.jsonClasses.UserJson;
import org.springframework.stereotype.Component;

@Component
public class AuthCheckResponse {

    @JsonProperty
    private boolean result;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserJson user;
}
