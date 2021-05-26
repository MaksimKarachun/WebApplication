package main.api.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import main.api.response.jsonClasses.TagJson;

import java.util.ArrayList;
import java.util.List;

public class TagResponse {

    @JsonProperty()
    private List<TagJson> tags;

    public void addToTagList(TagJson tag){
        if (tags == null){
            tags = new ArrayList<>();
        }
        tags.add(tag);
    }
}
