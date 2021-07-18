package main.DTO.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.DTO.dtoObj.TagDTO;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagResponse {

    private List<TagDTO> tags;

    public void addToTagList(TagDTO tag){
        if (tags == null){
            tags = new ArrayList<>();
        }
        tags.add(tag);
    }
}
