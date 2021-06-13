package main.DTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.DTO.dtoObj.Post;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {

    private Integer count;
    private List<Post> posts;

    public void addToPostList(Post post){
        if (posts == null) {
            posts = new ArrayList<>();
        }
        posts.add(post);
    }
}
