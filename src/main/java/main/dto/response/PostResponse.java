package main.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private Long count;
    private List<PostDTO> posts;

    public void addToPostList(PostDTO post){
        if (posts == null) {
            posts = new ArrayList<>();
        }
        posts.add(post);
    }
}
