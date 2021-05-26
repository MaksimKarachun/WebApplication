package main.api.response.jsonClasses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserJson {

    @JsonProperty
    private int id;
    @JsonProperty
    private String name;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean moderation;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int moderationCount;
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private boolean settings;

    public UserJson(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
